package com.example.weysi.firabaseuserregistration.activitys;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.EventLogTags;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.adapters.UserListAdapter;
import com.example.weysi.firabaseuserregistration.fragments.FriendsFragment;
import com.example.weysi.firabaseuserregistration.fragments.PersonelTimeLineFragment;
import com.example.weysi.firabaseuserregistration.informations.PlaceInformation;
import com.example.weysi.firabaseuserregistration.informations.UserInformation;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.google.android.gms.location.places.Place;
import com.google.firebase.appindexing.builders.Actions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlaceActivity extends AppCompatActivity implements View.OnClickListener{
    PieChart pieChart ;
    ArrayList<Entry> entries ;
    ArrayList<String> PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;
    private DatabaseReference mPlaceDatabaseReference;
    private DatabaseReference mInPlaceCheckInsDatabaseReference;
    private DatabaseReference mUsersDatabaseReference;
    private long start;
    private long end;
    private long count;
    private String place_id;
    private int maleCount;
    private int femaleCount;
    private int sumCount;
    private int hsumCount;
    private int hmaleCount;
    private int hfemaleCount;
        private String sumString;
    private ImageButton imageButtonBack;
    private TextView mekanAdiTextView;
    private  TextView mekanAdresiTextView;
    private  TextView birSaatlikSayacTextView;
    private TextView tumZamanlarSayaciTextView;
    private TextView bilgiTextView;
    private Context context;
    ListView listViewUsers;
    List<UserInformation> userInformationList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        context=this;
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Sayfa Bilgileri Getiriliyor...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        imageButtonBack = (ImageButton) findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(this);
        mekanAdiTextView = (TextView)findViewById(R.id.mekanAdiTextView);
        mekanAdresiTextView = (TextView)findViewById(R.id.mekanAdresiTextView);
        birSaatlikSayacTextView = (TextView)findViewById(R.id.birSaatlikSayac);

        tumZamanlarSayaciTextView = (TextView)findViewById(R.id.tumZamanlarSayaci);
        bilgiTextView=(TextView)findViewById(R.id.textViewBilgi);
        pieChart = (PieChart) findViewById(R.id.chart1);

        pieChart.setUsePercentValues(true);
        pieChart.setHoleColorTransparent(true);
        pieChart.setHoleRadius(0);
        pieChart.setTransparentCircleRadius(0);
        pieChart.setDescription("*Son 1 saat");
        pieChart.setDescriptionColor(Color.rgb(226,115,24));


        entries = new ArrayList<>();

        PieEntryLabels = new ArrayList<String>();
        place_id = getIntent().getStringExtra("placeID");
        mPlaceDatabaseReference = FirebaseDatabase.getInstance().getReference("place");
        mInPlaceCheckInsDatabaseReference = FirebaseDatabase.getInstance().getReference("InPlaceCheckIns").child(place_id);

        start = System.currentTimeMillis();
        end = start-3600000;
        userInformationList=new ArrayList<>();
        Query checkQuery = mInPlaceCheckInsDatabaseReference.orderByChild("checkInTime").startAt((-1)*start).endAt((-1)*end);
        checkQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInformationList.clear();
                count = dataSnapshot.getChildrenCount();
                birSaatlikSayacTextView.setText(String.valueOf(count));
                if(dataSnapshot.hasChildren())
                {
                    for(DataSnapshot postsnapshot:dataSnapshot.getChildren()) {
                        final UserInformation userInformation = (UserInformation) postsnapshot.getValue(UserInformation.class);
                        userInformation.setName(postsnapshot.child("userName").getValue().toString());
                        mUsersDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userInformation.getUserID());
                        mUsersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String userImage = dataSnapshot.child("image").getValue().toString();

                                userInformation.setImage(userImage);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        userInformationList.add(userInformation);
                        if(postsnapshot.child("cinsiyet").getValue().toString().compareTo("Erkek")==0)
                            hmaleCount++;
                        else
                            hfemaleCount++;
                    }
                    birSaatlikSayacTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.activity_in_place_check_in);

                            dialog.setTitle("Şimdi Buradakiler");

                            listViewUsers =(ListView) dialog.findViewById(R.id.listViewUsers);
                            listViewUsers.setClickable(true);
                            listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    Object o = listViewUsers.getItemAtPosition(position);
                                    UserInformation userInformation=(UserInformation) o;
                                    Intent intent=new Intent(getApplicationContext(),AnotherUserProfileActivity.class);
                                    intent.putExtra("UserID",userInformation.getUserID());
                                    startActivity(intent);
                                }
                            });

                            if(userInformationList.size()!=0) {
                                UserListAdapter userListAdapter = new UserListAdapter(PlaceActivity.this, userInformationList,getResources());

                                listViewUsers.setAdapter(userListAdapter);
                            }
                            dialog.show();


                        }
                    });

                }
                else
                {
                    hmaleCount=0;
                    hfemaleCount=0;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

         mPlaceDatabaseReference.child(place_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              PlaceInformation placeInformation = dataSnapshot.getValue(PlaceInformation.class);
                mekanAdiTextView.setText(placeInformation.getName());
                mekanAdresiTextView.setText(placeInformation.getPlaceAddress());
                maleCount = placeInformation.getMaleCount();
                femaleCount = placeInformation.getFemaleCount();
                sumCount  = maleCount + femaleCount;
                hsumCount  = hmaleCount + hfemaleCount;
                sumString=String.valueOf(sumCount);
                tumZamanlarSayaciTextView.setText(sumString);

                if (hsumCount==0)
                {
                    pieChart.setVisibility(View.INVISIBLE);
                    bilgiTextView.setVisibility(View.VISIBLE);

                }

                Legend l = pieChart.getLegend();
                l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
                l.setXEntrySpace(7);
                l.setYEntrySpace(5);
                        // AddValuesToPIEENTRY();
                entries.add(new BarEntry((float)(hmaleCount*100)/(hsumCount), 0));
                entries.add(new BarEntry((float)(hfemaleCount*100)/(hsumCount), 1));
                AddValuesToPieEntryLabels();

                pieDataSet = new PieDataSet(entries, "");
                pieDataSet.setSliceSpace(3);
                pieDataSet.setSelectionShift(5);

                pieData = new PieData(PieEntryLabels, pieDataSet);
                pieData.setValueFormatter(new PercentFormatter());
                pieData.setValueTextSize(14f);
                pieData.setValueTextColor(Color.WHITE);
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                pieChart.setData(pieData);
                pd.dismiss();
                pieChart.animateY(3000);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void AddValuesToPieEntryLabels(){
       PieEntryLabels.add("Male");
       PieEntryLabels.add("Female");

    }

    @Override
    public void onClick(View v) {
        if(v == imageButtonBack){
            finish();
        }
    }
}
