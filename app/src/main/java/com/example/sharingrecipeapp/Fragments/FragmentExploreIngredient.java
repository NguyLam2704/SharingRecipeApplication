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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class FragmentExploreIngredient extends Fragment {
    private FragmentExploreBinding binding;
    private BottomNavigationCustomActivity bottomNavigationCustomActivity;
    RecipesAdapter Explore_recipesAdapter;
    SearchView Explore_searchview_ingredients;
    ProgressBar Explore_progressbar;
    LinearLayout Explore_linear_ingredients;
    List<Recipes> Explore_listRecipes_suggest; // goi ý
    TextView txtIngredients;
    ResultExploreAdapter Explore_adapter;
    private RecyclerView Explore_recyclerViewRandom;
    private List<Recipes> Explore_listRecipes;
    private FirebaseAuth Explore_firebaseAuth;
    private FirebaseFirestore Explore_db;
    List<String> List_ingre_db;


    public FragmentExploreIngredient() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_explore_ingredient, container, false);
        bottomNavigationCustomActivity = (BottomNavigationCustomActivity) getActivity();
        Explore_searchview_ingredients = (SearchView) view.findViewById(R.id.explore_searchbar_ingredients);
        txtIngredients= (TextView) view.findViewById(R.id.txt_explore_ingredient);
        txtIngredients.setText("Một số món gợi ý");
        Explore_searchview_ingredients.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Explore_searchName(query);
                Explore_searchIngre(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Explore_searchName(newText);
                Explore_searchIngre(newText);
                return true;
            }
        });

        Explore_recyclerViewRandom = (RecyclerView) view.findViewById(R.id.explore_recycler_ingredient);
        Explore_firebaseAuth = FirebaseAuth.getInstance();
        Explore_db = FirebaseFirestore.getInstance();
        setdataRecycRandom();


        return view;
    }


    private void Explore_searchIngre(String newtext)
    {
        List<Recipes> ResultSearchList = new ArrayList<>();
        Explore_listRecipes_suggest=new ArrayList<>();

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
                    String time = documentSnapshot.getString("timecook");

                    List<String> nguyenlieu = (List<String>) documentSnapshot.get("NguyenLieu");

//
//                    for (String ingres_item : nguyenlieu)
//                    {
//                        if(unAccent(ingres_item.replace(" ","")).toLowerCase().contains(unAccent(newtext.toLowerCase().replace(" ",""))))
//                        {
//                            ResultSearchList.add(new Recipes(id, image, name, save, time));
//                            break;
//                        }
//                    }


                    Explore_db.collection("SaveRecipes").whereEqualTo("Recipes",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            ArrayList<String> idUser = new ArrayList<>();

                            for (QueryDocumentSnapshot doc :value) {
                                if (doc.get("idUsers") != null) {
                                    idUser = (ArrayList<String>) doc.get("idUsers");
                                }
                                String save = String.valueOf(idUser.size());
                                Recipes Newrcp = new Recipes(id, image, name, save, time);
                                Explore_listRecipes.add(Newrcp);
                                if (idUser.size()>3){
                                    Explore_listRecipes_suggest.add(Newrcp);
                                }

//                                if(unAccent(Newrcp.getName().replace(" ","")).toLowerCase().contains(unAccent(newtext.toLowerCase().replace(" ",""))))
//                                {
//                                    ResultSearchList.add(Newrcp);
//                                }

                              for (String ingres_item : nguyenlieu)
                              {
                                  if(unAccent(ingres_item.replace(" ","")).toLowerCase().contains(unAccent(newtext.toLowerCase().replace(" ",""))))
                                    {
                                        ResultSearchList.add(new Recipes(id, image, name, save, time));
                                        break;
                                    }
                              }


                            }

                            if(!ResultSearchList.isEmpty()) {

                                if(newtext.equals(""))
                                {
                                    txtIngredients.setText("Một số món gợi ý");
                                    Explore_adapter.setData(Explore_listRecipes,new IClickOnItemRecipe() {
                                        @Override
                                        public void onClickItemRecipe(Recipes recipes) {
                                            onClickGoToDetailFood(recipes);
                                        }
                                    });
                                }
                                else {
                                    txtIngredients.setText("Có " + ResultSearchList.size() + " kết quả phù hợp");
                                    Explore_adapter.setData(ResultSearchList,new IClickOnItemRecipe() {
                                        @Override
                                        public void onClickItemRecipe(Recipes recipes) {
                                            onClickGoToDetailFood(recipes);
                                        }
                                    });

                                }
                                Explore_recyclerViewRandom.setAdapter(Explore_adapter);

                            }
                            else{

                                txtIngredients.setText("Không có kết quả phù hợp\nMột số món được yêu thích");
                                Explore_adapter.setData(Explore_listRecipes_suggest,new IClickOnItemRecipe() {
                                    @Override
                                    public void onClickItemRecipe(Recipes recipes) {
                                        onClickGoToDetailFood(recipes);
                                    }
                                });

                                Explore_recyclerViewRandom.setAdapter(Explore_adapter);

                            }



//                            RecipesAdapter myAdapter = new RecipesAdapter();
//                            myAdapter.setData(ResultSearchList,new IClickOnItemRecipe() {
//                                @Override
//                                public void onClickItemRecipe(Recipes recipes) {
//                                    onClickGoToDetailFood(recipes);
//                                }
//                            });
//                            Explore_recyclerViewRandom.setAdapter(myAdapter);
                        }
                    });
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
                    String time = documentSnapshot.getString("timecook");


                    Explore_db.collection("SaveRecipes").whereEqualTo("Recipes",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            ArrayList<String> idUser = new ArrayList<>();

                            for (QueryDocumentSnapshot doc :value) {
                                if (doc.get("idUsers") != null) {
                                    idUser = (ArrayList<String>) doc.get("idUsers");
                                }
                                String save = String.valueOf(idUser.size());
                                Recipes Newrcp = new Recipes(id, image, name, save, time);
                                Explore_listRecipes.add(Newrcp);
                                Explore_adapter.setData(Explore_listRecipes, new IClickOnItemRecipe() {
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

            }
        });

    }
    private void onClickGoToDetailFood(Recipes recipes) {
        bottomNavigationCustomActivity.gotoFoodDetail(recipes);
    }

}