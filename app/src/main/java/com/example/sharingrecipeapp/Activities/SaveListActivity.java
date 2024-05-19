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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SaveListActivity extends AppCompatActivity {

    ActivitySaveListBinding binding;

    RecyclerView recy_ViewRecipes;
    FirebaseAuth auth;
    FirebaseFirestore save_db;

    TextView soluong;

    ArrayList<Recipes> recipesList;
    ArrayList<Recipes> result_search;
    Integer number = 0;
    ImageButton back_btn;
    SearchView save_searchbar;
    List <Recipes> ResultSearchList;

    ProgressBar Prgrss_save;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_list);

        binding = ActivitySaveListBinding.inflate(getLayoutInflater());

        Prgrss_save = findViewById(R.id.prgrss_save);
        recy_ViewRecipes = findViewById(R.id.recy_save);
        save_db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        back_btn = findViewById(R.id.btn_back_profile);
        soluong = findViewById(R.id.textTb);
        save_searchbar = findViewById(R.id.Searchbar);
        save_searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                ResultSearchList.clear();
                save_searchbar.setBackgroundResource(R.drawable.edittext_bound);
                Prgrss_save.setVisibility(View.VISIBLE);
                save_searchName(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

//                save_searchName(newText);
//                ResultSearchList.clear();
                save_searchbar.setBackgroundResource(R.drawable.query_bound);
                if(newText.equals(""))
                {
                    save_searchbar.setBackgroundResource(R.drawable.edittext_bound);
//                    recipesList.clear();
                    displaySavedRecipes();
                }
                return true;
            }
        });

        displaySavedRecipes();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void displaySavedRecipes(){
        save_db.collection("SaveRecipes").whereArrayContains("idUsers",auth.getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        List<String> tenRecipes = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                            tenRecipes.add(documentSnapshot.get("Recipes").toString());
                        }
                        recipesList = new ArrayList<>();
                        for (String i : tenRecipes){
                            save_db.collection("Recipes").document(i).get()
                                    .addOnSuccessListener(documentSnapshot -> {

                                        save_db.collection("SaveRecipes").whereEqualTo("Recipes",i).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                                    soluong.setText("Bạn đã lưu được " + recipesList.size() +" món ăn");
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



//                                         String image = documentSnapshot.getString("image");
//                                         String name = documentSnapshot.getString("name");
//                                         String save = documentSnapshot.get("save").toString();
//                                         String time = documentSnapshot.get("timecook").toString();

//                                         Recipes recipes = new Recipes(i,image,name,save,time);

//                                         recipesList.add(recipes);
//                                         number++;
//                                         soluong.setText("Bạn đã lưu được " + recipesList.size() +" món ăn");
//                                         if (i == tenRecipes.get(tenRecipes.size()-1)){
//                                             RecipesAdapter myAdapter = new RecipesAdapter();
//                                             myAdapter.setData(recipesList, new IClickOnItemRecipe() {
//                                                 @Override
//                                                 public void onClickItemRecipe(Recipes recipes) {
//                                                     onClickGoToDetailFood(recipes);
//                                                 }
//                                             });
//                                             recy_ViewRecipes.setLayoutManager(new GridLayoutManager(binding.getRoot().getContext(), 2));
//                                             recy_ViewRecipes.setAdapter(myAdapter);

//                                         }


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

    private void save_searchName(String newtext)
    {
        ResultSearchList = new ArrayList<>();
        save_db.collection("SaveRecipes").whereArrayContains("idUsers",auth.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("Error", "listen:error", error);
                }

                //lấy dữ liệu từ firebase
                List<String> tenRecipes = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                    tenRecipes.add(documentSnapshot.get("Recipes").toString());
                }

                for (String nameRecipe : tenRecipes){
                    save_db.collection("Recipes").document(nameRecipe).get()
                            .addOnSuccessListener(documentSnapshot -> {
                                String image = documentSnapshot.getString("image");
                                String name = documentSnapshot.getString("name");
                                String time = documentSnapshot.get("timecook").toString();

                                save_db.collection("SaveRecipes").whereEqualTo("Recipes",nameRecipe).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                        ArrayList<String> idUser = new ArrayList<>();
                                        String save;
                                        for (QueryDocumentSnapshot doc :value) {
                                            if (doc.get("idUsers") != null) {
                                                idUser = (ArrayList<String>) doc.get("idUsers");
                                            }
                                                save = String.valueOf(idUser.size());
                                                Recipes Newrcp = new Recipes(nameRecipe, image, name, save, time);
                                                if(unAccent(Newrcp.getName().replace(" ","")).toLowerCase().contains(unAccent(newtext.toLowerCase().replace(" ",""))))
                                                {
                                                    ResultSearchList.add(Newrcp);
                                                }
                                                if(ResultSearchList.isEmpty()) {
                                                    soluong.setText("Không có kết quả phù hợp");
                                                }
                                                else{
                                                    //tạm
                                                    soluong.setText("Có "+ResultSearchList.size()+" kết quả phù hợp");
                                                }
                                                RecipesAdapter myAdapter = new RecipesAdapter();
                                                myAdapter.setData(ResultSearchList,new IClickOnItemRecipe() {
                                                    @Override
                                                    public void onClickItemRecipe(Recipes recipes) {
                                                        onClickGoToDetailFood(recipes);
                                                    }
                                                });
                                                recy_ViewRecipes.setAdapter(myAdapter);
                                        }
                                    }
                                });
//                    Explore_listRecipes_suggest = new ArrayList<>();// tim lai danh sach, dieu kien có luot save lon
//                    save_db.collection("SaveRecipes").whereArrayContains("idUsers",auth.getUid())
//                            //.whereGreaterThanOrEqualTo("Save",2)
//                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                    if (error != null) {
//                                        Log.w("Error", "listen:error", error);
//                                    }
//                                    //lấy dữ liệu từ firebase
//                                    for (DocumentSnapshot documentSnapshot : value.getDocuments()){
//                                        String id = documentSnapshot.getString("id");
//                                        String image = documentSnapshot.getString("image");
//                                        String name = documentSnapshot.getString("name");
//                                        String save = String.valueOf(documentSnapshot.get("save"));
//                                        String time = documentSnapshot.getString("timecook");
//
//                                        Explore_listRecipes_suggest.add(new Recipes(id, image, name, save, time));
//                                    }
//                                }
//                            });
//                    Explore_adapter.setData(Explore_listRecipes_suggest,new IClickOnItemRecipe() {
//                        @Override
//                        public void onClickItemRecipe(Recipes recipes) {
//                            onClickGoToDetailFood(recipes);
//                        }
//                    });
//                    txtIngredients.setText("Món ăn bạn tìm đang được cập nhật\nMột số món gợi ý");
//                    Explore_recyclerViewRandom.setAdapter(Explore_adapter);
                    });

                }
                //search ko co ket qua
            }

        });
        Prgrss_save.setVisibility(View.GONE);
    }

    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        if (s.equals("Đ") || s.equals("đ"))
        {
            return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "d");
        }
        return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D');

    }

}