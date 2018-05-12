package com.example.weysi.firabaseuserregistration.activitys;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.adapters.PlaceListAdapter;
import com.example.weysi.firabaseuserregistration.informations.GetTimeAgo;
import com.example.weysi.firabaseuserregistration.informations.TimeLineCheckInInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Enes on 30.04.2018.
 */



public class TimeLineActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView mTimeLineListView;

    private DatabaseReference mUserFriendsDatabase;
    private DatabaseReference mCheckInsDatabase;
    private ImageButton imageButtonBack;


    private String mCurrentUserId;
    private int mUserFriendCount;
    private String []friendsId;
    private List<TimeLineCheckInInformation> timeLineCheckInInformationList;


    private GetTimeAgo getTimeAgo;
    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        mCurrentUserId=(String)getIntent().getSerializableExtra("current_user_id");
        mUserFriendsDatabase=FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrentUserId);
        mUserFriendsDatabase.keepSynced(true);
        mCheckInsDatabase=FirebaseDatabase.getInstance().getReference().child("check");
        mCheckInsDatabase.keepSynced(true);

        mTimeLineListView=(ListView)findViewById(R.id.listViewCheckIns) ;
        mTimeLineListView.setClickable(true);
        imageButtonBack = (ImageButton)findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(this);

        timeLineCheckInInformationList=new ArrayList<TimeLineCheckInInformation>();
        getTimeAgo=new GetTimeAgo();
        context=this;


        mUserFriendsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUserFriendCount=(int)dataSnapshot.getChildrenCount();
                friendsId=new String[mUserFriendCount];
                int sayac=0;
                for(DataSnapshot messageSnapshot:dataSnapshot.getChildren())
                {
                    friendsId[sayac]=messageSnapshot.getKey();
                    sayac++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mTimeLineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object o = mTimeLineListView.getItemAtPosition(position);
                TimeLineCheckInInformation tlcii = (TimeLineCheckInInformation) o;

                /*Intent intent = new Intent(getApplicationContext(), InPlaceCheckInActivity.class);
                intent.putExtra("placeID3", placeInformation.getPlaceID());

                startActivity(intent);*/
        }
        });

        mCheckInsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                timeLineCheckInInformationList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    boolean isFriend=false;

                    TimeLineCheckInInformation tlcii = postSnapshot.getValue(TimeLineCheckInInformation.class);
                    if(tlcii.getUserId().compareTo(mCurrentUserId)==0)
                        isFriend=true;
                    else if(friendsId.length!=0)
                    {
                        for(int i=0;i<friendsId.length;i++) {
                            if (friendsId[i].compareTo(tlcii.getUserId()) == 0) {
                                isFriend = true;
                                break;
                            }
                        }
                    }

                    if(isFriend)
                        timeLineCheckInInformationList.add(tlcii);
                }

                if (timeLineCheckInInformationList.size() != 0) {
                    Collections.reverse(timeLineCheckInInformationList);
                    PlaceListAdapter timeLineListAdapter = new PlaceListAdapter(TimeLineActivity.this, timeLineCheckInInformationList);
                    mTimeLineListView.setAdapter(timeLineListAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        if(v == imageButtonBack){
            finish();
        }
    }
}
