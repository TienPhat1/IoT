package com.example.iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.iot.Model.Light;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MQTT startMQTT = new MQTT(getApplicationContext());
        Context context = startMQTT.getAppContex();
        startMQTT.startMQTT(context);

        Timer timer = new Timer();


        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                TimeZone etTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
                Date dateCurrent = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
                formatDate.setTimeZone(etTimeZone);
                formatTime.setTimeZone(etTimeZone);
                String DayTime = formatDate.format(dateCurrent).replaceAll("/","");
                Log.d("DayTime",DayTime);
                String [] timeParts = formatTime.format(dateCurrent).split(":");
                String convertTimeToSecond = String.valueOf(Integer.parseInt(timeParts[0])*3600
                        +Integer.parseInt(timeParts[1])*60+Integer.parseInt(timeParts[2]) - 60);
                double doubleSecondNormal= Double.parseDouble(convertTimeToSecond)/10267.0;
                String TimeToSecond = String.valueOf(doubleSecondNormal);
                Log.d("Timenormal",TimeToSecond);
                Log.d("Result ", String.valueOf(doubleSecondNormal));
                double number2 = 1.0;
                int count = 0;
                for (double i = 0.0; i < 100000.0; i++){
                    double number1 = (double)i/(double)10267.0;
                    if(number1 == number2) {
                        Log.d("Result ", i + " " + number1);
                    }
                    else
                    {
                        count++;
                    }
                    number2 = number1;
                }
                Log.d("Count ", String.valueOf(count));
                Log.d("DayTime",convertTimeToSecond);
                final ArrayList<Light> list = new ArrayList<>();
                Query query = FirebaseDatabase.getInstance().getReference().child("History").orderByChild(DayTime).startAt(TimeToSecond);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            Log.d("DayTime", String.valueOf(1));
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Light data = snapshot.getValue(Light.class);
                                list.add(data);
                            }
                            receiveData(list);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        },0,1000*20);

        Intent intent = new Intent(MainActivity.this,HomeActivity.class);

        startActivity(intent);

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