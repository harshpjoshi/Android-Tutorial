package teqvirtual.deep.firebasechatapp.Utilities;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import teqvirtual.deep.firebasechatapp.MainWindow.Freindtab;
import teqvirtual.deep.firebasechatapp.MainWindow.RequestTab;

public class SectionPagerAdapter extends FragmentPagerAdapter {


    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                RequestTab requestTab=new RequestTab();
                return requestTab;

            case 1:
                Freindtab freindtab=new Freindtab();
                return freindtab;

                default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position)
        {
            case 0:
                return "Request";
            case 1:
                return "Friends";
                default:
                    return null;
        }
    }
}
