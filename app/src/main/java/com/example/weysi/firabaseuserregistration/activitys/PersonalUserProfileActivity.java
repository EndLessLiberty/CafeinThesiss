package com.example.weysi.firabaseuserregistration.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
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

public class PersonalUserProfileActivity extends AppCompatActivity implements View.OnClickListener{

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
    private ImageButton imageButtonBack;
    private TextView mProfileDuzenle;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private Bitmap bmp;
    private String sImage;
    private byte [] byteArray;
    private String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_user_profile);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Kullanıcı Veri Yükleniyor");
        mProgressDialog.setMessage("Kullanıcı verilerini yüklerken, lütfen bekleyiniz...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        user_id = getIntent().getStringExtra("UserId");

        mRootRef = FirebaseDatabase.getInstance().getReference();


        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        mStorage = FirebaseStorage.getInstance().getReference(user_id);

        mProfileImage = (CircleImageView) findViewById(R.id.settings_image);
        mProfileName = (TextView) findViewById(R.id.textViewUserName);
        mProfileStatus = (TextView) findViewById(R.id.textViewDurum);
        imageButtonBack = (ImageButton)findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(this);
        mProfileDuzenle=(TextView) findViewById(R.id.textViewProfiliDuzenle);
        mTabLayout = (TabLayout) findViewById(R.id.personal_tabs);
        mViewPager = (ViewPager) findViewById(R.id.personalViewPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), user_id);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sImage=dataSnapshot.child("image").getValue().toString();
                byteArray = Base64.decode(sImage, Base64.DEFAULT);
                bmp=BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                String display_name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("durum").getValue().toString();

                mProfileName.setText(display_name);
                mProfileStatus.setText(status);
                mProfileImage.setImageBitmap(bmp);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mProfileDuzenle.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mProgressDialog.dismiss();

    }

    @Override
    public void onClick(View v) {
        if(v==mProfileDuzenle)
        {
            Intent intent = new Intent(this, ProfileSettingsActivity.class);
            //intent.putExtra("profile_photo",sImage);
            startActivity(intent);

        }else if(v == imageButtonBack){
            finish();
        }
    }

}