package com.example.iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iot.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class WarningActivity extends AppCompatActivity {
    private TextView username;
    private String admindata = "admin";
    private TextView tv_warning;
    private ImageView i_logo;
    private Button btn_change_light;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);
        i_logo = findViewById(R.id.i_logo_warning);
        i_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WarningActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        username = (TextView) findViewById(R.id.tv_admin_warning);
        FirebaseDatabase dataUser = FirebaseDatabase.getInstance();
        DatabaseReference RootRef = dataUser.getReference();
        DatabaseReference rf1= RootRef;
        DatabaseReference rf2 = RootRef.child("History");
        rf1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users adminData = dataSnapshot.child("Admin").child(admindata).getValue(Users.class);
                username.setText(adminData.getUsername().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ///////////////////////
        tv_warning = findViewById(R.id.tv_warning);
        Random rand = new Random();
        int i_rand = rand.nextInt(6);
        switch (i_rand) {
            case 0: tv_warning.setText("Ánh sáng khu vực Livingroom có vấn đề");
                break;
            case 1: tv_warning.setText("Ánh sáng khu vực Bedroom có vấn đề");
                break;
            case 2: tv_warning.setText("Ánh sáng khu vực Bathroom  có vấn đề");
                break;
            case 3: tv_warning.setText("Ánh sáng khu vực Diningroom  có vấn đề");
                break;
            case 4: tv_warning.setText("Ánh sáng khu vực Garden  có vấn đề");
                break;
            case 5: tv_warning.setText("Ánh sáng khu vực Kitchenroom có vấn đề");
        }

    }
}