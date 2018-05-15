package com.example.weysi.firabaseuserregistration.parsers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ArkadasCikarCheckInSilThread extends Thread {
    private DatabaseReference databaseReferenceTargetID;
    private DatabaseReference databaseReferenceUsersID;
    private DatabaseReference mtoplamTimeLineCheckInmyID;
    private DatabaseReference mtoplamTimeLineCheckInTargetID;
    private String target_id;
    private String current_id;
    public ArkadasCikarCheckInSilThread(String target_id,String current_id){
        this.target_id=target_id;
        this.current_id=current_id;
    }
    @Override
    public void run() {

        databaseReferenceTargetID = FirebaseDatabase.getInstance().getReference("UsersCheckIns").child(target_id);
        databaseReferenceUsersID = FirebaseDatabase.getInstance().getReference("UsersCheckIns").child(current_id);
        mtoplamTimeLineCheckInmyID= FirebaseDatabase.getInstance().getReference("toplamTimeLineCheckIn").child(current_id);
        mtoplamTimeLineCheckInTargetID= FirebaseDatabase.getInstance().getReference("toplamTimeLineCheckIn").child(target_id);


        databaseReferenceTargetID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    String id = postSnapshot.getKey();
                    mtoplamTimeLineCheckInmyID.child(id).removeValue();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReferenceUsersID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    String id = postSnapshot.getKey();
                    mtoplamTimeLineCheckInTargetID.child(id).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
