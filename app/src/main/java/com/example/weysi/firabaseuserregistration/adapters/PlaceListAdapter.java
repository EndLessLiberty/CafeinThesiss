package com.example.weysi.firabaseuserregistration.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.informations.GetTimeAgo;
import com.example.weysi.firabaseuserregistration.informations.TimeLineCheckInInformation;
import com.example.weysi.firabaseuserregistration.informations.UserInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Enes on 1.05.2018.
 */

public class PlaceListAdapter extends ArrayAdapter<TimeLineCheckInInformation> {

    private Activity context;
    private DatabaseReference mUserDB;
    private List<TimeLineCheckInInformation> timeLineCheckInInformationList;
    private Bitmap bitmap;
    private byte []byteArray;


    public PlaceListAdapter(@NonNull Activity context, List<TimeLineCheckInInformation> timeLineCheckInInformationList) {
        super(context, R.layout.single_check_in_layout, timeLineCheckInInformationList);
        this.context=context;
        this.timeLineCheckInInformationList=timeLineCheckInInformationList;
        mUserDB= FirebaseDatabase.getInstance().getReference("Users");

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.single_check_in_layout, null, true);

        final EditText editTextPlace= listViewItem.findViewById(R.id.editTextCheckInPlace);
        final TextView textViewTime= listViewItem.findViewById(R.id.user_single_time);
        final TextView textViewUserName= listViewItem.findViewById(R.id.user_single_name);
        final TextView textViewUserMessage= listViewItem.findViewById(R.id.user_single_status);
        final CircleImageView circleImageViewUserPhoto= listViewItem.findViewById(R.id.user_single_image);

        final TimeLineCheckInInformation tlcii = timeLineCheckInInformationList.get(position);
        final String time = GetTimeAgo.getTimeAgo(tlcii.getCheckInTime() * (-1), context);

        mUserDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation ui=dataSnapshot.child(tlcii.getUserId()).getValue(UserInformation.class);

                if(ui.getImage().compareTo("default")==0)
                {
                    bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.default_avatar);
                }
                else
                {
                    byteArray = Base64.decode(ui.getImage(), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                }

                editTextPlace.setText(tlcii.getPlaceName());
                textViewTime.setText(time);
                textViewUserName.setText(tlcii.getUserName());
                textViewUserMessage.setText(tlcii.getMessage());
                circleImageViewUserPhoto.setImageBitmap(bitmap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return listViewItem;
    }
}
