package com.example.sharingrecipeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharingrecipeapp.R;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.CreateUser)
        {
            Intent signup = new Intent(WelcomeActivity.this, SignupActivity.class);
            startActivity(signup);
        }
        if(v.getId() == R.id.logintxt)
        {
            Intent login = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(login);
        }
    }
    Button welcome_btn;
    TextView login_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcome_btn = findViewById(R.id.CreateUser);
        welcome_btn.setOnClickListener(this);

        login_txt = findViewById(R.id.logintxt);
        login_txt.setOnClickListener(this);
    }
}
