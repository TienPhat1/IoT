package com.example.iot;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

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
                Log.d("Mqtt", message.toString());

                Log.d("topic",topic);
                convertData(message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    private void convertData(MqttMessage message) throws JSONException {
        JSONObject jsonmgs = new JSONObject(new String(message.getPayload()));
            String key = jsonmgs.keys().next();
            Object valueData = jsonmgs.get(String.valueOf(key));
            Log.i("Info", "Key: " + key + ", value: " + jsonmgs.getString(String.valueOf(key)));
            String sValue= valueData.toString();
            value = (TextView) findViewById(R.id.tv_value_light);
            value.setText(sValue);

            int intValue = Integer.parseInt(sValue);

    }

}