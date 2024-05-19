package com.example.sharingrecipeapp.Activities;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharingrecipeapp.Adapters.DetailRecipe.ViewPagerImageFoodAdapter;
import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Adapters.Home.RecipesAdapter;
import com.example.sharingrecipeapp.Classes.AutoScrollTask;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ThemeActivity extends AppCompatActivity {

    String idTheme;

    BottomNavigationCustomActivity bottomNavigationCustomActivity;

    List<Recipes> listRecipes;

    FirebaseFirestore firebaseFirestore;
    RecipesAdapter recipesAdapter;
    ImageView btnBack;
    TextView titleTheme;
    RecyclerView recycFoodTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        btnBack = findViewById(R.id.btn_back_theme);
        titleTheme = findViewById(R.id.tittle_Theme);

        recycFoodTheme = findViewById(R.id.recyc_ThemeDetail);
        recycFoodTheme.setLayoutManager(new GridLayoutManager(this.getApplicationContext(), 2));

        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        idTheme = intent.getStringExtra("id");

        getData(idTheme);
        getDataRecy(idTheme);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getDataRecy(String idTheme) {

        recipesAdapter = new RecipesAdapter();
        firebaseFirestore.collection("Recipes").whereEqualTo(idTheme, true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Error", "listen:error", error);
                            return;
                        }
                        listRecipes = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                            String id = documentSnapshot.getString("id");
                            firebaseFirestore.collection("SaveRecipes").whereEqualTo("Recipes",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                    ArrayList<String> idUser = new ArrayList<>();
                                    for (QueryDocumentSnapshot doc :value)
                                    {
                                        idUser = (ArrayList<String>) doc.get("idUsers");
                                        String save = String.valueOf(idUser.size());
                                        String image = documentSnapshot.getString("image");
                                        String name = documentSnapshot.getString("name");
                                        String time = documentSnapshot.getString("timecook");
                                        listRecipes.add(new Recipes(id, image, name, save, time));
                                        recipesAdapter.setData(listRecipes, new IClickOnItemRecipe() {
                                            @Override
                                            public void onClickItemRecipe(Recipes recipes) {
                                                onClickGoToDetailFood(recipes);
                                            }
                                        });
                                        recycFoodTheme.setAdapter(recipesAdapter);
                                    }
                                }
                            });

                        }

                    }
                });
    }

    private void getData(String idTheme) {

    firebaseFirestore.collection("Theme")
                    .whereEqualTo("id", idTheme)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                                    for(QueryDocumentSnapshot doc: value){
                                        if(doc.exists()){
                                            String tenTheme = doc.getString("name");
                                            titleTheme.setText(tenTheme);
                                        }
                                    }
                                }
                            });
    }

    private void onClickGoToDetailFood(Recipes recipes) {
            Intent intent = new Intent(ThemeActivity.this, FoodDetailActivity.class);
            intent.putExtra("id", recipes.getId());
            startActivity(intent);

    }
}