package com.example.iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iot.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText email,password;
    private TextView forget_password;
    private Button btn_login;
    private ProgressBar loadingBar;
    public String adminData = "admin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);
        forget_password = (TextView) findViewById(R.id.tv_forget_pwd);
        btn_login = (Button) findViewById(R.id.btn_login);
        loadingBar = (ProgressBar) findViewById(R.id.probar_login);
        loadingBar.setVisibility(View.INVISIBLE);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatedAccount();
            }
        });
    }

    private void validatedAccount() {
        String user = email.getText().toString();
        String pwd = password.getText().toString();
        if(TextUtils.isEmpty(user))
        {
            Toast.makeText(this,"Please write email....",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pwd))
        {
            Toast.makeText(this,"Please write password....",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setVisibility(View.VISIBLE);
            btn_login.setText("Checking...");
            allowAccessToAccount(user,pwd);
            loadingBar.setVisibility(View.INVISIBLE);
            btn_login.setText("Login");

        }
    }

    private void allowAccessToAccount(final String user, final String pwd) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference RootRef = database.getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Admin").child(adminData).exists())
                {
                    Users userdata = dataSnapshot.child("Admin").child(adminData).getValue(Users.class);
                    if(userdata.getEmail().equals(user)) {
                        if (userdata.getPassword().equals(pwd)) {
                            Toast.makeText(LoginActivity.this, "Login success...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,"Wrong password...",Toast.LENGTH_SHORT).show();
                            loadingBar.setVisibility(View.INVISIBLE);
                            btn_login.setText("Login");
                        }
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"Wrong email...",Toast.LENGTH_SHORT).show();
                        loadingBar.setVisibility(View.INVISIBLE);
                        btn_login.setText("Login");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}