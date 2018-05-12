package com.example.weysi.firabaseuserregistration.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.drm.ProcessedData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.informations.UserInformation;
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

import java.io.ByteArrayOutputStream;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private DatabaseReference mUserDatabase;
   // private View header;

    //view objects

    private ImageButton buttonLogout;
    private ImageButton buttonGetPlace;
    private ImageButton buttonZamanTuneli;
    private ImageButton buttonMessages;
    private ImageButton buttonNotifications;
    private ImageButton buttonStatistics;
    private de.hdodenhof.circleimageview.CircleImageView circleImageView;
    private String sImage;
    private Bitmap bmp;
    private byte[] byteArray;
    private ProgressDialog pd;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        pd=new ProgressDialog(this);
        pd.setMessage("Bilgileriniz Yükleniyor...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        //getting current user
        firebaseUser = firebaseAuth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference(firebaseUser.getUid());
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");

        //initializing views

        buttonLogout = (ImageButton) findViewById(R.id.imageButtonLogOut);
        buttonGetPlace = (ImageButton) findViewById(R.id.imageButtonGetNearbyPlaces);
        buttonZamanTuneli = (ImageButton) findViewById(R.id.imageButtonTimeLine);
        buttonMessages= (ImageButton) findViewById(R.id.imageButtonMessages);
        buttonNotifications=(ImageButton) findViewById(R.id.imageButtonNotifications);
        buttonStatistics=(ImageButton)findViewById(R.id.imageButtonStatistics);

        //header = getLayoutInflater().inflate(R.layout.header, null);
        circleImageView = (de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.profile_image);

        //adding listener to button
        buttonLogout.setOnClickListener(this);
        buttonGetPlace.setOnClickListener(this);
        buttonZamanTuneli.setOnClickListener(this);
        buttonMessages.setOnClickListener(this);
        buttonNotifications.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        buttonStatistics.setOnClickListener(this);

        mUserDatabase.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation ui=dataSnapshot.getValue(UserInformation.class);
                sImage=ui.getImage();
                if(sImage.compareTo("default")==0)
                {
                    bmp=BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                    bmp.recycle();

                }
                else
                {
                    byteArray = Base64.decode(sImage, Base64.DEFAULT);
                    bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    circleImageView.setImageBitmap(bmp);
                }
                pd.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();

    }



    @Override
    public void onClick(View view) {
        //if logout is pressed
        if (view == buttonLogout) {
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        } else if (view == buttonGetPlace) {
            Intent intent = new Intent(this, NearbyPlaceActivity.class);
            //intent.putExtra("profile_photo",sImage);
            startActivity(intent);
        } else if (view == circleImageView) {
            Intent intent = new Intent(getApplicationContext(), PersonalUserProfileActivity.class);
            //intent.putExtra("profile_photo",sImage);
            intent.putExtra("UserId",firebaseUser.getUid());
            startActivity(intent);
        }else if (view==buttonZamanTuneli){
            Intent intent = new Intent(getApplicationContext(), TimeLineActivity.class);
            intent.putExtra("current_user_id",firebaseUser.getUid());
            startActivity(intent);
        }
        else if(view == buttonMessages) {
            startActivity(new Intent(this, MessageActivity.class));
        }
        else if(view==buttonNotifications){
            startActivity(new Intent(this, NotificationsActivity.class));
        }
        else if(view==buttonStatistics){
            startActivity(new Intent (this,StatisticsActivity.class));
        }
    }


}