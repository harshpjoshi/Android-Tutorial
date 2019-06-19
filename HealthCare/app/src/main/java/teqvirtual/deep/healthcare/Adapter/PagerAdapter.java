package teqvirtual.deep.healthcare.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import teqvirtual.deep.healthcare.Fragments.DoctorChats;
import teqvirtual.deep.healthcare.Fragments.Requests;
import teqvirtual.deep.healthcare.Fragments.Subscriber;

public class PagerAdapter extends FragmentPagerAdapter {

    Fragment fragment;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {


        if (position==0)
        {
            fragment=new Requests();
        }
        else if (position==1)
        {
            fragment=new Subscriber();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        if (position==0)
        {
            return "Requests";
        }
        else if (position==1)
        {
            return "Subscribers";
        }
        return null;
    }
}