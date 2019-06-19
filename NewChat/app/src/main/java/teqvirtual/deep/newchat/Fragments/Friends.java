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
import teqvirtual.deep.newchat.Adapter.FriendsTabAdapter;
import teqvirtual.deep.newchat.R;


public class Friends extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    FriendsTabAdapter friendsTabAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_friends, container, false);

        tabLayout=(TabLayout)view.findViewById(R.id.friends_tab);
        viewPager=(ViewPager)view.findViewById(R.id.friends_pager);

        friendsTabAdapter=new FriendsTabAdapter(getChildFragmentManager());
        viewPager.setAdapter(friendsTabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

}
