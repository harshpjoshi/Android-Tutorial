package teqvirtual.deep.fashionbook;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.ArrayAdapter;

import teqvirtual.deep.fashionbook.Adapter.CatAdapter;
import teqvirtual.deep.fashionbook.Adapter.ProdCategoryAdapter;
import teqvirtual.deep.fashionbook.Adapter.ShoesAdapter;

public class MainProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int cat_img[]={R.drawable.menb,R.drawable.womenb,R.drawable.kidsb};
    int prod_cat_img[]={R.drawable.footb,R.drawable.beltb,R.drawable.walletb,R.drawable.giftb,R.drawable.watchb};
    String prod_cat_name[]={"footwear","Belt","Wallet","Gifts","Watches"};
    int sho_img[]={R.drawable.shoe1,R.drawable.shoe2,R.drawable.shoe3,R.drawable.shoe4,R.drawable.shoe5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        RecyclerView catview=(RecyclerView)findViewById(R.id.category);
        CatAdapter catAdapter=new CatAdapter(MainProfileActivity.this,cat_img);
        catview.setAdapter(catAdapter);

        RecyclerView prodcatview=(RecyclerView)findViewById(R.id.prd_type);
        ProdCategoryAdapter prodCategoryAdapter=new ProdCategoryAdapter(MainProfileActivity.this,prod_cat_img,prod_cat_name);
        prodcatview.setAdapter(prodCategoryAdapter);

        RecyclerView shoesView=(RecyclerView)findViewById(R.id.shoe_view);
        ShoesAdapter shoesAdapter=new ShoesAdapter(MainProfileActivity.this,sho_img);
        shoesView.setAdapter(shoesAdapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
