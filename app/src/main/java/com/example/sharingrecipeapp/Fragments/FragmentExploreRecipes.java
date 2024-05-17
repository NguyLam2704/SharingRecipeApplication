package com.example.sharingrecipeapp.Fragments;

import static com.example.sharingrecipeapp.Fragments.ExploreFragment.unAccent;

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
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentExploreRecipes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentExploreRecipes extends Fragment {


    private FragmentExploreBinding binding;
    private BottomNavigationCustomActivity bottomNavigationCustomActivity;
    TextView txtRecipes;
    RecipesAdapter Explore_recipesAdapter;
    SearchView Explore_searchview_recipes;
    ProgressBar Explore_progressbar;
    LinearLayout Explore_linear;
    ResultExploreAdapter Explore_adapter;
    private RecyclerView Explore_recyclerViewRandom;
    private List<Recipes> Explore_listRecipes;
    List<Recipes> Explore_listRecipes_suggest; // danh sach goi y
    private FirebaseAuth Explore_firebaseAuth;
    private FirebaseFirestore Explore_db;
    List<String> List_ingre_db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentExploreRecipes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentExploreRecipes.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentExploreRecipes newInstance(String param1, String param2) {
        FragmentExploreRecipes fragment = new FragmentExploreRecipes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_explore_recipes, container, false);
        bottomNavigationCustomActivity = (BottomNavigationCustomActivity) getActivity();
        txtRecipes= (TextView) view.findViewById(R.id.txt_explore_recipes);
        txtRecipes.setText("Một số món gợi ý");
        Explore_searchview_recipes = (SearchView) view.findViewById(R.id.explore_searchbar_recipes);
        Explore_searchview_recipes.clearFocus();
        Explore_searchview_recipes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Explore_searchName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Explore_searchName(newText);
//                Explore_searchIngre(newText);
                return true;
            }
        });
        Explore_recyclerViewRandom = (RecyclerView) view.findViewById(R.id.explore_recycler_recipes);
        Explore_firebaseAuth = FirebaseAuth.getInstance();
        Explore_db = FirebaseFirestore.getInstance();
        setdataRecycRandom();



        return view;
    }
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

                    Explore_listRecipes_suggest = new ArrayList<>();// tim lai danh sach, dieu kien có luot save lon
                    Explore_db.collection("Recipes")
                            //.whereGreaterThanOrEqualTo("Save",2)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if (error != null) {
                                                Log.w("Error", "listen:error", error);
                                            }
                                            //lấy dữ liệu từ firebase
                                            for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                                                String id = documentSnapshot.getString("id");
                                                String image = documentSnapshot.getString("image");
                                                String name = documentSnapshot.getString("name");
                                                String save = String.valueOf(documentSnapshot.get("save"));
                                                String time = documentSnapshot.getString("timecook");

                                                Explore_listRecipes_suggest.add(new Recipes(id, image, name, save, time));
                                            }
                                        }
                                    });
                    Explore_adapter.setData(Explore_listRecipes_suggest,new IClickOnItemRecipe() {
                        @Override
                        public void onClickItemRecipe(Recipes recipes) {
                            onClickGoToDetailFood(recipes);
                        }
                    });
                    txtRecipes.setText("Món ăn bạn tìm đang được cập nhật\nMột số món gợi ý");
                    Explore_recyclerViewRandom.setAdapter(Explore_adapter);

                }
                else{
                    //tạm
                    txtRecipes.setText("Có "+ResultSearchList.size()+" món ăn theo yêu cầu");
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