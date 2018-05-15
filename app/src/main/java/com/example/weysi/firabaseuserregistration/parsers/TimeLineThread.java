package com.example.weysi.firabaseuserregistration.parsers;

import com.example.weysi.firabaseuserregistration.informations.TimeLineCheckInInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TimeLineThread extends   Thread {

    private DatabaseReference mtoplamTimeLineCheckInmyID;
    private DatabaseReference mtoplamTimeLineCheckInTargetID;
    private DatabaseReference databaseReferenceUsersID;
    private DatabaseReference databaseReferenceTargetID;
    private String my_id;
    private String target_id;
    public  TimeLineThread(String my_id,String target_id){

        this.my_id=my_id;
        this.target_id=target_id;
    }
    @Override
    public void run() {

        mtoplamTimeLineCheckInmyID= FirebaseDatabase.getInstance().getReference("toplamTimeLineCheckIn").child(my_id);
        mtoplamTimeLineCheckInTargetID= FirebaseDatabase.getInstance().getReference("toplamTimeLineCheckIn").child(target_id);

        databaseReferenceTargetID = FirebaseDatabase.getInstance().getReference("UsersCheckIns").child(target_id);
        databaseReferenceUsersID = FirebaseDatabase.getInstance().getReference("UsersCheckIns").child(my_id);

        // onun checkinlerini benim toplam chekcinlerine yükledi
        databaseReferenceTargetID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String id = postSnapshot.getKey();
                    TimeLineCheckInInformation asd = postSnapshot.getValue(TimeLineCheckInInformation.class);
                    mtoplamTimeLineCheckInmyID.child(id).setValue(asd);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // venim checkbnlerini onun toplam chekcinlerine yükledi
        databaseReferenceUsersID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String id = postSnapshot.getKey();
                    TimeLineCheckInInformation asd = postSnapshot.getValue(TimeLineCheckInInformation.class);
                    mtoplamTimeLineCheckInTargetID.child(id).setValue(asd);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
