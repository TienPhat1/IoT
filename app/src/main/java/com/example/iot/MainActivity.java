package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<< HEAD
        Intent intent = new Intent(MainActivity.this,ControllLightActivity.class);
=======
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
>>>>>>> 75c8539264ee53290f7de91972a1ced239579aff
        startActivity(intent);

    }
}