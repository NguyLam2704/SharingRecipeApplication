package com.example.sharingrecipeapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sharingrecipeapp.Adapters.RandomRecipeAdapter;
import com.example.sharingrecipeapp.R;
import com.example.sharingrecipeapp.RequestManager;

public class HomeFragment extends Fragment {
//    ProgressDialog dialog;
    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        dialog = new ProgressDialog(getActivity());
//        dialog.setTitle("Loading...");

//        manager =new RequestManager(getActivity());
//        manager.getRandomRecipes(randomRecipeResponseListener);
//        dialog.show();
//
//        recyclerView = view.findViewById(R.id.recyRate);
        return view;
    }

//    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
//        @Override
//        public void didFetch(RandomRecipeApiResponse response, String message) {
//            recyclerView.setHasFixedSize(true);
//            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
//            randomRecipeAdapter = new RandomRecipeAdapter(getActivity(),response.recipes);
//            recyclerView.setAdapter(randomRecipeAdapter);
//        }
//
//        @Override
//        public void didError(String message) {
//            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);
//        }
//    };

}