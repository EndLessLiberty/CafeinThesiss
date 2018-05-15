package com.example.weysi.firabaseuserregistration.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Enes on 8.05.2018.
 */

public class LastHourStatisticFragment extends android.support.v4.app.Fragment{

    private View mView;
    private RecyclerView mRecylerView;

    private DatabaseReference inPlaceDatabase;
    private DatabaseReference placeDatabase;
    private FirebaseRecyclerAdapter<StatisticInformation,lastHourStatisticViewHolder> lastHourRecylerViewAdapter;

    private Long start;
    private Long end;
    private int maxWidth;

    public LastHourStatisticFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_last_hour_statistic, container, false);

        inPlaceDatabase= FirebaseDatabase.getInstance().getReference("InPlaceCheckIns");
        inPlaceDatabase.keepSynced(true);
        placeDatabase=FirebaseDatabase.getInstance().getReference("place");
        placeDatabase.keepSynced(true);
        mRecylerView=(RecyclerView) mView.findViewById(R.id.recyclerViewCheckInst);
        mRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecylerView.setHasFixedSize(true);

        maxWidth=getArguments().getInt("width");


        start = System.currentTimeMillis();
        end = start-3600000;
        lastHourRecylerViewAdapter =new FirebaseRecyclerAdapter<StatisticInformation, lastHourStatisticViewHolder>(
                StatisticInformation.class,
                R.layout.single_statistic_layout,
                lastHourStatisticViewHolder.class,
                inPlaceDatabase
        ) {
            @Override
            protected void populateViewHolder(final lastHourStatisticViewHolder viewHolder, StatisticInformation model, final int position) {


                final String placeID=getRef(position).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(getContext(), PlaceActivity.class);
                        intent.putExtra("placeID",placeID);
                        startActivity(intent);
                    }
                });
                Query firebaseQuery=getRef(position).orderByChild("checkInTime").startAt((-1)*start).endAt((-1)*end);
                firebaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    double maleCount=0;
                    double femaleCount=0;
                    int percent;

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
                        {
                            String cinsiyet=postSnapShot.child("cinsiyet").getValue().toString();
                            if(cinsiyet.compareTo("Erkek")==0)
                                maleCount++;
                            else
                                femaleCount++;
                        }

                        placeDatabase.child(placeID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                percent=(int)(((maxWidth/2)-16)*(maleCount/(maleCount+femaleCount)));
                                viewHolder.setTextViewManPercent(String.valueOf((maleCount/(maleCount+femaleCount))*100));
                                viewHolder.setTextViewWomanPercent(String.valueOf((femaleCount/(maleCount+femaleCount))*100));
                                viewHolder.setTextViewPlaceName(dataSnapshot.child("name").getValue().toString());

                                if((((maxWidth/2)-16)-percent)==0)
                                {
                                    viewHolder.setImageButtonBlue(percent-8);
                                    viewHolder.setImageButtonPink((((maxWidth/2)-16)-percent)+8);
                                } else if(femaleCount!=0 && percent==0){
                                    viewHolder.setImageButtonBlue(percent+8);
                                    viewHolder.setImageButtonPink((((maxWidth/2)-16)-percent)-8);
                                } else if((maleCount+femaleCount)==0){
                                    viewHolder.setTextViewNoData();
                                }else{
                                    viewHolder.setImageButtonPink(((maxWidth/2)-16)-percent);
                                    viewHolder.setImageButtonBlue(percent);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        mRecylerView.setAdapter(lastHourRecylerViewAdapter);


        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    public static class lastHourStatisticViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageButton imageButtonBlue;
        ImageButton imageButtonPink;
        TextView textViewPlaceName;
        TextView textViewManPercent;
        TextView textViewWomanPercent;
        TextView textViewNoData;
        ConstraintLayout.LayoutParams paramsBlue;
        ConstraintLayout.LayoutParams paramsPink;


        public lastHourStatisticViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            imageButtonBlue=(ImageButton) mView.findViewById(R.id.imageButtonBlue);
            imageButtonPink=(ImageButton) mView.findViewById(R.id.imageButtonPink);
            textViewManPercent=(TextView)mView.findViewById(R.id.textViewMan);
            textViewWomanPercent=(TextView)mView.findViewById(R.id.textViewWoman);
            textViewPlaceName=(TextView)mView.findViewById(R.id.textViewPlaceName);
            textViewNoData=(TextView) mView.findViewById(R.id.textViewNoData);
            paramsBlue = (ConstraintLayout.LayoutParams) imageButtonBlue.getLayoutParams();
            paramsPink = (ConstraintLayout.LayoutParams) imageButtonPink.getLayoutParams();
        }

        public void setTextViewNoData(){
            imageButtonBlue.setVisibility(View.INVISIBLE);
            imageButtonPink.setVisibility(View.INVISIBLE);
            textViewManPercent.setVisibility(View.INVISIBLE);
            textViewWomanPercent.setVisibility(View.INVISIBLE);
            textViewNoData.setVisibility(View.VISIBLE);
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
