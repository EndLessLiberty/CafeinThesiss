package com.example.weysi.firabaseuserregistration.activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.adapters.SectionsPagerAdapter;
import com.example.weysi.firabaseuserregistration.informations.NotificationInformation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnotherUserProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mProfileImage;
    private TextView mProfileName, mProfileStatus, mProfileFriendsCount;
    private ImageButton mProfileSendReqBtn;
    private ImageButton mDeclineBtn;
    private ImageButton mAcceptFriend;
    private ImageButton mAddedFriend;
    private ImageButton mBlockedFriend;
    private ImageButton mSendMessage;
    private ImageButton imageButtonBack;

    private ViewPager mViewPager;

    private DatabaseReference mUsersDatabase;

    private ProgressDialog mProgressDialog;

    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mRootRef;
    private DatabaseReference mNotificationDatabase;
    private StorageReference mStorage;
    private TabLayout mTabLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private String user_id;
    private FirebaseUser mCurrent_user;
    private byte[] bytes;

    private String mCurrent_state;
    private String notificatinId;
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_user_profile);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Kullanıcı Veri Yükleniyor");
        mProgressDialog.setMessage("Kullanıcı verilerini yüklerken, lütfen bekleyiniz...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        //Getting
        user_id= getIntent().getStringExtra("UserID");




        //Firebase initialization
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();
        mStorage= FirebaseStorage.getInstance().getReference(user_id);

        //Layout initialization
        mProfileImage = (CircleImageView) findViewById(R.id.settings_image);
        mProfileSendReqBtn = (ImageButton) findViewById(R.id.imageButtonAddFriend);
        mDeclineBtn = (ImageButton) findViewById(R.id.imageButtonCancelAddFriend);
        mAcceptFriend = (ImageButton) findViewById(R.id.imageButtonAcceptAddFriend);
        mAddedFriend= (ImageButton) findViewById(R.id.imageButtonAddedFriend);
        mSendMessage= (ImageButton) findViewById(R.id.imageButtonSendMessage);
        mProfileName = (TextView) findViewById(R.id.textViewUserName);
        mProfileStatus = (TextView) findViewById(R.id.textViewDurum);
        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        imageButtonBack = (ImageButton)findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),user_id);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mCurrent_state = "not_friends";



        final long ONE_MEGABYTE = 720 * 1024;

        mUsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String display_name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("durum").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                Bitmap bmp;
                if(image.compareTo("default")==0)
                {
                    bmp=BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
                }
                else
                {
                    bytes = Base64.decode(image, Base64.DEFAULT);
                    bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                }
                mProfileImage.setImageBitmap(bmp);
                mProfileName.setText(display_name);
                mProfileStatus.setText(status);

                //--------------- FRIENDS LIST / REQUEST FEATURE -----

                mFriendReqDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(user_id)){

                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();

                            if(req_type.equals("received") ){

                                mCurrent_state = "req_received";
                                mDeclineBtn.setVisibility(View.VISIBLE);
                                mDeclineBtn.setEnabled(true);
                                mDeclineBtn.setClickable(true);
                                mProfileSendReqBtn.setVisibility(View.INVISIBLE);
                                mProfileSendReqBtn.setEnabled(false);
                                mProfileSendReqBtn.setClickable(false);
                                mAddedFriend.setVisibility(View.INVISIBLE);
                                mAddedFriend.setEnabled(false);
                                mAddedFriend.setClickable(false);
                                mAcceptFriend.setVisibility(View.INVISIBLE);
                                mAcceptFriend.setEnabled(false);
                                mAcceptFriend.setClickable(false);

                            }
                            else if(req_type.equals("sent"))
                            {
                                mCurrent_state = "req_sent";
                                mDeclineBtn.setVisibility(View.VISIBLE);
                                mDeclineBtn.setEnabled(true);
                                mDeclineBtn.setClickable(true);
                                mProfileSendReqBtn.setVisibility(View.INVISIBLE);
                                mProfileSendReqBtn.setEnabled(false);
                                mProfileSendReqBtn.setClickable(false);
                                mAddedFriend.setVisibility(View.INVISIBLE);
                                mAddedFriend.setEnabled(false);
                                mAddedFriend.setClickable(false);
                                mAcceptFriend.setVisibility(View.INVISIBLE);
                                mAcceptFriend.setEnabled(false);
                                mAcceptFriend.setClickable(false);
                            }

                            mProgressDialog.dismiss();


                        } else {

                            mFriendDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.hasChild(user_id)){

                                        mCurrent_state = "friends";
                                        mAddedFriend.setVisibility(View.VISIBLE);
                                        mAddedFriend.setEnabled(true);
                                        mAddedFriend.setClickable(true);
                                        mDeclineBtn.setVisibility(View.INVISIBLE);
                                        mDeclineBtn.setEnabled(false);
                                        mDeclineBtn.setClickable(false);
                                        mProfileSendReqBtn.setVisibility(View.INVISIBLE);
                                        mProfileSendReqBtn.setEnabled(false);
                                        mProfileSendReqBtn.setClickable(false);
                                        mAcceptFriend.setVisibility(View.INVISIBLE);
                                        mAcceptFriend.setEnabled(false);
                                        mAcceptFriend.setClickable(false);
                                    }
                                    else
                                    {
                                        mCurrent_state = "not_friends";
                                        mProfileSendReqBtn.setVisibility(View.VISIBLE);
                                        mProfileSendReqBtn.setEnabled(true);
                                        mProfileSendReqBtn.setClickable(true);
                                        mAddedFriend.setVisibility(View.INVISIBLE);
                                        mAddedFriend.setEnabled(false);
                                        mAddedFriend.setClickable(false);
                                        mDeclineBtn.setVisibility(View.INVISIBLE);
                                        mDeclineBtn.setEnabled(false);
                                        mDeclineBtn.setClickable(false);
                                        mAcceptFriend.setVisibility(View.INVISIBLE);
                                        mAcceptFriend.setEnabled(false);
                                        mAcceptFriend.setClickable(false);
                                    }


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {


                                }
                            });

                            mProgressDialog.dismiss();

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

        mProfileSendReqBtn.setOnClickListener(this);
        mDeclineBtn.setOnClickListener(this);
        mAddedFriend.setOnClickListener(this);
        mSendMessage.setOnClickListener(this);
        mAcceptFriend.setOnClickListener(this);

    }// End of OnCreate()

    @Override
    public void onClick(View v) {

        if(v==mProfileSendReqBtn)
        {
            if(mCurrent_state.equals("not_friends")){
                final AlertDialog.Builder builder =new AlertDialog.Builder(context);
                builder.setTitle("Arkadaşlık İsteği");
                CharSequence []options =new CharSequence[]{"İstek Gönder"};

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map requestMap = new HashMap();
                        requestMap.put("Friend_req/" + mCurrent_user.getUid() + "/" + user_id + "/request_type", "sent");
                        requestMap.put("Friend_req/" + user_id + "/" + mCurrent_user.getUid() + "/request_type", "received");

                        mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                if(databaseError != null){

                                } else {
                                    HashMap<String,Object> notificationData =new HashMap<>();
                                    notificationData.put("from", mCurrent_user.getUid());
                                    notificationData.put("type", "friendRequest");
                                    notificationData.put("time",ServerValue.TIMESTAMP);
                                    notificationData.put("result","null");
                                    notificatinId=mNotificationDatabase.child(user_id).push().getKey();
                                    mNotificationDatabase.child(user_id).child(notificatinId).setValue(notificationData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mCurrent_state = "req_sent";
                                            mDeclineBtn.setVisibility(View.VISIBLE);
                                            mDeclineBtn.setEnabled(true);
                                            mDeclineBtn.setClickable(true);
                                            mAddedFriend.setVisibility(View.INVISIBLE);
                                            mAddedFriend.setEnabled(false);
                                            mAddedFriend.setClickable(false);
                                            mProfileSendReqBtn.setVisibility(View.INVISIBLE);
                                            mProfileSendReqBtn.setEnabled(false);
                                            mProfileSendReqBtn.setClickable(false);
                                            mAcceptFriend.setVisibility(View.INVISIBLE);
                                            mAcceptFriend.setEnabled(false);
                                            mAcceptFriend.setClickable(false);
                                            mNotificationDatabase.child(user_id).child(notificatinId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    NotificationInformation ni= dataSnapshot.getValue(NotificationInformation.class);
                                                    ni.setTime((ni.getTime()*(-1)));
                                                    mNotificationDatabase.child(user_id).child(notificatinId).setValue(ni);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    });
                                }
                            }
                        });

                    }
                });
                builder.show();
            }
        }else if(v==mAcceptFriend)
        {
            if(mCurrent_state=="req_received"){
                CharSequence options[] = new CharSequence[]{"Kabul Et", "Reddet"};

                final AlertDialog.Builder builder2 = new AlertDialog.Builder(context);

                builder2.setTitle("Arkadaşlık İsteği");
                builder2.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Click Event for each item.
                        if(i == 0){

                            final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                            Map friendsMap = new HashMap();
                            friendsMap.put("Friends/" + mCurrent_user.getUid() + "/" + user_id + "/date", currentDate);
                            friendsMap.put("Friends/" + user_id + "/"  + mCurrent_user.getUid() + "/date", currentDate);


                            friendsMap.put("Friend_req/" + mCurrent_user.getUid() + "/" + user_id, null);
                            friendsMap.put("Friend_req/" + user_id + "/" + mCurrent_user.getUid(), null);


                            mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if(databaseError == null){
                                        mCurrent_state = "friends";
                                        mAddedFriend.setVisibility(View.VISIBLE);
                                        mAddedFriend.setEnabled(true);
                                        mAddedFriend.setClickable(true);
                                        mDeclineBtn.setVisibility(View.INVISIBLE);
                                        mDeclineBtn.setEnabled(false);
                                        mDeclineBtn.setClickable(false);
                                        mProfileSendReqBtn.setVisibility(View.INVISIBLE);
                                        mProfileSendReqBtn.setEnabled(false);
                                        mProfileSendReqBtn.setClickable(false);
                                        mAcceptFriend.setVisibility(View.INVISIBLE);
                                        mAcceptFriend.setEnabled(false);
                                        mAcceptFriend.setClickable(false);

                                        mNotificationDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                                                    if(messageSnapshot.child("from").getValue().toString().compareTo(user_id)==0  && messageSnapshot.child("result").getValue().toString().compareTo("null")==0) {
                                                        HashMap<String,Object> notificationData =new HashMap<>();
                                                        notificationData.put("from", user_id);
                                                        notificationData.put("type", "friendRequest");
                                                        notificationData.put("time",ServerValue.TIMESTAMP);
                                                        notificationData.put("result","accepted");
                                                        notificatinId=messageSnapshot.getKey();
                                                        mNotificationDatabase.child(mCurrent_user.getUid()).child(notificatinId).setValue(notificationData);
                                                        mNotificationDatabase.child(mCurrent_user.getUid()).child(notificatinId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                NotificationInformation ni= dataSnapshot.getValue(NotificationInformation.class);
                                                                ni.setTime((ni.getTime()*(-1)));
                                                                mNotificationDatabase.child(mCurrent_user.getUid()).child(notificatinId).setValue(ni);
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



                                    } else {
                                        String error = databaseError.getMessage();
                                        Toast.makeText(AnotherUserProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }

                        if(i == 1){

                            mFriendReqDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    mFriendReqDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mAcceptFriend.setVisibility(View.VISIBLE);
                                            mAcceptFriend.setEnabled(true);
                                            mAcceptFriend.setClickable(true);
                                            mAddedFriend.setVisibility(View.INVISIBLE);
                                            mAddedFriend.setEnabled(false);
                                            mAddedFriend.setClickable(false);
                                            mDeclineBtn.setVisibility(View.INVISIBLE);
                                            mDeclineBtn.setEnabled(false);
                                            mDeclineBtn.setClickable(false);
                                            mProfileSendReqBtn.setVisibility(View.INVISIBLE);
                                            mProfileSendReqBtn.setEnabled(false);
                                            mProfileSendReqBtn.setClickable(false);
                                            mCurrent_state = "not_friends";
                                            mNotificationDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                                                        if(messageSnapshot.child("from").getValue().toString().compareTo(user_id)==0  && messageSnapshot.child("result").getValue().toString().compareTo("null")==0) {
                                                            HashMap<String,Object> notificationData =new HashMap<>();
                                                            notificationData.put("from", user_id);
                                                            notificationData.put("type", "friendRequest");
                                                            notificationData.put("time",ServerValue.TIMESTAMP);
                                                            notificationData.put("result","declined");
                                                            notificatinId=messageSnapshot.getKey();
                                                            mNotificationDatabase.child(mCurrent_user.getUid()).child(notificatinId).setValue(notificationData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    mNotificationDatabase.child(mCurrent_user.getUid()).child(notificatinId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            NotificationInformation ni= dataSnapshot.getValue(NotificationInformation.class);
                                                                            ni.setTime((ni.getTime()*(-1)));
                                                                            mNotificationDatabase.child(mCurrent_user.getUid()).child(notificatinId).setValue(ni);
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });
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


                    }
                });

                builder2.show();
            }
        }
        else if(v==mDeclineBtn)
        {
            final Boolean[] flag = {true};
            if(mCurrent_state=="req_sent")
            {
                CharSequence options[] = new CharSequence[]{"İsteği İptal Et"};

                final AlertDialog.Builder builder3 = new AlertDialog.Builder(context);

                builder3.setTitle("Arkadaşlık İsteği");
                builder3.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Click Event for each item.
                        if(i == 0){
                            mFriendReqDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    mFriendReqDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mProfileSendReqBtn.setVisibility(View.VISIBLE);
                                            mProfileSendReqBtn.setEnabled(true);
                                            mProfileSendReqBtn.setClickable(true);
                                            mAddedFriend.setVisibility(View.INVISIBLE);
                                            mAddedFriend.setEnabled(false);
                                            mAddedFriend.setClickable(false);
                                            mDeclineBtn.setVisibility(View.INVISIBLE);
                                            mDeclineBtn.setClickable(false);
                                            mDeclineBtn.setEnabled(false);
                                            mAcceptFriend.setVisibility(View.INVISIBLE);
                                            mAcceptFriend.setEnabled(false);
                                            mAcceptFriend.setClickable(false);
                                            mCurrent_state = "not_friends";

                                            mNotificationDatabase.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                                                        if(messageSnapshot.child("from").getValue().toString().compareTo(mCurrent_user.getUid())==0 && flag[0] && messageSnapshot.child("result").getValue().toString().compareTo("null")==0) {
                                                            HashMap<String,Object> notificationData =new HashMap<>();
                                                            notificationData.put("from", mCurrent_user.getUid());
                                                            notificationData.put("type", "friendRequest");
                                                            notificationData.put("time",ServerValue.TIMESTAMP);
                                                            notificationData.put("result","undo");
                                                            notificatinId=messageSnapshot.getKey();
                                                            mNotificationDatabase.child(user_id).child(notificatinId).setValue(notificationData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    mNotificationDatabase.child(user_id).child(notificatinId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            NotificationInformation ni= dataSnapshot.getValue(NotificationInformation.class);
                                                                            ni.setTime((ni.getTime()*(-1)));
                                                                            mNotificationDatabase.child(user_id).child(notificatinId).setValue(ni);
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                                    flag[0] =false;
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

                    }
                });

                builder3.show();
            }

        }
        else if(v==mAddedFriend)
        {
            CharSequence options[] = new CharSequence[]{"Arkadaşlıktan Çıkar"};

            final AlertDialog.Builder builder4 = new AlertDialog.Builder(context);

            builder4.setTitle("Arkadaşlık Durumu");
            builder4.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    //Click Event for each item.
                    if(i == 0){
                        Map unfriendMap = new HashMap();
                        unfriendMap.put("Friends/" + mCurrent_user.getUid() + "/" + user_id, null);
                        unfriendMap.put("Friends/" + user_id + "/" + mCurrent_user.getUid(), null);

                        mRootRef.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                if(databaseError == null){
                                    mCurrent_state = "not_friends";
                                    mProfileSendReqBtn.setVisibility(View.VISIBLE);
                                    mProfileSendReqBtn.setEnabled(true);
                                    mProfileSendReqBtn.setClickable(true);
                                    mAddedFriend.setVisibility(View.INVISIBLE);
                                    mAddedFriend.setEnabled(false);
                                    mAddedFriend.setClickable(false);
                                    mDeclineBtn.setVisibility(View.INVISIBLE);
                                    mDeclineBtn.setEnabled(false);
                                    mDeclineBtn.setClickable(false);
                                    mAcceptFriend.setVisibility(View.INVISIBLE);
                                    mAcceptFriend.setEnabled(false);
                                    mAcceptFriend.setClickable(false);

                                } else {
                                    String error = databaseError.getMessage();
                                    Toast.makeText(AnotherUserProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


                    }

                }
            });
            builder4.show();
        }
        else if(v==mSendMessage)
        {
            Intent intent=new Intent(context,ChatActivity.class);
            intent.putExtra("user_id",user_id);
            intent.putExtra("user_name",mProfileName.getText().toString());
            startActivity(intent);
        }
    if(v == imageButtonBack){
            finish();
    }

    }



}

