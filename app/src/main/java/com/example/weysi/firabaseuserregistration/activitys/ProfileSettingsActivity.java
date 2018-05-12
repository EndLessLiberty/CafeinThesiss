package com.example.weysi.firabaseuserregistration.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.informations.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileSettingsActivity extends AppCompatActivity implements View.OnClickListener{

    public static  final int IMAGE_GALLERY_REQUEST=234;
    //buttons
    private TextView kaydet;
    private TextView fotoDegistir;
    private ImageButton imageButtonBack;
    private EditText adSoyadDuzenle;
    private EditText kullaniciAdiDuzenle;
    private EditText emailDuzenle;
    private EditText telefonDuzenle;
    private EditText dogumTarihiDuzenle;
    private EditText yeniSifreDuzenle;
    private EditText yeniSifreTekrarDuzenle;
    private EditText eskiSifreDuzenle;
    private de.hdodenhof.circleimageview.CircleImageView circleImageViewPicture;

    private Uri filePath;
    private Bitmap bmp;
    private byte [] byteArray;
    private String sUserPhoto;

    private StorageReference mStorage;
    private DatabaseReference mUserDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private UserInformation userInformation ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        //Reference
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        mStorage= FirebaseStorage.getInstance().getReference(firebaseUser.getUid());

        //buttonListener
        circleImageViewPicture =(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.imageViewPicture);


        fotoDegistir =(TextView) findViewById(R.id.fotoDegistir);
        kaydet =(TextView) findViewById(R.id.kaydet);
        imageButtonBack = (ImageButton) findViewById(R.id.imageButtonBack);
        adSoyadDuzenle = (EditText)findViewById(R.id.adSoyadDuzenle);
        kullaniciAdiDuzenle = (EditText)findViewById(R.id.kullaniciAdiDuzenle);
        kullaniciAdiDuzenle.setKeyListener(null);
        emailDuzenle = (EditText)findViewById(R.id.emailDuzenle);
        emailDuzenle.setKeyListener(null);
        telefonDuzenle = (EditText)findViewById(R.id.telefonDuzenle);
        dogumTarihiDuzenle = (EditText)findViewById(R.id.dogumTarihiDuzenle);
        yeniSifreDuzenle = (EditText) findViewById(R.id.yeniSifreDuzenle);
        yeniSifreTekrarDuzenle = (EditText) findViewById(R.id.yeniSifreTekrarDuzenle);
        eskiSifreDuzenle = (EditText)findViewById(R.id.eskiSifreDuzenle);

        byteArray=getIntent().getByteArrayExtra("profile_photo");
        bmp=BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        circleImageViewPicture.setImageBitmap(bmp);

        fotoDegistir.setOnClickListener(this);
        circleImageViewPicture.setOnClickListener(this);
        kaydet.setOnClickListener(this);
        imageButtonBack.setOnClickListener(this);


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        mUserDatabase.keepSynced(true);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInformation = dataSnapshot.getValue(UserInformation.class);
                String name = dataSnapshot.child("name").getValue().toString();
                String nickName = dataSnapshot.child("nickName").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String dogumTarihi = dataSnapshot.child("dogumTarihi").getValue().toString();
                String cinsiyet = dataSnapshot.child("cinsiyet").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("durum").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                String device_token = dataSnapshot.child("device_token").getValue().toString();

                adSoyadDuzenle.setText(name);
                kullaniciAdiDuzenle.setText(nickName);
                emailDuzenle.setText(firebaseUser.getEmail());
                telefonDuzenle.setText(phone);
                dogumTarihiDuzenle.setText(dogumTarihi);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_GALLERY_REQUEST);


    }

    @Override
    public void onClick(View view) {

        if(view==fotoDegistir || view == circleImageViewPicture){
            showFileChooser();


        }else if(view==kaydet){
            kaydetIslemi();

        }else if (view==imageButtonBack){
            finish();
        }


    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                circleImageViewPicture.setImageBitmap(bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                bitmap.recycle();
                sUserPhoto= Base64.encodeToString(byteArray, Base64.DEFAULT);

                mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userInformation=dataSnapshot.getValue(UserInformation.class);
                        userInformation.setImage(sUserPhoto);
                        mUserDatabase.setValue(userInformation);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //uploadFile();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }}



        private void kaydetIslemi(){

            String eskiSifre = eskiSifreDuzenle.getText().toString();
            final String yeniSifre = yeniSifreDuzenle.getText().toString().trim();
            final String yeniSifreTekrar = yeniSifreTekrarDuzenle.getText().toString().trim();
            String adSoyad = adSoyadDuzenle.getText().toString();
            String dogumTarihi = dogumTarihiDuzenle.getText().toString();
            String telefon = telefonDuzenle.getText().toString();
            // Şifre Değiştir Kısmı kontrol ediliyor.
            if (eskiSifre.length()>0){
                firebaseAuth.signInWithEmailAndPassword(firebaseUser.getEmail(), eskiSifre)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                //if the task is successfull
                                if(task.isSuccessful()){
                                    if(yeniSifre.length()>5){
                                        if (yeniSifreTekrar.equals(yeniSifre)){
                                            firebaseUser.updatePassword(yeniSifreDuzenle.getText().toString());
                                            Toast.makeText(ProfileSettingsActivity.this,"Şifreniz başarıyla değiştirildi.",Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(ProfileSettingsActivity.this,"Yeni şifreler eşleşmiyor.Lütfen doğru giriniz.",Toast.LENGTH_LONG).show();
                                        }
                                    }else{
                                        Toast.makeText(ProfileSettingsActivity.this,"Şifreniz en az 6 karakter olmalı.",Toast.LENGTH_LONG).show();
                                    }

                                }else{
                                    Toast.makeText(ProfileSettingsActivity.this,"Lütfen eski şifrenizi doğru giriniz.",Toast.LENGTH_LONG).show();
                                }

                            }
                        });
            }
            //Profil Güncelleme Kısmı kontrol ediliyor.
            if(adSoyad.length()>0 && dogumTarihi.length()>0 && telefon.length()>0){

                userInformation.setName(adSoyad);
                userInformation.setDogumTarihi(dogumTarihi);
                userInformation.setPhone(telefon);
                mUserDatabase.setValue(userInformation);
                Toast.makeText(this,"Değişiklikler başarıyla kaydedildi.",Toast.LENGTH_LONG).show();

            }else {
                Toast.makeText(this,"Lütfen boş kısımları doldurunuz.",Toast.LENGTH_LONG).show();
            }



        }

        /*private void uploadFile() {
            //if there is a file to upload

            if (filePath != null) {
                //displaying a progress dialog while upload is going on
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();


                mStorage.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //if the upload is successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();

                                //and displaying a success toast
                                Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                //if the upload is not successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();

                                //and displaying error message
                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                //calculating progress percentage
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                //displaying percentage in progress dialog
                                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                            }
                        });
            } else {
            }

        }*/
    }