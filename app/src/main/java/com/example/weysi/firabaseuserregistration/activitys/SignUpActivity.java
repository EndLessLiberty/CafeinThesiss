package com.example.weysi.firabaseuserregistration.activitys;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.informations.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextDogumTarihi;
    private EditText editTextNickName;
    DatePickerDialog datePickerDialog;
    private RadioGroup radioGroupCinsiyet;
    private RadioButton radioButtonCinsiyet;
    private Button buttonSignup;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;


    //defining firebaseauth object
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
        }

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDogumTarihi = (EditText) findViewById(R.id.editTextDogumTarihi);
        editTextNickName=(EditText) findViewById(R.id.editTextNickName);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        radioGroupCinsiyet=(RadioGroup) findViewById(R.id.radioGroupCinsiyet);
        int selectedId = radioGroupCinsiyet.getCheckedRadioButtonId();
        radioButtonCinsiyet= (RadioButton) findViewById(selectedId);

        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        editTextDogumTarihi.setOnClickListener(this);
    }

    private void registerUser(){

        //getting email and password from edit texts
        int selectedId = radioGroupCinsiyet.getCheckedRadioButtonId();
        radioButtonCinsiyet= (RadioButton) findViewById(selectedId);
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();
        final String nickName=editTextNickName.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String cinsiyet=radioButtonCinsiyet.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(nickName)){
            Toast.makeText(this,"Please enter nickname",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please enter phone",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String userID = user.getUid();
                            String device_token = FirebaseInstanceId.getInstance().getToken();
                            UserInformation userInformation = new UserInformation(name,phone,userID,nickName,cinsiyet,editTextDogumTarihi.getText().toString().trim(),"I am using Cafe'in","default","default",device_token);
                            databaseReference.child(userID).setValue(userInformation);
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
                        }else{
                            //display some message here
                            Toast.makeText(SignUpActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View view) {

        if(view == buttonSignup){
            registerUser();
        }

        if(view == textViewSignin){
            //open login activity when user taps on the already registered textview
            startActivity(new Intent(this, LoginActivity.class));
        }

        if (view == editTextDogumTarihi){

            // calender class's instance and get current date , month and year from calender
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            datePickerDialog = new DatePickerDialog(SignUpActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // set day of month , month and year value in the edit text
                            editTextDogumTarihi.setText(year+"/"+(monthOfYear + 1)+"/"+dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

    }
}
