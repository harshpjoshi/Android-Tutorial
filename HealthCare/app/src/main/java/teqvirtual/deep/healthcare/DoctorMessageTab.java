package teqvirtual.deep.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import teqvirtual.deep.healthcare.Adapter.PagerAdapter;
import teqvirtual.deep.healthcare.Adapter.SubscriberPagerAdapter;
import teqvirtual.deep.healthcare.Fragments.DoctorChats;
import teqvirtual.deep.healthcare.Fragments.Subscriber;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

public class DoctorMessageTab extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_message_tab);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        TabLayout tabLayout=(TabLayout)findViewById(R.id.doc_message_tab);
        ViewPager viewPager=(ViewPager)findViewById(R.id.doctor_message_view_pager);
        viewPager.setAdapter(new SubscriberPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        if (id==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
