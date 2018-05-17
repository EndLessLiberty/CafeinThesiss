package com.example.weysi.firabaseuserregistration.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.activitys.PlaceActivity;
import com.example.weysi.firabaseuserregistration.informations.StatisticInformation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Enes on 14.05.2018.
 */

public class TotalStatisticFragment extends Fragment{

    private View mView;
    private RecyclerView mRecylerView;

    private DatabaseReference placeDatabase;
    private FirebaseRecyclerAdapter<StatisticInformation,totalStatisticViewHolder> totalRecylerViewAdapter;

    private int maxWidth;



    public TotalStatisticFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_last_hour_statistic, container, false);

        placeDatabase=FirebaseDatabase.getInstance().getReference("place");
        placeDatabase.keepSynced(true);
        mRecylerView=(RecyclerView) mView.findViewById(R.id.recyclerViewCheckInst);
        mRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecylerView.setHasFixedSize(true);

        maxWidth=getArguments().getInt("width");

        totalRecylerViewAdapter=new FirebaseRecyclerAdapter<StatisticInformation, totalStatisticViewHolder>(
                StatisticInformation.class,
                R.layout.single_statistic_layout,
                totalStatisticViewHolder.class,
                placeDatabase
        ) {
            @Override
            protected void populateViewHolder(final totalStatisticViewHolder viewHolder, StatisticInformation model, int position) {
                final String placeID=getRef(position).getKey();
                final int[] maleCount = new int[1];
                final int[] femaleCount = new int[1];
                final int[] percent = new int[1];
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(getContext(), PlaceActivity.class);
                        intent.putExtra("placeID",placeID);
                        startActivity(intent);
                    }
                });

                getRef(position).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        maleCount[0] =Integer.parseInt(dataSnapshot.child("maleCount").getValue().toString());
                        femaleCount[0] =Integer.parseInt(dataSnapshot.child("femaleCount").getValue().toString());
                        double mRate=(float)(maleCount[0])/(maleCount[0]+femaleCount[0]);
                        double fRate=(float)(femaleCount[0])/(maleCount[0]+femaleCount[0]);
                        percent[0] =(int)(((maxWidth/2)-16)*mRate);
                        viewHolder.setTextViewManPercent(String.valueOf( mRate*100));
                        viewHolder.setTextViewWomanPercent(String.valueOf(fRate*100));
                        viewHolder.setTextViewPlaceName(dataSnapshot.child("name").getValue().toString());

                        if((((maxWidth/2)-16)- percent[0])==0)
                        {
                            viewHolder.setImageButtonBlue(percent[0] -8);
                            viewHolder.setImageButtonPink((((maxWidth/2)-16)- percent[0])+8);
                        } else if(femaleCount[0]!=0 && percent[0] ==0){
                            viewHolder.setImageButtonBlue(percent[0] +8);
                            viewHolder.setImageButtonPink((((maxWidth/2)-16)- percent[0])-8);
                        }else{
                            viewHolder.setImageButtonPink(((maxWidth/2)-16)- percent[0]);
                            viewHolder.setImageButtonBlue(percent[0]);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        mRecylerView.setAdapter(totalRecylerViewAdapter);



        return mView;
    }

    public static class totalStatisticViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageButton imageButtonBlue;
        ImageButton imageButtonPink;
        TextView textViewPlaceName;
        TextView textViewManPercent;
        TextView textViewWomanPercent;
        ConstraintLayout.LayoutParams paramsBlue;
        ConstraintLayout.LayoutParams paramsPink;


        public totalStatisticViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            imageButtonBlue=(ImageButton) mView.findViewById(R.id.imageButtonBlue);
            imageButtonPink=(ImageButton) mView.findViewById(R.id.imageButtonPink);
            textViewManPercent=(TextView)mView.findViewById(R.id.textViewMan);
            textViewWomanPercent=(TextView)mView.findViewById(R.id.textViewWoman);
            textViewPlaceName=(TextView)mView.findViewById(R.id.textViewPlaceName);
            paramsBlue = (ConstraintLayout.LayoutParams) imageButtonBlue.getLayoutParams();
            paramsPink = (ConstraintLayout.LayoutParams) imageButtonPink.getLayoutParams();
        }

        public void setTextViewPlaceName(String name){
            textViewPlaceName.setText(name);
        }

        public void setTextViewManPercent(String man){
            textViewManPercent.setText("%"+man);
        }

        public void setTextViewWomanPercent(String woman){
            textViewWomanPercent.setText("%"+woman);
        }

        public void setImageButtonBlue(int width){
            paramsBlue.width=width;
            paramsBlue.height=32;
            imageButtonBlue.setLayoutParams(paramsBlue);
        }

        public void setImageButtonPink(int width ){
            paramsPink.width=width;
            paramsPink.height=32;
            imageButtonPink.setLayoutParams(paramsPink);

        }

    }

}
