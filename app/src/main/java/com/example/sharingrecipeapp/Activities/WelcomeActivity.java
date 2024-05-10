package com.example.sharingrecipeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharingrecipeapp.R;

public class WelcomeActivity extends AppCompatActivity {

    Button welcome_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcome_btn = findViewById(R.id.WelcometoHome);
        welcome_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,BottomNavigationCustomActivity.class));
            }
        });


    }
}
