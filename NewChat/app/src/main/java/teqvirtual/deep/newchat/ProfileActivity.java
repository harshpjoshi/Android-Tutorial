package teqvirtual.deep.newchat;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import teqvirtual.deep.newchat.Fragments.Chat;
import teqvirtual.deep.newchat.Fragments.Friends;
import teqvirtual.deep.newchat.Fragments.Notification;
import teqvirtual.deep.newchat.Fragments.Profile;


public class ProfileActivity extends AppCompatActivity {


    String current_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        String mobile = settings.getString("mobile_key", "");
        current_user=settings.getString("c_userid","");


        DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference();
        DatabaseReference onlineRef=rootRef.child("Users").child(current_user).child("screnn_status");
        onlineRef.setValue("Online");

        //screen status

        screenStatus();

        if (mobile.equals("")) {
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        }

        loadFragment(new Chat(),current_user);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment;

            if (item.getItemId() == R.id.messages) {
                fragment = new Chat();
                Log.d("profff","idddd2"+current_user);
                loadFragment(fragment,current_user);
                return true;
            }
            if (item.getItemId() == R.id.notification) {
                fragment = new Notification();
                Log.d("profff","idddd2"+current_user);
                loadFragment(fragment,current_user);
                return true;
            }
            if (item.getItemId() == R.id.friends) {
                fragment = new Friends();
                Log.d("profff","idddd2"+current_user);
                loadFragment(fragment,current_user);
                return true;
            }
            if (item.getItemId() == R.id.myaccount) {
                fragment = new Profile();
                Log.d("profff","idddd2"+current_user);
                loadFragment(fragment,current_user);
                return true;
            } else {
                return false;
            }

        }

    };

    private void loadFragment(Fragment fragment,String current_user) {
        // load fragment

        Log.d("profff","idddd"+current_user);

        Bundle bundle=new Bundle();
        bundle.putString("c_user",current_user);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void screenStatus()
    {
        DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
        DatabaseReference onlineRef=rootRef.child("Users").child(current_user).child("screnn_status");
        String time = new SimpleDateFormat("hh : mm a", Locale.getDefault()).format(Calendar.getInstance().getTime());
        onlineRef.onDisconnect().setValue("last seen "+time);
    }
}
