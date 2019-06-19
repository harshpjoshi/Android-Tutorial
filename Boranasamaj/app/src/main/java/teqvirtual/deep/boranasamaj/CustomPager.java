package teqvirtual.deep.boranasamaj;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class CustomPager extends FragmentPagerAdapter {
    FirstFragment firstFragment;
    SecondFragment secondFragment;
    ThirdFragment thirdFragment;
    public CustomPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if(position==0)
        {
           if(firstFragment==null)
           {
               firstFragment=new FirstFragment();
           }
           return firstFragment;
        }
        if(position==1)
        {
            if(secondFragment==null)
            {
                secondFragment=new SecondFragment();
            }
            return secondFragment;
        }
        if(position==2)
        {
            if(thirdFragment==null)
            {
                thirdFragment=new ThirdFragment();
            }
            return thirdFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
