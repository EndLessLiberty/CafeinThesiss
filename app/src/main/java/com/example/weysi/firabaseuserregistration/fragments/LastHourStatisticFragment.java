package com.example.weysi.firabaseuserregistration.fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weysi.firabaseuserregistration.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Enes on 8.05.2018.
 */

public class LastHourStatisticFragment extends Fragment{

    private View mView;
    private RecyclerView mRecylerView;

    private DatabaseReference placeDatabase;

    public LastHourStatisticFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_last_hour_statistic, container, false);

        placeDatabase= FirebaseDatabase.getInstance().getReference("InPlaceCheckIns");
        placeDatabase.keepSynced(true);
        mRecylerView=(RecyclerView) mView.findViewById(R.id.recylerViewLastHourStatistic);

        mRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    public static class lastHourStatisticViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public lastHourStatisticViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDate(String date){

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(date);

        }

        public void setName(String name){

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }

        public void setUserImage(String thumb_image, Context ctx){

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);

        }

        public void setUserOnline(String online_status) {

            ImageView userOnlineView = (ImageView) mView.findViewById(R.id.user_single_online_icon);

            if(online_status.equals("true")){

                userOnlineView.setVisibility(View.VISIBLE);

            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }

        }


    }
}
