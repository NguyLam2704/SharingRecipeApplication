package com.example.sharingrecipeapp.Fragments;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.example.sharingrecipeapp.Activities.BottomNavigationCustomActivity;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.R;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class ExploreFragment extends Fragment {
    private BottomNavigationCustomActivity bottomNavigationCustomActivity;
    ProgressBar Explore_progressbar;
    TabExploreAdapter adapter;
    LinearLayout Explore_linear;
    private ViewPager2 viewPager;
    private View mView;
    TabLayout tabLayout;


//    public ExploreFragment() {
//        // Required empty public constructor
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /////////////////////
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_explore, container, false);
        bottomNavigationCustomActivity = (BottomNavigationCustomActivity) getActivity();
        Explore_linear = (LinearLayout) mView.findViewById(R.id.explore_linearLayout);
        Explore_progressbar = (ProgressBar) mView.findViewById(R.id.explore_progressbar);
        //Explore_searchview = (SearchView) view.findViewById(R.id.explore_searchbar);
        viewPager = (ViewPager2) mView.findViewById(R.id.explore_viewpage);
        tabLayout= (TabLayout) mView.findViewById(R.id.explore_tabs);
        //viewPager.setSaveEnabled(false);
        adapter= new TabExploreAdapter(this);
        viewPager.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
        return mView;

    }

    //chuyển có dấu thành không dấu
    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        if (s.equals("Đ") || s.equals("đ"))
        {
            return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "d");
        }
        return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D');

    }

    private void onClickGoToDetailFood(Recipes recipes) {
        bottomNavigationCustomActivity.gotoFoodDetail(recipes);
    }
}