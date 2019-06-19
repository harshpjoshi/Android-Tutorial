package teqvirtual.deep.healthcare;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.AdvertiseAdapter;
import Adapter.CategoryAdapter;
import Adapter.DocFunctionAdapter;
import Adapter.FunctionAdapter;
import Adapter.HotsellerAdapter;
import Adapter.NearByHospitalAdapter;
import Adapter.SeasonAdapter;
import Adapter.TipsAdapter;
import Adapter.TrendingProductAdapter;
import Helper.AsyncResponse;
import Helper.Utils;
import Helper.WebserviceCall;
import Model.HospitalModel;
import Model.MainCategoryModel;
import Model.MedicineModel;
import Model.TipsModel;

public class ProfileDoctor extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    int ads_img[]={R.drawable.banner1,R.drawable.banner2,R.drawable.banner3};
    int fun_img[]={R.drawable.ic_seo_tips,R.drawable.ic_person_outline_black_24dp};
    String[] fun_txt={"View Tips","Subscriber"};
    TextView textCartItemCount;
    int mCartItemCount;
    String counter;
    String userid;
    LocationManager locationManager;
    double start_lat,start_longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


        Intent intent=getIntent();
        userid=intent.getStringExtra("id");

        Toast.makeText(this, ""+userid, Toast.LENGTH_SHORT).show();

        final String name_p=intent.getStringExtra("name");
        final String email_p=intent.getStringExtra("email");
        final String image_p=intent.getStringExtra("image");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_doc);
        navigationView.setNavigationItemSelectedListener(this);
        View hview=navigationView.getHeaderView(0);
        final ImageView profileimage=(ImageView)hview.findViewById(R.id.pro_image_doc);
        final TextView profile_name=(TextView)hview.findViewById(R.id.pro_name_doc);
        final TextView profile_email=(TextView)hview.findViewById(R.id.pro_email_doc);

        Picasso.get().load(image_p).into(profileimage);
        profile_email.setText(email_p);
        profile_name.setText(name_p);

        String url="http://teqvirtual.com/healthcare/index.php";

        RecyclerView ads_banner=(RecyclerView)findViewById(R.id.ads_banner_doc);
        AdvertiseAdapter advertiseAdapter=new AdvertiseAdapter(ProfileDoctor.this,ads_img);
        ads_banner.setAdapter(advertiseAdapter);

        RecyclerView fun_doc=(RecyclerView)findViewById(R.id.function_doc);
        DocFunctionAdapter docFunctionAdapter=new DocFunctionAdapter(ProfileDoctor.this,fun_img,fun_txt);
        fun_doc.setAdapter(docFunctionAdapter);

        final RecyclerView nearby_hos=(RecyclerView)findViewById(R.id.near_hospital_doc);

        String hos_keys[]=new String[]{"action","latitude","logitude"};
        String hos_values[]=new String[]{"near_by_hospital",start_lat+"",start_longi+""};

        String hos_jsonrequest=Utils.createJsonRequest(hos_keys,hos_values);

        new WebserviceCall(ProfileDoctor.this, url, hos_jsonrequest, "Loading...", false, new AsyncResponse() {
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
                        String name=jsonObject1.getString("name");
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

                    NearByHospitalAdapter nearByHospitalAdapter=new NearByHospitalAdapter(ProfileDoctor.this,arrayList);
                    nearby_hos.setAdapter(nearByHospitalAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile_doc) {
            // Handle the camera action

        } else if (id == R.id.nav_order_doc) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}
