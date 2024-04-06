package com.example.sharingrecipeapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sharingrecipeapp.Fragments.ExploreFragment;
import com.example.sharingrecipeapp.Fragments.GroceriesFragment;
import com.example.sharingrecipeapp.Fragments.HomeFragment;
import com.example.sharingrecipeapp.Fragments.MoreFragment;
import com.example.sharingrecipeapp.Fragments.PlanFragment;
import com.example.sharingrecipeapp.R;
import com.example.sharingrecipeapp.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              if(item.getItemId() == R.id.home_menu){
                replaceFragment(new HomeFragment());
              } else if (item.getItemId() == R.id.search_menu) {
                  replaceFragment(new ExploreFragment());
              } else if (item.getItemId() == R.id.plan_menu) {
                  replaceFragment(new PlanFragment());
              } else if (item.getItemId() == R.id.mall_menu) {
                  replaceFragment(new GroceriesFragment());
              } else if (item.getItemId() == R.id.user_menu) {
                  replaceFragment(new MoreFragment());
              }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}