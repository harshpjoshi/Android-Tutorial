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

public class Profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LocationListener {

    int ads_img[]={R.drawable.banner1,R.drawable.banner2,R.drawable.banner3};
    int fun_img[]={R.drawable.ic_barcode_color,R.drawable.ic_search_black_24dp};
    String[] fun_txt={"Scan Medicine","Search Medicine"};
    TextView textCartItemCount;
    int mCartItemCount;
    String counter;
    String userid;
    LocationManager locationManager;
    double start_lat,start_longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
        final String name_p=intent.getStringExtra("name");
        final String email_p=intent.getStringExtra("email");
        final String image_p=intent.getStringExtra("image");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hview=navigationView.getHeaderView(0);
        final ImageView profileimage=(ImageView)hview.findViewById(R.id.pro_image);
        final TextView profile_name=(TextView)hview.findViewById(R.id.pro_name);
        final TextView profile_email=(TextView)hview.findViewById(R.id.pro_email);

        Picasso.get().load(image_p).into(profileimage);
        profile_email.setText(email_p);
        profile_name.setText(name_p);

        RecyclerView ads_banner=(RecyclerView)findViewById(R.id.ads_banner);
        AdvertiseAdapter advertiseAdapter=new AdvertiseAdapter(Profile.this,ads_img);
        ads_banner.setAdapter(advertiseAdapter);

        RecyclerView function_view=(RecyclerView)findViewById(R.id.function);
        FunctionAdapter functionAdapter=new FunctionAdapter(Profile.this,fun_img,fun_txt);
        function_view.setAdapter(functionAdapter);


        String keys[]=new String[]{"action"};
        String values[]=new String[]{"maincat_view"};

        String url="http://teqvirtual.com/healthcare/index.php";

        String jsonrequest=Utils.createJsonRequest(keys,values);

        final RecyclerView category_view=(RecyclerView)findViewById(R.id.category);

        new WebserviceCall(Profile.this, url, jsonrequest, "Loading...", true, new AsyncResponse() {
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


                    CategoryAdapter categoryAdapter=new CategoryAdapter(Profile.this,arrayList,userid);
                    category_view.setAdapter(categoryAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();

        final RecyclerView nearby_hos=(RecyclerView)findViewById(R.id.near_hospital);

        String hos_keys[]=new String[]{"action","latitude","logitude"};
        String hos_values[]=new String[]{"near_by_hospital",start_lat+"",start_longi+""};

        String hos_jsonrequest=Utils.createJsonRequest(hos_keys,hos_values);

        new WebserviceCall(Profile.this, url, hos_jsonrequest, "Loading...", false, new AsyncResponse() {
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

                    NearByHospitalAdapter nearByHospitalAdapter=new NearByHospitalAdapter(Profile.this,arrayList);
                    nearby_hos.setAdapter(nearByHospitalAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();



        final RecyclerView hotseller=(RecyclerView)findViewById(R.id.hotseller_view);


        String hotsel_keys[]=new String[]{"action"};
        String hotsel_values[]=new String[]{"medicine_random"};

        String hotsel_jsonreq=Utils.createJsonRequest(hotsel_keys,hotsel_values);

        new WebserviceCall(Profile.this, url, hotsel_jsonreq, "Loading...", false, new AsyncResponse() {
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

                    HotsellerAdapter hotsellerAdapter=new HotsellerAdapter(Profile.this,arrayList,userid);
                    hotseller.setAdapter(hotsellerAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).execute();



        final RecyclerView trending_prod=(RecyclerView)findViewById(R.id.trending_view);

        String trend_keys[]=new String[]{"action"};
        String trend_values[]=new String[]{"medicine_random"};

        String trend_jsonreq=Utils.createJsonRequest(trend_keys,trend_values);

        new WebserviceCall(Profile.this, url, trend_jsonreq, "Loading...", false, new AsyncResponse() {
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

                    TrendingProductAdapter trendingProductAdapter=new TrendingProductAdapter(Profile.this,arrayList,userid);
                    trending_prod.setAdapter(trendingProductAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).execute();


        final RecyclerView tips_view=(RecyclerView)findViewById(R.id.tips_view);

        String tips_keys[]=new String[]{"action"};
        String tips_value[]=new String[]{"tips_view"};

        String tips_request=Utils.createJsonRequest(tips_keys,tips_value);

        new WebserviceCall(Profile.this, url, tips_request, "Loading...", false, new AsyncResponse() {
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
                        String image=jsonObject1.getString("image");
                        String description=jsonObject1.getString("description");

                        TipsModel tipsModel=new TipsModel();
                        tipsModel.setId(id);
                        tipsModel.setDoctor_id(doctor_id);
                        tipsModel.setDescription(description);
                        tipsModel.setImage(image);

                        arrayList.add(tipsModel);
                    }

                    TipsAdapter tipsAdapter=new TipsAdapter(Profile.this,arrayList);
                    tips_view.setAdapter(tipsAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).execute();

        final RecyclerView season_view=(RecyclerView)findViewById(R.id.season_view);

        String seasnal_keys[]=new String[]{"action"};
        String seasnal_values[]=new String[]{"medicine_random"};

        String seasnal_jsonreq=Utils.createJsonRequest(seasnal_keys,seasnal_values);

        new WebserviceCall(Profile.this, url, seasnal_jsonreq, "Loading...", false, new AsyncResponse() {
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

                    SeasonAdapter seasonAdapter=new SeasonAdapter(Profile.this,arrayList,userid);
                    season_view.setAdapter(seasonAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).execute();


        String key2[]=new String[]{"action","subscriber"};
        String value2[]=new String[]{"like_count",userid};

        String medicin_count=Utils.createJsonRequest(key2,value2);

        new WebserviceCall(Profile.this, url, medicin_count, "Loading...", true, new AsyncResponse() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        final MenuItem menuItem=menu.findItem(R.id.action_cart);
        View actionview=MenuItemCompat.getActionView(menuItem);
        textCartItemCount=(TextView)actionview.findViewById(R.id.cart_badge);

        actionview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
                Intent intent=new Intent(Profile.this,Bag.class);
                intent.putExtra("userid",userid);
                startActivity(intent);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action

        } else if (id == R.id.nav_order) {

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
