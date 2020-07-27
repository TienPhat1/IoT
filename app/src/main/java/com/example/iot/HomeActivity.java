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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
        MQTT startMQTT = new MQTT(getApplicationContext());
        Context context = startMQTT.getAppContex();
        startMQTT.startMQTT(context);
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

        Date date = new Date();
        Log.d("Date", String.valueOf(date));
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("kk:mm:ss");
        Log.d("time", dateFormat.format(date));

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final ArrayList<Light> list = new ArrayList<>();
                Query query = FirebaseDatabase.getInstance().getReference().child("History");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Light data = snapshot.getValue(Light.class);
                            list.add(data);
                        }
                        receiveData(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                
                
            }
        },0,1000*20);

    }

    private void receiveData(ArrayList<Light> list) {
        int i = 0;
        for(Light datalight: list){
            ++i;
            if(i == list.size())
                Log.d("Data repeat", datalight.getArea());
        }
        Log.d("Data lenght", String.valueOf(list.size()));
    }
}