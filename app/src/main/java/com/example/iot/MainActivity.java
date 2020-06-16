package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
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
                Log.w("data", String.valueOf(reconnect));
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("Mqtt", message.toString());
                Log.d("data",topic);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

}