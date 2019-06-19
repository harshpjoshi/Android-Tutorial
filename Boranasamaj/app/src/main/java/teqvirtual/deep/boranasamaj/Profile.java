package teqvirtual.deep.boranasamaj;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.Timer;


public class Profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    String[] data={"Member","News","Event","Gallery","Advertise"};
    int img[]={R.drawable.man_user,R.drawable.text_lines,R.drawable.calendar,R.drawable.picture,R.drawable.ads};
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
       // getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();




        GridView gridView=(GridView)findViewById(R.id.gridData);
        CustomAdapter customAdapter=new CustomAdapter(Profile.this,data,img);
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position==0)
                {
                    startActivity(new Intent(Profile.this,ViewMember.class));
                }
                if(position==1)
                {
                    startActivity(new Intent(Profile.this,News.class));
                }
                if(position==2)
                {
                    startActivity(new Intent(Profile.this,Event.class));
                }
                if(position==3)
                {
                    startActivity(new Intent(Profile.this,Gallery.class));
                }
                if(position==4)
                {
                    startActivity(new Intent(Profile.this,Advertise.class));
                }
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        if (id==R.id.nav_home)
        {
            startActivity(new Intent(Profile.this,Home.class));
        }
        if (id==R.id.nav_About)
        {
            startActivity(new Intent(Profile.this,Aboutus.class));
        }
        if (id==R.id.nav_contactus)
        {
            startActivity(new Intent(Profile.this,Contactus.class));
        }
        if (id==R.id.nav_member)
        {
            startActivity(new Intent(Profile.this,ViewMember.class));
        }
        if (id==R.id.nav_news)
        {
            startActivity(new Intent(Profile.this,News.class));
        }
        if (id==R.id.nav_event)
        {
            startActivity(new Intent(Profile.this,Event.class));
        }
        if(id==R.id.nav_gallery)
        {
            startActivity(new Intent(Profile.this,Gallery.class));
        }
        if(id==R.id.nav_advertise)
        {
            startActivity(new Intent(Profile.this,Advertise.class));
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
