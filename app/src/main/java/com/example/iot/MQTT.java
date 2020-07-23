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

public class MQTT {
    public Context getAppContex = null;

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
        String val = null;
        for(int i = 0; i < jsonObject.length(); i++)
        {
            val = jsonObject.getString("values").replaceAll("[^0-9]","");

        }
        pushDataToDatabase(val);
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
