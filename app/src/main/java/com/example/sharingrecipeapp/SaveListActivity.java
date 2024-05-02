package com.example.sharingrecipeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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

import com.example.sharingrecipeapp.Adapters.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Adapters.RecipesAdapter;
import com.example.sharingrecipeapp.Classes.ABrief;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SaveListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecipesAdapter recipesAdapter;
    List<String> listSaveRe;
     List<Recipes> listRecipes;
     FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
     FirebaseFirestore firebaseFirestore;
    String userID;
    TextView txt;
    int Number;
    private BottomNavigationCustomActivity bottomNavigationCustomActivity;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_list);
        recyclerView= findViewById(R.id.recy_save);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            userID = currentUser.getUid();
        }
        firebaseFirestore = FirebaseFirestore.getInstance();

        setdataGrid();


    }

    private void setdataGrid(){

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recipesAdapter = new RecipesAdapter();
        txt= (TextView) findViewById(R.id.text_save);
        CollectionReference collectionReferenceuser= firebaseFirestore.collection("SaveRecipes");
        collectionReferenceuser
                .whereEqualTo("idUser",userID )
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Error", "listen:error", error);
                            return;
                        }
                        listSaveRe = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot: value.getDocuments()){
                            listSaveRe.add(documentSnapshot.getString("idRecipes"));
                        }
                        Number=listSaveRe.size();

                    }
                });

        txt.setText("Bạn đã lưu được "+Number+"món ăn");

        firebaseFirestore.collection("Recipes")
                .whereIn("idRecipes", listSaveRe)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        listRecipes = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                            String id = documentSnapshot.getString("id");
                            String image = documentSnapshot.getString("image");
                            String name = documentSnapshot.getString("name");
                            String save = String.valueOf(documentSnapshot.get("save"));
                            String time = documentSnapshot.getString("timecook");
                            listRecipes.add(new Recipes(id, image, name, save, time));
                        }
                        Number=listRecipes.size();
                        recipesAdapter.setData(listRecipes, new IClickOnItemRecipe() {
                            @Override
                            public void onClickItemRecipe(Recipes recipes) {
                                onClickGoToDetailFood(recipes);
                            }
                            //public void onLongClickItemR
                        });
                        recyclerView.setAdapter(recipesAdapter);
                    }
                });


    }

    private void onClickGoToDetailFood(Recipes recipes) {
        bottomNavigationCustomActivity.gotoFoodDetail(recipes);
    }

}