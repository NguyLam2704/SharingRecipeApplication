package com.example.sharingrecipeapp.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabExploreAdapter extends FragmentStateAdapter {




    public TabExploreAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FragmentExploreRecipes();
            case 1:
                return new FragmentExploreIngredient();
            case 2:
                return new FragmentExploreCook();
            default:
                return new FragmentExploreRecipes();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
