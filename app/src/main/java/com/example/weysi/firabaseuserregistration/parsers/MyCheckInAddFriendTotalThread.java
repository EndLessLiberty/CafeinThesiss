package com.example.weysi.firabaseuserregistration.parsers;

import com.example.weysi.firabaseuserregistration.informations.TimeLineCheckInInformation;
import com.example.weysi.firabaseuserregistration.informations.UserInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyCheckInAddFriendTotalThread extends Thread  {

    private String myID;
    private  String id;
    private TimeLineCheckInInformation veri;
    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mtoplamTimeLineCheckInTargetID;
    public MyCheckInAddFriendTotalThread(String myID, TimeLineCheckInInformation veri, String id){
        this.myID=myID;
        this.id=id;
        this.veri=veri;

    }

    @Override
    public void run() {
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(myID);

        mtoplamTimeLineCheckInTargetID= FirebaseDatabase.getInstance().getReference("toplamTimeLineCheckIn").child(myID);
        mtoplamTimeLineCheckInTargetID.child(id).setValue(veri);

        mFriendsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    mtoplamTimeLineCheckInTargetID= FirebaseDatabase.getInstance().getReference("toplamTimeLineCheckIn").child(postSnapshot.getKey());
                    mtoplamTimeLineCheckInTargetID.child(id).setValue(veri);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
