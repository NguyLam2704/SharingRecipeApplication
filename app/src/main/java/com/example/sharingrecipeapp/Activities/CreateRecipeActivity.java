package com.example.sharingrecipeapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.sharingrecipeapp.R;

public class CreateRecipeActivity extends AppCompatActivity {
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        btnBack = findViewById(R.id.btnback);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}