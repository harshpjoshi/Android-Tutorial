package teqvirtual.deep.healthcare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapter.CityAdapter;
import Adapter.DoctorTypeAdapter;
import Helper.AsyncResponse;
import Helper.Utils;
import Helper.WebserviceCall;
import Model.CityModel;
import Model.DoctorTypeModel;


public class DoctorRegistration extends AppCompatActivity {

    GoogleMap map;
    double lat,longi,new_lat,new_log;
    Marker marker;
    String city_holder,doctortype_holder;
    ArrayList<CityModel> arrayList=new ArrayList<>();
    Bitmap bitmap;
    ImageView sub_image;


    String subscriber_id,email,firstname,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        getSupportActionBar().hide();

        final EditText address=(EditText)findViewById(R.id.input_address_login_doc);

        final EditText firstname_sub,lastname_sub,email_sub,password_sub,contact_sub,address_sub;
        final Spinner city_sub,doc_type;

        firstname_sub=(EditText)findViewById(R.id.input_firstname_login_doc);
        lastname_sub=(EditText)findViewById(R.id.input_lastname_login_doc);
        email_sub=(EditText)findViewById(R.id.input_email_login_doc);
        password_sub=(EditText)findViewById(R.id.input_pasword_login_doc);
        contact_sub=(EditText)findViewById(R.id.input_contactno_login_doc);
        sub_image=(ImageView) findViewById(R.id.logo_doc);
        city_sub=(Spinner)findViewById(R.id.city_doc);
        doc_type=(Spinner)findViewById(R.id.doc_type);
        address_sub=(EditText)findViewById(R.id.input_address_login_doc);
        Button button=(Button)findViewById(R.id.login_btn_doc);


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


                                Toast.makeText(DoctorRegistration.this, ""+location, Toast.LENGTH_SHORT).show();
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


        final String url="http://teqvirtual.com/healthcare/index.php";

        String c_key[]=new String[]{"action"};
        String c_value[]=new String[]{"city_view"};

        String c_jsonRequest= Utils.createJsonRequest(c_key,c_value);

        new WebserviceCall(DoctorRegistration.this, url, c_jsonRequest, "Loading...", true, new AsyncResponse() {
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

                    CityAdapter cityAdapter=new CityAdapter(DoctorRegistration.this,arrayList);
                    city_sub.setAdapter(cityAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();

        city_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                city_holder=arrayList.get(position).getId();

                Geocoder geocoder=new Geocoder(DoctorRegistration.this);
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

        new WebserviceCall(DoctorRegistration.this, url, docjsonReq, "Loading...", false, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                ArrayList<DoctorTypeModel> arrayList=new ArrayList<>();
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int j=0;j<jsonArray.length();j++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(j);
                        String doctor_type_id=jsonObject1.getString("doctor_type_id");
                        String name=jsonObject1.getString("name");

                        DoctorTypeModel doctorTypeModel=new DoctorTypeModel();
                        doctorTypeModel.setId(doctor_type_id);
                        doctorTypeModel.setName(name);

                        arrayList.add(doctorTypeModel);
                    }

                    DoctorTypeAdapter doctorTypeAdapter=new DoctorTypeAdapter(getApplicationContext(),arrayList);
                    doc_type.setAdapter(doctorTypeAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();

        doc_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                doctortype_holder=arrayList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fname_holder=firstname_sub.getText().toString().trim();
                String lname_holder=lastname_sub.getText().toString().trim();
                String email_holder=email_sub.getText().toString().trim();
                String password_holder=password_sub.getText().toString().trim();
                String contact_holder=contact_sub.getText().toString().trim();
                String image_holder=getStringImage(bitmap);
                String address_holder=address_sub.getText().toString().trim();

                if (fname_holder.isEmpty())
                {
                    Toast.makeText(DoctorRegistration.this, "Firstname Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (lname_holder.isEmpty())
                {
                    Toast.makeText(DoctorRegistration.this, "Lastname Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (email_holder.isEmpty())
                {
                    Toast.makeText(DoctorRegistration.this, "Email Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (password_holder.isEmpty())
                {
                    Toast.makeText(DoctorRegistration.this, "Password Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (contact_holder.isEmpty())
                {
                    Toast.makeText(DoctorRegistration.this, "Mobileno Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (image_holder.isEmpty())
                {
                    Toast.makeText(DoctorRegistration.this, "Please Choose Profile Image", Toast.LENGTH_SHORT).show();
                }
                else if (address_holder.isEmpty())
                {
                    Toast.makeText(DoctorRegistration.this, "Address Can't Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String key[]=new String[]{"action","firstname","lastname","email","mobileno","image","password","city","address","type","latitude","longitude"};
                    String[] value=new String[]{"doctor_registration",fname_holder,lname_holder,email_holder,contact_holder,image_holder,password_holder,city_holder,address_holder,doctortype_holder,new_lat+"",new_log+""};

                    String jsonRequest=Utils.createJsonRequest(key,value);

                    new WebserviceCall(DoctorRegistration.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
                        @Override
                        public void onCallback(String response) {

                            try {

                                JSONObject jsonObject=new JSONObject(response);

                                Toast.makeText(DoctorRegistration.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                JSONArray jsonArray=jsonObject.getJSONArray("data");



                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    subscriber_id=jsonObject1.getString("doctor_id");
                                    email=jsonObject1.getString("email");
                                    firstname=jsonObject1.getString("firstname");
                                    image=jsonObject1.getString("image");

                                    Intent intent=new Intent(DoctorRegistration.this,ProfileDoctor.class);
                                    intent.putExtra("id",subscriber_id);
                                    intent.putExtra("email",email);
                                    intent.putExtra("name",firstname);
                                    intent.putExtra("image",image);
                                    startActivity(intent);

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }).execute();
                }
            }
        });

        sub_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == this.RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                sub_image.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                sub_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void fileChooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
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
