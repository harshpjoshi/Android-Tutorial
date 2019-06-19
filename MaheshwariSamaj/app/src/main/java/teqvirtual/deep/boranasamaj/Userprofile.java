package teqvirtual.deep.boranasamaj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import Adapter.ProfileGrid;

public class Userprofile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String items[]={"Member","News","Event","Gallery","Advertise"};
    int items_imges[]={R.drawable.ic_man_user,R.drawable.ic_text_lines,R.drawable.ic_calendar,R.drawable.ic_picture,R.drawable.ic_advertising};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        toolbar.setTitleTextColor(getResources().getColor(R.color.font));
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        toggle.syncState();

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
                if(drawerLayout.isDrawerOpen(GravityCompat.START))
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView recyclerView;
        RecyclerView.Adapter adapter;
        RecyclerView.LayoutManager layoutManager;

        recyclerView=(RecyclerView)findViewById(R.id.profile_grid);
        layoutManager=new GridLayoutManager(Userprofile.this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ProfileGrid(Userprofile.this,items,items_imges);
        recyclerView.setAdapter(adapter);

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

        if (id == R.id.nav_user_member) {
            // Handle the camera action
            startActivity(new Intent(this,Member.class));
        } else if (id == R.id.nav_user_news) {
            startActivity(new Intent(this,News.class));
        } else if (id == R.id.nav_user_event) {
            startActivity(new Intent(this,Event.class));
        } else if (id == R.id.nav_user_advertise) {
            startActivity(new Intent(this,Advertise.class));
        } else if (id == R.id.nav_user_gallery) {
            startActivity(new Intent(this,Gallery.class));
        }
        else if (id == R.id.nav_user_about) {
            startActivity(new Intent(this,Aboutus.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
