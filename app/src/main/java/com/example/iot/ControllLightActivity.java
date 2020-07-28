package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

public class ControllLightActivity extends AppCompatActivity {
    private BubbleSeekBar bubbleseekbar;
    private TextView value_controller_light;
    private Button btn_changevalue,turn_on,turn_off;
    private ImageView logo;
    private int value;
    MQTTHelper mqttHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controll_light);

        bubbleseekbar = (BubbleSeekBar) findViewById(R.id.sbar_controller_light);
        value_controller_light = (TextView) findViewById(R.id.tv_value_controller_light);
        btn_changevalue = (Button) findViewById(R.id.btn_change_light);
        turn_on = (Button) findViewById(R.id.btn_turn_on_light);
        turn_off = (Button) findViewById(R.id.btn_turn_off_light);
        logo = (ImageView) findViewById(R.id.i_logo_controller_light);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ControllLightActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        bubbleseekbar.setProgress(0);
        bubbleseekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                value = progress;
                value_controller_light.setText(Integer.toString(value));
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });
//        MQTT startMQTT = new MQTT(getApplicationContext());
//        Context context = startMQTT.getAppContex();
//        startMQTT.startMQTT(context);

        try {
            startMQTT();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        btn_changevalue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String values = String.valueOf(value);
                sendDataToMQTT("Topic/lightD","1",values);
            }
        });
        turn_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToMQTT("Topic/lightD","1","255");
            }
        });
        turn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToMQTT("Topic/lightD","0","0");
            }
        });

    }
    public void startMQTT() throws MqttException{
        mqttHelper = new MQTTHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
    public void sendDataToMQTT(final String ID, final String value1, final String value2){

                MqttMessage msg = new MqttMessage();
                msg.setId(1234);
                msg.setQos(0);
                msg.setRetained(true);

                String data = "[{\"device_id\":\"LightD\", \"values\":[\"" + value1 + "\",\"" + value2 + "\"]}]";
                byte[] b = data.getBytes(StandardCharsets.UTF_8);
                msg.setPayload(b);

                try {
                    mqttHelper.mqttAndroidClient.publish("Topic/lightD", msg);
                    Log.e("publish","published");
                }catch (MqttException ignored){
                }
    }
}