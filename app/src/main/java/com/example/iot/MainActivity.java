package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

<<<<<<< HEAD
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
=======
public class MainActivity extends AppCompatActivity {
    private String data;
>>>>>>> 69dd9dcd2baefd2864ef774da4f9e43f05a86482
    @Override
    protected void onCreate(Bundle savedInstanceState) {;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this,LightIntensityActivity.class);
        startActivity(intent);

    }
<<<<<<< HEAD
=======



>>>>>>> 69dd9dcd2baefd2864ef774da4f9e43f05a86482
}