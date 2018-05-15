package com.example.weysi.firabaseuserregistration.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.activitys.PlaceActivity;
import com.example.weysi.firabaseuserregistration.informations.GetTimeAgo;
import com.example.weysi.firabaseuserregistration.informations.TimeLineCheckInInformation;
import com.example.weysi.firabaseuserregistration.informations.UserInformation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.location.places.Place;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonelTimeLineFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerViewCheckIns;
    RecyclerView.Adapter adapter;
    private DatabaseReference databaseCheckIns;
    private DatabaseReference mUserDatabase;
    private View mMainView;
    private String targetUserId;
    private Bitmap bmp;

    public PersonelTimeLineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_personel_time_line, container, false);
        recyclerViewCheckIns = (RecyclerView) mMainView.findViewById(R.id.recyclerViewCheckInst);

        targetUserId = getArguments().getString("targetUserId");

        databaseCheckIns = FirebaseDatabase.getInstance().getReference().child("UsersCheckIns").child(targetUserId);
        databaseCheckIns.keepSynced(true);
        mUserDatabase=FirebaseDatabase.getInstance().getReference("Users");

        mUserDatabase.child(targetUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image=dataSnapshot.child("image").getValue().toString();
                if(image.compareTo("default")==0)
                {
                    bmp= BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);

                }
                else
                {
                    byte []byteArray = Base64.decode(image, Base64.DEFAULT);
                    bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final FirebaseRecyclerAdapter<TimeLineCheckInInformation, TimeLineViewHolder> timeLineRecylerViewAdapter = new FirebaseRecyclerAdapter<TimeLineCheckInInformation, TimeLineViewHolder>(
                TimeLineCheckInInformation.class,
                R.layout.single_check_in_layout,
                TimeLineViewHolder.class,
                databaseCheckIns.orderByChild("checkInTime").limitToFirst(10)

        ) {
            @Override
            protected void populateViewHolder(final TimeLineViewHolder viewHolder, final TimeLineCheckInInformation model, int position) {


                mUserDatabase.child(targetUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserInformation user=dataSnapshot.getValue(UserInformation.class);
                        String time = (String) (GetTimeAgo.getTimeAgo(model.getCheckInTime() * (-1), getContext()));

                        viewHolder.setCircleImageViewUserPhoto(bmp);
                        viewHolder.setEditTextPlace(model.getPlaceName());
                        viewHolder.setTextViewTime(time);
                        viewHolder.setTextViewUserName(model.getUserName());
                        viewHolder.setTextViewUserMessage(model.getMessage());
                        viewHolder.editTextPlace.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), PlaceActivity.class);
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
        recyclerViewCheckIns.setAdapter(timeLineRecylerViewAdapter);
        recyclerViewCheckIns.setLayoutManager(new LinearLayoutManager(getContext()));


        // Inflate the layout for this fragment
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {

    }

    public static class TimeLineViewHolder extends RecyclerView.ViewHolder {


        View mView;
        EditText editTextPlace;
        TextView textViewTime;
        TextView textViewUserName;
        TextView textViewUserMessage;
        CircleImageView circleImageViewUserPhoto;


        public TimeLineViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            editTextPlace = (EditText) mView.findViewById(R.id.editTextCheckInPlace);
            textViewTime = (TextView) mView.findViewById(R.id.user_single_time);
            textViewUserName = (TextView) mView.findViewById(R.id.user_single_name);
            textViewUserMessage = (TextView) mView.findViewById(R.id.user_single_status);
            circleImageViewUserPhoto = (CircleImageView) mView.findViewById(R.id.user_single_image);

        }


        public void setEditTextPlace(String place) {
            editTextPlace.setText(place);
            editTextPlace.setClickable(true);

        }

        public void setTextViewTime(String time) {
            textViewTime.setText(time);
        }

        public void setTextViewUserName(String userName) {
            textViewUserName.setText(userName);
        }

        public void setTextViewUserMessage(String userMessage) {
            textViewUserMessage.setText(userMessage);
        }

        public void setCircleImageViewUserPhoto(Bitmap userPhoto) {
            circleImageViewUserPhoto.setImageBitmap(userPhoto);
        }

    }


}
