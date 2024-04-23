package com.example.sharingrecipeapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sharingrecipeapp.Adapters.BriefAdapter;
import com.example.sharingrecipeapp.Classes.ABrief;

import java.util.ArrayList;

public class SaveListActivity extends AppCompatActivity {

    GridView GvListSave;
    ArrayList<ABrief> arrayBrief;
    BriefAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_list);
        AnhXa();
        adapter= new BriefAdapter(this, R.layout.brief_save, arrayBrief);
        GvListSave.setAdapter(adapter);
        GvListSave.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText((Context) SaveListActivity.this, arrayBrief.get(position).getNameRe(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AnhXa(){
        GvListSave = (GridView) findViewById(R.id.GridView);
        arrayBrief= new ArrayList<>();
        arrayBrief.add(new ABrief("Bún Bò",R.drawable.bunbo,"30m","5.0","300"));
        arrayBrief.add(new ABrief("Bánh Xèo",R.drawable.banhxeo,"30m","5.0","300"));
        arrayBrief.add(new ABrief("Cơm Tấm",R.drawable.comtam,"30m","5.0","300"));
        arrayBrief.add(new ABrief("Chả Giò",R.drawable.chagio,"30m","5.0","300"));
        arrayBrief.add(new ABrief("Trà Sữa",R.drawable.trasua,"30m","5.0","300"));
    }


}