package com.example.weysi.firabaseuserregistration.activitys;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.weysi.firabaseuserregistration.informations.CheckInInformation;
import com.example.weysi.firabaseuserregistration.informations.Data;
import com.example.weysi.firabaseuserregistration.informations.PlaceInformation;
import com.example.weysi.firabaseuserregistration.informations.TimeLineCheckInInformation;
import com.example.weysi.firabaseuserregistration.informations.UserInformation;
import com.example.weysi.firabaseuserregistration.parsers.PlaceClass;
import com.example.weysi.firabaseuserregistration.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class NearbyPlaceActivity extends AppCompatActivity {

    private LocationManager konumYoneticisi;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseCheckIn;
    private DatabaseReference databaseReferencePlace;
    private DatabaseReference databaseReferenceUsersID;
    private DatabaseReference  databaseReferencePlaceID;
    private DatabaseReference databaseReferenceUser;

    private ListView lv;
    private EditText mMessage;

    private double x;
    private double y;
    private List<CheckInInformation> checkInActivityList;
    private String message;
    private byte [] byteArray;
    private String sUserPhoto;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_place);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceUsersID=FirebaseDatabase.getInstance().getReference("UsersCheckIns");
        databaseCheckIn = FirebaseDatabase.getInstance().getReference("check");
        databaseReferencePlace=FirebaseDatabase.getInstance().getReference("place");
        databaseReferenceUser=FirebaseDatabase.getInstance().getReference("Users");
        databaseReferencePlaceID=FirebaseDatabase.getInstance().getReference("InPlaceCheckIns");


        databaseReferenceUser.child(firebaseAuth.getCurrentUser().getUid()).child("online").setValue("true");

        konumYoneticisi = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mMessage=(EditText)findViewById(R.id.edit_text_checkin_message);

        byteArray=getIntent().getByteArrayExtra("profile_photo");
        if(byteArray!=null)
        sUserPhoto= Base64.encodeToString(byteArray, Base64.DEFAULT);


        lv = (ListView) findViewById(R.id.listView1);

        lv.setClickable(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object o = lv.getItemAtPosition(position);
                final Data data=(Data)o;


                View view2 =(LayoutInflater.from(NearbyPlaceActivity.this)).inflate(R.layout.single_check_in_message_layout,null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(NearbyPlaceActivity.this);
                builder.setView(view2);
                final EditText editTextMessage= view2.findViewById(R.id.edit_text_checkin_message);

                builder.setCancelable(true)
                        .setTitle(data.getName())
                        .setPositiveButton("Check'in", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                message=editTextMessage.getText().toString();
                                databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        UserInformation userInformation= dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).getValue(UserInformation.class);
                                        final TimeLineCheckInInformation[] temptlcii = new TimeLineCheckInInformation[1];
                                        databaseReferencePlaceID.child(data.getPlaceId()).child(firebaseAuth.getCurrentUser().getUid()).setValue(userInformation);
                                        final HashMap<String,Object> checkInInfo=new HashMap<>();
                                        final String id=databaseCheckIn.push().getKey();
                                        checkInInfo.put("placeID",data.getPlaceId());
                                        checkInInfo.put("placeName",data.getName());
                                        checkInInfo.put("userName",userInformation.getName());
                                        checkInInfo.put("userId",userInformation.getUserID());
                                        checkInInfo.put("checkInTime",ServerValue.TIMESTAMP);
                                        checkInInfo.put("message",message);
                                        checkInInfo.put("checkInID",id);
                                        checkInInfo.put("userPhoto",sUserPhoto);
                                        databaseCheckIn.child(id).setValue(checkInInfo);
                                        databaseCheckIn.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                temptlcii[0]= dataSnapshot.getValue(TimeLineCheckInInformation.class);
                                                temptlcii[0].setCheckInTime((temptlcii[0].getCheckInTime()*(-1)));
                                                databaseCheckIn.child(id).setValue(temptlcii[0]);
                                                PlaceInformation placeInformation=new PlaceInformation(data.getName(),data.getPlaceId());
                                                databaseReferencePlace.child(data.getPlaceId()).setValue(placeInformation);
                                                databaseReferenceUsersID.child(firebaseAuth.getCurrentUser().getUid()).child(id).setValue(temptlcii[0]);

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

                            }
                        });

                Dialog dialog=builder.create();
                dialog.show();



            }
        });

        LocationListener konumDinleyicisi = new LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {

                x = loc.getLatitude();
                y = loc.getLongitude();
                PlaceClass p = new PlaceClass(lv, x, y, NearbyPlaceActivity.this);
                p.execute();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        konumYoneticisi.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, konumDinleyicisi);




    }
    @Override
    protected void onPause() {
        super.onPause();
        databaseReferenceUser.child(firebaseAuth.getCurrentUser().getUid()).child("online").setValue(ServerValue.TIMESTAMP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReferenceUser.child(firebaseAuth.getCurrentUser().getUid()).child("online").setValue("true");
    }


}
