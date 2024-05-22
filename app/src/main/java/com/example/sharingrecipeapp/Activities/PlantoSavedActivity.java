package com.example.sharingrecipeapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import io.github.muddz.styleabletoast.StyleableToast;

public class PlantoSavedActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    BottomNavigationCustomActivity bottomNavigationCustomActivity;
    ActivityPlantoSavedBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore db;
    TextView soluong;
    Integer number = 0;
    List <Recipes> ResultSearchList;

    List<Recipes> recipesList;

    ProgressBar Prgrss_save;

    SearchView save_searchbar;
    ImageButton back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_list);
        binding = ActivityPlantoSavedBinding.inflate(getLayoutInflater());



        recyclerView = findViewById(R.id.recy_save);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        back_btn = findViewById(R.id.btn_back_profile);
        soluong = findViewById(R.id.textTb);
        save_searchbar = findViewById(R.id.Searchbar);
        Prgrss_save = findViewById(R.id.prgrss_save);

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
                                                        recyclerView.setLayoutManager(new GridLayoutManager(binding.getRoot().getContext(), 2));
                                                        recyclerView.setAdapter(myAdapter);
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
        StyleableToast.makeText(binding.getRoot().getContext(), "Thêm món " + recipes.getName()+" thành công", R.style.mytoast).show();
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


    private void save_searchName(String newtext)
    {
        ResultSearchList = new ArrayList<>();
        db.collection("SaveRecipes").whereArrayContains("idUsers",auth.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    db.collection("Recipes").document(nameRecipe).get()
                            .addOnSuccessListener(documentSnapshot -> {
                                String image = documentSnapshot.getString("image");
                                String name = documentSnapshot.getString("name");
                                String time = documentSnapshot.get("timecook").toString();

                                db.collection("SaveRecipes").whereEqualTo("Recipes",nameRecipe).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                            recyclerView.setAdapter(myAdapter);
                                        }
                                    }
                                });
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