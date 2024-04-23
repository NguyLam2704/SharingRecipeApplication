package com.example.sharingrecipeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

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
        setContentView(R.layout.activity_language);

        ImageButton returns = findViewById(R.id.returns);
        returns.setOnClickListener((View.OnClickListener) this);
    }
}