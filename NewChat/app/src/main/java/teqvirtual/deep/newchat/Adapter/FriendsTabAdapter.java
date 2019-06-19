package teqvirtual.deep.newchat.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import teqvirtual.deep.newchat.Fragments.AvailableFriendFragment;
import teqvirtual.deep.newchat.Fragments.YourFriendsFragment;

public class FriendsTabAdapter extends FragmentPagerAdapter {
    public FriendsTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new YourFriendsFragment();
        }
        else if (position == 1)
        {
            fragment = new AvailableFriendFragment();
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
            return "Friends";
        }
        if (position==1)
        {
            return  "Available Users";
        }
        else
        {
            return null;
        }
    }
}
