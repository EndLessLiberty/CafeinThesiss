package com.example.weysi.firabaseuserregistration.activitys;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.adapters.SectionsPagerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalUserProfileActivity extends AppCompatActivity{

    private ImageView mProfileImage;
    private TextView mProfileName, mProfileStatus, mProfileFriendsCount;
    private ViewPager mViewPager;

    private DatabaseReference mUsersDatabase;

    private ProgressDialog mProgressDialog;

    private DatabaseReference mFriendDatabase;
    private DatabaseReference mRootRef;
    private DatabaseReference mNotificationDatabase;
    private StorageReference mStorage;
    private TabLayout mTabLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static FragmentManager fragmentManager;
    private Bitmap bmp;
    private byte [] byteArray;
    private String user_id;
    private FirebaseUser mCurrent_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_user_profile);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Kullanıcı Veri Yükleniyor");
        mProgressDialog.setMessage("Kullanıcı verilerini yüklerken, lütfen bekleyiniz...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        user_id = getIntent().getStringExtra("UserId");

        byteArray=getIntent().getByteArrayExtra("profile_photo");
        if(byteArray!=null)
        bmp=BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        mRootRef = FirebaseDatabase.getInstance().getReference();


        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference(user_id);

        mProfileImage = (CircleImageView) findViewById(R.id.settings_image);
        mProfileName = (TextView) findViewById(R.id.textViewUserName);
        mProfileStatus = (TextView) findViewById(R.id.textViewDurum);

        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), user_id);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        if(bmp!=null)
            mProfileImage.setImageBitmap(bmp);
        else
        {
            final long ONE_MEGABYTE = 720 * 1024;
            mStorage.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {                 // Data for "images/island.jpg" is returns, use this as needed
                    bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    if (bmp != null)
                        mProfileImage.setImageBitmap(bmp);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //  imageViewHeader.setImageResource(R.mipmap.ic_launcher_round);
                }
            });
        }


        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String display_name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("durum").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                mProfileName.setText(display_name);
                mProfileStatus.setText(status);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProgressDialog.dismiss();

    }
}