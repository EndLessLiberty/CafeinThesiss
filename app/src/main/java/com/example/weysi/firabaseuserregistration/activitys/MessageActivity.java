package com.example.weysi.firabaseuserregistration.activitys;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.adapters.MessagePagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MessageActivity extends AppCompatActivity {
    private MessagePagerAdapter mMessagePagerAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Button aramaBtn;

    private FirebaseUser firebaseUser;
    private DatabaseReference mUserDatabase;
    private FirebaseAuth firebaseAuth;
    String myUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        firebaseAuth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
        mUserDatabase.child("online").setValue("true");

        aramaBtn=(Button)findViewById(R.id.aramaBtn);
        mTabLayout = (TabLayout) findViewById(R.id.message_mainTabs);
        mViewPager = (ViewPager) findViewById(R.id.message_viewPager);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        myUserID = firebaseUser.getUid();
        mMessagePagerAdapter = new MessagePagerAdapter(getSupportFragmentManager(), myUserID);

        mViewPager.setAdapter(mMessagePagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        aramaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this, SearchActivity.class));
            }
        });


    }


    @Override
    protected void onPause() {
        super.onPause();
        mUserDatabase.child("online").setValue(ServerValue.TIMESTAMP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUserDatabase.child("online").setValue("true");
    }
}