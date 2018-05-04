package com.example.weysi.firabaseuserregistration.adapters;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
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
    private List<UserInformation> userInformationList;
    private Activity context;
    public UserListAdapter(@NonNull Activity context,  List<UserInformation> userInformationList) {
        super(context, R.layout.place_list_layout, userInformationList);
        this.context=context;
        this.userInformationList=userInformationList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.users_single_layout, null, true);


        final CircleImageView imageViewResim=(CircleImageView)  listViewItem.findViewById(R.id.user_single_image);
        TextView textViewPlaceName = (TextView) listViewItem.findViewById(R.id.user_single_name);
        TextView textViewNickName = (TextView) listViewItem.findViewById(R.id.user_single_status);

        final    UserInformation userInformation=userInformationList.get(position);
        textViewNickName.setText(userInformation.getNickName());
        textViewPlaceName.setText(userInformation.getName());

        storageReference = FirebaseStorage.getInstance().getReference(userInformation.getUserID());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                //Picasso.with(context).load(uri).fit().centerCrop().into(holder.userImage);
                /*Glide.with(context)
                        .load(uri)
                        .asBitmap()
                        .centerCrop()
                        .into(new SimpleTarget<Bitmap>(200,200) {
                            @Override
                            public void onResourceReady(Bitmap resource,GlideAnimation glideAnimation) {
                                imageViewResim.setImageBitmap(resource);
                            }
                        });*/ // Zaman tünelinde çökmeye
                Picasso.with(context).load(uri).placeholder(R.drawable.default_avatar).into(imageViewResim);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });
        return listViewItem;
    }

}

