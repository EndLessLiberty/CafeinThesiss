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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Enes on 1.05.2018.
 */

public class PlaceListAdapter extends ArrayAdapter<TimeLineCheckInInformation> {

    private Activity context;
    private List<TimeLineCheckInInformation> timeLineCheckInInformationList;


    public PlaceListAdapter(@NonNull Activity context, List<TimeLineCheckInInformation> timeLineCheckInInformationList) {
        super(context, R.layout.single_check_in_layout, timeLineCheckInInformationList);
        this.context=context;
        this.timeLineCheckInInformationList=timeLineCheckInInformationList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.single_check_in_layout, null, true);

        EditText editTextPlace= listViewItem.findViewById(R.id.editTextCheckInPlace);
        TextView textViewTime= listViewItem.findViewById(R.id.user_single_time);
        TextView textViewUserName= listViewItem.findViewById(R.id.user_single_name);
        TextView textViewUserMessage= listViewItem.findViewById(R.id.user_single_status);
        CircleImageView circleImageViewUserPhoto= listViewItem.findViewById(R.id.user_single_image);

        TimeLineCheckInInformation tlcii = timeLineCheckInInformationList.get(position);
        String time = GetTimeAgo.getTimeAgo(tlcii.getCheckInTime() * (-1), context);
        byte[] encodeByte = Base64.decode(tlcii.getUserPhoto(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        editTextPlace.setText(tlcii.getPlaceName());
        textViewTime.setText(time);
        textViewUserName.setText(tlcii.getUserName());
        textViewUserMessage.setText(tlcii.getMessage());
        circleImageViewUserPhoto.setImageBitmap(bitmap);

        return listViewItem;
    }
}
