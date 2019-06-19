package teqvirtual.deep.healthcare.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import teqvirtual.deep.healthcare.Fragments.Doctor;
import teqvirtual.deep.healthcare.Fragments.DoctorChats;
import teqvirtual.deep.healthcare.Fragments.Requests;
import teqvirtual.deep.healthcare.Fragments.Subscriber;

public class SubscriberPagerAdapter extends FragmentPagerAdapter {

    Fragment fragment;

    public SubscriberPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {


        if (position==0)
        {
            fragment=new Doctor();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        if (position==0)
        {
            return "Doctors";
        }

        return null;
    }
}