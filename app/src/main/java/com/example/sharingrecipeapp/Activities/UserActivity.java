package com.example.sharingrecipeapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.sharingrecipeapp.R;

public class UserActivity extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setupToolbar();
    }


    private void setupToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.setting_menu,menu);
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu,menu);
        return  true;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.update) {
//
//        } else if (item.getItemId() == R.id.display) {
//            Toast.makeText(this, "you click display", Toast.LENGTH_LONG).show();
//        } else if (item.getItemId() == R.id.language) {
//            Toast.makeText(this, "you click language", Toast.LENGTH_LONG).show();
//        }else if(item.getItemId()==R.id.logout) {
//            Toast.makeText(this, "you click logout", Toast.LENGTH_LONG).show();
//        }
//        return true;
//    }
}