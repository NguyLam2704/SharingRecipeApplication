package com.example.sharingrecipeapp.Fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Activities.BottomNavigationCustomActivity;
import com.example.sharingrecipeapp.Activities.LoginActivity;
import com.example.sharingrecipeapp.Activities.PlantoSavedActivity;
import com.example.sharingrecipeapp.Adapters.AdapterPlanListRecipes;
import com.example.sharingrecipeapp.Models.Recipes;
import com.example.sharingrecipeapp.R;
import com.example.sharingrecipeapp.databinding.FragmentPlanBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PlanFragment extends Fragment {
    private FragmentPlanBinding binding;
    private Calendar calendar;
    FirebaseFirestore db;

    RecyclerView recyclerView;

    public PlanFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PlanFragment newInstance() {
        PlanFragment fragment = new PlanFragment();
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        View CN, t2, t3, t4, t5, t6, t7;
        t2 = view.findViewById(R.id.add2);
        t3 = view.findViewById(R.id.add3);
        t4 = view.findViewById(R.id.add4);
        t5 = view.findViewById(R.id.add5);
        t6 = view.findViewById(R.id.add6);
        t7 = view.findViewById(R.id.add7);
        CN = view.findViewById(R.id.add1);

        doAddBtn(t2);
        doAddBtn(t3);
        doAddBtn(t4);
        doAddBtn(t5);
        doAddBtn(t6);
        doAddBtn(t7);
        doAddBtn(CN);

    }

    public void doAddBtn(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(binding.getRoot().getContext(), PlantoSavedActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private ImageView prev;
    private ImageView next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPlanBinding.inflate(inflater, container, false);
        calendar = Calendar.getInstance();

        InitUI();

        PlanOfDay();

        prev = binding.prevWeek;
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -7);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setDateTime(binding.DateTime);
                }

                LightOffCurDate(binding.getRoot());
                PlanOfDay();
            }
        });

        next = binding.nextWeek;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 7);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setDateTime(binding.DateTime);
                }
                resetRecyclerView();
                LightOffCurDate(binding.getRoot());
                PlanOfDay();
            }
        });

        return binding.getRoot();
    }

    private void resetRecyclerView() {

    }

    private void LightOffCurDate(View view) {
        Calendar cur = Calendar.getInstance();
        cur.setTimeInMillis(System.currentTimeMillis());

        if (cur.get(Calendar.WEEK_OF_YEAR) != calendar.get(Calendar.WEEK_OF_YEAR)) {
            TextView date;
            int color = requireContext().getColor(R.color.black);
            switch (cur.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY:
                    date = view.findViewById(R.id.Thu2);
                    date.setTextColor(color);
                    break;
                case Calendar.TUESDAY:
                    date = view.findViewById(R.id.Thu3);
                    date.setTextColor(color);
                    break;
                case Calendar.WEDNESDAY:
                    date = view.findViewById(R.id.Thu4);
                    date.setTextColor(color);
                    break;
                case Calendar.THURSDAY:
                    date = view.findViewById(R.id.Thu5);
                    date.setTextColor(color);
                    break;
                case Calendar.FRIDAY:
                    date = view.findViewById(R.id.Thu6);
                    date.setTextColor(color);
                    break;
                case Calendar.SATURDAY:
                    date = view.findViewById(R.id.Thu7);
                    date.setTextColor(color);
                    break;
                case Calendar.SUNDAY:
                    date = view.findViewById(R.id.ChuNhat);
                    date.setTextColor(color);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + calendar.get(Calendar.DAY_OF_WEEK));

            }
        } else {
            int color = requireContext().getColor(R.color.color_primary);
            TextView date;
            switch (cur.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY:
                    date = view.findViewById(R.id.Thu2);
                    date.setTextColor(color);
                    break;
                case Calendar.TUESDAY:
                    date = view.findViewById(R.id.Thu3);
                    date.setTextColor(color);
                    break;
                case Calendar.WEDNESDAY:
                    date = view.findViewById(R.id.Thu4);
                    date.setTextColor(color);
                    break;
                case Calendar.THURSDAY:
                    date = view.findViewById(R.id.Thu5);
                    date.setTextColor(color);
                    break;
                case Calendar.FRIDAY:
                    date = view.findViewById(R.id.Thu6);
                    date.setTextColor(color);
                    break;
                case Calendar.SATURDAY:
                    date = view.findViewById(R.id.Thu7);
                    date.setTextColor(color);
                    break;
                case Calendar.SUNDAY:
                    date = view.findViewById(R.id.ChuNhat);
                    date.setTextColor(color);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + calendar.get(Calendar.DAY_OF_WEEK));
            }
        }
    }

    private void PlanOfDay() {
        Calendar cur = calendar;
        db = FirebaseFirestore.getInstance();
        String weekID = String.valueOf(cur.get(Calendar.WEEK_OF_YEAR));

        List<String> date = new ArrayList<>();
        date.add("Thu2");
        date.add("Thu3");
        date.add("Thu4");
        date.add("Thu5");
        date.add("Thu6");
        date.add("Thu7");
        date.add("ChuNhat");


        db.collection("PlanList").document(weekID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (String dateOfWeek : date){
                            if ( task.getResult().get(dateOfWeek) == null) {

                                CardView empty = selectEmptyRecycler(dateOfWeek);
                                empty.setVisibility(View.VISIBLE);

                                ConstraintLayout setting = selectBtnSetting(dateOfWeek);
                                setting.setVisibility(View.GONE);

                                recyclerView = SelectRecycleView(dateOfWeek);
                                recyclerView.setVisibility(View.GONE);
//                                ArrayList<Recipes> recipes = new ArrayList<>();
//                                AdapterPlanListRecipes adapter = new AdapterPlanListRecipes(binding.getRoot().getContext(),recipes);
//                                recyclerView.setAdapter(adapter);
//                                recyclerView.
//                                recipes.clear();
//                                adapter.notifyDataSetChanged();
                                continue;
                            }
                            ArrayList<DocumentReference> doc = (ArrayList<DocumentReference>) task.getResult().get(dateOfWeek);
                            if (!doc.isEmpty()){
                                List<Recipes> recipesList = new ArrayList<Recipes>();
                                for (DocumentReference x : doc) {

                                    x.get().addOnSuccessListener(documentSnapshot -> {

                                        String a, b;
                                        a = documentSnapshot.get("name").toString();
                                        b = documentSnapshot.get("image").toString();
                                        recipesList.add(new Recipes(a, b));
                                        recyclerView = SelectRecycleView(dateOfWeek);
                                        recyclerView.setVisibility(View.VISIBLE);

                                        CardView empty = selectEmptyRecycler(dateOfWeek);
                                        empty.setVisibility(View.GONE);

                                        ConstraintLayout setting = selectBtnSetting(dateOfWeek);
                                        setting.setVisibility(View.VISIBLE);

                                        recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.VERTICAL, false));
                                        recyclerView.setAdapter(new AdapterPlanListRecipes(getActivity().getApplicationContext(), recipesList));

                                    });
                                }
                            }

                        }

                    }
                });


    }

    private ConstraintLayout selectBtnSetting(String dateOfWeek) {
        switch (dateOfWeek){
            case "Thu2":
                return binding.btnSetting2;
            case "Thu3":
                return binding.btnSetting3;
            case "Thu4":
                return binding.btnSetting4;
            case "Thu5":
                return binding.btnSetting5;
            case "Thu6":
                return binding.btnSetting6;
            case "Thu7":
                return binding.btnSetting7;
            default:
                return binding.btnSetting1;
        }
    }

    private CardView selectEmptyRecycler(String a) {
        switch (a){
            case "Thu2":
                return binding.emptyRecyclerView2;
            case "Thu3":
                return binding.emptyRecyclerView3;
            case "Thu4":
                return binding.emptyRecyclerView4;
            case "Thu5":
                return binding.emptyRecyclerView5;
            case "Thu6":
                return binding.emptyRecyclerView6;
            case "Thu7":
                return binding.emptyRecyclerView7;
            default:
                return binding.emptyRecyclerView1;
        }
    }

    private void InitUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setDateTime(binding.getRoot());
        }
        lightCurrentDate(binding.getRoot());
    }

    private void lightCurrentDate(View view) {
        Calendar cur = calendar;
        cur.setTimeInMillis(System.currentTimeMillis());
        int color = requireContext().getColor(R.color.color_primary);
        TextView date;
        switch (cur.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                date = view.findViewById(R.id.Thu2);
                date.setTextColor(color);
                break;
            case Calendar.TUESDAY:
                date = view.findViewById(R.id.Thu3);
                date.setTextColor(color);
                break;
            case Calendar.WEDNESDAY:
                date = view.findViewById(R.id.Thu4);
                date.setTextColor(color);
                break;
            case Calendar.THURSDAY:
                date = view.findViewById(R.id.Thu5);
                date.setTextColor(color);
                break;
            case Calendar.FRIDAY:
                date = view.findViewById(R.id.Thu6);
                date.setTextColor(color);
                break;
            case Calendar.SATURDAY:
                date = view.findViewById(R.id.Thu7);
                date.setTextColor(color);
                break;
            case Calendar.SUNDAY:
                date = view.findViewById(R.id.ChuNhat);
                date.setTextColor(color);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + calendar.get(Calendar.DAY_OF_WEEK));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDateTime(View view) {

        TextView datetime = view.findViewById(R.id.DateTime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }

        String date = dateFormat.format(calendar.getTime());

        calendar.add(Calendar.DATE, 6);
        String nextWeek = dateFormat.format(calendar.getTime());

        datetime.setText(date + " - " + nextWeek);
    }


    private RecyclerView SelectRecycleView(String a) {
        switch (a) {
            case "Thu2":
                return binding.recyclerViewThu2;
            case "Thu3":
                return binding.recyclerViewThu3;
            case "Thu4":
                return binding.recyclerViewThu4;
            case "Thu5":
                return binding.recyclerViewThu5;
            case "Thu6":
                return binding.recyclerViewThu6;
            case "Thu7":
                return binding.recyclerViewThu7;
            default:
                return binding.recyclerViewChuNhat;
        }
    }

}