package com.example.sharingrecipeapp.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharingrecipeapp.Adapters.RandomRecipeAdapter;
import com.example.sharingrecipeapp.R;
import com.example.sharingrecipeapp.RequestManager;
import com.example.sharingrecipeapp.databinding.FragmentHomeBinding;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {
//    ProgressDialog dialog;
    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;

    TextView like, time, tittle;
    ImageView img;

    Uri uri;

    FragmentHomeBinding binding;
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