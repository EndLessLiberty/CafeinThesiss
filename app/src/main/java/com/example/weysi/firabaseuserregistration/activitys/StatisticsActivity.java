package com.example.weysi.firabaseuserregistration.activitys;

import android.graphics.Point;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;

import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.adapters.StatisticPagerAdapter;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener{

    private StatisticPagerAdapter mStatisticPagerAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ImageButton mBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;


        mTabLayout=(TabLayout) findViewById(R.id.statistic_mainTabs);
        mViewPager=(ViewPager) findViewById(R.id.statistic_viewPager);
        mBack=(ImageButton)findViewById(R.id.imageButtonBack) ;

        mStatisticPagerAdapter = new StatisticPagerAdapter(getSupportFragmentManager(),width);
        mViewPager.setAdapter(mStatisticPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);



        mBack.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if(v==mBack)
            finish();
    }
}
