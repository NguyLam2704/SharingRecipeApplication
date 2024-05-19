package com.example.sharingrecipeapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.sharingrecipeapp.Adapters.DetailRecipe.ListMethodInDetailAdapter;
import com.example.sharingrecipeapp.Adapters.MethodViewPager2Adapter;
import com.example.sharingrecipeapp.Classes.Method;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class Method_Silder_Activity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;

    private ViewPager2 mViewPager2;
    private CircleIndicator3 mCircleIndicator3;

    private List<Method> mListMethod;

    ImageView btn;

    String idRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_silder);

        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        idRecipe = intent.getStringExtra("id");


        mViewPager2 = findViewById(R.id.view_pager_2);
        mCircleIndicator3 = findViewById(R.id.circle_indicator_3);
        btn = findViewById(R.id.img_btn_back_method);

        mListMethod = getMethod(idRecipe);
        //mListMethod = getList();
        MethodViewPager2Adapter adapter = new MethodViewPager2Adapter(mListMethod);
        mViewPager2.setAdapter(adapter);

        mCircleIndicator3.setViewPager(mViewPager2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }



    private List<Method> getMethod(String idRecipe) {
        List<Method> methodList = new ArrayList<>();
        firebaseFirestore.collection("Recipes").document(idRecipe).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<String> doc = (ArrayList<String>) task.getResult().get("method");
                            if(!doc.isEmpty()){
                                for (int i = 0;i<doc.size();i++){
                                    String step =  doc.get(i);
                                    methodList.add(new Method(step));
                                }

                            }
                        }
                    }
                });
        return methodList;
    }




}