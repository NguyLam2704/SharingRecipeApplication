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

//        fragmentArrayList.add(new TestFragment());

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

//                switch (item.getItemId())
//                {
//                    case R.id.home_menu:
//                        mViewPager.setCurrentItem(0);
//                        break;
//                    case R.id.search_menu:
//                        mViewPager.setCurrentItem(1);
//                        break;
//                    case R.id.plan_menu:
//                        mViewPager.setCurrentItem(2);
//                        break;
//                    case R.id.mall_menu:
//                        mViewPager.setCurrentItem(3);
//                        break;
//                    case R.id.user_menu:
//                        mViewPager.setCurrentItem(4);
//                        break;
//                    default:
//                        break;
//                }
                return true;
            }
        });


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

  public void gotoSaved(){
      Intent intent=new Intent(this, SaveListActivity.class);
      startActivity(intent);
  }
    public void gotoChangeProfile(){
        Intent intent=new Intent(this, UpdateProfileActivity.class);
        startActivity(intent);
    }
    public void gotoSetting(){
        Intent intent=new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
    public void gotoLogout(){
       logoutAccount();
    }
    private void logoutAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đăng xuất");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        builder.create().show();
    }


}