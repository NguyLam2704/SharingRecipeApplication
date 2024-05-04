package com.example.sharingrecipeapp.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sharingrecipeapp.Activities.BottomNavigationCustomActivity;
import com.example.sharingrecipeapp.Adapters.Home.RecipesAdapter;
import com.example.sharingrecipeapp.R;


public class ExploreFragment extends Fragment {
    private BottomNavigationCustomActivity bottomNavigationCustomActivity;
    RecipesAdapter recipesAdapter;
    SearchView Eplore_searchview;

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
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }
}