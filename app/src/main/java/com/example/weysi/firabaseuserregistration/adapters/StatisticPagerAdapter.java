package com.example.weysi.firabaseuserregistration.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.weysi.firabaseuserregistration.fragments.LastHourStatisticFragment;
import com.example.weysi.firabaseuserregistration.fragments.TotalStatisticFragment;

/**
 * Created by Enes on 8.05.2018.
 */

public class StatisticPagerAdapter extends FragmentPagerAdapter {

    private int witdh;

    public StatisticPagerAdapter(FragmentManager fm,int witdh) {

        super(fm);
        this.witdh=witdh;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                Bundle bundle = new Bundle();
                bundle.putInt("width",witdh);
                LastHourStatisticFragment lastHourStatisticFragment = new LastHourStatisticFragment();
                lastHourStatisticFragment.setArguments(bundle);
                return lastHourStatisticFragment;
            case 1:
                Bundle bundle2 = new Bundle();
                bundle2.putInt("width",witdh);
                TotalStatisticFragment totalStatisticFragment = new TotalStatisticFragment();
                totalStatisticFragment.setArguments(bundle2);
                return totalStatisticFragment;
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
                return "Son Saatler";

            case 1:
                return"Tüm Zamanlar";
            default:
                return null;
        }

    }
}
