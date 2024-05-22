package com.example.sharingrecipeapp.Fragments;

import static com.example.sharingrecipeapp.Fragments.ExploreFragment.unAccent;


import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.sharingrecipeapp.Activities.BottomNavigationCustomActivity;
import com.example.sharingrecipeapp.Activities.FoodDetailActivity;
import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Adapters.Home.RecipesAdapter;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentExploreCook#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentExploreCook extends Fragment {


    private BottomNavigationCustomActivity bottomNavigationCustomActivity;
    TextView txtCooks;

    SearchView Explore_searchview_cook;
    private RecyclerView Explore_recyclerView;
    private List<Recipes> Explore_listRecipes;
    List<Recipes> Explore_listRecipes_suggest; // goi ý
    private FirebaseAuth Explore_firebaseAuth;
    private FirebaseUser user;
    private FirebaseFirestore Explore_db;
    RecipesAdapter myAdapter;

    String username;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentExploreCook() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentExploreCook.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentExploreCook newInstance(String param1, String param2) {
        FragmentExploreCook fragment = new FragmentExploreCook();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    ActivityResultLauncher<Intent> activityResultLauncher =  registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
        if (result.getResultCode() == 111){
            for (int i = 0; i < Explore_listRecipes_suggest.size(); i++){
                if (result.getData().getExtras().get("id").toString().equals(Explore_listRecipes_suggest.get(i).getId())){
                    Explore_listRecipes_suggest.get(i).setSave(String.valueOf(result.getData().getExtras().getString("save")));
                }
            }
            myAdapter.notifyDataSetChanged();
           displayRecipes();

        }
    });

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
        View view =  inflater.inflate(R.layout.fragment_explore_cook, container, false);
        bottomNavigationCustomActivity = (BottomNavigationCustomActivity) getActivity();
        Explore_searchview_cook = (SearchView) view.findViewById(R.id.explore_searchbar_cook);
        txtCooks= (TextView) view.findViewById(R.id.txt_explore_cook);



        Explore_db = FirebaseFirestore.getInstance();
        Explore_firebaseAuth = FirebaseAuth.getInstance();
        user = Explore_firebaseAuth.getCurrentUser();
        displayRecipes();
        Explore_searchview_cook.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Co gi doi lai thanh cook
                Explore_searchview_cook.setBackgroundResource(R.drawable.edittext_bound);
                Search_person(query);
                //Explore_searchCook(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Explore_searchview_cook.setBackgroundResource(R.drawable.query_bound);
                if(newText.equals(""))
                {
                    Explore_searchview_cook.setBackgroundResource(R.drawable.edittext_bound);
//                    recipesList.clear();
                    displayRecipes();
                }
                return true;
            }
        });
        Explore_recyclerView = (RecyclerView) view.findViewById(R.id.explore_recycler_cook);
        Explore_firebaseAuth = FirebaseAuth.getInstance();
        Explore_db = FirebaseFirestore.getInstance();

        return view;
    }
    private void displayRecipes(){
        Explore_listRecipes_suggest = new ArrayList<>();
        Explore_db.collection("Recipes").limit(10).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                {
                    return;
                }
                for (QueryDocumentSnapshot queryDocumentSnapshot : value)
                {
                    String image = queryDocumentSnapshot.getString("image");
                    String id = queryDocumentSnapshot.getString("id");
                    String name = queryDocumentSnapshot.getString("name");
                    String time = queryDocumentSnapshot.get("timecook").toString();
                    DocumentReference docRef = queryDocumentSnapshot.getDocumentReference("Users");
                    Explore_db.collection("SaveRecipes").whereEqualTo("Recipes",id).limit(10).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            ArrayList<String> idUser = new ArrayList<>();
                            String save;
                            for (QueryDocumentSnapshot queryDocumentSnapshot1 : value)
                            {
                                if(queryDocumentSnapshot1.get("idUsers") != null)
                                {
                                    idUser = (ArrayList<String>) queryDocumentSnapshot1.get("idUsers");
                                }
                                save = String.valueOf(idUser.size());
                                docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        username = value.getString("username");

                                    }
                                });
                                Recipes recipes = new Recipes(id,image,name,save,time,username);
                                Explore_listRecipes_suggest.add(recipes);
                                txtCooks.setText("Một số công thức gợi ý");
                                myAdapter = new RecipesAdapter();
                                myAdapter.setData( Explore_listRecipes_suggest,new IClickOnItemRecipe() {
                                    @Override
                                    public void onClickItemRecipe(Recipes recipes) {
                                        onClickGoToDetailFood(recipes);
                                    }
                                });
                                Explore_recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                                Explore_recyclerView.setAdapter(myAdapter);
                            }
                        }
                    });
                }
            }
        });
    }

    public void Search_person(String newtext)
    {
        Explore_listRecipes = new ArrayList<>();
        DocumentReference current_user = Explore_db.collection("Users").document(user.getUid());
        Explore_db.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                {
                    Log.w("Error", "listen:error", error);
                    return;
                }
//
                for (QueryDocumentSnapshot queryDocumentSnapshot : value)
                {
                    String uid = queryDocumentSnapshot.getString("id");
                    String fullname = queryDocumentSnapshot.getString("username");
                    if(unAccent(fullname.replace(" ","")).toLowerCase().contains(unAccent(newtext.toLowerCase().replace(" ",""))))
                    {
                        DocumentReference find_user = Explore_db.collection("Users").document(uid);
                        Explore_db.collection("Recipes").whereEqualTo("Users",find_user).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error != null)
                                {
                                    Log.w("Error", "listen:error", error);
                                }
                                for (QueryDocumentSnapshot queryDocumentSnapshot1 : value)
                                {
                                    String image = queryDocumentSnapshot1.getString("image");
                                    String id = queryDocumentSnapshot1.getString("id");
                                    String name = queryDocumentSnapshot1.getString("name");
                                    String time = queryDocumentSnapshot1.get("timecook").toString();
                                    DocumentReference docRef = queryDocumentSnapshot1.getDocumentReference("Users");
                                    Explore_db.collection("SaveRecipes").whereEqualTo("Recipes",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if(error != null)
                                            {
                                                Log.w("Error", "listen:error", error);
                                            }
                                            ArrayList<String> idUser = new ArrayList<>();
                                            String save;
                                            for (QueryDocumentSnapshot queryDocumentSnapshot2 : value)
                                            {
                                                if(queryDocumentSnapshot2.get("idUsers") != null) {
                                                    idUser = (ArrayList<String>) queryDocumentSnapshot2.get("idUsers");
                                                }
                                                save = String.valueOf(idUser.size());
                                                docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                        username = value.getString("username");
                                                    }
                                                });
                                                Recipes recipes = new Recipes(id,image,name,save,time,username);
                                                Explore_listRecipes.add(recipes);
                                                myAdapter = new RecipesAdapter();
                                                myAdapter.setData( Explore_listRecipes,new IClickOnItemRecipe() {
                                                    @Override
                                                    public void onClickItemRecipe(Recipes recipes) {
                                                        onClickGoToDetailFood(recipes);
                                                    }
                                                });
                                                Explore_recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                                                Explore_recyclerView.setAdapter(myAdapter);
                                            }
                                            txtCooks.setText("Có " + Explore_listRecipes.size() +" kết quả phù hợp");
                                        }
                                    });
                                }
                            }
                        });
                        if(Explore_listRecipes.isEmpty())
                        {
                            txtCooks.setText("Không có kết quả phù hợp\nMột số món được yêu thích");
                        }
                        RecipesAdapter myAdapter = new RecipesAdapter();
                        myAdapter.setData( Explore_listRecipes,new IClickOnItemRecipe() {
                            @Override
                            public void onClickItemRecipe(Recipes recipes) {
                                onClickGoToDetailFood(recipes);
                            }
                        });
                        Explore_recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        Explore_recyclerView.setAdapter(myAdapter);
                        break;
                    }

                }
            }
        });
    }
    //    chuyển có dấu thành không dấu
    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        if (s.equals("Đ") || s.equals("đ"))
        {
            return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "d");
        }
        return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D');
    }

    private void onClickGoToDetailFood(Recipes recipes) {
        Intent intent = new Intent(getContext(), FoodDetailActivity.class);
        intent.putExtra("id", recipes.getId());
        activityResultLauncher.launch(intent);
    }
}