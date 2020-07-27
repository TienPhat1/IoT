package com.example.iot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.TimeZone;

public class MQTT {
    public Context getAppContex = null;
    public String value;

    public MQTT(Context getAppCont){
        getAppContex = getAppCont;
    }

    public Context getAppContex() {
        return getAppContex;
    }

    public void startMQTT(Context context){
        MQTTHelper mqttHelper = new MQTTHelper(context);
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
        JSONArray jsonArray = new JSONArray(message.toString());
        JSONObject jsonObject = null;
        for (int i = 0; i <jsonArray.length() ; i ++)
        {
            jsonObject = jsonArray.getJSONObject(i);
            Log.d("data",String.valueOf(i));

        }
        String val = jsonObject.getString("values").replaceAll("[^0-9]","");
        pushDataToDatabase(val);
    }

    private void pushDataToDatabase(final String val) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference rootref = database.getReference();

        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TimeZone etTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
                Date dateCurrent = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
                formatDate.setTimeZone(etTimeZone);
                formatTime.setTimeZone(etTimeZone);
                String [] dateParts = formatDate.format(dateCurrent).split("/");
                String [] timeParts = formatTime.format(dateCurrent).split(":");
                HashMap<String,Object> dataSensor = new HashMap<>();
                Random random = new Random();
                int id_room = random.nextInt(6);
                switch (id_room) {
                    case 0: dataSensor.put("Area","Livingroom");
                        break;
                    case 1: dataSensor.put("Area","Bedroom");
                        break;
                    case 2: dataSensor.put("Area","Kitchenroom");
                        break;
                    case 3: dataSensor.put("Area","Garden");
                        break;
                    case 4: dataSensor.put("Area","Diningroom");
                        break;
                    case 5: dataSensor.put("Area","Bathroom");
                }
                dataSensor.put("Power",String.valueOf(1+random.nextInt(5)));
                dataSensor.put("Time",timeParts[0]+":"+timeParts[1]+":"+dateParts[2]);
                dataSensor.put("Value",val);
                dataSensor.put("Year",dateParts[0]);
                dataSensor.put("Month",dateParts[0]+dateParts[1]);
                dataSensor.put("Day",dateParts[0]+dateParts[1]+dateParts[2]);
                String convertTimeToSecond = String.valueOf(Integer.parseInt(timeParts[0])*3600
                        +Integer.parseInt(timeParts[1])*60+Integer.parseInt(timeParts[2]));
                String keyOfSecond = dateParts[0]+dateParts[1]+dateParts[2];
                dataSensor.put(keyOfSecond,convertTimeToSecond);

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
