package com.example.weysi.firabaseuserregistration.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.informations.PlaceInformation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonelTimeLineFragment extends Fragment {

    private RecyclerView recyclerViewCheckIns;
    RecyclerView.Adapter adapter ;
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
        list=new ArrayList<>();
        recyclerViewCheckIns = (RecyclerView) mMainView.findViewById(R.id.recyclerViewCheckInst);

        targetUserId = getArguments().getString("targetUserId");

        databaseCheckIns = FirebaseDatabase.getInstance().getReference().child("UsersCheckIns").child(targetUserId);
        databaseCheckIns.keepSynced(true);


        recyclerViewCheckIns.setHasFixedSize(true);


        FirebaseRecyclerAdapter<PlaceInformation,TimeLineViewHolder> timeLineRecyclerViewAdapter =new FirebaseRecyclerAdapter<PlaceInformation, TimeLineViewHolder>(
                PlaceInformation.class,
                R.layout.satir_layout_2,
                TimeLineViewHolder.class,
                databaseCheckIns
        ) {
            @Override
            protected void populateViewHolder(TimeLineViewHolder viewHolder, PlaceInformation model, int position) {
                viewHolder.setName(model.getName());


            }


        };

        recyclerViewCheckIns.setAdapter(timeLineRecyclerViewAdapter);
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

        public TimeLineViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setName(String name){

            TextView userNameView = (TextView) mView.findViewById(R.id.placeName);
            userNameView.setText(name);

        }

    }

}
