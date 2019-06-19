package teqvirtual.deep.healthcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import teqvirtual.deep.healthcare.Adapter.CityAdapter;
import teqvirtual.deep.healthcare.Adapter.DoctorTypeAdapter;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;
import teqvirtual.deep.healthcare.Model.CityModel;
import teqvirtual.deep.healthcare.Model.DoctorTypeModel;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DoctorSignup extends AppCompatActivity {

    Bitmap bitmap;
    String city_holder;
    ImageView sub_image;
    Spinner spinner,spinner2;
    String doctortype_holder;
    Uri imageUri;
    GoogleMap map;
    double lat,longi,new_lat,new_log;
    Marker marker;
    MaterialButton sub_signup;
    String subscriber_id,email,firstname,image,password;
    TextInputEditText firstname_sub,lastname_sub,email_sub,password_sub,contact_sub,address_sub;
    String url="https://teqvirtual.com/healthcare/index.php";
    ArrayList<CityModel> arrayList=new ArrayList<>();
    ArrayList<DoctorTypeModel> arrayList2=new ArrayList<>();


    //firebase variables

    private FirebaseAuth mAuth;
    DatabaseReference reference;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_signup);


        mAuth = FirebaseAuth.getInstance();

        spinner= (Spinner) findViewById(R.id.city_doc);
        spinner2=(Spinner) findViewById(R.id.type_doc);
        firstname_sub=(TextInputEditText) findViewById(R.id.firstname_doc);
        lastname_sub=(TextInputEditText)findViewById(R.id.lastname_doc);
        email_sub=(TextInputEditText)findViewById(R.id.email_doc);
        password_sub=(TextInputEditText)findViewById(R.id.password_doc);
        contact_sub=(TextInputEditText)findViewById(R.id.mobile_doc);
        sub_image=(ImageView) findViewById(R.id.sub_profile);
        address_sub=(TextInputEditText)findViewById(R.id.address_doc);
        sub_signup=(MaterialButton)findViewById(R.id.button_signup_doc);

        ((GoogleMapWithScrollFix) getSupportFragmentManager()
                .findFragmentById(R.id.map_doc)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                map=googleMap;


                final ScrollView mScrollView = findViewById(R.id.scrollmap); //parent scrollview in xml, give your scrollview id value
                ((GoogleMapWithScrollFix) getSupportFragmentManager()
                        .findFragmentById(R.id.map_doc)).setListener(new GoogleMapWithScrollFix.OnTouchListener() {
                    @Override
                    public void onTouch() {
                        //Here is the magic happens.
                        //we disable scrolling of outside scroll view here
                        mScrollView.requestDisallowInterceptTouchEvent(true);


                        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng location) {


                                Toast.makeText(DoctorSignup.this, ""+location, Toast.LENGTH_SHORT).show();
                                if(marker!=null)
                                {
                                    marker.remove();
                                    map.clear();
                                }
                                marker=map.addMarker(new MarkerOptions().position(location));

                                new_lat=location.latitude;
                                new_log=location.longitude;
                            }
                        });

                    }
                });
            }
        });




        String c_key[]=new String[]{"action"};
        String c_value[]=new String[]{"city_view"};

        String c_jsonRequest= Utils.createJsonRequest(c_key,c_value);

        new WebserviceCall(DoctorSignup.this, url, c_jsonRequest, "Loading...", true, new AsyncResponse() {
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

                    CityAdapter cityAdapter=new CityAdapter(DoctorSignup.this,arrayList);
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

                Geocoder geocoder=new Geocoder(DoctorSignup.this);
                List<Address> list=new ArrayList<>();

                try {
                    list=geocoder.getFromLocationName(arrayList.get(position).getName(),1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (list.size() > 0)
                {
                    Address address1=list.get(0);

                    lat=address1.getLatitude();
                    longi=address1.getLongitude();

                    LatLng latLng=new LatLng(lat,longi);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));

//                                        MarkerOptions markerOptions=new MarkerOptions();
//                                        markerOptions.position(latLng);
//                                        map.addMarker(markerOptions);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        String[] doc_key=new String[]{"action"};
        String[] doc_val=new String[]{"doctortype_view"};

        String docjsonReq=Utils.createJsonRequest(doc_key,doc_val);

        new WebserviceCall(DoctorSignup.this, url, docjsonReq, "Loading...", false, new AsyncResponse() {
            @Override
            public void onCallback(String response) {


                try {
                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int j=0;j<jsonArray.length();j++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(j);
                        String doctor_type_id=jsonObject1.getString("doctor_type_id");
                        String name=jsonObject1.getString("type_name");

                        DoctorTypeModel doctorTypeModel=new DoctorTypeModel();
                        doctorTypeModel.setId(doctor_type_id);
                        doctorTypeModel.setName(name);

                        arrayList2.add(doctorTypeModel);
                    }

                    DoctorTypeAdapter doctorTypeAdapter=new DoctorTypeAdapter(getApplicationContext(),arrayList2);
                    spinner2.setAdapter(doctorTypeAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                doctortype_holder=arrayList2.get(position).getId();
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


                doctorRegistred();

            }
        });

    }

    private void doctorRegistred() {

        String fname_holder=firstname_sub.getText().toString().trim();
        String lname_holder=lastname_sub.getText().toString().trim();
        String email_holder=email_sub.getText().toString().trim();
        String password_holder=password_sub.getText().toString().trim();
        String contact_holder=contact_sub.getText().toString().trim();
        String image_holder=getStringImage(bitmap);
        String address_holder=address_sub.getText().toString().trim();


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


            mAuth.createUserWithEmailAndPassword(email_holder,password_holder).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful())
                    {
                        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                        String f_doctor_id=firebaseUser.getUid();

                        mDatabase=FirebaseDatabase.getInstance().getReference("Doctors").child(f_doctor_id);

                        HashMap<String,String> hashMap=new HashMap<>();
                        hashMap.put("f_doctor_id",f_doctor_id);
                        hashMap.put("firstname",fname_holder);
                        hashMap.put("email",email_holder);
                        hashMap.put("mobileno",contact_holder);
                        hashMap.put("token", FirebaseInstanceId.getInstance().getToken());

                        mDatabase.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {

                                    String key[]=new String[]{"action","firstname","lastname","email","mobileno","image","password","city","address","type","latitude","longitude","f_doctor_id"};
                                    String[] value=new String[]{"doctor_registration",fname_holder,lname_holder,email_holder,contact_holder,image_holder,password_holder,city_holder,address_holder,doctortype_holder,new_lat+"",new_log+"",f_doctor_id};

                                    String jsonRequest=Utils.createJsonRequest(key,value);

                                    new WebserviceCall(DoctorSignup.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
                                        @Override
                                        public void onCallback(String response) {

                                            try {

                                                JSONObject jsonObject=new JSONObject(response);

                                                Toast.makeText(DoctorSignup.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                                JSONObject jsonObject1=jsonObject.getJSONObject("data");

                                                String doctor_id=jsonObject1.getString("doctor_id");
                                                String firstname=jsonObject1.getString("firstname");
                                                String lastname=jsonObject1.getString("lastname");
                                                String image=jsonObject1.getString("image");
                                                String address=jsonObject1.getString("address");
                                                String email=jsonObject1.getString("email");
                                                String password=jsonObject1.getString("password");
                                                String mobileno=jsonObject1.getString("mobileno");


                                                mDatabase.child("image").setValue(image);



                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }).execute();

                                }
                            }
                        });

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d("signup","error"+e.getMessage());
                    Toast.makeText(DoctorSignup.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });




        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && resultCode == RESULT_OK) {
            //for copy image path
            Uri path = data.getData();
            CropImage.activity(path).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(DoctorSignup.this);
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
}
