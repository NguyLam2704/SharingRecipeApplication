package com.example.sharingrecipeapp.Fragments;

import static com.example.sharingrecipeapp.Fragments.ExploreFragment.unAccent;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sharingrecipeapp.Activities.BottomNavigationCustomActivity;
import com.example.sharingrecipeapp.Adapters.Explore.ResultExploreAdapter;
import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Adapters.Home.RecipesAdapter;
import com.example.sharingrecipeapp.Adapters.Home.RecipesRandomAdapter;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.R;
import com.example.sharingrecipeapp.databinding.FragmentExploreBinding;
import com.google.android.gms.tasks.OnSuccessListener;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_explore_recipes, container, false);
        bottomNavigationCustomActivity = (BottomNavigationCustomActivity) getActivity();
        txtRecipes= (TextView) view.findViewById(R.id.txt_explore_recipes);
        txtRecipes.setText("Một số công thức gợi ý");
        Explore_searchview_recipes = (SearchView) view.findViewById(R.id.explore_searchbar_recipes);
        Explore_searchview_recipes.clearFocus();
        Explore_searchview_recipes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Explore_searchview_recipes.setBackgroundResource(R.drawable.edittext_bound);
                Explore_searchName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Explore_searchview_recipes.setBackgroundResource(R.drawable.query_bound);
                if(newText.equals(""))
                {
                    Explore_searchview_recipes.setBackgroundResource(R.drawable.edittext_bound);
//                    recipesList.clear();
                    setdataRecycRandom();
                }
                return true;
            }
        });
        Explore_recyclerViewRandom = (RecyclerView) view.findViewById(R.id.explore_recycler_recipes);
        Explore_firebaseAuth = FirebaseAuth.getInstance();
        Explore_db = FirebaseFirestore.getInstance();
        setdataRecycRandom();



        return view;
    }

    String username;
    private void Explore_searchName(String newtext)
    {
        Explore_listRecipes_suggest = new ArrayList<>();// tim lai danh sach, dieu kien có luot save lon
        List<Recipes> ResultSearchList = new ArrayList<>();
        Explore_listRecipes = new ArrayList<>();
        Explore_db.collection("Recipes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("Error", "listen:error", error);
                }

                //lấy dữ liệu từ firebase

                for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                    String id = documentSnapshot.getString("id");
                    DocumentReference docRef = documentSnapshot.getDocumentReference("Users");

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
                                Integer userSize = idUser.size();
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot) {
                                        username = snapshot.getString("username");
                                        Recipes Newrcp = new Recipes(id, image, name, save, time, "username");
                                        Explore_listRecipes.add(Newrcp);
                                        if (userSize>3){
                                            Explore_listRecipes_suggest.add(Newrcp);
                                        }
                                        if(unAccent(Newrcp.getName().replace(" ","")).toLowerCase().contains(unAccent(newtext.toLowerCase().replace(" ",""))))
                                        {
                                            ResultSearchList.add(Newrcp);
                                        }

                                        if(ResultSearchList.isEmpty()) {
                                            txtRecipes.setText("Không có kết quả phù hợp");
                                        }
                                        else{
                                            //tạm
                                            txtRecipes.setText("Có "+ResultSearchList.size()+" kết quả phù hợp");
                                        }

                                        if(!ResultSearchList.isEmpty())
                                        {
                                            if (newtext.equals("")){
                                                txtRecipes.setText("Một số món gợi ý");
                                                Explore_adapter.setData(Explore_listRecipes,new IClickOnItemRecipe() {
                                                    @Override
                                                    public void onClickItemRecipe(Recipes recipes) {
                                                        onClickGoToDetailFood(recipes);
                                                    }
                                                });
                                            }else
                                            {
                                                txtRecipes.setText("Có "+ResultSearchList.size()+" kết quả phù hợp");
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
                                            Explore_adapter.setData(Explore_listRecipes_suggest,new IClickOnItemRecipe() {
                                                @Override
                                                public void onClickItemRecipe(Recipes recipes) {
                                                    onClickGoToDetailFood(recipes);
                                                }
                                            });
                                            txtRecipes.setText("Không tìm thấy kết quả phù hợp\nMột số món được yêu thích");
                                            Explore_recyclerViewRandom.setAdapter(Explore_adapter);
                                        }
                                    }
                                });



                            }


                        }
                    });



                }

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



        });
    }


    private void setdataRecycRandom()
    {
        GridLayoutManager Explore_gridlayoutMng = new GridLayoutManager(getContext(),2);
        Explore_recyclerViewRandom.setLayoutManager(Explore_gridlayoutMng);

        Explore_adapter = new ResultExploreAdapter();
        Explore_listRecipes = new ArrayList<>();
        Explore_adapter.setData(Explore_listRecipes, new IClickOnItemRecipe() {
            @Override
            public void onClickItemRecipe(Recipes recipes) {
                onClickGoToDetailFood(recipes);
            }
        });
        Explore_recyclerViewRandom.setAdapter(Explore_adapter);
        Explore_db.collection("Recipes")
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

                                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot snapshot) {
                                                username= snapshot.getString("username");
                                                Explore_listRecipes.add(new Recipes(id, image, name, save, time, "luan"));
                                                Explore_adapter.notifyDataSetChanged();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
    }

//    private void setdataRecycRandom() {
//
//
//        Explore_recyclerViewRandom.setLayoutManager(new GridLayoutManager(getContext(),2));
//
//        Explore_adapter = new ResultExploreAdapter();
//        Explore_listRecipes = new ArrayList<>();
//        Explore_recipesAdapter.setData(Explore_listRecipes, new IClickOnItemRecipe() {
//            @Override
//            public void onClickItemRecipe(Recipes recipes) {
//                onClickGoToDetailFood(recipes);
//            }
//        });
//        Explore_recyclerViewRandom.setAdapter(Explore_recipesAdapter);
//        Explore_db.collection("Recipes").limit(10)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            Log.w("Error", "listen:error", error);
//                            return;
//                        }
//
//                        for (DocumentSnapshot documentSnapshot : value.getDocuments()){
//                            String id = documentSnapshot.getString("id");
//                            DocumentReference docRef = documentSnapshot.getDocumentReference("Users");
//
//
//                            Explore_db.collection("SaveRecipes").whereEqualTo("Recipes",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                                    ArrayList<String> idUser = new ArrayList<>();
//                                    for (QueryDocumentSnapshot doc :value)
//                                    {
//                                        idUser = (ArrayList<String>) doc.get("idUsers");
//                                        String save = String.valueOf(idUser.size());
//                                        String image = documentSnapshot.getString("image");
//                                        String name = documentSnapshot.getString("name");
//                                        String time = documentSnapshot.getString("timecook");
//
//                                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                            @Override
//                                            public void onSuccess(DocumentSnapshot doc) {
//                                                username = doc.getString("username");
//                                                Explore_listRecipes.add(new Recipes(id, image, name, save,time, username));
//                                                Explore_adapter.notifyDataSetChanged();
//                                            }
//                                        });
//                                    }
//                                }
//                            });
//                        }
//
//                    }
//                });
//    }
    private void onClickGoToDetailFood(Recipes recipes) {
        bottomNavigationCustomActivity.gotoFoodDetail(recipes);
    }

}