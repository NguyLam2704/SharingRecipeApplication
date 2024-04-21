package com.example.sharingrecipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sharingrecipeapp.Activities.UserActivity;

public class UpdateProfileActivity extends AppCompatActivity {

    public void onClick(View v) {
        if (v.getId() == R.id.returns)
        {
            finish();
        }
        if (v.getId() == R.id.update_btn)
        {
            startActivity(new Intent(UpdateProfileActivity.this, UserActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_profile);

        ImageButton returns = findViewById(R.id.returns);
        returns.setOnClickListener((View.OnClickListener) this);

        Button update_btn = findViewById(R.id.update_btn);
        update_btn.setOnClickListener((View.OnClickListener) this);
    }
}