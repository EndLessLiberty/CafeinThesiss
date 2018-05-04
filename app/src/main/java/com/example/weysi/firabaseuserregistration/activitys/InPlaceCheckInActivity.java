package com.example.weysi.firabaseuserregistration.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.adapters.UserListAdapter;
import com.example.weysi.firabaseuserregistration.fragments.FriendsFragment;
import com.example.weysi.firabaseuserregistration.fragments.PersonelTimeLineFragment;
import com.example.weysi.firabaseuserregistration.informations.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InPlaceCheckInActivity extends AppCompatActivity {

    TextView textViewdeneme;
    ListView listViewUsers;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference databasePlaceUsers;

    String placeID;

    List<UserInformation> userInformationList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_place_check_in);
        firebaseAuth = FirebaseAuth.getInstance();
        textViewdeneme=(TextView)findViewById(R.id.denememe);

        Intent i=getIntent();
        placeID=(String)i.getSerializableExtra("placeID3");
        databasePlaceUsers= FirebaseDatabase.getInstance().getReference("InPlaceCheckIns").child(placeID);

        listViewUsers = (ListView) findViewById(R.id.listViewUsers);
        listViewUsers.setClickable(true);
        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object o = listViewUsers.getItemAtPosition(position);
                UserInformation userInformation=(UserInformation) o;
                //Data data=(Data)o;
                //   Toast.makeText(getApplicationContext(),(String)data.getName()+"  "+data.getPlaceId(),Toast.LENGTH_LONG).show();

                Intent intent=new Intent(getApplicationContext(),AnotherUserProfileActivity.class);
                intent.putExtra("UserID",userInformation.getUserID());
                Bundle bundle = new Bundle();
                bundle.putString("targetUserId",userInformation.getUserID());
                // set MyFragment Arguments
                FriendsFragment myObj = new FriendsFragment();
                PersonelTimeLineFragment myObj2= new PersonelTimeLineFragment();
                myObj.setArguments(bundle);
                myObj2.setArguments(bundle);
                //intent.putExtra("placeName1",data.getName());
                startActivity(intent);
            }
        });
        userInformationList=new ArrayList<>();

    }
    @Override
    protected void onStart() {
        super.onStart();

        databasePlaceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userInformationList.clear();


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    UserInformation userInformation = (UserInformation) postSnapshot.getValue(UserInformation.class);

                    userInformationList.add(userInformation);
                }

                if(userInformationList.size()!=0) {
                    UserListAdapter userListAdapter = new UserListAdapter(InPlaceCheckInActivity.this, userInformationList);

                    listViewUsers.setAdapter(userListAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }}