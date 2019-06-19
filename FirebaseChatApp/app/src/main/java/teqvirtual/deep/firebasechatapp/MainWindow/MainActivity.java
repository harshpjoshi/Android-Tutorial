package teqvirtual.deep.firebasechatapp.MainWindow;


import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import teqvirtual.deep.firebasechatapp.Account_Setting_Activity;
import teqvirtual.deep.firebasechatapp.HomeActivity;
import teqvirtual.deep.firebasechatapp.R;
import teqvirtual.deep.firebasechatapp.SearchActivity;
import teqvirtual.deep.firebasechatapp.Utilities.SectionPagerAdapter;

public class MainActivity extends AppCompatActivity {

    String TAG="mainactivity";
    Context mContext=MainActivity.this;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    StorageReference mStorage;

    SectionPagerAdapter sectionPagerAdapter;

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    ImageView mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();


        toolbar=(Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        viewPager=(ViewPager) findViewById(R.id.container);
        tabLayout=(TabLayout) findViewById(R.id.tabs);

        mSearch=(ImageView)findViewById(R.id.search);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, SearchActivity.class));

            }
        });

        sectionPagerAdapter=new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.logout_btn)
        {
            user_logout();
        }
        if (item.getItemId() == R.id.settings_btn)
        {
            startActivity(new Intent(mContext, Account_Setting_Activity.class));
        }

        return true;
    }

    private void user_logout()
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(mContext, HomeActivity.class));
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user=mAuth.getCurrentUser();

        if (user==null)
        {
            user_logout();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }



    public void screenStatus(String sc_status)
    {
        mDatabaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("screnn_status",sc_status);

        mDatabaseReference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();

        screenStatus("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();

        screenStatus("Offline");
    }
}
