package com.dhanvi.fashionfusion1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dhanvi.fashionfusion1.databinding.ActivityNavMainBinding;

public class
nav_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ActivityNavMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_main);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_nav_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String[] findservices={"Hot","New","Top"};
        String[] useservices={"swime","Yoga","•Summer","Fitness","Beach"};

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setOnClickListener();
    }

    private void setOnClickListener() {
        binding.main.layout.globalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startListActivity(Cat.GLOBAL);
            }
        });

        binding.main.layout.trendingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startListActivity(Cat.TRENDING);
            }
        });
        binding.main.layout.styleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startListActivity(Cat.STYLE);
            }
        });
        binding.main.layout.occasionsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startListActivity(Cat.OCASIONS);
            }
        });
    }

    private void startListActivity(String category) {
        Intent intent = new Intent(this,grid_view.class);
        intent.putExtra(Cat.category,category);
        startActivity(intent);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_main, menu);
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

        if (id == R.id.nav_Global)
        {
            startListActivity(Cat.GLOBAL);

        }
        else if (id == R.id.nav_Trending)
        {
            startListActivity(Cat.TRENDING);

        }
        else if (id == R.id.nav_Styles)
        {
            startListActivity(Cat.STYLE);

        }
        else if (id == R.id.nav_Occasions)
        {
            startListActivity(Cat.OCASIONS);

        }
        else if (id == R.id.nav_nearby)
        {
            Intent i = new Intent(getApplicationContext(),category.class);
            startActivity(i);
        }
        else if (id == R.id.nav_logout)
        {
            SharedPreferences preferences =getSharedPreferences("mypref",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            Intent i = new Intent(getApplicationContext(), fashion.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}