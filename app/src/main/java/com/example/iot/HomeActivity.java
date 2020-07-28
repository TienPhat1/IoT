package com.example.iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iot.Model.Light;
import com.example.iot.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {
    private TextView username;
    private String admindata = "admin";
    private ImageView history, light_indensity, controller_light, average;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        username = (TextView) findViewById(R.id.tv_admin);
        FirebaseDatabase dataUser = FirebaseDatabase.getInstance();
        DatabaseReference RootRef = dataUser.getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users adminData = dataSnapshot.child("Admin").child(admindata).getValue(Users.class);
                username.setText(adminData.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        ////////////////////
//        MQTT startMQTT = new MQTT(getApplicationContext());
//        Context context = startMQTT.getAppContex();
//        startMQTT.startMQTT(context);
        //////////////////
        history = (ImageView) findViewById(R.id.i_history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,HistoryActivity.class);
                startActivity(intent);
            }
        });

        light_indensity = (ImageView) findViewById(R.id.i_light);
        light_indensity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,LightIntensityActivity.class);
                startActivity(intent);
            }
        });

        controller_light = (ImageView) findViewById(R.id.i_controller);
        controller_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ControllLightActivity.class);
                startActivity(intent);
            }
        });

        average = (ImageView) findViewById(R.id.i_avg_light);
        average.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,AverageActivity.class);
                startActivity(intent);
            }
        });

        SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");
        TimeZone etTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        date.setTimeZone(etTimeZone);
        Date current = new Date();
        Calendar currentTime = Calendar.getInstance();
        String time = date.format(current.getTime());
        Log.d("Date", time);
        String a = "01";
        int b = Integer.parseInt(a);
        Log.d("b", String.valueOf(b));
        String[] splitTime = time.split("/");
        for(String s : splitTime){
            Log.d("Time", s);
        }




    }

}