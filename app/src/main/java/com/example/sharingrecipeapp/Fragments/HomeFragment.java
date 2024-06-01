package com.example.sharingrecipeapp.Fragments;


import static android.content.Intent.getIntent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.sharingrecipeapp.Activities.BottomNavigationCustomActivity;
import com.example.sharingrecipeapp.Activities.FoodDetailActivity;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.ViewPagerImagerAvtAdapter;
import com.example.sharingrecipeapp.Adapters.Home.AdapterUser;
import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Adapters.Home.ThemeAdapter;
import com.example.sharingrecipeapp.Adapters.Home.iClickOnItemTheme;
import com.example.sharingrecipeapp.Classes.Method;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.Adapters.Home.RecipesAdapter;
import com.example.sharingrecipeapp.Adapters.Home.RecipesRandomAdapter;
import com.example.sharingrecipeapp.Classes.Theme;
import com.example.sharingrecipeapp.Classes.User;
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

    TextView btnAllRecipe;

    ActivityResultLauncher<Intent> activityResultLauncher =  registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
        if (result.getResultCode() == 111){
            for (int i = 0; i < listRecipes.size(); i++){
                if (result.getData().getExtras().get("id").toString().equals(listRecipes.get(i).getId())){
                    listRecipes.get(i).setSave(String.valueOf(result.getData().getExtras().getString("save")));
                }
            }
            recipesRandomAdapter.notifyDataSetChanged();


//            for (int i = 0; i < listRecipesRate.size(); i++){
//                if (result.getData().getExtras().get("id").toString().equals(listRecipesRate.get(i).getId())){
//                    listRecipesRate.get(i).setSave(String.valueOf(result.getData().getExtras().getString("save")));
//                }
//            }
//            recipesAdapter.notifyDataSetChanged();
            setdataRecycRate();
        }
});

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
        btnAllRecipe = view.findViewById(R.id.txtXemThem);


        //bottomNavigationCustomActivity.reload();
        setdataRecycRate();
        setdataRecycRandom();
        setdataRecycTheme();


        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomNavigationCustomActivity.gotoAddRecipe();
            }
        });

        btnAllRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoAllRecipes();
            }
        });
        return view;
    }

    String username;
    private void setdataRecycRate() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewRate.setLayoutManager(linearLayoutManager);

        recipesAdapter = new RecipesAdapter();
        listRecipesRate = new ArrayList<>();
        recipesAdapter.setData(listRecipesRate, new IClickOnItemRecipe() {
            @Override
            public void onClickItemRecipe(Recipes recipes) {
                onClickGoToDetailFood(recipes);
            }
        });
        recyclerViewRate.setAdapter(recipesAdapter);
        firebaseFirestore.collection("Recipes").limit(10)
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
                                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots)
                                            {
                                                if(doc.get("idUsers") != null)
                                                {
                                                    idUser = (ArrayList<String>) doc.get("idUsers");
                                                }
                                                String image = documentSnapshot.getString("image");
                                                String name = documentSnapshot.getString("name");
                                                String save = String.valueOf(idUser.size());
                                                String time = documentSnapshot.getString("timecook");

                                                if (idUser.size()>3) {
                                                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot doc) {
                                                            username = doc.getString("username");
                                                            listRecipesRate.add(new Recipes(id, image, name, save, time, username));
                                                            recipesAdapter.notifyDataSetChanged();
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });

//                            firebaseFirestore.collection("SaveRecipes").whereEqualTo("Recipes",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                                    ArrayList<String> idUser = new ArrayList<>();
//                                    for (QueryDocumentSnapshot doc :value)
//                                    {
//                                        if(doc.get("idUsers") != null)
//                                        {
//                                            idUser = (ArrayList<String>) doc.get("idUsers");
//                                        }
//                                        String image = documentSnapshot.getString("image");
//                                        String name = documentSnapshot.getString("name");
//                                        String save = String.valueOf(idUser.size());
//                                        String time = documentSnapshot.getString("timecook");
//
//                                        if (idUser.size()>3) {
//                                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                                @Override
//                                                public void onSuccess(DocumentSnapshot doc) {
//                                                    username = doc.getString("username");
//                                                    listRecipesRate.add(new Recipes(id, image, name, save, time, username));
//                                                    recipesAdapter.notifyDataSetChanged();
//                                                }
//                                            });
//                                        }
//                                    }
//                                }
//                            });
                        }

                    }
                });
    }

    private void onClickGoToDetailFood(Recipes recipes) {
        Intent intent = new Intent(getContext(), FoodDetailActivity.class);
        intent.putExtra("id", recipes.getId());
        activityResultLauncher.launch(intent);

        //bottomNavigationCustomActivity.gotoFoodDetail(recipes);
    }


    private void setdataRecycRandom() {


        recyclerViewRandom.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));

        recipesRandomAdapter = new RecipesRandomAdapter();
        listRecipes = new ArrayList<>();
        recipesRandomAdapter.setData(listRecipes, new IClickOnItemRecipe() {
            @Override
            public void onClickItemRecipe(Recipes recipes) {
                onClickGoToDetailFood(recipes);
            }
        });
        recyclerViewRandom.setAdapter(recipesRandomAdapter);
        firebaseFirestore.collection("Recipes").limit(10)
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

                                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot doc) {
                                                username = doc.getString("username");
                                                listRecipes.add(new Recipes(id, image, name, save,time, username));
                                                recipesRandomAdapter.notifyDataSetChanged();
                                            }
                                        });
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
