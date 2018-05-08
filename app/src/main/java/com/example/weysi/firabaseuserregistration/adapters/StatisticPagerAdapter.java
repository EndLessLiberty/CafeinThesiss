package com.example.weysi.firabaseuserregistration.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Enes on 8.05.2018.
 */

public class StatisticPagerAdapter extends FragmentPagerAdapter {


    public StatisticPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;


        /*switch (position)
        {
            case 0:
                Bundle bundle =new Bundle();
                return;

            case 1:
                return;

            default:
                return  null;
        }*/
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){

        switch (position) {
            case 0:
                return "Son Saatler";

            case 1:
                return "Tüm Zamanlar";

            default:
                return null;
        }

    }
}
