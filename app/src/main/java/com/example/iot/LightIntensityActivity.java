package com.example.iot;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class LightIntensityActivity extends AppCompatActivity {
    private ImageView logoApp;
    private TextView value;
    public String data;

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_intensity);

        logoApp = (ImageView) findViewById(R.id.i_logo_light_intensity);

        logoApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LightIntensityActivity.this,HomeActivity.class);
                startActivity(intent);

            }
        });
        try {
            startMQTT();

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void startMQTT() throws MqttException {
        MQTTHelper mqttHelper = new MQTTHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.w("mqtt", serverURI);
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                //Log.d("Mqtt", message.toString());

                Log.d("topic",topic);
                convertData(message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    private void convertData(MqttMessage message) throws JSONException {
            Log.d("abc",message.toString());
            JSONArray jsonArray = new JSONArray(message.toString());
            JSONObject jsonObject = null;
            for (int i = 0; i <jsonArray.length() ; i ++)
            {
                jsonObject = jsonArray.getJSONObject(i);
                Log.d("data",String.valueOf(i));

            }
            String val = null;
            for(int i = 0; i < jsonObject.length(); i++)
            {
                val = jsonObject.getString("values").replaceAll("[^0-9]","");
                value = (TextView) findViewById(R.id.tv_value_light);
                value.setText(val);
                
            }
            pushDataToDatabase(val);


            //Log.d("abc", String.valueOf(jsonmgs));

    }

    private void pushDataToDatabase(final String val) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference rootref = database.getReference();

        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LocalTime time = LocalTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
                Date dateCurrent = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
                String date = formatter.format(dateCurrent);
                String dateCurrent1 =formatter1.format(dateCurrent);
                String [] dateParts = dateCurrent1.split("/");

                HashMap<String,Object> dataSensor = new HashMap<>();
                Random random = new Random();
                int id_room = random.nextInt(3);
                switch (id_room) {
                    case 0: dataSensor.put("Area","Livingroom");
                        break;
                    case 1: dataSensor.put("Area","Bedroom");
                        break;
                    case 2: dataSensor.put("Area","Kitchenroom");
                }
                time.format(DateTimeFormatter.ofPattern("hh/mm/ss"));
                dataSensor.put("Time",String.valueOf(time));
                Log.d("Time",time.toString());
                dataSensor.put("Value",val);
                dataSensor.put("Year",dateParts[0]);
                dataSensor.put("Month",dateParts[0]+dateParts[1]);
                dataSensor.put("Day",date);
                if(dataSnapshot.child("History").exists())
                    rootref.child("History").push().updateChildren(dataSensor);
                else
                    rootref.child("History").updateChildren(dataSensor);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}