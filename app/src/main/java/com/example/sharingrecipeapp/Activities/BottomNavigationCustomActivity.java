package com.example.sharingrecipeapp.Activities;

import static android.content.Intent.getIntent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;


import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.Classes.Theme;
import com.example.sharingrecipeapp.Fragments.ExploreFragment;
import com.example.sharingrecipeapp.Fragments.GroceriesFragment;
import com.example.sharingrecipeapp.Fragments.HomeFragment;
import com.example.sharingrecipeapp.Fragments.NonUserFragment;
import com.example.sharingrecipeapp.Fragments.PlanFragment;
import com.example.sharingrecipeapp.Fragments.UserFragment;
import com.example.sharingrecipeapp.R;

//import com.example.sharingrecipeapp.databinding.ActivityHomeBinding;
import com.example.sharingrecipeapp.databinding.ActivityBottomNavigationCustomBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class BottomNavigationCustomActivity extends AppCompatActivity {
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private BottomNavigationView mBottomNavigationView;

    public FirebaseAuth firebaseAuth;
    protected FirebaseFirestore firestore;
    protected FirebaseUser currentUser;
    ActivityBottomNavigationCustomBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bottom_navigation_custom);
//        **
        binding = ActivityBottomNavigationCustomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();




        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home_menu:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.search_menu:
                    replaceFragment(new ExploreFragment());
                    break;
                case R.id.plan_menu:
                    if(currentUser != null){
                        replaceFragment(new PlanFragment());
                    }else{
                        replaceFragment(new NonUserFragment());
                    }
                    break;
                case R.id.mall_menu:
                    if(currentUser != null){
                        replaceFragment(new GroceriesFragment());
                    }else{
                        replaceFragment(new NonUserFragment());
                    }
                    break;
                case R.id.user_menu:
                    if(currentUser != null){
                        replaceFragment(new UserFragment());
                    }else{
                        replaceFragment(new NonUserFragment());
                    }
                    break;
            }
            return true;
        });

    }

    public void gotoAddRecipe(){
        Intent intent = new Intent(this, CreateRecipeActivity.class);
        startActivity(intent);
    }

    public void gotoFoodDetail(Recipes recipes) {
        Intent intent = new Intent(this, FoodDetailActivity.class);
        intent.putExtra("id", recipes.getId());
        startActivity(intent);
    }

    public void gotoFoodDetail(String id) {
        Intent intent = new Intent(this, FoodDetailActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }


  public void gotoSaved(){
      Intent intent=new Intent(this, SaveListActivity.class);
      startActivity(intent);
  }


    public void gotoChangeProfile() {
        Intent intent = new Intent(this, UpdateProfileActivity.class);
        startActivity(intent);
    }

    public void gotoShared() {
        Intent intent = new Intent(this, SharedListActivity.class);
        startActivity(intent);
    }


    public void gotoLogout() {
        logoutAccount();
    }

    private void logoutAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đăng xuất");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseAuth.signOut();
                Intent intent = new Intent(BottomNavigationCustomActivity.this, BottomNavigationCustomActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface abc) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.color_primary));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.color_primary));
            }
        });
        dialog.show();
    }

    public void gotoThemeDetail(Theme theme) {
        Intent intent = new Intent(BottomNavigationCustomActivity.this, ThemeActivity.class);
        intent.putExtra("id", theme.getId());
        startActivity(intent);
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.view_pager,fragment);
        fragmentTransaction.commit();
    }
    public void gotoLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void reload() {
        Intent intent = new Intent(this, BottomNavigationCustomActivity.class);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    public void gotoAllRecipes(){
        Intent intent = new Intent(this, AllRecipesActivity.class);
        startActivity(intent);
    }
}




