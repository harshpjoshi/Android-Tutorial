package teqvirtual.deep.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import ss.com.bannerslider.Slider;
import teqvirtual.deep.healthcare.SliderImage.MainSliderAdapter;
import teqvirtual.deep.healthcare.SliderImage.PicassoImageLoadingService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class DoctorMainProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    MaterialCardView message,suggesttips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main_profile);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        String userid=pref.getString("doctorid", null);
        String profile_image=pref.getString("docimg",null);
        String profile_name=pref.getString("docname",null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        Slider.init(new PicassoImageLoadingService(DoctorMainProfile.this));
        Slider slider = findViewById(R.id.banner_slider1);
        slider.setAdapter(new MainSliderAdapter());

        message=(MaterialCardView)findViewById(R.id.sms_card);
        suggesttips=(MaterialCardView)findViewById(R.id.suggest_tips_card);


        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorMainProfile.this,SubscriberMessageTab.class));
            }
        });

        suggesttips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DoctorMainProfile.this,SuggestTips.class));

            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hview=navigationView.getHeaderView(0);
        final ImageView profileimage=(ImageView)hview.findViewById(R.id.profile_doc);
        final TextView profile_name2=(TextView)hview.findViewById(R.id.profileName_doc);


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

        if(id==R.id.nav_profile_doc)
        {
            startActivity(new Intent(DoctorMainProfile.this,DoctorEditProfile.class));
        }
        else if (id==R.id.nav_mysugget_doc)
        {
            startActivity(new Intent(DoctorMainProfile.this,SuggestedTips.class));
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
}
