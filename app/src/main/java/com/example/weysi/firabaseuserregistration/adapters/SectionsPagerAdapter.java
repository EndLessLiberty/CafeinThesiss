package com.example.weysi.firabaseuserregistration.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.weysi.firabaseuserregistration.fragments.FriendsFragment;
import com.example.weysi.firabaseuserregistration.fragments.PersonelTimeLineFragment;

/**
 * Created by Enes on 20.04.2018.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    String tuid;

    public SectionsPagerAdapter(FragmentManager fm,String targetUserId) {
        super(fm);
        tuid=targetUserId;

    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                Bundle bundle = new Bundle();
                bundle.putString("targetUserId",tuid);
                // set Fragmentclass Arguments
                PersonelTimeLineFragment pTimeLineFragment = new PersonelTimeLineFragment();
                pTimeLineFragment.setArguments(bundle);
                return  pTimeLineFragment;

            case 1:
                FriendsFragment friendsFragment = new FriendsFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("targetUserId",tuid);
                // set Fragmentclass Arguments
                friendsFragment.setArguments(bundle2);
                return friendsFragment;

            default:
                return  null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){

        switch (position) {
            case 0:
                return "Zaman Tüneli";

            case 1:
                return "Arkadaşlar";

            default:
                return null;
        }

    }
}
