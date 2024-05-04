package com.example.sharingrecipeapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Adapters.Home.RecipesAdapter;
import com.example.sharingrecipeapp.Adapters.PlanList.AdapterPlanListRecipes;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.Fragments.PlanFragment;
import com.example.sharingrecipeapp.R;
import com.example.sharingrecipeapp.databinding.ActivityPlantoSavedBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PlantoSavedActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    BottomNavigationCustomActivity bottomNavigationCustomActivity;
    ActivityPlantoSavedBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore db;

    ImageButton back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planto_saved);
        binding = ActivityPlantoSavedBinding.inflate(getLayoutInflater());


        recyclerView = findViewById(R.id.List_save_recipes);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        back_btn = findViewById(R.id.btn_Back_to_plan);
        displaySavedRecipes();


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void displaySavedRecipes() {
        List<String> tenRecipe = new ArrayList<>();
        db.collection("SaveRecipes").whereEqualTo("idUser",auth.getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            tenRecipe.add(doc.get("idRecipes").toString());
                        }
                        ArrayList<Recipes> recipesList = new ArrayList<>();
                        for (String i : tenRecipe) {
                            db.collection("Recipes").document(i).get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        String image = documentSnapshot.getString("image");
                                        String name = documentSnapshot.getString("name");
                                        String save = documentSnapshot.get("save").toString();
                                        String time = documentSnapshot.get("timecook").toString();

                                        Recipes recipes = new Recipes(i, image, name, save, time);
                                        recipesList.add(recipes);

                                        RecipesAdapter myAdapter = new RecipesAdapter();
                                        myAdapter.setData(recipesList, new IClickOnItemRecipe() {
                                            @Override
                                            public void onClickItemRecipe(Recipes recipes) {
                                                onClickGoToDetailFood(recipes);
                                            }
                                        });
                                        recyclerView.setLayoutManager(new GridLayoutManager(binding.getRoot().getContext(), 2));
                                        recyclerView.setAdapter(myAdapter);
                                    });
                        }
                    }
                });

    }


    private void onClickGoToDetailFood(Recipes recipes) {
        Intent intent = new Intent(binding.getRoot().getContext(),FoodDetailActivity.class);
        intent.putExtra("id", recipes.getId());
        startActivity(intent);
    }


}