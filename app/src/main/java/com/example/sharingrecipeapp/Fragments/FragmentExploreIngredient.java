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
<<<<<<< HEAD
        Explore_listRecipes_suggest=new ArrayList<>();

=======
>>>>>>> 8f713cba9408e678c2318d046478cebb73355708
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
<<<<<<< HEAD
                    nguyenlieu = (List<String>)documentSnapshot.get("NguyenLieu");
                    // nếu save trong dữ lieu firebase lơn hơn 2 thì sẽ duoc luu trong danh sach
                    if (Integer.parseInt(save)>2){
                        Explore_listRecipes_suggest.add(new Recipes(id,image,name, save, time));
                    }
                    for (String ingres_item : nguyenlieu)
                    {
                        if(unAccent(ingres_item.replace(" ","")).toLowerCase().contains(unAccent(newtext.toLowerCase().replace(" ",""))))
                        {
                            ResultSearchList.add(new Recipes(id, image, name, save, time));
                            break;
                        }
                    }
                    if(!ResultSearchList.isEmpty()){
                        if(newtext.equals(""))
                        {
                            txtIngredients.setText("Một số món gợi ý");
                        }
                        else {
                            txtIngredients.setText("Có " + ResultSearchList.size() + " kết quả phù hợp");}
                        Explore_adapter.setData(ResultSearchList,new IClickOnItemRecipe() {
                            @Override
                            public void onClickItemRecipe(Recipes recipes) {
                                onClickGoToDetailFood(recipes);
                            }
                        });
                        Explore_recyclerViewRandom.setAdapter(Explore_adapter);
                    }
                    //search ko co ket qua
                    else
                    {
                        txtIngredients.setText("Không tìm thấy kết quả phù hợp\nMột số món được yêu thích");

                        Explore_adapter.setData(Explore_listRecipes_suggest,new IClickOnItemRecipe() {
                            @Override
                            public void onClickItemRecipe(Recipes recipes) {
                                onClickGoToDetailFood(recipes);
                            }
                        });
                        Explore_recyclerViewRandom.setAdapter(Explore_adapter);
                    }

=======
                    Explore_db.collection("SaveRecipes").whereEqualTo("Recipes",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            ArrayList<String> idUser = new ArrayList<>();
                            String save;
                            for (QueryDocumentSnapshot doc :value) {
                                if (doc.get("idUsers") != null) {
                                    idUser = (ArrayList<String>) doc.get("idUsers");
                                }
                                save = String.valueOf(idUser.size());
                                Recipes Newrcp = new Recipes(id, image, name, save, time);
                                if(unAccent(Newrcp.getName().replace(" ","")).toLowerCase().contains(unAccent(newtext.toLowerCase().replace(" ",""))))
                                {
                                    ResultSearchList.add(Newrcp);
                                }
                                if(ResultSearchList.isEmpty()) {
                                    txtIngredients.setText("Không có kết quả phù hợp");
                                }
                                else{
                                    //tạm
                                    txtIngredients.setText("Có "+ResultSearchList.size()+" kết quả phù hợp");
                                }
                                RecipesAdapter myAdapter = new RecipesAdapter();
                                myAdapter.setData(ResultSearchList,new IClickOnItemRecipe() {
                                    @Override
                                    public void onClickItemRecipe(Recipes recipes) {
                                        onClickGoToDetailFood(recipes);
                                    }
                                });
                                Explore_recyclerViewRandom.setAdapter(myAdapter);
                            }
                        }
                    });
//                    get list nguyen lieu
//                    ingres = (List<String>) documentSnapshot.get("NguyenLieu");
//                    Explore_listRecipes.add(new Recipes(id, image, name, save, time));
>>>>>>> 8f713cba9408e678c2318d046478cebb73355708
                }
