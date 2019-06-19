package teqvirtual.deep.boranasamaj;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

public class AdminProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    String[] data={"Add Member","Add News","Add Event","Add Gallery","Add Advertise"};
    int img[]={R.drawable.man_user,R.drawable.text_lines,R.drawable.calendar,R.drawable.picture,R.drawable.ads};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        TextView textView=(TextView)findViewById(R.id.admin_name);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        GridView gridView=(GridView)findViewById(R.id.admin_grid);
        AdminDataAdapter admindataAdapter=new AdminDataAdapter(AdminProfile.this,data,img);
        gridView.setAdapter(admindataAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position==0)
                {
                    startActivity(new Intent(AdminProfile.this,AddMember.class));
                }
                if (position==1)
                {
                    startActivity(new Intent(AdminProfile.this,AddNews.class));
                }
                if(position==2)
                {
                    startActivity(new Intent(AdminProfile.this,AddEvent.class));
                }
                if (position==3)
                {
                    startActivity(new Intent(AdminProfile.this,AddGallery.class));
                }
                if (position==4)
                {
                    startActivity(new Intent(AdminProfile.this,AddAdverise.class));
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


        if (id==R.id.admin_nav_home)
        {
            startActivity(new Intent(AdminProfile.this,Home.class));
        }
        if (id==R.id.admin_nav_About)
        {
            startActivity(new Intent(AdminProfile.this,Aboutus.class));
        }
        if (id==R.id.admin_nav_contactus)
        {
            startActivity(new Intent(AdminProfile.this,Contactus.class));
        }
        if (id==R.id.admin_nav_member)
        {
            startActivity(new Intent(AdminProfile.this,ViewMember.class));
        }
        if (id==R.id.admin_nav_news)
        {
            startActivity(new Intent(AdminProfile.this,News.class));
        }
        if (id==R.id.admin_nav_event)
        {
            startActivity(new Intent(AdminProfile.this,Event.class));
        }
        if(id==R.id.admin_nav_gallery)
        {
            startActivity(new Intent(AdminProfile.this,Gallery.class));
        }
        if(id==R.id.admin_nav_advertise)
        {
            startActivity(new Intent(AdminProfile.this,Advertise.class));
        }
        if(id==R.id.nav_logout)
        {
            startActivity(new Intent(AdminProfile.this,Select.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
