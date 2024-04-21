package com.example.sharingrecipeapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.sharingrecipeapp.databinding.FragmentGroceriesBinding;
import androidx.fragment.app.Fragment;

import com.example.sharingrecipeapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroceriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroceriesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroceriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroceriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroceriesFragment newInstance(String param1, String param2) {
        GroceriesFragment fragment = new GroceriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    ListView ingListView;
    ArrayList<ListIngredient> arrayListIng;
    ListInAdapter adapter;
    private FragmentGroceriesBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        AnhXa();
        adapter = new ListInAdapter(binding.getRoot().getContext(), R.layout.list_ingredients, arrayListIng);
        ingListView.setAdapter(adapter);

        // Xac nhan xoa het tat ca cac item



    }

    private void AnhXa(){
        ingListView = (ListView) binding.getRoot().findViewById(R.id.list_groceries);
        arrayListIng =new ArrayList<>();
        arrayListIng.add(new ListIngredient("avocado","4", R.drawable.avocado, false));
        arrayListIng.add(new ListIngredient("baguette","2", R.drawable.baguette, false));
        arrayListIng.add(new ListIngredient("broccoli", "5", R.drawable.broccoli, false));
        arrayListIng.add(new ListIngredient("cabbage","3", R.drawable.cabbage, false));
        arrayListIng.add(new ListIngredient("carrot","2", R.drawable.carrot, false));
        arrayListIng.add(new ListIngredient("cheese","4", R.drawable.cheese, false));
        arrayListIng.add(new ListIngredient("corn","3", R.drawable.corn, false));
        arrayListIng.add(new ListIngredient("crab","4", R.drawable.crab,false));
        arrayListIng.add(new ListIngredient("eggs","4", R.drawable.eggs, false));
        arrayListIng.add(new ListIngredient("fish", "2", R.drawable.fish,false));
        arrayListIng.add(new ListIngredient("milk", "3", R.drawable.milk, false));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groceries, container, false);
    }


}