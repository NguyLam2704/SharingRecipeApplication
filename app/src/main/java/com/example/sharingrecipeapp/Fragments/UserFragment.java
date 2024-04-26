package com.example.sharingrecipeapp.Fragments;

import static android.widget.Toast.makeText;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sharingrecipeapp.R;
import com.example.sharingrecipeapp.UpdateProfileActivity;
import com.example.sharingrecipeapp.databinding.FragmentPlanBinding;
import com.example.sharingrecipeapp.databinding.FragmentUserBinding;

public class UserFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected ImageButton image_user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user, container, false);

        image_user = view.findViewById(R.id.image_user);
        image_user.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent update_profile=new Intent(getActivity(), UpdateProfileActivity.class);
                startActivity(update_profile);
            }
        });
        return view;
    }

}