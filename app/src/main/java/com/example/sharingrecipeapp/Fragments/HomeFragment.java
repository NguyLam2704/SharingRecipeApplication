package com.example.sharingrecipeapp.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sharingrecipeapp.Activities.BottomNavigationCustomActivity;
import com.example.sharingrecipeapp.Adapters.ThemeAdapter;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.Adapters.RecipesAdapter;
import com.example.sharingrecipeapp.Adapters.RecipesRandomAdapter;
import com.example.sharingrecipeapp.Classes.Theme;
import com.example.sharingrecipeapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.List;

public class HomeFragment extends Fragment {

    private  BottomNavigationCustomActivity bottomNavigationCustomActivity;
    RecipesAdapter recipesAdapter;

    RecipesRandomAdapter recipesRandomAdapter;

    ThemeAdapter themeAdapter;

    private RecyclerView recyclerViewRate, recyclerViewRandom, recyclerViewTheme;

    private List<Recipes> listRecipes;
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


//        firebaseAuth = FirebaseAuth.getInstance();
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

        FirestoreRecyclerOptions<Recipes> options = new FirestoreRecyclerOptions.Builder<Recipes>()
                .setQuery(firebaseFirestore.collection("Recipes")
                        .whereGreaterThan("like",100), Recipes.class)
                .build();

        recipesAdapter = new RecipesAdapter(options);
        recyclerViewRate.setAdapter(recipesAdapter);
    }


    private void setdataRecycRandom() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewRandom.setLayoutManager(linearLayoutManager);

        FirestoreRecyclerOptions<Recipes> options = new FirestoreRecyclerOptions.Builder<Recipes>()
                .setQuery(firebaseFirestore.collection("Recipes"),Recipes.class)
                .build();
        recipesRandomAdapter = new RecipesRandomAdapter(options);
        recyclerViewRandom.setAdapter(recipesRandomAdapter);
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
        recipesAdapter.startListening();
        recipesRandomAdapter.startListening();
        themeAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        recipesAdapter.startListening();
        recipesRandomAdapter.stopListening();
        themeAdapter.stopListening();
    }
}


