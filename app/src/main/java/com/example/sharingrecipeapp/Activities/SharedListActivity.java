package com.example.sharingrecipeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Adapters.Home.RecipesAdapter;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.R;
import com.example.sharingrecipeapp.databinding.ActivitySaveListBinding;
import com.example.sharingrecipeapp.databinding.ActivitySharedListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SharedListActivity extends AppCompatActivity {
    ActivitySharedListBinding binding;
    ImageButton back_btn;
    RecyclerView share_rcy;
    FirebaseFirestore share_db;
    FirebaseAuth share_auth;
    FirebaseUser user;
    TextView soluong;
    ArrayList<Recipes> recipesList;
    ArrayList<Recipes> result_search;
    androidx.appcompat.widget.SearchView share_searchbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_list);

        back_btn = findViewById(R.id.btn_back);
        back_btn.setOnClickListener(v -> finish());
        soluong = findViewById(R.id.result_announce);
        share_searchbar = findViewById(R.id.SearchShared);
        share_auth = FirebaseAuth.getInstance();
        share_db = FirebaseFirestore.getInstance();
        user = share_auth.getCurrentUser();
        binding = ActivitySharedListBinding.inflate(getLayoutInflater());

        share_rcy = findViewById(R.id.SharedRecipe);
        displayShareRecipes();


        share_searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                share_searchbar.setBackgroundResource(R.drawable.edittext_bound);
                search_name(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                share_searchbar.setBackgroundResource(R.drawable.query_bound);
                if(newText.equals(""))
                {
                    share_searchbar.setBackgroundResource(R.drawable.edittext_bound);
                    displayShareRecipes();
                }
                return true;
            }
        });
//        if(share_searchbar.isEnabled() && share_searchbar.)
//        {
//            share_searchbar.setBackgroundResource(R.drawable.query_bound);
//        }
//        else {share_searchbar.setBackgroundResource(R.drawable.edittext_bound);}
    }

    String username;
    private void displayShareRecipes(){

        recipesList = new ArrayList<>();
        RecipesAdapter ShareAdapter = new RecipesAdapter();
        ShareAdapter.setData(recipesList, new IClickOnItemRecipe() {
            @Override
            public void onClickItemRecipe(Recipes recipes) {
                onClickGoToDetailFood(recipes);
            }
        });
        share_rcy.setLayoutManager(new GridLayoutManager(binding.getRoot().getContext(), 2));
        share_rcy.setAdapter(ShareAdapter);

        DocumentReference current_user = share_db.collection("Users").document(user.getUid());
        share_db.collection("Recipes")
                .whereEqualTo("Users",current_user)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null)
                        {
                            return;
                        }

                        for (QueryDocumentSnapshot queryDocumentSnapshot : value)
                        {
                            DocumentReference docRef = queryDocumentSnapshot.getDocumentReference("Users");
                            String id = queryDocumentSnapshot.getString("id");


                            share_db.collection("SaveRecipes").whereEqualTo("Recipes", id).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    ArrayList<String> idUser = new ArrayList<>();
                                    for (QueryDocumentSnapshot doc :value){
                                        if(doc.get("idUsers") != null)
                                        {
                                            idUser = (ArrayList<String>) doc.get("idUsers");
                                        }
                                        String save = String.valueOf(idUser.size());
                                        String name = queryDocumentSnapshot.getString("name");
                                        String time = queryDocumentSnapshot.get("timecook").toString();
                                        String image = queryDocumentSnapshot.getString("image");
                                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot snapshot) {
                                                username = snapshot.getString("username");
                                                recipesList.add(new Recipes(id,image,name,save,time,username));
                                                soluong.setText("Bạn đã đăng tải " + recipesList.size() +" món ăn");
                                                ShareAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    }
                                }
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

    private void search_name(String newtext){
        result_search = new ArrayList<>();
        RecipesAdapter myAdapter = new RecipesAdapter();
        myAdapter.setData(result_search,new IClickOnItemRecipe() {
            @Override
            public void onClickItemRecipe(Recipes recipes) {
                onClickGoToDetailFood(recipes);
            }
        });
        share_rcy.setLayoutManager(new GridLayoutManager(binding.getRoot().getContext(), 2));
        share_rcy.setAdapter(myAdapter);

        DocumentReference current_user = share_db.collection("Users").document(user.getUid());
        share_db.collection("Recipes").whereEqualTo("Users",current_user).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                {
                    return;
                }

                for (QueryDocumentSnapshot queryDocumentSnapshot : value)
                {
                    String id = queryDocumentSnapshot.getString("id");
                  DocumentReference docRef = queryDocumentSnapshot.getDocumentReference("Users");
                    share_db.collection("SaveRecipes").whereEqualTo("Recipes",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            ArrayList<String> idUser = new ArrayList<>();

                            for (QueryDocumentSnapshot doc :value) {
                                if (doc.get("idUsers") != null) {
                                    idUser = (ArrayList<String>) doc.get("idUsers");
                                }
                                String save = String.valueOf(idUser.size());
                                String image = queryDocumentSnapshot.getString("image");
                                String name = queryDocumentSnapshot.getString("name");
                                String time = queryDocumentSnapshot.get("timecook").toString();
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot) {
                                        username = snapshot.getString("username");
                                        Recipes Newrcp = new Recipes(id, image, name, save, time, username);
                                        if(unAccent(Newrcp.getName().replace(" ","")).toLowerCase().contains(unAccent(newtext.toLowerCase().replace(" ",""))))
                                        {
                                            result_search.add(Newrcp);
                                            myAdapter.notifyDataSetChanged();
                                        }
                                        if(result_search.isEmpty()) {
                                            soluong.setText("Không có kết quả phù hợp");
                                        }
                                        else{
                                            //tạm
                                            soluong.setText("Có "+result_search.size()+" kết quả phù hợp");
                                        }

                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
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