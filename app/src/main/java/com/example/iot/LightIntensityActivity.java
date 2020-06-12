package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LightIntensityActivity extends AppCompatActivity {
    private ImageView logoApp;
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
    }
}