package com.example.sharingrecipeapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlantoSavedActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    BottomNavigationCustomActivity bottomNavigationCustomActivity;
    ActivityPlantoSavedBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore db;

    PlanFragment planFragment;

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

    private void displaySavedRecipes(){
        db.collection("SaveRecipes").whereArrayContains("idUsers",auth.getUid()).get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful()){
                       List<String> tenRecipes = new ArrayList<>();
                       for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                           tenRecipes.add(documentSnapshot.get("Recipes").toString());
                       }
                       ArrayList<Recipes> recipesList = new ArrayList<>();
                       for (String i : tenRecipes){
                           db.collection("Recipes").document(i).get()
                                   .addOnSuccessListener(documentSnapshot -> {
                                       String image = documentSnapshot.getString("image");
                                       String name = documentSnapshot.getString("name");
                                       String save = documentSnapshot.get("save").toString();
                                       String time = documentSnapshot.get("timecook").toString();

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
                                           recyclerView.setLayoutManager(new GridLayoutManager(binding.getRoot().getContext(), 2));
                                           recyclerView.setAdapter(myAdapter);
                                       }

                                   });
                       }
                   }
                });
    }


    private void onClickGoToDetailFood(Recipes recipes) {
        Bundle extras = getIntent().getExtras();
        String date = extras.getString("date");
        String weekID = extras.getString("weekID");
        ArrayList<String> tenRecipe = new ArrayList<>();
        tenRecipe.add(recipes.getId());


        DocumentReference documentReference = db.collection("PlanList").document(auth.getUid()).collection(weekID).document(date);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    documentReference.update("recipes",FieldValue.arrayUnion(recipes.getId()    ));
                }else {
                    Map<String, Object> data = new HashMap<>();
                    data.put("recipes", tenRecipe);
                    documentReference.set(data);
                }
            }
        });


        Toast.makeText(binding.getRoot().getContext(),"Đang thêm món ăn",Toast.LENGTH_SHORT).show();

        Intent turnBack = new Intent();
        int weekOfYear = extras.getInt("weekOfYear");
        turnBack.putExtra("weekOfYear",weekOfYear);
        turnBack.putExtra("id",recipes.getId());
        turnBack.putExtra("name",recipes.getName());
        turnBack.putExtra("img",recipes.getImage());
        turnBack.putExtra("date",date);
        setResult(123,turnBack);
//        setResult(123);
        finish();

    }


}