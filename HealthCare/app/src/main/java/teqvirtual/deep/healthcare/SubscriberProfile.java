package teqvirtual.deep.healthcare;


import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ss.com.bannerslider.Slider;
import teqvirtual.deep.healthcare.Adapter.CategoryAdapter;
import teqvirtual.deep.healthcare.Adapter.HotsellerAdapter;
import teqvirtual.deep.healthcare.Adapter.NearByDoctorAdapter;
import teqvirtual.deep.healthcare.Adapter.NearByHospitalAdapter;
import teqvirtual.deep.healthcare.Adapter.SubscriberAdapter;
import teqvirtual.deep.healthcare.Adapter.TipsAdapter;
import teqvirtual.deep.healthcare.Adapter.TrendingProductAdapter;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;
import teqvirtual.deep.healthcare.Model.DoctorModel;
import teqvirtual.deep.healthcare.Model.HospitalModel;
import teqvirtual.deep.healthcare.Model.MainCategoryModel;
import teqvirtual.deep.healthcare.Model.MedicineModel;
import teqvirtual.deep.healthcare.Model.SubscriberModel;
import teqvirtual.deep.healthcare.Model.TipsModel;
import teqvirtual.deep.healthcare.SliderImage.MainSliderAdapter;
import teqvirtual.deep.healthcare.SliderImage.PicassoImageLoadingService;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubscriberProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    String url="https://teqvirtual.com/healthcare/index.php";
    LocationManager locationManager;
    double start_lat, start_longi;
    TextView textCartItemCount;
    String counter;
    int mCartItemCount;

    ArrayList<DoctorModel> mUser=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_profile);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        String userid=pref.getString("userid", null);
        String profile_image=pref.getString("image",null);
        String profile_name=pref.getString("name",null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);






        MaterialCardView smscard=(MaterialCardView)findViewById(R.id.sms_card);

        smscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SubscriberProfile.this,DoctorMessageTab.class);
                startActivity(intent);

            }
        });


        MaterialCardView suggest_medical_card=(MaterialCardView)findViewById(R.id.suggest_medical_card);

        suggest_medical_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SubscriberProfile.this,SuggestMedicalstore.class);
                startActivity(intent);

            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(location);


        String key2[]=new String[]{"action","subscriber"};
        String value2[]=new String[]{"like_count",userid};

        String medicin_count=Utils.createJsonRequest(key2,value2);

        new WebserviceCall(SubscriberProfile.this, url, medicin_count, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        counter=jsonObject1.getString("COUNT(id)");

                        mCartItemCount= Integer.parseInt(counter);
                        setBadge();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();


        Slider.init(new PicassoImageLoadingService(SubscriberProfile.this));
        Slider slider = findViewById(R.id.banner_slider1);
        slider.setAdapter(new MainSliderAdapter());

        String key1[]=new String[]{"action","latitude","logitude"};
        String val2[]=new String[]{"near_doctor",start_lat+"",start_longi+""};

        String jsonReq1=Utils.createJsonRequest(key1,val2);

        RecyclerView nearby_doctor=(RecyclerView)findViewById(R.id.near_doctor);

        new WebserviceCall(SubscriberProfile.this, url, jsonReq1, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                ArrayList<DoctorModel> doctorModelArrayList=new ArrayList<>();
                try {

                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {

                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        String id=jsonObject1.getString("doctor_id");
                        String f_doctor_id=jsonObject1.getString("f_doctor_id");
                        String firstname=jsonObject1.getString("firstname");
                        String lastname=jsonObject1.getString("lastname");
                        String image=jsonObject1.getString("image");
                        String address=jsonObject1.getString("address");
                        String city=jsonObject1.getString("city");
                        String type=jsonObject1.getString("type");
                        String email=jsonObject1.getString("email");
                        String password=jsonObject1.getString("password");
                        String mobileno=jsonObject1.getString("mobileno");
                        String latitude=jsonObject1.getString("latitude");
                        String longitude=jsonObject1.getString("longitude");

                        DoctorModel doctorModel=new DoctorModel();
                        doctorModel.setId(id);
                        doctorModel.setF_doctor_id(f_doctor_id);
                        doctorModel.setFirstname(firstname);
                        doctorModel.setLastname(lastname);
                        doctorModel.setImage(image);
                        doctorModel.setAddress(address);
                        doctorModel.setCity(city);
                        doctorModel.setType(type);
                        doctorModel.setEmail(email);
                        doctorModel.setPassword(password);
                        doctorModel.setMobileno(mobileno);
                        doctorModel.setLatitude(latitude);
                        doctorModel.setLongitude(longitude);

                        doctorModelArrayList.add(doctorModel);

                    }

                    NearByDoctorAdapter nearByDoctorAdapter=new NearByDoctorAdapter(getApplicationContext(),doctorModelArrayList);
                    nearby_doctor.setAdapter(nearByDoctorAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();



        String keys[]=new String[]{"action"};
        String values[]=new String[]{"maincat_view"};

        String jsonrequest= Utils.createJsonRequest(keys,values);

        final RecyclerView category_view=(RecyclerView)findViewById(R.id.category);

        new WebserviceCall(SubscriberProfile.this, url, jsonrequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {


                ArrayList<MainCategoryModel> arrayList=new ArrayList<>();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String id=jsonObject1.getString("maincat_id");
                        String name=jsonObject1.getString("name");
                        String image=jsonObject1.getString("image");

                        MainCategoryModel mainCategoryModel=new MainCategoryModel();
                        mainCategoryModel.setId(id);
                        mainCategoryModel.setName(name);
                        mainCategoryModel.setImage(image);

                        arrayList.add(mainCategoryModel);
                    }


                    CategoryAdapter categoryAdapter=new CategoryAdapter(SubscriberProfile.this,arrayList);
                    category_view.setAdapter(categoryAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();


        final RecyclerView nearby_hos=(RecyclerView)findViewById(R.id.nhos);

        String hos_keys[]=new String[]{"action","latitude","logitude"};
        String hos_values[]=new String[]{"near_by_hospital",start_lat+"",start_longi+""};

        String hos_jsonrequest=Utils.createJsonRequest(hos_keys,hos_values);

        new WebserviceCall(SubscriberProfile.this, url, hos_jsonrequest, "Loading...", false, new AsyncResponse() {
            @Override
            public void onCallback(String response) {


                ArrayList<HospitalModel> arrayList=new ArrayList<>();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String id=jsonObject1.getString("hospital_id");
                        String name=jsonObject1.getString("hospital_name");
                        String description=jsonObject1.getString("description");
                        String mobileno=jsonObject1.getString("mobileno");
                        String type=jsonObject1.getString("type");
                        String image=jsonObject1.getString("image");
                        String city=jsonObject1.getString("city");
                        String opentime=jsonObject1.getString("opentime");
                        String closetime=jsonObject1.getString("closetime");

                        HospitalModel hospitalModel=new HospitalModel();
                        hospitalModel.setId(id);
                        hospitalModel.setName(name);
                        hospitalModel.setDescription(description);
                        hospitalModel.setMobileno(mobileno);
                        hospitalModel.setType(type);
                        hospitalModel.setImage(image);
                        hospitalModel.setCity(city);
                        hospitalModel.setOpentime(opentime);
                        hospitalModel.setClosetime(closetime);

                        arrayList.add(hospitalModel);
                    }

                    NearByHospitalAdapter nearByHospitalAdapter=new NearByHospitalAdapter(SubscriberProfile.this,arrayList);
                    nearby_hos.setAdapter(nearByHospitalAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();


        final RecyclerView hotseller=(RecyclerView)findViewById(R.id.hotsell);


        String hotsel_keys[]=new String[]{"action"};
        String hotsel_values[]=new String[]{"medicine_random"};

        String hotsel_jsonreq=Utils.createJsonRequest(hotsel_keys,hotsel_values);

        new WebserviceCall(SubscriberProfile.this, url, hotsel_jsonreq, "Loading...", false, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                ArrayList<MedicineModel> arrayList=new ArrayList<>();
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        String id=jsonObject1.getString("medicine_id");
                        String barcode=jsonObject1.getString("barcode");
                        String description=jsonObject1.getString("description");
                        String image=jsonObject1.getString("image");
                        String price=jsonObject1.getString("price");
                        String power=jsonObject1.getString("power");
                        String name=jsonObject1.getString("name");
                        String subcat=jsonObject1.getString("subcat");

                        MedicineModel medicineModel=new MedicineModel();
                        medicineModel.setId(id);
                        medicineModel.setBarcode(barcode);
                        medicineModel.setDescription(description);
                        medicineModel.setImage(image);
                        medicineModel.setPrice(price);
                        medicineModel.setPower(power);
                        medicineModel.setName(name);
                        medicineModel.setSubcat(subcat);

                        arrayList.add(medicineModel);

                    }

                    HotsellerAdapter hotsellerAdapter=new HotsellerAdapter(SubscriberProfile.this,arrayList);
                    hotseller.setAdapter(hotsellerAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).execute();



        final RecyclerView trending_prod=(RecyclerView)findViewById(R.id.trending);

        String trend_keys[]=new String[]{"action"};
        String trend_values[]=new String[]{"medicine_random"};

        String trend_jsonreq=Utils.createJsonRequest(trend_keys,trend_values);

        new WebserviceCall(SubscriberProfile.this, url, trend_jsonreq, "Loading...", false, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                ArrayList<MedicineModel> arrayList=new ArrayList<>();
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        String id=jsonObject1.getString("medicine_id");
                        String barcode=jsonObject1.getString("barcode");
                        String description=jsonObject1.getString("description");
                        String image=jsonObject1.getString("image");
                        String price=jsonObject1.getString("price");
                        String power=jsonObject1.getString("power");
                        String name=jsonObject1.getString("name");
                        String subcat=jsonObject1.getString("subcat");

                        MedicineModel medicineModel=new MedicineModel();
                        medicineModel.setId(id);
                        medicineModel.setBarcode(barcode);
                        medicineModel.setDescription(description);
                        medicineModel.setImage(image);
                        medicineModel.setPrice(price);
                        medicineModel.setPower(power);
                        medicineModel.setName(name);
                        medicineModel.setSubcat(subcat);

                        arrayList.add(medicineModel);

                    }

                    TrendingProductAdapter trendingProductAdapter=new TrendingProductAdapter(SubscriberProfile.this,arrayList);
                    trending_prod.setAdapter(trendingProductAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).execute();


        final RecyclerView tips_view=(RecyclerView)findViewById(R.id.tips);

        String tips_keys[]=new String[]{"action"};
        String tips_value[]=new String[]{"tips_view"};

        String tips_request=Utils.createJsonRequest(tips_keys,tips_value);

        new WebserviceCall(SubscriberProfile.this, url, tips_request, "Loading...", false, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                ArrayList<TipsModel> arrayList=new ArrayList<>();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String id=jsonObject1.getString("tips_id");
                        String doctor_id=jsonObject1.getString("doctor_id");
                        String image=jsonObject1.getString("tips_image");
                        String description=jsonObject1.getString("description");

                        TipsModel tipsModel=new TipsModel();
                        tipsModel.setId(id);
                        tipsModel.setDoctor_id(doctor_id);
                        tipsModel.setDescription(description);
                        tipsModel.setImage(image);

                        arrayList.add(tipsModel);
                    }

                    TipsAdapter tipsAdapter=new TipsAdapter(SubscriberProfile.this,arrayList);
                    tips_view.setAdapter(tipsAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).execute();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hview=navigationView.getHeaderView(0);
        final ImageView profileimage=(ImageView)hview.findViewById(R.id.profile);
        final TextView profile_name2=(TextView)hview.findViewById(R.id.profileName);


        Picasso.get().load(profile_image).into(profileimage);
        profile_name2.setText(profile_name);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        toggle.setHomeAsUpIndicator(R.drawable.ic_iconfinder_menu_alt_134216);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.nav_logout)
        {

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.remove("userid");
            editor.remove("name");
            editor.remove("image");
            editor.commit();

            startActivity(new Intent(SubscriberProfile.this,Login.class));
        }
        else if(id==R.id.nav_profile)
        {
            startActivity(new Intent(SubscriberProfile.this,SubscriberEditprofile.class));
        }
        else if (id==R.id.nav_doctor)
        {
            startActivity(new Intent(SubscriberProfile.this,DoctorList.class));
        }
        else if (id==R.id.nav_hospital)
        {
            startActivity(new Intent(SubscriberProfile.this,HospitalList.class));
        }
        else if (id==R.id.nav_medicalstore)
        {
            startActivity(new Intent(SubscriberProfile.this,MedicalstoreList.class));
        }
        else if (id==R.id.nav_mysugget)
        {
            startActivity(new Intent(SubscriberProfile.this,SuggestedStore.class));
        }
        else if (id==R.id.nav_scan)
        {
            startActivity(new Intent(SubscriberProfile.this,QrscanActivity.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        start_lat=location.getLatitude();
        start_longi=location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void setBadge()
    {
        if(textCartItemCount!=null)
        {
            if (mCartItemCount==0)
            {
                if(textCartItemCount.getVisibility() != View.GONE)
                {
                    textCartItemCount.setVisibility(View.GONE);
                }
            }
            else
            {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount,99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        final MenuItem menuItem=menu.findItem(R.id.action_cart);
        View actionview= MenuItemCompat.getActionView(menuItem);
        textCartItemCount=(TextView)actionview.findViewById(R.id.cart_badge);

        actionview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);

                startActivity(new Intent(SubscriberProfile.this,MyCart.class));

            }
        });

        return true;
    }
}
