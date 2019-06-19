package teqvirtual.deep.newchat.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import teqvirtual.deep.newchat.Adapter.NotificationTabAdapter;
import teqvirtual.deep.newchat.R;


public class Notification extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    NotificationTabAdapter notificationTabAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_notification, container, false);

        tabLayout=(TabLayout)view.findViewById(R.id.notification_tab);
        viewPager=(ViewPager)view.findViewById(R.id.notification_pager);

        notificationTabAdapter=new NotificationTabAdapter(getChildFragmentManager());
        viewPager.setAdapter(notificationTabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }


}
