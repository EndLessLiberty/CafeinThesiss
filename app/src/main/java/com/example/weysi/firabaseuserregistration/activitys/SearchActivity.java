package com.example.weysi.firabaseuserregistration.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weysi.firabaseuserregistration.MyApplication;
import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.fragments.FriendsFragment;
import com.example.weysi.firabaseuserregistration.informations.Friends;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.weysi.firabaseuserregistration.MyApplication.getContext;

public class SearchActivity extends AppCompatActivity {
    private EditText editText;
    private Button btnArama;
    private RecyclerView recyclerView;
    private DatabaseReference mUserDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //controller tanımlama
        editText=(EditText)findViewById(R.id.editTextSearchAct);
        btnArama=(Button)findViewById(R.id.buttonSearchAct);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerViewSearchAct);


        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mUserDatabase.keepSynced(true);

    }

    @Override
    protected void onStart() {
        super.onStart();

        btnArama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=editText.getText().toString();
                ara(str);
            }
        });
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDate(String date){

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(date);

        }

        public void setName(String name){

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }

        public void setUserImage(String thumb_image, Context ctx){

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);

        }

        public void setUserOnline(String online_status) {

            ImageView userOnlineView = (ImageView) mView.findViewById(R.id.user_single_online_icon);

            if(online_status.equals("true")){

                userOnlineView.setVisibility(View.VISIBLE);

            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }

        }


    }


private void ara(String kelime){

    Query firebaseQuery=mUserDatabase.orderByChild("name").startAt(kelime).endAt(kelime+"\uf8ff");
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    FirebaseRecyclerAdapter<Friends, FriendsFragment.FriendsViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Friends, FriendsFragment.FriendsViewHolder>(

            Friends.class,
            R.layout.users_single_layout,
            FriendsFragment.FriendsViewHolder.class,
            firebaseQuery


    ) {
        @Override
        protected void populateViewHolder(final FriendsFragment.FriendsViewHolder friendsViewHolder, Friends friends, int i) {

            friendsViewHolder.setDate(friends.getDate());

            final String list_user_id = getRef(i).getKey();

            mUserDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final String userName = dataSnapshot.child("name").getValue().toString();
                    String userThumb = dataSnapshot.child("image").getValue().toString();

                    if(dataSnapshot.hasChild("online")) {
                        String userOnline = dataSnapshot.child("online").getValue().toString();
                        friendsViewHolder.setUserOnline(userOnline);
                    }
                    friendsViewHolder.setName(userName);
                    friendsViewHolder.setUserImage(userThumb, getContext());
                    friendsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent profileIntent = new Intent(getContext(), AnotherUserProfileActivity.class);
                            profileIntent.putExtra("UserID", list_user_id);
                            startActivity(profileIntent);
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

    };
    recyclerView.setAdapter(friendsRecyclerViewAdapter);
}

}