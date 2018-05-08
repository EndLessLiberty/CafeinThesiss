package com.example.weysi.firabaseuserregistration.fragments;


import android.content.Context;
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
import com.example.weysi.firabaseuserregistration.informations.GetTimeAgo;
import com.example.weysi.firabaseuserregistration.informations.PlaceInformation;
import com.example.weysi.firabaseuserregistration.informations.TimeLineCheckInInformation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonelTimeLineFragment extends Fragment {

    private RecyclerView recyclerViewCheckIns;
    RecyclerView.Adapter adapter;
    private DatabaseReference databaseCheckIns;
    private List<PlaceInformation> list;
    private View mMainView;
    private String targetUserId;

    public PersonelTimeLineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_personel_time_line, container, false);
        list = new ArrayList<>();
        recyclerViewCheckIns = (RecyclerView) mMainView.findViewById(R.id.recyclerViewCheckInst);

        targetUserId = getArguments().getString("targetUserId");

        databaseCheckIns = FirebaseDatabase.getInstance().getReference().child("UsersCheckIns").child(targetUserId);
        databaseCheckIns.keepSynced(true);

        recyclerViewCheckIns.setHasFixedSize(true);


        final FirebaseRecyclerAdapter<TimeLineCheckInInformation, TimeLineViewHolder> timeLineRecylerViewAdapter = new FirebaseRecyclerAdapter<TimeLineCheckInInformation, TimeLineViewHolder>(
                TimeLineCheckInInformation.class,
                R.layout.single_check_in_layout,
                TimeLineViewHolder.class,
                databaseCheckIns.orderByChild("checkInTime")

        ) {
            @Override
            protected void populateViewHolder(final TimeLineViewHolder viewHolder, final TimeLineCheckInInformation model, int position) {

                String time = (String) (GetTimeAgo.getTimeAgo(model.getCheckInTime() * (-1), getContext()));
                byte[] encodeByte = Base64.decode(model.getUserPhoto(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                viewHolder.setCircleImageViewUserPhoto(bitmap);
                viewHolder.setEditTextPlace(model.getPlaceName());
                viewHolder.setTextViewTime(time);
                viewHolder.setTextViewUserName(model.getUserName());
                viewHolder.setTextViewUserMessage(model.getMessage());


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
