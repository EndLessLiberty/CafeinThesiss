package com.example.weysi.firabaseuserregistration.activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;
import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.adapters.PlaceListAdapter;
import com.example.weysi.firabaseuserregistration.informations.GetTimeAgo;
import com.example.weysi.firabaseuserregistration.informations.TimeLineCheckInInformation;
import com.example.weysi.firabaseuserregistration.informations.UserInformation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Enes on 30.04.2018.
 */



public class TimeLineActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recylerViewCheckIns;
    private DatabaseReference mToplamCheckInsDatabase;
    private DatabaseReference mUserDatabase;
    private ImageButton imageButtonBack;


    private String mCurrentUserId;


    private List<TimeLineCheckInInformation> timeLineCheckInInformationList;


    private GetTimeAgo getTimeAgo;
    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        mCurrentUserId=(String)getIntent().getSerializableExtra("current_user_id");

        mToplamCheckInsDatabase=FirebaseDatabase.getInstance().getReference().child("toplamTimeLineCheckIn");
        mToplamCheckInsDatabase.keepSynced(true);
        mUserDatabase=FirebaseDatabase.getInstance().getReference("Users");
        mUserDatabase.keepSynced(true);

        recylerViewCheckIns=(RecyclerView) findViewById(R.id.recylerViewCheckIns) ;
        //recylerViewCheckIns.setClickable(true);
        recylerViewCheckIns.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        imageButtonBack = (ImageButton)findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(this);

        timeLineCheckInInformationList=new ArrayList<TimeLineCheckInInformation>();
        getTimeAgo=new GetTimeAgo();
        context=this;

        Query query=mToplamCheckInsDatabase.child(mCurrentUserId).orderByChild("checkInTime");
        FirebaseRecyclerAdapter<TimeLineCheckInInformation,timeLineViewHolder> timeLineRecylerViewAdapter =new FirebaseRecyclerAdapter<TimeLineCheckInInformation, timeLineViewHolder>(
                TimeLineCheckInInformation.class,
                R.layout.single_check_in_layout,
                timeLineViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(final timeLineViewHolder viewHolder, final TimeLineCheckInInformation model, int position) {

                mUserDatabase.child(model.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserInformation user=dataSnapshot.getValue(UserInformation.class);
                        String time=GetTimeAgo.getTimeAgo(model.getCheckInTime() * (-1), context);
                        viewHolder.setMessage(model.getMessage());
                        viewHolder.setName(model.getUserName());
                        viewHolder.setPlaceName(model.getPlaceName());
                        viewHolder.setCircleImageViewUserPhoto(user.getImage(),getResources());
                        viewHolder.setTime(time);
                        viewHolder.editTextPlace.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, PlaceActivity.class);
                                intent.putExtra("placeID",model.getPlaceID());
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };
        recylerViewCheckIns.setAdapter(timeLineRecylerViewAdapter);



    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    public static class timeLineViewHolder extends RecyclerView.ViewHolder{

        View mView;
        EditText editTextPlace;
        TextView textViewTime;
        TextView textViewUserName;
        TextView textViewUserMessage;
        CircleImageView circleImageViewUserPhoto;

        public timeLineViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            editTextPlace= mView.findViewById(R.id.editTextCheckInPlace);
            textViewTime= mView.findViewById(R.id.user_single_time);
            textViewUserName= mView.findViewById(R.id.user_single_name);
            textViewUserMessage= mView.findViewById(R.id.user_single_status);
            circleImageViewUserPhoto= mView.findViewById(R.id.user_single_image);
        }

        public void setPlaceName(String placeName){
            editTextPlace.setText(placeName);
        }

        public void setTime(String time){
            textViewTime.setText(time);
        }

        public void setName(String name){
            textViewUserName.setText(name);
        }

        public void setMessage(String message){
            textViewUserMessage.setText(message);
        }

        public void setCircleImageViewUserPhoto(String sImage,Resources r)
        {
            Bitmap bmp;
            if(sImage.compareTo("default")==0)
            {
                bmp= BitmapFactory.decodeResource(r, R.drawable.default_avatar);

            }
            else
            {
                byte []byteArray = Base64.decode(sImage, Base64.DEFAULT);
                bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            }
            circleImageViewUserPhoto.setImageBitmap(bmp);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == imageButtonBack){
            finish();
        }
    }
}
