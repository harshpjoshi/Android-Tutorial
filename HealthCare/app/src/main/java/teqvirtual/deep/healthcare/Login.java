package teqvirtual.deep.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class Login extends AppCompatActivity {

    TextView signup_text;
    String[] type={"Select User Type","Subscriber","Doctor"};
    String url="https://teqvirtual.com/healthcare/index.php";
    String type_holder;
    TextInputEditText email_login,password_login;
    MaterialButton button_login;

    //firebase variable

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mReference;

    String firstname,image,email,password,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        mReference=firebaseDatabase.getReference();

        if(checkPermission())
        {

        }
        else
        {
            requestPermission();
        }

        email_login=(TextInputEditText)findViewById(R.id.email_login);
        password_login=(TextInputEditText)findViewById(R.id.password_login);
        button_login=(MaterialButton)findViewById(R.id.button_login); 
        Spinner spinner = (Spinner) findViewById(R.id.login_type);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(Login.this,android.R.layout.simple_spinner_dropdown_item,type);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                type_holder=type[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        signup_text=(TextView)findViewById(R.id.signup_text);

        signup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this,SelectSignup.class));

            }
        });
        
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (type_holder.equals("Subscriber"))
                {
                    subscriberLogin();
                }
                else if (type_holder.equals("Doctor"))
                {
                    doctorLogin();
                }
                
            }
        });
    }

    public void alertDialog(String msg)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void doctorLogin() {

        String[] key;
        String[] value;
        String jsonRequest;


        final String email_hold=email_login.getText().toString().trim();
        final String password_hold=password_login.getText().toString().trim();

        if (email_hold.isEmpty())
        {
            alertDialog("Email Not Empty");
        }
        else if (password_hold.isEmpty())
        {
            alertDialog("Password not Empty");
        }
        else
        {



            key=new String[]{"action","email","password"};
            value=new String[]{"doctor_login",email_hold,password_hold};
            jsonRequest= Utils.createJsonRequest(key,value);

            new WebserviceCall(Login.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
                @Override
                public void onCallback(String response) {

                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        Toast.makeText(Login.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");

                         id=jsonObject1.getString("doctor_id");
                         firstname=jsonObject1.getString("firstname");
                         email=jsonObject1.getString("email");
                         image=jsonObject1.getString("image");
                         password=jsonObject1.getString("password");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }).execute();

            mAuth.signInWithEmailAndPassword(email_hold,password_hold)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful())
                            {
                                mReference.child("Doctors").child(mAuth.getCurrentUser().getUid()).child("token").setValue(FirebaseInstanceId.getInstance().getToken());

                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("doctorid",id);
                                editor.putString("docimg",image);
                                editor.putString("docname",firstname);
                                editor.commit();

                                Intent intent=new Intent(Login.this,DoctorMainProfile.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Log.d("signup","error");
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d("signup","error"+e.getMessage());
                    Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }

    }

    private void subscriberLogin() {

        String[] key;
        String[] value;
        String jsonRequest;

        final String email_hold=email_login.getText().toString().trim();
        final String password_hold=password_login.getText().toString().trim();

        if (email_hold.isEmpty())
        {
            alertDialog("Email Not Empty");
        }
        else if (password_hold.isEmpty())
        {
            alertDialog("Password not Empty");
        }
        else
        {
            key=new String[]{"action","email","password"};
            value=new String[]{"subscriber_login",email_hold,password_hold};
            jsonRequest=Utils.createJsonRequest(key,value);

            new WebserviceCall(Login.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
                @Override
                public void onCallback(String response) {

                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        Toast.makeText(Login.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");

                         id=jsonObject1.getString("subscriber_id");
                         firstname=jsonObject1.getString("firstname");
                         email=jsonObject1.getString("email");
                         image=jsonObject1.getString("image");

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("userid",id);
                        editor.putString("name",firstname);
                        editor.putString("image",image);
                        editor.commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }).execute();


            mAuth.signInWithEmailAndPassword(email_hold,password_hold)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful())
                            {

                                mReference.child("Subscribers").child(mAuth.getCurrentUser().getUid()).child("token").setValue(FirebaseInstanceId.getInstance().getToken());


                                Intent intent=new Intent(Login.this,SubscriberProfile.class);
                                intent.putExtra("id",id);
                                intent.putExtra("name",firstname);
                                intent.putExtra("email",email);
                                intent.putExtra("image",image);
                                startActivity(intent);

                            }
                            else
                            {
                                Log.d("signup","error");
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d("signup","error"+e.getMessage());

                }
            });
        }

    }


    private void requestPermission() {

        ActivityCompat.requestPermissions(Login.this, new String[]
                {
                        INTERNET,
                        CALL_PHONE,
                        ACCESS_FINE_LOCATION,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE,
                        CAMERA
                }, 200);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 200:

                if (grantResults.length > 0) {

                    boolean internetpermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean callphone = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean location = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean internalread = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean write = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean camera= grantResults[5] == PackageManager.PERMISSION_GRANTED;

                }

                break;
        }
    }

    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int SixthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FourthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SixthPermissionResult == PackageManager.PERMISSION_GRANTED;

    }
}
