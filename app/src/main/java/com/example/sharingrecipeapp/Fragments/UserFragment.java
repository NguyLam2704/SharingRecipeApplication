package com.example.sharingrecipeapp.Fragments;

import static android.widget.Toast.makeText;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sharingrecipeapp.Activities.BottomNavigationCustomActivity;
import com.example.sharingrecipeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class UserFragment extends Fragment {

    public static final int MY_REQUEST_CODE = 10;
    private BottomNavigationCustomActivity bottomNavigationCustomActivity;
    protected LinearLayout Saved,Change_profile, Setting, Logout;
    protected TextView btn_Saved, btn_Change, btn_shared, btn_logout, text_name;
    protected ImageView image_account;
    public FirebaseAuth firebaseAuth;
    protected FirebaseFirestore firestore;
    protected FirebaseUser currentUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user, container, false);
        Saved = view.findViewById(R.id.layout1);
        btn_Saved = view.findViewById(R.id.btn_saved);
        Change_profile = view.findViewById(R.id.layout2);
        btn_Change = view.findViewById(R.id.btn_change_profile);
        Setting = view.findViewById(R.id.layout3);
        btn_shared = view.findViewById(R.id.btn_setting);
        Logout = view.findViewById(R.id.layout4);
        btn_logout = view.findViewById(R.id.btn_logout);
        image_account = view.findViewById(R.id.image_user);
        text_name = view.findViewById(R.id.name_user);
        bottomNavigationCustomActivity= (BottomNavigationCustomActivity) getActivity();

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        setProfileUser();

        setOnClickSaved();
        setOnClickBtnSaved();
        setOnClickChangeProfile();
        setOnClickBtnChange();
        setOnClickSetting();
        setOnClickBtnSetting();
        setOnClickLogout();
        setOnClickBtnLogout();
        setOnClickImageAccount();
        setOnClickTextName();

        return view;
    }
    public void setProfileUser(){
        if (currentUser != null) {
            String userID = currentUser.getUid();
            firestore = FirebaseFirestore.getInstance();
            DocumentReference userRef = firestore.collection("Users").document(userID);
            userRef.addSnapshotListener((value, error) -> {
                if(value!=null && value.exists()){
                    String name = value.getString("username");
                    String avatarURL = value.getString("avatar");
                    text_name.setText(name);
                    Picasso.get().load(avatarURL).into(image_account);
                }
            });
        }

    }
    private void setOnClickImageAccount(){
        image_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoChangeProfile();
            }
        });
    }
    private void setOnClickTextName(){
        text_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoChangeProfile();
            }
        });
    }
    private void setOnClickSaved(){
        Saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoSaved();
            }
        });
    }
    private void setOnClickBtnSaved(){
        btn_Saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoSaved();
            }
        });
    }
    private void setOnClickChangeProfile(){
        Change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoChangeProfile();
            }
        });
    }
    private void setOnClickBtnChange(){
        btn_Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoChangeProfile();
            }
        });
    }
    private void setOnClickSetting(){
        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoShared();
            }
        });
    }
    private void setOnClickBtnSetting(){
        btn_shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoShared();
            }
        });
    }
    private void setOnClickBtnLogout(){
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoLogout();
            }
        });
    }
    private void setOnClickLogout() {
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoLogout();
            }
        });
    }
}