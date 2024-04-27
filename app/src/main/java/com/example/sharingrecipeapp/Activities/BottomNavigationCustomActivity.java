package com.example.sharingrecipeapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import androidx.viewpager2.widget.ViewPager2;



import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.example.sharingrecipeapp.Adapters.ViewPagerAdapter;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.Fragments.ExploreFragment;
import com.example.sharingrecipeapp.Fragments.GroceriesFragment;
import com.example.sharingrecipeapp.Fragments.HomeFragment;
import com.example.sharingrecipeapp.Fragments.PlanFragment;
import com.example.sharingrecipeapp.Fragments.UserFragment;
import com.example.sharingrecipeapp.R;

//import com.example.sharingrecipeapp.databinding.ActivityHomeBinding;
import com.example.sharingrecipeapp.SaveListActivity;
import com.example.sharingrecipeapp.UpdateProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class BottomNavigationCustomActivity extends AppCompatActivity {
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ViewPager2 mViewPager;
    UserFragment userFragment;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_custom);

        mViewPager = findViewById(R.id.view_pager);
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);


        fragmentArrayList.add(new HomeFragment());
        fragmentArrayList.add(new ExploreFragment());
        fragmentArrayList.add(new PlanFragment());
        fragmentArrayList.add(new GroceriesFragment());
        fragmentArrayList.add(new UserFragment());

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, fragmentArrayList);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.setSelectedItemId(R.id.home_menu);
                        break;
                    case 1:
                        mBottomNavigationView.setSelectedItemId(R.id.search_menu);
                        break;
                    case 2:
                        mBottomNavigationView.setSelectedItemId(R.id.plan_menu);
                        break;
                    case 3:
                        mBottomNavigationView.setSelectedItemId(R.id.mall_menu);
                        break;
                    case 4:
                        mBottomNavigationView.setSelectedItemId(R.id.user_menu);
                        break;
                    default:
                        mBottomNavigationView.setSelectedItemId(R.id.home_menu);
                        break;
                }
                super.onPageSelected(position);
            }
        });

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home_menu) {
                    mViewPager.setCurrentItem(0);
                } else if (item.getItemId() == R.id.search_menu) {
                    mViewPager.setCurrentItem(1);
                } else if (item.getItemId() == R.id.plan_menu) {
                    mViewPager.setCurrentItem(2);
                } else if (item.getItemId() == R.id.mall_menu) {
                    mViewPager.setCurrentItem(3);
                } else if (item.getItemId() == R.id.user_menu) {
                    mViewPager.setCurrentItem(4);
                }
                return true;
            }
        });
    }


    public void gotoFoodDetail(Recipes recipes) {
        Intent intent = new Intent(BottomNavigationCustomActivity.this, FoodDetailActivity.class);
        intent.putExtra("id", recipes.getId());
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
    public void gotoSetting() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
    public void gotoLogout() {
        Intent intent = new Intent(BottomNavigationCustomActivity.this, BottomNavigationCustomActivity.class);
        startActivity(intent);
    }
}