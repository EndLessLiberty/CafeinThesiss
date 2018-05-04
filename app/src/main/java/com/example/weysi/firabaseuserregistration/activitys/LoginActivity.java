package com.example.weysi.firabaseuserregistration.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weysi.firabaseuserregistration.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import evids.Android.Veri.$;
import evids.Android.Veri.$$;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    public $ $(@IdRes int t){
        return new $(t,LoginActivity.this);
    }
    public $ $(View v){
        return new $(v);
    }
    public $$ $$(){
        return $$.kur();
    }


    //defining views
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private ImageButton ımageButtonGirisDugmesi;

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mUserDatabaseReference;

    //progress dialog
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        mUserDatabaseReference= FirebaseDatabase.getInstance().getReference().child("Users");

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
        }

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        //buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        ımageButtonGirisDugmesi=(ImageButton)findViewById(R.id.giris_dugmesi);
        textViewSignup  = (TextView) findViewById(R.id.textViewSignUp);

        progressDialog = new ProgressDialog(this);

        //attaching click listener
        //buttonSignIn.setOnClickListener(this);
        ımageButtonGirisDugmesi.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);

    }

    //method for user login
    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Loging Please Wait...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //start the profile activity
                            // String online_user_id=firebaseAuth.getCurrentUser().getUid();
                            final String current_user_id=firebaseAuth.getCurrentUser().getUid();
                            String deviceToken= FirebaseInstanceId.getInstance().getToken();
                            mUserDatabaseReference.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mUserDatabaseReference.child(current_user_id).child("online").setValue("true");
                                    startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
                                    finish();
                                }
                            });


                        }
                    }
                });





    }

    @Override
    public void onClick(View view) {
        if(view == ımageButtonGirisDugmesi){
            userLogin();
        }

        if(view == textViewSignup){
            finish();
            startActivity(new Intent(this, SignUpActivity.class));
        }
    }
}
