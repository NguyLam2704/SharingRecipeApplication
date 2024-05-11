package com.example.sharingrecipeapp.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.sharingrecipeapp.Activities.BottomNavigationCustomActivity;
import com.example.sharingrecipeapp.Adapters.Home.RecipesAdapter;


import com.example.sharingrecipeapp.Adapters.Explore.ResultExploreAdapter;
import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Adapters.Home.RecipesAdapter;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.R;
import com.example.sharingrecipeapp.databinding.FragmentExploreBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ExploreFragment extends Fragment {
    private FragmentExploreBinding binding;
    private BottomNavigationCustomActivity bottomNavigationCustomActivity;
    RecipesAdapter Explore_recipesAdapter;
    SearchView Explore_searchview;
    ProgressBar Explore_progressbar;
    LinearLayout Explore_linear;
    ResultExploreAdapter Explore_adapter;
    private RecyclerView Explore_recyclerViewRandom;
    private List<Recipes> Explore_listRecipes;
    private FirebaseAuth Explore_firebaseAuth;
    private FirebaseFirestore Explore_db;
    List<String> List_ingre_db;


//    public ExploreFragment() {
//        // Required empty public constructor
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_explore, container, false);
        bottomNavigationCustomActivity = (BottomNavigationCustomActivity) getActivity();
        Explore_linear = (LinearLayout) view.findViewById(R.id.explore_linearLayout);
        Explore_progressbar = (ProgressBar) view.findViewById(R.id.explore_progressbar);
        Explore_searchview = (SearchView) view.findViewById(R.id.explore_searchbar);
        Explore_searchview.clearFocus();
        //chuc nang search
        Explore_searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Explore_searchName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Explore_searchName(newText);
//                Explore_searchIngre(newText);
                return true;
            }
        });
        Explore_recyclerViewRandom = (RecyclerView) view.findViewById(R.id.explore_recycler);
        Explore_firebaseAuth = FirebaseAuth.getInstance();
        Explore_db = FirebaseFirestore.getInstance();
        setdataRecycRandom();
        return view;

    }
//tim kiem cong thuc
    private void Explore_searchName(String newtext)
    {
        List<Recipes> ResultSearchList = new ArrayList<>();
        Explore_db.collection("Recipes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("Error", "listen:error", error);
                }
                Explore_listRecipes = new ArrayList<>();
                //lấy dữ liệu từ firebase
                for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                    String id = documentSnapshot.getString("id");
                    String image = documentSnapshot.getString("image");
                    String name = documentSnapshot.getString("name");
                    String save = String.valueOf(documentSnapshot.get("save"));
                    String time = documentSnapshot.getString("timecook");
//                    get list nguyen lieu
//                    ingres = (List<String>) documentSnapshot.get("NguyenLieu");
                    Explore_listRecipes.add(new Recipes(id, image, name, save, time));
                }
                for (Recipes recipes : Explore_listRecipes)
                {
                    if(unAccent(recipes.getName().replace(" ","")).toLowerCase().contains(unAccent(newtext.toLowerCase().replace(" ",""))))
                    {
                        ResultSearchList.add(recipes);
                    }
                }
                //search ko co ket qua
                if(ResultSearchList.isEmpty())
                {
                    Explore_adapter.setData(ResultSearchList,new IClickOnItemRecipe() {
                        @Override
                        public void onClickItemRecipe(Recipes recipes) {
                            onClickGoToDetailFood(recipes);
                        }
                    });
                    Explore_recyclerViewRandom.setAdapter(Explore_adapter);
                }
                else{
                    //tạm

                    Explore_adapter.setData(ResultSearchList,new IClickOnItemRecipe() {
                    @Override
                    public void onClickItemRecipe(Recipes recipes) {
                        onClickGoToDetailFood(recipes);
                    }
                });
                    Explore_recyclerViewRandom.setAdapter(Explore_adapter);
                }

            }
        });
    }
//tim kiem nguyen lieu
    private void Explore_searchIngre(String newtext)
    {
        List<Recipes> ResultSearchList = new ArrayList<>();

        Explore_db.collection("Recipes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("Error", "listen:error", error);
                }
                List <String> nguyenlieu = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                    String id = documentSnapshot.getString("id");
                    String image = documentSnapshot.getString("image");
                    String name = documentSnapshot.getString("name");
                    String save = String.valueOf(documentSnapshot.get("save"));
                    String time = documentSnapshot.getString("timecook");
                    nguyenlieu = (List<String>)documentSnapshot.get("NguyenLieu");
                    for (String ingres_item : nguyenlieu)
                    {
                        if(unAccent(ingres_item.replace(" ","")).toLowerCase().contains(unAccent(newtext.toLowerCase().replace(" ",""))))
                        {
                                ResultSearchList.add(new Recipes(id, image, name, save, time));
                                break;
                        }
                    }
                    //search ko co ket qua
                    if(ResultSearchList.isEmpty())
                    {
                        Explore_adapter.setData(ResultSearchList,new IClickOnItemRecipe() {
                            @Override
                            public void onClickItemRecipe(Recipes recipes) {
                                onClickGoToDetailFood(recipes);
                            }
                        });
                        Explore_recyclerViewRandom.setAdapter(Explore_adapter);
                    }
                    else {
                        Explore_adapter.setData(ResultSearchList,new IClickOnItemRecipe() {
                            @Override
                            public void onClickItemRecipe(Recipes recipes) {
                                onClickGoToDetailFood(recipes);
                            }
                        });
                        Explore_recyclerViewRandom.setAdapter(Explore_adapter);
                    }
                }
            }
        });
    }

    //chuyển có dấu thành không dấu
    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        if (s.equals("Đ") || s.equals("đ"))
        {
            return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "d");
        }
        return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D');

    }

    private void setdataRecycRandom()
    {
        GridLayoutManager Explore_gridlayoutMng = new GridLayoutManager(getContext(),2);
        Explore_recyclerViewRandom.setLayoutManager(Explore_gridlayoutMng);
        Explore_adapter = new ResultExploreAdapter();
        Explore_db.collection("Recipes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Error", "listen:error", error);
                            return;
                        }
                        Explore_listRecipes = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                            String id = documentSnapshot.getString("id");
                            String image = documentSnapshot.getString("image");
                            String name = documentSnapshot.getString("name");
                            String save = String.valueOf(documentSnapshot.get("save"));
                            String time = documentSnapshot.getString("timecook");
                            Explore_listRecipes.add(new Recipes(id, image, name, save, time));
                        }

                        Explore_adapter.setData(Explore_listRecipes, new IClickOnItemRecipe() {
                            @Override
                            public void onClickItemRecipe(Recipes recipes) {
                                onClickGoToDetailFood(recipes);
                            }
                        });
                        Explore_recyclerViewRandom.setAdapter(Explore_adapter);
                    }
                });
    }
    private void onClickGoToDetailFood(Recipes recipes) {
        bottomNavigationCustomActivity.gotoFoodDetail(recipes);
    }
}