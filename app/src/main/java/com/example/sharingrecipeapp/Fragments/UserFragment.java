package com.example.sharingrecipeapp.Fragments;

import static android.widget.Toast.makeText;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sharingrecipeapp.Activities.BottomNavigationCustomActivity;
import com.example.sharingrecipeapp.R;
import com.example.sharingrecipeapp.UpdateProfileActivity;
import com.example.sharingrecipeapp.databinding.FragmentPlanBinding;
import com.example.sharingrecipeapp.databinding.FragmentUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;

public class UserFragment extends Fragment {

    public static final int MY_REQUEST_CODE = 10;
    private BottomNavigationCustomActivity bottomNavigationCustomActivity;
    protected LinearLayout Saved,Change_profile, Setting, Logout;
    protected TextView btn_Saved, btn_Change, btn_setting, btn_logout, text_name;
    protected ImageView image_account;
    public static FirebaseAuth firebaseAuth;
    protected FirebaseFirestore firestore;
    protected static FirebaseUser currentUser;
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
        btn_setting = view.findViewById(R.id.btn_setting);
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
                    Glide.with(this).load(avatarURL).error(R.drawable.round_account_circle).into(image_account);
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
                bottomNavigationCustomActivity.gotoSetting();
            }
        });
    }
    private void setOnClickBtnSetting(){
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoSetting();
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