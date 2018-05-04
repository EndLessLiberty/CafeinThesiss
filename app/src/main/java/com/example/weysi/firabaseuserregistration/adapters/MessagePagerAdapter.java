package com.example.weysi.firabaseuserregistration.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.weysi.firabaseuserregistration.fragments.ChatFriendsFragment;
import com.example.weysi.firabaseuserregistration.fragments.FriendsFragment;

public class MessagePagerAdapter extends FragmentPagerAdapter {

    String tuid;

    public MessagePagerAdapter(FragmentManager fm,String tuid) {
        super(fm);
        this.tuid=tuid;
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:

                ChatFriendsFragment chatFriendsFragment = new ChatFriendsFragment();
                Bundle bundle = new Bundle();

                bundle.putString("targetUserId",tuid);
                // set Fragmentclass Arguments
                chatFriendsFragment.setArguments(bundle);
                return chatFriendsFragment;

            case 1:
                FriendsFragment friendsFragment2 = new FriendsFragment();
                Bundle bundle2 = new Bundle();

                bundle2.putString("targetUserId",tuid);
                // set Fragmentclass Arguments
                friendsFragment2.setArguments(bundle2);
                return friendsFragment2;

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
                return "Sohbetler";

            case 1:
                return "Arkadaşlar";

            default:
                return null;
        }

    }
}
