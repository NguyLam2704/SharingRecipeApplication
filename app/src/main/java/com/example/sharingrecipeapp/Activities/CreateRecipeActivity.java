package com.example.sharingrecipeapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharingrecipeapp.Adapters.AddRecipe.AddMethodAdapter;
import com.example.sharingrecipeapp.Adapters.AddRecipe.AddNguyenLieuAdapter;
import com.example.sharingrecipeapp.Classes.AddMethod;
import com.example.sharingrecipeapp.Classes.AddNguyenLieu;
import com.example.sharingrecipeapp.R;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipeActivity extends AppCompatActivity {

    EditText method, nameNL, soluongNl, donviNL;

    AddMethodAdapter methodAdapter;
    List<AddMethod> methodList;

    AddNguyenLieuAdapter nguyenLieuAdapter;
    List<AddNguyenLieu> nguyenLieuList;
    RecyclerView recy_method, recy_nguyenlieu;
    ImageView btnBack;
    TextView btnAddMethod, btnAddNguyenLieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        recy_method = findViewById(R.id.recyclerViewMedthod);
        recy_method.setLayoutManager(new LinearLayoutManager(this));
        recy_nguyenlieu = findViewById(R.id.recy_NL);
        recy_nguyenlieu.setLayoutManager(new LinearLayoutManager(this));

        nameNL = findViewById(R.id.editTextNameNL);
        soluongNl = findViewById(R.id.editTextslnl);
        donviNL = findViewById(R.id.editTextDonVi);

        method = findViewById(R.id.editTextMethod);
        btnBack = findViewById(R.id.btnback);
        btnAddMethod = findViewById(R.id.btnAddMethod);
        btnAddNguyenLieu = findViewById(R.id.btnAddNguyenLieu);

        methodList = new ArrayList<>();
        methodAdapter = new AddMethodAdapter();
        methodAdapter.setData(methodList);
        recy_method.setAdapter(methodAdapter);


        nguyenLieuList = new ArrayList<>();
        nguyenLieuAdapter = new AddNguyenLieuAdapter();
        nguyenLieuAdapter.setData(nguyenLieuList);
        recy_nguyenlieu.setAdapter(nguyenLieuAdapter);

        btnAddNguyenLieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNguyenLieu();
            }
        });

        btnAddMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addmethod();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void addNguyenLieu() {
        String strName = nameNL.getText().toString().trim();
        String strSL = soluongNl.getText().toString().trim();
        String strDonVi = donviNL.getText().toString().trim();

        if(TextUtils.isEmpty(strName) || TextUtils.isEmpty(strSL) || TextUtils.isEmpty(strDonVi)){
            return;
        }

        nguyenLieuList.add(new AddNguyenLieu(strName, strSL, strDonVi));
        nguyenLieuAdapter.notifyDataSetChanged();
        recy_nguyenlieu.scrollToPosition(nguyenLieuList.size() - 1);

        nameNL.setText("");
        soluongNl.setText("");
        donviNL.setText("");
    }

    private void addmethod() {
        String strMethod = method.getText().toString().trim();
        if(TextUtils.isEmpty(strMethod)){
            return;
        }

        methodList.add(new AddMethod(strMethod));
        methodAdapter.notifyDataSetChanged();
        recy_method.scrollToPosition(methodList.size() - 1);

        method.setText("");

    }
}