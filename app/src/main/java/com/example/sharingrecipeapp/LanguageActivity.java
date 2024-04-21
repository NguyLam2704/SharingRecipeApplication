package com.example.sharingrecipeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LanguageActivity extends AppCompatActivity implements View.OnClickListener{

    public void onClick(View v) {
        if (v.getId() == R.id.returns) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_language);

        ImageButton returns = findViewById(R.id.returns);
        returns.setOnClickListener((View.OnClickListener) this);
    }
}