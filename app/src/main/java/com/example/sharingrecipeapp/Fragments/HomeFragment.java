package com.example.sharingrecipeapp.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.sharingrecipeapp.Activities.FoodDetailActivity;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.ViewPagerImagerAvtAdapter;
import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Adapters.Home.ThemeAdapter;
import com.example.sharingrecipeapp.Adapters.Home.iClickOnItemTheme;
import com.example.sharingrecipeapp.Classes.Method;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.Adapters.Home.RecipesAdapter;
import com.example.sharingrecipeapp.Adapters.Home.RecipesRandomAdapter;
import com.example.sharingrecipeapp.Classes.Theme;
import com.example.sharingrecipeapp.R;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.github.muddz.styleabletoast.StyleableToast;

public class HomeFragment extends Fragment {
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ViewPager2 mViewPager;



    private  BottomNavigationCustomActivity bottomNavigationCustomActivity;
    RecipesAdapter recipesAdapter;

    RecipesRandomAdapter recipesRandomAdapter;

    ThemeAdapter themeAdapter;


    private RecyclerView recyclerViewRate, recyclerViewRandom, recyclerViewTheme;

    private List<Recipes> listRecipes;

    private List<Recipes> listRecipesRate;

    private List<Theme> listTheme;
    ImageView img_food, btn_create;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    ProgressDialog progressDialog;

    private List<Method> mMethodList;

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
        btn_create = view.findViewById(R.id.imageButton);


        setdataRecycRate();
        setdataRecycRandom();
        setdataRecycTheme();


        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoAddRecipe();
            }
        });

        return view;
    }


    private void setdataRecycRate() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewRate.setLayoutManager(linearLayoutManager);

        recipesAdapter = new RecipesAdapter();
        firebaseFirestore.collection("Recipes")
                .whereEqualTo("trending",true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Error", "listen:error", error);
                            return;
                        }
                        listRecipesRate = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                            String id = documentSnapshot.getString("id");

                            firebaseFirestore.collection("SaveRecipes").whereEqualTo("Recipes",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                    ArrayList<String> idUser = new ArrayList<>();
                                    for (QueryDocumentSnapshot doc :value)
                                    {
                                        if(doc.get("idUsers") != null)
                                        {
                                            idUser = (ArrayList<String>) doc.get("idUsers");
                                        }
                                        String image = documentSnapshot.getString("image");
                                        String name = documentSnapshot.getString("name");
                                        String save = String.valueOf(idUser.size());
                                        String time = documentSnapshot.getString("timecook");
                                        listRecipesRate.add(new Recipes(id, image, name, save, time));
                                        recipesAdapter.setData(listRecipesRate, new IClickOnItemRecipe() {
                                            @Override
                                            public void onClickItemRecipe(Recipes recipes) {
                                                onClickGoToDetailFood(recipes);
                                            }
                                        });
                                        recyclerViewRate.setAdapter(recipesAdapter);
                                    }
                                }
                            });

                        }

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
        firebaseFirestore.collection("Recipes").limit(10)
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

                            firebaseFirestore.collection("SaveRecipes").whereEqualTo("Recipes",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                    ArrayList<String> idUser = new ArrayList<>();
                                    for (QueryDocumentSnapshot doc :value)
                                    {

                                                                    idUser = (ArrayList<String>) doc.get("idUsers");
                                                                    String save = String.valueOf(idUser.size());
                                                                    String image = documentSnapshot.getString("image");
                                                                    String name = documentSnapshot.getString("name");
                                                                    String time = documentSnapshot.getString("timecook");
                                                                    listRecipes.add(new Recipes(id, image, name, save,time, "Username"));
                                                                    recipesRandomAdapter.setData(listRecipes, new IClickOnItemRecipe() {
                                                                        @Override
                                                                        public void onClickItemRecipe(Recipes recipes) {
                                                                            onClickGoToDetailFood(recipes);
                                                                        }
                                                                    });
                                                                    recyclerViewRandom.setAdapter(recipesRandomAdapter);

                                            }
                                        }
                                    });
                        }

                    }
                });
    }

    private void setdataRecycTheme() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewTheme.setLayoutManager(linearLayoutManager);

        themeAdapter = new ThemeAdapter();
        firebaseFirestore.collection("Theme")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Error", "listen:error", error);
                            return;
                        }
                        listTheme = new ArrayList<>();
                        for (DocumentSnapshot snapshot : value.getDocuments()){
                            String id = snapshot.getString("id");
                            String name = snapshot.getString("name");
                            String image = snapshot.getString("image");
                            listTheme.add(new Theme(id, name, image));
                        }
                        themeAdapter.setData(listTheme, new iClickOnItemTheme() {
                            @Override
                            public void onClickItemTheme(Theme theme) {
                                onClickGoToDetailTheme(theme);
                            }
                        });
                        recyclerViewTheme.setAdapter(themeAdapter);
                    }
                });

    }

    private void onClickGoToDetailTheme(Theme theme) {
        bottomNavigationCustomActivity.gotoThemeDetail(theme);
    }

    private List<String> getUser(String id){
        List<String> username = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Recipes").document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentReference doc = (DocumentReference) task.getResult().get("Users");
                            doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot snapshot) {
                                    username.add(new String(snapshot.getString("username")));
                                }
                            });
                        }
                    }
                });
        return username;
    }




}
