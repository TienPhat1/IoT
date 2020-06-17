package com.example.iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iot.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {
    private TextView username;
    private String admindata = "admin";
    private ImageView history, light_indensity, controller_light;

    MQTTHelper mqttHelper;
    public void startMQTT(){
        mqttHelper = new MQTTHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("sendData",mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    public void sendDataToMQTT(final String ID, final String value1, final String value2){
        final Timer aTimer = new Timer();
        TimerTask aTask = new TimerTask() {
            @Override
            public void run() {
                MqttMessage msg = new MqttMessage();
                msg.setId(1234);
                msg.setQos(0);
                msg.setRetained(true);

                String data = "[{\"device_id\":\"LightD\", \"values\":[\"" + value1 + "\",\"" + value2 + "\"]}]";
                byte[] b = data.getBytes(Charset.forName("UTF-8"));
                msg.setPayload(b);

                try {
                    mqttHelper.mqttAndroidClient.publish("Topic/LightD", msg);
                    Log.e("publish","published");
                }catch (MqttException e){
                }
            }
        };
        aTimer.schedule(aTask, 10000, 10000);
    }

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
                username.setText(adminData.getUsername().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        startMQTT();
        sendDataToMQTT("LightD","1","0");

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
    }
}