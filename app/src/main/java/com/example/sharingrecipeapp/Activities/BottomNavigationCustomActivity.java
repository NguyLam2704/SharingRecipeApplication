package com.example.sharingrecipeapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.sharingrecipeapp.Adapters.ViewPagerAdapter;
import com.example.sharingrecipeapp.Fragments.ExploreFragment;
import com.example.sharingrecipeapp.Fragments.HomeFragment;
import com.example.sharingrecipeapp.Fragments.PlanFragment;
import com.example.sharingrecipeapp.Fragments.UserFragment;
import com.example.sharingrecipeapp.R;
//import com.example.sharingrecipeapp.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class BottomNavigationCustomActivity extends AppCompatActivity {
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ViewPager2 mViewPager;
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
        fragmentArrayList.add(new ExploreFragment());
        fragmentArrayList.add(new UserFragment());

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, fragmentArrayList );
        mViewPager.setAdapter(viewPagerAdapter);



//        replaceFragment(new HomeFragment());
//        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//              if(item.getItemId() == R.id.home_menu){
//                replaceFragment(new HomeFragment());
//              } else if (item.getItemId() == R.id.search_menu) {
//                  replaceFragment(new ExploreFragment());
//              } else if (item.getItemId() == R.id.plan_menu) {
//                  replaceFragment(new PlanFragment());
//              } else if (item.getItemId() == R.id.mall_menu) {
//                  replaceFragment(new GroceriesFragment());
//              } else if (item.getItemId() == R.id.user_menu) {
//                  replaceFragment(new UserFragment());
//              }
//                return true;
//            }
//        });
//    }
//
//    private void replaceFragment(Fragment fragment){
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.view_pager,fragment);
//        fragmentTransaction.commit();
  }
}