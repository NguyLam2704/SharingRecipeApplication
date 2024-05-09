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
    List<String> ingres;


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
        Explore_searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        Explore_recyclerViewRandom = (RecyclerView) view.findViewById(R.id.explore_recycler);
        Explore_firebaseAuth = FirebaseAuth.getInstance();
        Explore_db = FirebaseFirestore.getInstance();
        setdataRecycRandom();
        return view;

    }


//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//
//        MenuItem item = menu.findItem(R.id.explore_searchbar);
//        SearchView searchView = (SearchView)MenuItemCompat.getActionView(item);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchData(s);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == R.id.act)
//        return super.onOptionsItemSelected(item);
//    }

    private void filterList(String newtext)
    {
        List<Recipes> filteredList = new ArrayList<>();
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
                    if(unAccent(recipes.getName()).toLowerCase().contains(unAccent(newtext.toLowerCase())))
                    {
                        filteredList.add(recipes);
                    }
                }
                if(filteredList.isEmpty())
                {
                    Explore_adapter.setData(filteredList,new IClickOnItemRecipe() {
                        @Override
                        public void onClickItemRecipe(Recipes recipes) {
                            onClickGoToDetailFood(recipes);
                        }
                    });
                    Explore_recyclerViewRandom.setAdapter(Explore_adapter);
                }
                else{
                    //tạm

                    Explore_adapter.setData(filteredList,new IClickOnItemRecipe() {
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

    //chuyển có dấu thành không dấu
    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        if (s.equals("Đ") || s.equals("đ"))
        {
            return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "");
        }
        return pattern.matcher(temp).replaceAll("");

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