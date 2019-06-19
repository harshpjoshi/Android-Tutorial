package teqvirtual.deep.healthcare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import teqvirtual.deep.healthcare.Adapter.CityAdapter;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;
import teqvirtual.deep.healthcare.Model.CityModel;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SubscriberEditprofile extends AppCompatActivity {

    Bitmap bitmap;
    String city_holder;
    ImageView sub_image;
    Spinner spinner;
    Uri imageUri;
    MaterialButton sub_signup;
    TextInputEditText firstname_sub,lastname_sub,email_sub,password_sub,contact_sub,address_sub;
    String url="https://teqvirtual.com/healthcare/index.php";
    ArrayList<CityModel> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_editprofile);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner= (Spinner) findViewById(R.id.city_sub);
        firstname_sub=(TextInputEditText) findViewById(R.id.firstname_sub);
        lastname_sub=(TextInputEditText)findViewById(R.id.lastname_sub);
        email_sub=(TextInputEditText)findViewById(R.id.email_sub);
        password_sub=(TextInputEditText)findViewById(R.id.password_sub);
        contact_sub=(TextInputEditText)findViewById(R.id.mobile_sub);
        sub_image=(ImageView) findViewById(R.id.sub_profile);
        address_sub=(TextInputEditText)findViewById(R.id.address_sub);
        sub_signup=(MaterialButton)findViewById(R.id.button_signup_sub);



        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        String userid=pref.getString("userid", null);

        Toast.makeText(this, ""+userid, Toast.LENGTH_SHORT).show();

        String skey[]=new String[]{"action","subscriber_id"};
        String sval[]=new String[]{"subscriber_view",userid};

        String jsonReq=Utils.createJsonRequest(skey,sval);

        new WebserviceCall(SubscriberEditprofile.this, url, jsonReq, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        String subscriber_id=jsonObject1.getString("subscriber_id");
                        String firstname=jsonObject1.getString("firstname");
                        String lastname=jsonObject1.getString("lastname");
                        String image=jsonObject1.getString("image");
                        String address=jsonObject1.getString("address");
                        String email=jsonObject1.getString("email");
                        String password=jsonObject1.getString("password");
                        String mobileno=jsonObject1.getString("mobileno");

                        Picasso.get().load(image).into(sub_image);
                        firstname_sub.setText(firstname);
                        lastname_sub.setText(lastname);
                        email_sub.setText(email);
                        address_sub.setText(address);
                        contact_sub.setText(mobileno);
                        password_sub.setText(password);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();

        String c_key[]=new String[]{"action"};
        String c_value[]=new String[]{"city_view"};

        String c_jsonRequest= Utils.createJsonRequest(c_key,c_value);

        new WebserviceCall(SubscriberEditprofile.this, url, c_jsonRequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray jsonArray=jsonObject.getJSONArray("data");


                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String id=jsonObject1.getString("city_id");
                        String state_id=jsonObject1.getString("state_id");
                        String name=jsonObject1.getString("name");


                        CityModel cityModel=new CityModel();
                        cityModel.setId(id);
                        cityModel.setStateid(state_id);
                        cityModel.setName(name);

                        arrayList.add(cityModel);
                    }

                    CityAdapter cityAdapter=new CityAdapter(SubscriberEditprofile.this,arrayList);
                    spinner.setAdapter(cityAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                city_holder=arrayList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sub_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                startActivityForResult(chooser,5);

            }
        });

        sub_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editProfile(userid);

            }
        });
    }

    private void editProfile(String userid) {

        String fname_holder=firstname_sub.getText().toString().trim();
        String lname_holder=lastname_sub.getText().toString().trim();
        String email_holder=email_sub.getText().toString().trim();
        String password_holder=password_sub.getText().toString().trim();
        String contact_holder=contact_sub.getText().toString().trim();
        String image_holder=getStringImage(bitmap);
        String address_holder=address_sub.getText().toString().trim();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("image",image_holder);
        editor.commit();

        if (fname_holder.isEmpty())
        {
            alertDialog("Firstname Not Empty");
        }
        else if (lname_holder.isEmpty())
        {
            alertDialog("Lastname Not Empty");
        }
        else if (email_holder.isEmpty())
        {
            alertDialog("Email Not Empty");
        }
        else if (password_holder.isEmpty())
        {
            alertDialog("Password Not Empty");
        }
        else if (password_holder.length()<6)
        {
            alertDialog("Password must atleast 6 Character");
        }
        else if (contact_holder.isEmpty())
        {
            alertDialog("Mobileno Not Empty");
        }
        else if (address_holder.isEmpty())
        {
            alertDialog("Address not Empty");
        }
        else {

            String key[]=new String[]{"action","subscriber_id","firstname","lastname","email","mobileno","image","password","city","address"};
            String[] value=new String[]{"subscriber_edit_profile",userid,fname_holder,lname_holder,email_holder,contact_holder,image_holder,password_holder,city_holder,address_holder};

            String jsonRequest=Utils.createJsonRequest(key,value);

            new WebserviceCall(SubscriberEditprofile.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
                @Override
                public void onCallback(String response) {

                    JSONObject jsonObject= null;
                    try {
                        jsonObject = new JSONObject(response);
                        Toast.makeText(SubscriberEditprofile.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
            }).execute();

        }

        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && resultCode == RESULT_OK) {
            //for copy image path
            Uri path = data.getData();
            CropImage.activity(path).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(SubscriberEditprofile.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Picasso.get().load(imageUri).into(sub_image);
            }
        }
    }

    public String getStringImage(Bitmap bmp)
    {
        if (bmp!=null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            String encodedimage = Base64.encodeToString(bytes, Base64.DEFAULT);
            return encodedimage;
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        if (id==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
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
}
