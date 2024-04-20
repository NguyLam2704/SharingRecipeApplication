package com.example.sharingrecipeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sharingrecipeapp.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.returns)
        {
            finish();
        }
        if (v.getId() == R.id.Login_btn)
        {
            startActivity(new Intent(LoginActivity.this, BottomNavigationCustomActivity.class));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageButton returns = findViewById(R.id.returns);
        returns.setOnClickListener(this);

        Button login_btn = findViewById(R.id.Login_btn);
        login_btn.setOnClickListener(this);
    }
}