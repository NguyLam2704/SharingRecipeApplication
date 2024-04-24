package com.example.sharingrecipeapp.Fragments;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sharingrecipeapp.Activities.BottomNavigationCustomActivity;
import com.example.sharingrecipeapp.Adapters.Recipes;
import com.example.sharingrecipeapp.Adapters.RecipesAdapter;
import com.example.sharingrecipeapp.R;
import com.example.sharingrecipeapp.RequestManager;
import com.example.sharingrecipeapp.databinding.FragmentHomeBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Query;
import retrofit2.http.Tag;

public class HomeFragment extends Fragment {

    private  BottomNavigationCustomActivity bottomNavigationCustomActivity;
    RecipesAdapter recipesAdapter;

    RecyclerView recyclerViewRate;
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
//        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerViewRate = (RecyclerView) view.findViewById(R.id.recyRate);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewRate.setLayoutManager(linearLayoutManager);

        setdataRecycRate();


        return view;
    }

    private void setdataRecycRate() {
        FirestoreRecyclerOptions<Recipes> options = new FirestoreRecyclerOptions.Builder<Recipes>()
                .setQuery(FirebaseFirestore.getInstance().collection("Recipes"), Recipes.class)
                .build();

        recipesAdapter = new RecipesAdapter(options);
        recyclerViewRate.setAdapter(recipesAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        recipesAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        recipesAdapter.stopListening();
    }


//    private void setDataRecycRate() {
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false);
//        recyclerViewRate.setLayoutManager(linearLayoutManager);
//        recipesAdapter = new RecipesAdapter();
//
//        DocumentReference docRef = firebaseFirestore.collection("Recipes").document("TrungChien");
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()){
//                    DocumentSnapshot document = task.getResult();
//                    listRecipes = new ArrayList<>();
//                    if (document.exists()){
//                            String id = document.getString("id");
//                            String image = document.getString("image");
//                            String name = document.getString("name");
//                            String save = document.getString("save");
//                            String timecook = document.getString("timecook");
//                            listRecipes.add(new Recipes(id, image, name, save, timecook));
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with", task.getException());
//                }
//                recyclerViewRate.setAdapter(recipesAdapter);
//            }
//        });

//        DocumentReference doc = firebaseFirestore.collection("Recipes").document("TrungChien");
//        doc.get()
//                .addOnSuccessListener(documentSnapshot -> {
//                            listRecipes = new ArrayList<>();
//                            String id = documentSnapshot.getString("id");
//                            String image = documentSnapshot.getString("image");
//                            String name = documentSnapshot.getString("name");
//                            String save = documentSnapshot.getString("save");
//                            String timecook = documentSnapshot.getString("timecook");
//                            listRecipes.add(new Recipes(id, image, name, save, timecook));
//                            recyclerViewRate.setAdapter(recipesAdapter);
//                });




            //    .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            Log.w("Error", "listen:error", error);
//                            return;
//                        }
//                        listRecipes = new ArrayList<>();
//                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
//                            String id = documentSnapshot.getString("id");
//                            String image = documentSnapshot.getString("image");
//                            String name = documentSnapshot.getString("name");
//                            String save = documentSnapshot.getString("save");
//                            String timecook = documentSnapshot.getString("timecook");
//                            listRecipes.add(new Recipes(id, image, name, save, timecook));
//                        }
//                        recyclerViewRate.setAdapter(recipesAdapter);
//                    }
//                });
}

