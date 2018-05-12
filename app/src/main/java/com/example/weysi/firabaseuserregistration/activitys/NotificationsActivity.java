package com.example.weysi.firabaseuserregistration.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.informations.GetTimeAgo;
import com.example.weysi.firabaseuserregistration.informations.NotificationInformation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Enes on 25.04.2018.
 */

public class NotificationsActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mRootRef;
    private DatabaseReference mNotificationDatabase;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mFriendReqDatabase;
    private RecyclerView mNotificationList;
    private String currentUserId;
    private String notificatinId;
    private GetTimeAgo getTimeAgo;
    private Context context;
    private ImageButton imageButtonBack;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        currentUserId=firebaseUser.getUid();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mNotificationDatabase=FirebaseDatabase.getInstance().getReference().child("notifications").child(currentUserId);
        mNotificationDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference();
        mUsersDatabase.keepSynced(true);
        mFriendReqDatabase=FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mFriendReqDatabase.keepSynced(true);
        getTimeAgo=new GetTimeAgo();
        mNotificationList=(RecyclerView)findViewById(R.id.notification_list);
        mNotificationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        context=this;
        imageButtonBack = (ImageButton)findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(this);

        FirebaseRecyclerAdapter<NotificationInformation,NotificationViewHolder> notificationsRecylerViewAdapter=new FirebaseRecyclerAdapter<NotificationInformation, NotificationViewHolder>(
                NotificationInformation.class,
                R.layout.friend_request_layout,
                NotificationViewHolder.class,
                mNotificationDatabase.orderByChild("time")
        ) {
            @Override
            protected void populateViewHolder(final NotificationViewHolder viewHolder, NotificationInformation model, int position) {

                final String targetNotification = getRef(position).getKey();
                final String[] targetUserId = new String[1];
                final String[] requestType = new String[1];
                final String[] requestResult = new String[1];

                mUsersDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        targetUserId[0] = dataSnapshot.child("notifications").child(currentUserId).child(targetNotification).child("from").getValue().toString();
                        requestType[0] = dataSnapshot.child("notifications").child(currentUserId).child(targetNotification).child("type").getValue().toString();
                        requestResult[0] = dataSnapshot.child("notifications").child(currentUserId).child(targetNotification).child("result").getValue().toString();


                        String name = dataSnapshot.child("Users").child(targetUserId[0]).child("name").getValue().toString();
                        String thumpImage = dataSnapshot.child("Users").child(targetUserId[0]).child("image").getValue().toString();
                        String ServerTime = dataSnapshot.child("notifications").child(currentUserId).child(targetNotification).child("time").getValue().toString();
                        String time=null;
                        String requestMessage = null;
                        StringBuilder message;
                        if (requestType[0].compareTo("friendRequest") == 0)
                        {

                            if (requestResult[0].compareTo("null") == 0) {
                                message = new StringBuilder(name);
                                time = GetTimeAgo.getTimeAgo((Long.parseLong(ServerTime) * (-1)), context);
                                message.append(" " + time);
                                message.append(" sana arkadaşlık isteği gönderdi.");
                                requestMessage = message.toString();
                                viewHolder.buttonRequestAccept.setVisibility(View.VISIBLE);
                                viewHolder.buttonRequestAccept.setEnabled(true);
                                viewHolder.buttonRequestDecline.setVisibility(View.VISIBLE);
                                viewHolder.buttonRequestDecline.setEnabled(true);
                            }else if (requestResult[0].compareTo("accepted") == 0) {
                                message = new StringBuilder(name);
                                time = GetTimeAgo.getTimeAgo((Long.parseLong(ServerTime) * (-1)), context);
                                message.append(" ile " + time);
                                message.append(" beri arkadaşsınız.");
                                requestMessage = message.toString();
                                viewHolder.buttonRequestAccept.setVisibility(View.INVISIBLE);
                                viewHolder.buttonRequestAccept.setEnabled(false);
                                viewHolder.buttonRequestDecline.setVisibility(View.INVISIBLE);
                                viewHolder.buttonRequestDecline.setEnabled(false);
                            } else if (requestResult[0].compareTo("declined") == 0) {
                                message = new StringBuilder(name);
                                time = (GetTimeAgo.getTimeAgo((Long.parseLong(ServerTime) * (-1)), context));
                                message.append(" isteğini " + time);
                                message.append(" önce reddettin.");
                                requestMessage = message.toString();
                                viewHolder.buttonRequestAccept.setVisibility(View.INVISIBLE);
                                viewHolder.buttonRequestAccept.setEnabled(false);
                                viewHolder.buttonRequestDecline.setVisibility(View.INVISIBLE);
                                viewHolder.buttonRequestDecline.setEnabled(false);
                            } else if (requestResult[0].compareTo("undo") == 0) {
                                requestMessage = name + " 'in isteği artık geçerli değil.";
                                viewHolder.buttonRequestAccept.setVisibility(View.INVISIBLE);
                                viewHolder.buttonRequestAccept.setEnabled(false);
                                viewHolder.buttonRequestDecline.setVisibility(View.INVISIBLE);
                                viewHolder.buttonRequestDecline.setEnabled(false);
                            }

                        }

                        viewHolder.setTextName(name);
                        viewHolder.setTextMessage(requestMessage);
                        viewHolder.setTextTime(time);
                        viewHolder.setImageUserImage(thumpImage,getApplicationContext());

                        viewHolder.buttonRequestAccept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                                Map friendsMap = new HashMap();
                                friendsMap.put("Friends/" + currentUserId + "/" + targetUserId[0] + "/date", currentDate);
                                friendsMap.put("Friends/" + targetUserId[0] + "/"  + currentUserId + "/date", currentDate);


                                friendsMap.put("Friend_req/" + currentUserId + "/" + targetUserId[0], null);
                                friendsMap.put("Friend_req/" + targetUserId[0] + "/" + currentUserId, null);


                                mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if(databaseError == null){

                                            viewHolder.buttonRequestAccept.setVisibility(View.INVISIBLE);
                                            viewHolder.buttonRequestAccept.setEnabled(false);
                                            viewHolder.buttonRequestDecline.setVisibility(View.INVISIBLE);
                                            viewHolder.buttonRequestDecline.setEnabled(false);

                                            mNotificationDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                                                        if(messageSnapshot.child("from").getValue().toString().compareTo(targetUserId[0])==0  && messageSnapshot.child("result").getValue().toString().compareTo("null")==0) {
                                                            HashMap<String,Object> notificationData =new HashMap<>();
                                                            // friendsMap.put("Friends/" + currentUserId + "/" + targetUserId[0] + "/date", currentDate);
                                                            notificationData.put("from", targetUserId[0]);
                                                            notificationData.put("type", "friendRequest");
                                                            notificationData.put("time",ServerValue.TIMESTAMP);
                                                            notificationData.put("result","accepted");
                                                            notificatinId=messageSnapshot.getKey();
                                                            mNotificationDatabase.child(notificatinId).setValue(notificationData);
                                                            mNotificationDatabase.child(notificatinId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    NotificationInformation ni= dataSnapshot.getValue(NotificationInformation.class);
                                                                    ni.setTime((ni.getTime()*(-1)));
                                                                    mNotificationDatabase.child(notificatinId).setValue(ni);
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });

                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                        } else {

                                        }

                                    }
                                });


                            }
                        });
                        viewHolder.buttonRequestDecline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mFriendReqDatabase.child(currentUserId).child(targetUserId[0]).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        mFriendReqDatabase.child(targetUserId[0]).child(currentUserId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                viewHolder.buttonRequestAccept.setVisibility(View.INVISIBLE);
                                                viewHolder.buttonRequestAccept.setEnabled(false);
                                                viewHolder.buttonRequestDecline.setVisibility(View.INVISIBLE);
                                                viewHolder.buttonRequestDecline.setEnabled(false);

                                                mNotificationDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                                                            if(messageSnapshot.child("from").getValue().toString().compareTo(targetUserId[0])==0  && messageSnapshot.child("result").getValue().toString().compareTo("null")==0) {
                                                                HashMap<String,Object> notificationData =new HashMap<>();
                                                                notificationData.put("from", targetUserId[0]);
                                                                notificationData.put("type", "friendRequest");
                                                                notificationData.put("time",ServerValue.TIMESTAMP);
                                                                notificationData.put("result","declined");
                                                                notificatinId=messageSnapshot.getKey();
                                                                mNotificationDatabase.child(notificatinId).setValue(notificationData);
                                                                mNotificationDatabase.child(notificatinId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        NotificationInformation ni= dataSnapshot.getValue(NotificationInformation.class);
                                                                        ni.setTime((ni.getTime()*(-1)));
                                                                        mNotificationDatabase.child(notificatinId).setValue(ni);
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });
                                                                break;
                                                            }

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                            }
                                        });

                                    }
                                });

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };

        mNotificationList.setAdapter(notificationsRecylerViewAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onClick(View v) {
        if(v == imageButtonBack){
            finish();
        }
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        View mView;
        Button buttonRequestDecline;
        Button buttonRequestAccept;
        TextView textViewRequestTime;
        TextView textViewRequesterName;
        TextView textViewRequestMessage;
        CircleImageView circleImageViewNotificationPhoto;


        public NotificationViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            buttonRequestDecline= mView.findViewById(R.id.buttonFriendRequestDecline);
            buttonRequestAccept= mView.findViewById(R.id.buttonFriendRequestAccept);
            textViewRequestTime = mView.findViewById(R.id.request_time_text_layout);
            textViewRequesterName = mView.findViewById(R.id.request_name_text_layout);
            textViewRequestMessage = mView.findViewById(R.id.request_text_layout);
            circleImageViewNotificationPhoto= mView.findViewById(R.id.request_profile_layout);

        }

        public void setTextTime(String date){

            textViewRequestTime.setText(date);

        }

        public void setTextName(String name){

            textViewRequesterName.setText(name);

        }

        public void setTextMessage(String message){

            textViewRequestMessage.setText(message);

        }


        public void setImageUserImage(String thumb_image, Context ctx){

            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.default_avatar).into(circleImageViewNotificationPhoto);

        }



    }

}
