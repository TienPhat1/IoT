package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this,ControllLightActivity.class);

        startActivity(intent);

    }
}