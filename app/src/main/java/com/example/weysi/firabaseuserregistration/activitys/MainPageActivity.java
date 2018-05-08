package com.example.weysi.firabaseuserregistration.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.informations.UserInformation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private DatabaseReference mUserDatabase;
   // private View header;
    //view objects

    private Button buttonLogout;
    private ImageButton buttonGetPlace;
    private ImageButton buttonZamanTuneli;
    private ImageButton buttonMessages;
    private ImageButton buttonNotifications;
    private de.hdodenhof.circleimageview.CircleImageView circleImageView;
    private Bitmap bmp;
    private byte[] byteArray;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in

      //  mTimeLineRecylerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //that means current user will return null
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
        mUserDatabase.keepSynced(true);
        //initializing views
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonGetPlace = (ImageButton) findViewById(R.id.imageButtonGetNearbyPlaces);
        buttonZamanTuneli = (ImageButton) findViewById(R.id.imageButtonTimeLine);
        buttonMessages= (ImageButton) findViewById(R.id.imageButtonMessages);
        buttonNotifications=(ImageButton) findViewById(R.id.imageButtonNotifications);

        //header = getLayoutInflater().inflate(R.layout.header, null);
        circleImageView = (de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.profile_image);

        //adding listener to button

        buttonLogout.setOnClickListener(this);
        buttonGetPlace.setOnClickListener(this);
        buttonZamanTuneli.setOnClickListener(this);
        buttonMessages.setOnClickListener(this);
        buttonNotifications.setOnClickListener(this);
        circleImageView.setOnClickListener(this);





    }

    @Override
    protected void onStart() {
        super.onStart();
        final long ONE_MEGABYTE = 720 * 1024;

        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {                 // Data for "images/island.jpg" is returns, use this as needed
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                byteArray=bytes;

                if (bmp != null)
                    circleImageView.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //  imageViewHeader.setImageResource(R.mipmap.ic_launcher_round);
            }
        });

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
            intent.putExtra("profile_photo",byteArray);
            startActivity(intent);
        } else if (view == circleImageView) {
            Intent intent = new Intent(getApplicationContext(), ProfileSettingsActivity.class);
            intent.putExtra("profile_photo",byteArray);
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
    }

}