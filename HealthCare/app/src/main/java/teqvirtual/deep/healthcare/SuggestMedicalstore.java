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
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuggestMedicalstore extends AppCompatActivity {

    GoogleMap map;
    Bitmap bitmap;
    double lat,longi,new_lat,new_log;
    Marker marker;
    Spinner city_sug;
    TextInputEditText name_sug,address_sug;
    String city_holder;
    ImageView sub_profile;
    Uri imageUri;
    String url="https://teqvirtual.com/healthcare/index.php";
    ArrayList<CityModel> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_medicalstore);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MaterialButton materialButton=(MaterialButton)findViewById(R.id.sug_btn);
        name_sug=(TextInputEditText)findViewById(R.id.medicalname_sug);
        address_sug=(TextInputEditText)findViewById(R.id.address_sug);
        city_sug=(Spinner)findViewById(R.id.city_sug);
        sub_profile=(ImageView)findViewById(R.id.profile_sug);

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


                                Toast.makeText(SuggestMedicalstore.this, ""+location, Toast.LENGTH_SHORT).show();
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

        new WebserviceCall(SuggestMedicalstore.this, url, c_jsonRequest, "Loading...", true, new AsyncResponse() {
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

                    CityAdapter cityAdapter=new CityAdapter(SuggestMedicalstore.this,arrayList);
                    city_sug.setAdapter(cityAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();

        city_sug.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                city_holder=arrayList.get(position).getId();

                Geocoder geocoder=new Geocoder(SuggestMedicalstore.this);
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

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sub_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                startActivityForResult(chooser,5);
            }
        });


        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String imageholder=getStringImage(bitmap);
                String nameholder=name_sug.getText().toString().trim();
                String addressholder=address_sug.getText().toString().trim();


                if (imageholder.isEmpty())
                {
                    alertDialog("Select Any Image of Medicalstore");
                }
                else if (nameholder.isEmpty())
                {
                    alertDialog("Name Not Empty");
                }
                else if (addressholder.isEmpty())
                {
                    alertDialog("Address Not Empty");
                }
                else if (city_holder.isEmpty())
                {
                    alertDialog("City Not Empty");
                }
                else
                {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

                    String userid=pref.getString("userid", null);



                    String key[]=new String[]{"action","smedical_name","subscriber","city","address","latitude","longitude","image"};
                    String[] value=new String[]{"smedical_add",nameholder,userid,city_holder,addressholder,new_lat+"",new_log+"",imageholder};

                    String jsonRequest=Utils.createJsonRequest(key,value);

                    new WebserviceCall(SuggestMedicalstore.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
                        @Override
                        public void onCallback(String response) {

                            try {
                                JSONObject jsonObject=new JSONObject(response);

                                Toast.makeText(SuggestMedicalstore.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }).execute();

                }

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && resultCode == RESULT_OK) {
            //for copy image path
            Uri path = data.getData();
            CropImage.activity(path).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(SuggestMedicalstore.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Picasso.get().load(imageUri).into(sub_profile);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        if (id==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