//                for (Recipes recipes : Explore_listRecipes)
//                {
//                    if(unAccent(recipes.getName().replace(" ","")).toLowerCase().contains(unAccent(newtext.toLowerCase().replace(" ",""))))
//                    {
//                        ResultSearchList.add(recipes);
//                    }
//                }
//                //search ko co ket qua
//                if(ResultSearchList.isEmpty())
//                {
//
//                    Explore_listRecipes_suggest = new ArrayList<>();// tim lai danh sach, dieu kien có luot save lon
//                    Explore_db.collection("Recipes")
//                            //.whereGreaterThanOrEqualTo("Save",2)
//                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                    if (error != null) {
//                                        Log.w("Error", "listen:error", error);
//                                    }
//                                    //lấy dữ liệu từ firebase
//                                    for (DocumentSnapshot documentSnapshot : value.getDocuments()){
//                                        String id = documentSnapshot.getString("id");
//                                        Explore_db.collection("SaveRecipes").whereEqualTo("Recipes",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                            @Override
//                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                                                ArrayList<String> idUser = new ArrayList<>();
//                                                for (QueryDocumentSnapshot doc :value)
//                                                {
//                                                    if(doc.get("idUsers") != null)
//                                                    {
//                                                        idUser = (ArrayList<String>) doc.get("idUsers");
//                                                    }
//                                                    String save = String.valueOf(idUser.size());
//                                                    String image = documentSnapshot.getString("image");
//                                                    String name = documentSnapshot.getString("name");
//                                                    String time = documentSnapshot.getString("timecook");
//                                                    Explore_listRecipes_suggest.add(new Recipes(id, image, name, save, time));
//                                                    Explore_adapter.setData(Explore_listRecipes_suggest,new IClickOnItemRecipe() {
//                                                        @Override
//                                                        public void onClickItemRecipe(Recipes recipes) {
//                                                            onClickGoToDetailFood(recipes);
//                                                        }
//                                                    });
//                                                    txtRecipes.setText("Món ăn bạn tìm đang được cập nhật\nMột số món gợi ý");
//                                                    Explore_recyclerViewRandom.setAdapter(Explore_adapter);
//                                                }
//                                            }
//                                        });
//
//                                    }
//                                }
//                            });
//
//                }
//                else{
//                    //tạm
//                    txtRecipes.setText("Có "+ResultSearchList.size()+" món ăn theo yêu cầu");
//                    Explore_adapter.setData(ResultSearchList,new IClickOnItemRecipe() {
//                        @Override
//                        public void onClickItemRecipe(Recipes recipes) {
//                            onClickGoToDetailFood(recipes);
//                        }
//                    });
//                    Explore_recyclerViewRandom.setAdapter(Explore_adapter);
//                }


            }
        });
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
                    String time = documentSnapshot.getString("timecook");
                    Explore_db.collection("SaveRecipes").whereEqualTo("Recipes",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            ArrayList<String> idUser = new ArrayList<>();
                            String save;
                            for (QueryDocumentSnapshot doc :value) {
                                if (doc.get("idUsers") != null) {
                                    idUser = (ArrayList<String>) doc.get("idUsers");
                                }
                                save = String.valueOf(idUser.size());
                                Recipes Newrcp = new Recipes(id, image, name, save, time);
                                if(unAccent(Newrcp.getName().replace(" ","")).toLowerCase().contains(unAccent(newtext.toLowerCase().replace(" ",""))))
                                {
                                    ResultSearchList.add(Newrcp);
                                }
                                if(ResultSearchList.isEmpty()) {
                                    txtIngredients.setText("Không có kết quả phù hợp");
                                }
                                else{
                                    //tạm
                                    txtIngredients.setText("Có "+ResultSearchList.size()+" kết quả phù hợp");
                                }
                                RecipesAdapter myAdapter = new RecipesAdapter();
                                myAdapter.setData(ResultSearchList,new IClickOnItemRecipe() {
                                    @Override
                                    public void onClickItemRecipe(Recipes recipes) {
                                        onClickGoToDetailFood(recipes);
                                    }
                                });
                                Explore_recyclerViewRandom.setAdapter(myAdapter);
                            }
                        }
                    });
//                    get list nguyen lieu
//                    ingres = (List<String>) documentSnapshot.get("NguyenLieu");
//                    Explore_listRecipes.add(new Recipes(id, image, name, save, time));
                }
//                for (Recipes recipes : Explore_listRecipes)
//                {
//                    if(unAccent(recipes.getName().replace(" ","")).toLowerCase().contains(unAccent(newtext.toLowerCase().replace(" ",""))))
//                    {
//                        ResultSearchList.add(recipes);
//                    }
//                }
//                //search ko co ket qua
//                if(ResultSearchList.isEmpty())
//                {
//
//                    Explore_listRecipes_suggest = new ArrayList<>();// tim lai danh sach, dieu kien có luot save lon
//                    Explore_db.collection("Recipes")
//                            //.whereGreaterThanOrEqualTo("Save",2)
//                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                    if (error != null) {
//                                        Log.w("Error", "listen:error", error);
//                                    }
//                                    //lấy dữ liệu từ firebase
//                                    for (DocumentSnapshot documentSnapshot : value.getDocuments()){
//                                        String id = documentSnapshot.getString("id");
//                                        Explore_db.collection("SaveRecipes").whereEqualTo("Recipes",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                            @Override
//                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                                                ArrayList<String> idUser = new ArrayList<>();
//                                                for (QueryDocumentSnapshot doc :value)
//                                                {
//                                                    if(doc.get("idUsers") != null)
//                                                    {
//                                                        idUser = (ArrayList<String>) doc.get("idUsers");
//                                                    }
//                                                    String save = String.valueOf(idUser.size());
//                                                    String image = documentSnapshot.getString("image");
//                                                    String name = documentSnapshot.getString("name");
//                                                    String time = documentSnapshot.getString("timecook");
//                                                    Explore_listRecipes_suggest.add(new Recipes(id, image, name, save, time));
//                                                    Explore_adapter.setData(Explore_listRecipes_suggest,new IClickOnItemRecipe() {
//                                                        @Override
//                                                        public void onClickItemRecipe(Recipes recipes) {
//                                                            onClickGoToDetailFood(recipes);
//                                                        }
//                                                    });
//                                                    txtRecipes.setText("Món ăn bạn tìm đang được cập nhật\nMột số món gợi ý");
//                                                    Explore_recyclerViewRandom.setAdapter(Explore_adapter);
//                                                }
//                                            }
//                                        });
//
//                                    }
//                                }
//                            });
//
//                }
//                else{
//                    //tạm
//                    txtRecipes.setText("Có "+ResultSearchList.size()+" món ăn theo yêu cầu");
//                    Explore_adapter.setData(ResultSearchList,new IClickOnItemRecipe() {
//                        @Override
//                        public void onClickItemRecipe(Recipes recipes) {
//                            onClickGoToDetailFood(recipes);
//                        }
//                    });
//                    Explore_recyclerViewRandom.setAdapter(Explore_adapter);
//                }


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
                            Explore_db.collection("SaveRecipes").whereEqualTo("Recipes",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                        Explore_listRecipes.add(new Recipes(id, image, name, save, time));
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