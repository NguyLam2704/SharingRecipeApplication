package com.example.sharingrecipeapp.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharingrecipeapp.Adapters.ViewPagerImageAdapter;
import com.example.sharingrecipeapp.Classes.AutoScrollTask;
import com.example.sharingrecipeapp.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Timer;
import java.util.TimerTask;

public class FoodDetailActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;

    String idRecipe;

    ViewPager2 viewPager2;
    ImageView back, avt;

    TextView username, titlefood, heart, save, add, cook, note;

    RecyclerView recycIngre, recycMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        back = findViewById(R.id.backImg);
        avt = findViewById(R.id.avt);
        viewPager2 = findViewById(R.id.viewpagerImage);
        username = findViewById(R.id.nameUser);
        titlefood = findViewById(R.id.TitleFood_Detail);
        heart = findViewById(R.id.text_heart);
        save = findViewById(R.id.text_save);
        add = findViewById(R.id.text_add);
        cook = findViewById(R.id.text_time_detail);
        note = findViewById(R.id.text_Note);
        recycIngre = findViewById(R.id.recyNguyenLieu);
        recycMethod = findViewById(R.id.recyMethod);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext(), RecyclerView.VERTICAL, false);
        recycIngre.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManagerMethod = new LinearLayoutManager(this.getApplicationContext(), RecyclerView.VERTICAL, false);
        recycMethod.setLayoutManager(linearLayoutManagerMethod);


        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        idRecipe = intent.getStringExtra("id");

        getRecipes(idRecipe);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodDetailActivity.this, BottomNavigationCustomActivity.class);
                startActivity(intent);
            }
        });
    }



    private void getRecipes(String idRecipe) {

        final DocumentReference docRef = firebaseFirestore.collection("Recipes").document(idRecipe);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.w(TAG,"Listen failed",e );
                    return;
                }

                if (snapshot != null && snapshot.exists()){
                    Log.d(TAG,"Current data: " + snapshot.getData());
                    String tenRecipe = snapshot.getString("name");
                    titlefood.setText(tenRecipe);


                    String picRecipe = snapshot.getString("image");

                    ViewPagerImageAdapter imageAdapter = new ViewPagerImageAdapter(getApplicationContext(), picRecipe);
                    viewPager2.setAdapter(imageAdapter);

                    TimerTask autoScrollTask = new AutoScrollTask(viewPager2);
                    Timer timer = new Timer();
                    timer.schedule(autoScrollTask, 2000, 2000);

                    heart.setText(String.valueOf(snapshot.get("like")));
                    save.setText(String.valueOf(snapshot.get("save")));


                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

    }


}