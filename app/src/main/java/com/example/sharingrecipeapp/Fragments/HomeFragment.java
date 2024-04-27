package com.example.sharingrecipeapp.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sharingrecipeapp.Activities.BottomNavigationCustomActivity;
import com.example.sharingrecipeapp.Adapters.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Adapters.ThemeAdapter;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.Adapters.RecipesAdapter;
import com.example.sharingrecipeapp.Adapters.RecipesRandomAdapter;
import com.example.sharingrecipeapp.Classes.Theme;
import com.example.sharingrecipeapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ViewPager2 mViewPager;

    private  BottomNavigationCustomActivity bottomNavigationCustomActivity;
    RecipesAdapter recipesAdapter;

    RecipesRandomAdapter recipesRandomAdapter;

    ThemeAdapter themeAdapter;

    private RecyclerView recyclerViewRate, recyclerViewRandom, recyclerViewTheme;

    private List<Recipes> listRecipes;

    private List<Theme> lisTheme;
    ImageView img_food;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home,container,false);
        bottomNavigationCustomActivity = (BottomNavigationCustomActivity) getActivity();


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerViewRate = (RecyclerView) view.findViewById(R.id.recyRate);
        recyclerViewRandom = (RecyclerView) view.findViewById(R.id.recyRanDom);
        recyclerViewTheme = (RecyclerView) view.findViewById(R.id.recyTheme);



        setdataRecycRate();
        setdataRecycRandom();
        setdataRecycTheme();


        return view;
    }


    private void setdataRecycRate() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewRate.setLayoutManager(linearLayoutManager);

        recipesAdapter = new RecipesAdapter();
        firebaseFirestore.collection("Recipes")
                .whereGreaterThan("like", 100)
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
                            String image = documentSnapshot.getString("image");
                            String name = documentSnapshot.getString("name");
                            String save = String.valueOf(documentSnapshot.get("save"));
                            String time = documentSnapshot.getString("timecook");
                            listRecipes.add(new Recipes(id, image, name, save, time));
                        }
                        recipesAdapter.setData(listRecipes, new IClickOnItemRecipe() {
                            @Override
                            public void onClickItemRecipe(Recipes recipes) {
                                onClickGoToDetailFood(recipes);
                            }
                        });
                        recyclerViewRate.setAdapter(recipesAdapter);
                    }
                });
    }

    private void onClickGoToDetailFood(Recipes recipes) {
        bottomNavigationCustomActivity.gotoFoodDetail(recipes);
    }


    private void setdataRecycRandom() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewRandom.setLayoutManager(linearLayoutManager);

        recipesRandomAdapter = new RecipesRandomAdapter();
        firebaseFirestore.collection("Recipes")
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
                            String image = documentSnapshot.getString("image");
                            String name = documentSnapshot.getString("name");
                            String save = String.valueOf(documentSnapshot.get("save"));
                            String time = documentSnapshot.getString("timecook");
                            listRecipes.add(new Recipes(id, image, name, save, time));
                        }
                        recipesRandomAdapter.setData(listRecipes, new IClickOnItemRecipe() {
                                    @Override
                                    public void onClickItemRecipe(Recipes recipes) {
                                        onClickGoToDetailFood(recipes);
                                    }
                                });
                        recyclerViewRandom.setAdapter(recipesRandomAdapter);
                    }
                });
    }

    private void setdataRecycTheme() {

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false);
            recyclerViewTheme.setLayoutManager(linearLayoutManager);

           FirestoreRecyclerOptions<Theme> options = new FirestoreRecyclerOptions.Builder<Theme>()
                   .setQuery(firebaseFirestore.collection("Theme"),Theme.class)
                   .build();
           themeAdapter = new ThemeAdapter(options);
           recyclerViewTheme.setAdapter(themeAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        themeAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        themeAdapter.stopListening();
    }

}


