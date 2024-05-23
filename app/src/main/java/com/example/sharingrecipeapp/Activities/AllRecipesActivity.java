package com.example.sharingrecipeapp.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Adapters.Home.RecipesAdapter;
import com.example.sharingrecipeapp.Adapters.Home.RecipesRandomAdapter;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllRecipesActivity extends AppCompatActivity {

    RecyclerView recyRecipes;

    RecipesAdapter recipesAdapter;
    private List<Recipes> listRecipes;

    ImageView btnBack;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    ActivityResultLauncher<Intent> activityResultLauncher =  registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
        if (result.getResultCode() == 111){
            for (int i = 0; i < listRecipes.size(); i++){
                if (result.getData().getExtras().get("id").toString().equals(listRecipes.get(i).getId())){
                    listRecipes.get(i).setSave(String.valueOf(result.getData().getExtras().getString("save")));
                }
            }
            recipesAdapter.notifyDataSetChanged();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_recipes);
        recyRecipes = findViewById(R.id.recyc_AllRecipes);
        recyRecipes.setLayoutManager(new GridLayoutManager(this,2));
        btnBack = findViewById(R.id.btn_back_all);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        setdataRecyc();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    String username;
    private void setdataRecyc() {


        recipesAdapter = new RecipesAdapter();
        listRecipes = new ArrayList<>();
        recipesAdapter.setData(listRecipes, new IClickOnItemRecipe() {
            @Override
            public void onClickItemRecipe(Recipes recipes) {
                onClickGoToDetailFood(recipes);
            }
        });
        recyRecipes.setAdapter(recipesAdapter);
        firebaseFirestore.collection("Recipes").orderBy("name")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Error", "listen:error", error);
                            return;
                        }

                        for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                            String id = documentSnapshot.getString("id");

                            DocumentReference docRef = documentSnapshot.getDocumentReference("Users");

                            firebaseFirestore.collection("SaveRecipes").whereEqualTo("Recipes",id).get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            ArrayList<String> idUser = new ArrayList<>();
                                            for (QueryDocumentSnapshot doc :queryDocumentSnapshots)
                                            {
                                                idUser = (ArrayList<String>) doc.get("idUsers");
                                                String save = String.valueOf(idUser.size());
                                                String image = documentSnapshot.getString("image");
                                                String name = documentSnapshot.getString("name");
                                                String time = documentSnapshot.getString("timecook");

                                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot doc) {
                                                        username = doc.getString("username");
                                                        listRecipes.add(new Recipes(id, image, name, save,time, username));
                                                        recipesAdapter.notifyDataSetChanged();
                                                    }
                                                });
                                            }
                                        }
                                    });

                        }

                    }
                });
    }

    private void onClickGoToDetailFood(Recipes recipes) {
        Intent intent = new Intent(this, FoodDetailActivity.class);
        intent.putExtra("id", recipes.getId());
        activityResultLauncher.launch(intent);
    }

}