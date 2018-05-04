package com.example.weysi.firabaseuserregistration.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weysi.firabaseuserregistration.MyApplication;
import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.informations.GetTimeAgo;
import com.example.weysi.firabaseuserregistration.informations.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;




/**
 * Created by Enes on 18.03.2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{


    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    private FirebaseAuth firebaseAuth;
    public String name;
    public  Context context,c;
    public MessageAdapter(List<Messages> mMessageList,Context context) {

        this.mMessageList = mMessageList;
        this.context = context;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout ,parent, false);

        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText,messageText2;
        public CircleImageView profileImage,profileImage2;
        public TextView displayName,displayName2;
        public ImageView messageImage;
        public TextView time,time2;

        public MessageViewHolder(View view) {
            super(view);

            messageText = view.findViewById(R.id.message_text_layout);
            profileImage = view.findViewById(R.id.request_profile_layout);
            displayName = view.findViewById(R.id.name_text_layout);
            messageImage = view.findViewById(R.id.bar_image_layout);
            time= view.findViewById(R.id.time_text_layout);
            time2= view.findViewById(R.id.time_text_layout2);

            //sağ taraftaki kişinin verileri
            messageText2= view.findViewById(R.id.message_text_layout2);
            displayName2= view.findViewById(R.id.name_text_layout2);
            profileImage2 = view.findViewById(R.id.request_profile_layout2);

        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        Messages c = mMessageList.get(i);

        String from_user = c.getFrom();
        String message_type = c.getType();

        GetTimeAgo getTimeAgo = new GetTimeAgo();



        String lastSeenTime = GetTimeAgo.getTimeAgo( c.getTime(), MyApplication.getContext());

        //    mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);
        firebaseAuth = FirebaseAuth.getInstance();
       /* mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("thumb_image").getValue().toString();




                Picasso.with(viewHolder.profileImage.getContext()).load(image)
                        .placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
        if(message_type.equals("text")) {
            if(firebaseAuth.getCurrentUser().getUid().equals(from_user)) {
                viewHolder.messageText2.setVisibility(View.VISIBLE);
                viewHolder.time2.setVisibility(View.VISIBLE);

                viewHolder.messageText2.setText(c.getMessage());
                viewHolder.time2.setText(lastSeenTime);

                viewHolder.displayName2.setVisibility(View.INVISIBLE);
                viewHolder.displayName.setVisibility(View.INVISIBLE);
                viewHolder.profileImage2.setVisibility(View.INVISIBLE);
                viewHolder.profileImage.setVisibility(View.INVISIBLE);
                viewHolder.time.setVisibility(View.INVISIBLE);
                //********************************
                viewHolder.messageText.setVisibility(View.INVISIBLE);
                viewHolder.displayName.setVisibility(View.INVISIBLE);

            }else {
                viewHolder.messageText.setVisibility(View.VISIBLE);
                viewHolder.time.setVisibility(View.VISIBLE);

                viewHolder.messageText.setText(c.getMessage());
                viewHolder.time.setText(lastSeenTime);

                viewHolder.messageImage.setVisibility(View.INVISIBLE);
                viewHolder.displayName.setText(name);

                //sağ taraf

                viewHolder.messageText2.setVisibility(View.INVISIBLE);
                viewHolder.displayName2.setVisibility(View.INVISIBLE);
                viewHolder.time2.setVisibility(View.INVISIBLE);

                viewHolder.displayName2.setVisibility(View.INVISIBLE);
                viewHolder.displayName.setVisibility(View.INVISIBLE);

                viewHolder.profileImage2.setVisibility(View.INVISIBLE);
                viewHolder.profileImage.setVisibility(View.INVISIBLE);
            }

        } else {

            viewHolder.messageText.setVisibility(View.INVISIBLE);
            Picasso.with(viewHolder.profileImage.getContext()).load(c.getMessage())
                    .placeholder(R.drawable.default_avatar).into(viewHolder.messageImage);

        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }



}
