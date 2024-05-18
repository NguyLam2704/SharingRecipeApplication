package com.example.sharingrecipeapp.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Activities.BottomNavigationCustomActivity;


import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Adapters.Home.RecipesAdapter;
import com.example.sharingrecipeapp.Classes.ABrief;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.R;
import com.example.sharingrecipeapp.databinding.ActivityPlantoSavedBinding;
import com.example.sharingrecipeapp.databinding.ActivitySaveListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaveListActivity extends AppCompatActivity {

    ActivitySaveListBinding binding;

    RecyclerView recy_ViewRecipes;
    FirebaseAuth auth;
    FirebaseFirestore db;

    TextView soluong;

    ArrayList<Recipes> recipesList;
    Integer number = 0;
    ImageButton back_btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_list);

        binding = ActivitySaveListBinding.inflate(getLayoutInflater());


        recy_ViewRecipes = findViewById(R.id.recy_save);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        back_btn = findViewById(R.id.btn_back_profile);
        soluong = findViewById(R.id.textTb);
        displaySavedRecipes();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void displaySavedRecipes(){
        db.collection("SaveRecipes").whereArrayContains("idUsers",auth.getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        List<String> tenRecipes = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                            tenRecipes.add(documentSnapshot.get("Recipes").toString());
                        }
                        recipesList = new ArrayList<>();
                        for (String i : tenRecipes){
                            db.collection("Recipes").document(i).get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        db.collection("SaveRecipes").whereEqualTo("Recipes",i).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                                ArrayList<String> idUser = new ArrayList<>();
                                                for (QueryDocumentSnapshot doc :value)
                                                {
                                                    if(doc.get("idUsers") != null)
                                                    {
                                                        idUser = (ArrayList<String>) doc.get("idUsers");
                                                    }
                                                    String save = String.valueOf(idUser.size());
                                                    String image = documentSnapshot.getString("image");
                                                    String name = documentSnapshot.getString("name");
                                                    String time = documentSnapshot.getString("timecook");
                                                    Recipes recipes = new Recipes(i,image,name,save,time);
                                                    recipesList.add(recipes);
                                                    if (i == tenRecipes.get(tenRecipes.size()-1)){
                                                        RecipesAdapter myAdapter = new RecipesAdapter();
                                                        myAdapter.setData(recipesList, new IClickOnItemRecipe() {
                                                            @Override
                                                            public void onClickItemRecipe(Recipes recipes) {
                                                                onClickGoToDetailFood(recipes);
                                                            }
                                                        });
                                                        recy_ViewRecipes.setLayoutManager(new GridLayoutManager(binding.getRoot().getContext(), 2));
                                                        recy_ViewRecipes.setAdapter(myAdapter);
                                                    }
//                                                    recipesRandomAdapter.setData(listRecipes, new IClickOnItemRecipe() {
//                                                        @Override
//                                                        public void onClickItemRecipe(Recipes recipes) {
//                                                            onClickGoToDetailFood(recipes);
//                                                        }
//                                                    });
//                                                    recyclerViewRandom.setAdapter(recipesRandomAdapter);
                                                }
                                            }
                                        });
                                        number++;
                                        soluong.setText("Bạn đã lưu được " + String.valueOf(number) +" món ăn");
                                    });
                        }
                    }
                });


    }

    private void onClickGoToDetailFood(Recipes recipes){
        Intent intent = new Intent(this, FoodDetailActivity.class);
        intent.putExtra("id", recipes.getId());
        startActivity(intent);
    }

//    private void setdataGrid(){
//
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        recipesAdapter = new RecipesAdapter();
//        txt= (TextView) findViewById(R.id.text_save);
//        CollectionReference collectionReferenceuser= firebaseFirestore.collection("SaveRecipes");
//        collectionReferenceuser
//                .whereArrayContainsAny("idUser", Arrays.asList(userID) )
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            Log.w("Error", "listen:error", error);
//                            return;
//                        }
//                        listSaveRe = new ArrayList<>();
//                        for (DocumentSnapshot documentSnapshot: value.getDocuments()){
//                            listSaveRe.add(documentSnapshot.getString("idRecipes"));
//                        }
//                        Number=listSaveRe.size();
//
//                    }
//                });
//
//        txt.setText("Bạn đã lưu được "+Number+"món ăn");
//
//        firebaseFirestore.collection("Recipes")
//                .whereIn("idRecipes", listSaveRe)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        listRecipes = new ArrayList<>();
//                        for (DocumentSnapshot documentSnapshot : value.getDocuments()){
//                            String id = documentSnapshot.getString("id");
//                            String image = documentSnapshot.getString("image");
//                            String name = documentSnapshot.getString("name");
//                            String save = String.valueOf(documentSnapshot.get("save"));
//                            String time = documentSnapshot.getString("timecook");
//                            listRecipes.add(new Recipes(id, image, name, save, time));
//                        }
//                        Number=listRecipes.size();
//                        recipesAdapter.setData(listRecipes, new IClickOnItemRecipe() {
//                            @Override
//                            public void onClickItemRecipe(Recipes recipes) {
//                                onClickGoToDetailFood(recipes);
//                            }
//                            //public void onLongClickItemR
//                        });
//
//                        recyclerView.setAdapter(recipesAdapter);
//
//                    }
//                });
//
//
//    }
//
//    private void onClickGoToDetailFood(Recipes recipes) {
//        bottomNavigationCustomActivity.gotoFoodDetail(recipes);
//    }

}