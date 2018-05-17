package com.example.weysi.firabaseuserregistration.adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.informations.UserInformation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by weysi on 8.11.2017.
 */

public class UserListAdapter extends ArrayAdapter<UserInformation> {
    private StorageReference storageReference;
    private  Resources resources;
    private List<UserInformation> userInformationList;
    private Activity context;
    public UserListAdapter(@NonNull Activity context, List<UserInformation> userInformationList, Resources resources) {
        super(context, R.layout.place_list_layout, userInformationList);
        this.context=context;
        this.userInformationList=userInformationList;
        this.resources = resources;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.users_single_layout, null, true);


        final CircleImageView imageViewResim=(CircleImageView)  listViewItem.findViewById(R.id.user_single_image);
        TextView textViewPlaceName = (TextView) listViewItem.findViewById(R.id.user_single_name);
        TextView textViewNickName = (TextView) listViewItem.findViewById(R.id.user_single_status);
        textViewNickName.setVisibility(View.INVISIBLE);

        final    UserInformation userInformation=userInformationList.get(position);
        //textViewNickName.setText(userInformation.getNickName());
        textViewPlaceName.setText(userInformation.getName());
        Bitmap bmp;
        if(userInformation.getImage().compareTo("default")==0)
        {
            bmp= BitmapFactory.decodeResource(resources, R.drawable.default_avatar);

        }
        else
        {
            byte []byteArray = Base64.decode(userInformation.getImage(), Base64.DEFAULT);
            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }
        imageViewResim.setImageBitmap(bmp);



        return listViewItem;
    }

}

