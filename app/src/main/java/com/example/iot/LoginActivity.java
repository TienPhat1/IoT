package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText email,password;
    private TextView forget_password;
    private Button btn_login;
    private ProgressBar loadingBar;
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
        }
    }
}