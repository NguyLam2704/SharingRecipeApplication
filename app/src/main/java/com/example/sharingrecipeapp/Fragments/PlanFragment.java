package com.example.sharingrecipeapp.Fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Activities.BottomNavigationCustomActivity;
import com.example.sharingrecipeapp.Activities.PlantoSavedActivity;
import com.example.sharingrecipeapp.Adapters.PlanList.AdapterPlanListRecipes;
import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.R;
import com.example.sharingrecipeapp.databinding.FragmentPlanBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;


public class PlanFragment extends Fragment {
    static int RECIPE_HEIGHT = 145;//272;
    static int PADDING_RECIPE_HEIGHT = 130;//272;
    private FragmentPlanBinding binding;
    private Calendar calendar;
    FirebaseFirestore db;
    FirebaseAuth auth;

    private BottomNavigationCustomActivity bottomNavigationCustomActivity;

    RecyclerView recyclerView;

    int curWeekOfYear;



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

        doAddBtn(t2,"Thu2");
        doAddBtn(t3,"Thu3");
        doAddBtn(t4,"Thu4");
        doAddBtn(t5,"Thu5");
        doAddBtn(t6,"Thu6");
        doAddBtn(t7,"Thu7");
        doAddBtn(CN,"ChuNhat");

        String weekID = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
        PlanOfDay(weekID);

        prev = binding.prevWeek;
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.WEEK_OF_YEAR, -1);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setDateTime();
                }
                LightOffCurDate(binding.getRoot());

                String weekID = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));

                resetList();

                PlanOfDay(weekID);
            }
        });

        next = binding.nextWeek;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.WEEK_OF_YEAR, 1);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setDateTime();
                }
                LightOffCurDate(binding.getRoot());
                String weekID = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));

                resetList();
                PlanOfDay(weekID);
            }
        });

    }

    public void doAddBtn(View view, String dateAdd) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weekID = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)) ;
                Intent intent = new Intent(binding.getRoot().getContext(), PlantoSavedActivity.class);
                intent.putExtra("date",dateAdd);
                intent.putExtra("weekID",weekID);
                intent.putExtra("weekOfYear",calendar.get(Calendar.WEEK_OF_YEAR)    );

                activityResultLauncher.launch(intent);

            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher =  registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result ->{
        if (result.getResultCode() == 123){
//            new Handler().postDelayed(new Runnable() {
//                public void run() {


                calendar.set(Calendar.WEEK_OF_YEAR,result.getData().getExtras().getInt("weekOfYear"));
                String id = result.getData().getStringExtra("id");
                String date = result.getData().getStringExtra("date");
                String name = result.getData().getStringExtra("name");
                String img = result.getData().getStringExtra("img");
                Recipes recipes = new Recipes(id,name,img);
                recyclerView = selectRecycleView(date);
                List<Recipes> recipesList = selectListRecipes(date);
                AdapterPlanListRecipes myAdapter = new AdapterPlanListRecipes();

                boolean biTrung = false;
                if (!recipesList.isEmpty()){
                    for (Recipes x : recipesList){
                        if (x.getId().equals(recipes.getId())){
                            StyleableToast.makeText(binding.getRoot().getContext(),"Món ăn đã có trong kế hoạch",R.style.mytoast).show();
                            biTrung = true;
                            break;
                        }else{
                            StyleableToast.makeText(binding.getRoot().getContext(),"Thêm món ăn thành công",R.style.mytoast).show();
                        }
                    }
                }
                else {
                    myAdapter.setData(recipesList, new IClickOnItemRecipe() {

                    @Override
                    public void onClickItemRecipe(Recipes recipes) {
                        onClickGoToDetailFood(recipes);
                    }
                });
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        int position = viewHolder.getLayoutPosition();
                        String ID = recipesList.get(position).getId();
                        String name = recipesList.get(position).getName();
                        String week = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));

                        StyleableToast.makeText(binding.getRoot().getContext(), "Đã xóa món " + name, R.style.mytoast).show();
                        db.collection("PlanList").document(auth.getUid()).collection(week).document(date).update("recipes", FieldValue.arrayRemove(ID));

                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                recipesList.remove(position);
                                viewHolder.getBindingAdapter().notifyDataSetChanged();
                                if (!recipesList.isEmpty()){
                                    selectRecycleView(date).setMinimumHeight(selectRecycleView(date).getHeight() - RECIPE_HEIGHT - PADDING_RECIPE_HEIGHT);
                                }
                                if (recipesList.isEmpty()){
                                    selectRecycleView(date).setVisibility(View.GONE);

//                                    ConstraintLayout setting = selectBtnSetting(date);
//                                    setting.setVisibility(View.GONE);
                                }
                            }
                        }, 200);
                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.VERTICAL, false));

                itemTouchHelper.attachToRecyclerView(recyclerView);
                recyclerView.setAdapter(myAdapter);
                recyclerView.setVisibility(View.VISIBLE);
//                selectBtnSetting(date).setVisibility(View.VISIBLE);
            }

            if (!biTrung){
                recipesList.add(recipes);
                selectRecycleView(date).setMinimumHeight(RECIPE_HEIGHT + selectListRecipes(date).size() * RECIPE_HEIGHT );
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    });






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
        calendar.setTimeInMillis(System.currentTimeMillis());
        curWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        bottomNavigationCustomActivity = (BottomNavigationCustomActivity) getActivity();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        InitUI();

        return binding.getRoot();
    }



    private void LightOffCurDate(View view) {

        if (curWeekOfYear != calendar.get(Calendar.WEEK_OF_YEAR)) {
            TextView date;
            int color = requireContext().getColor(R.color.black);
            switch (calendar.get(Calendar.DAY_OF_WEEK)) {
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
            switch (calendar.get(Calendar.DAY_OF_WEEK)) {
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

    int numberSize;

    private void PlanOfDay(String weekID){
        turnOffRecyclerView();

        List<String> date = new ArrayList<>();
        date.add("Thu2");
        date.add("Thu3");
        date.add("Thu4");
        date.add("Thu5");
        date.add("Thu6");
        date.add("Thu7");
        date.add("ChuNhat");

        for (String i : date){
            db.collection("PlanList").document(auth.getUid()).collection(weekID).document(i).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.get("recipes") != null){
                                List<String> listFromDB = (List<String>) documentSnapshot.get("recipes");
                                numberSize = listFromDB.size();
//                                if (i.equals("Thu2")){
//                                    Toast.makeText(binding.getRoot().getContext(),String.valueOf(listFromDB.size()),Toast.LENGTH_SHORT).show();
//                                }

                                //
                                if (!listFromDB.isEmpty()){
                                    displayDay(listFromDB,i);
                                }
                            }

                        }
                    });
        }
    }
    List<Recipes> monAnThu2 = new ArrayList<>();
    List<Recipes> monAnThu3 = new ArrayList<>();
    List<Recipes> monAnThu4 = new ArrayList<>();
    List<Recipes> monAnThu5 = new ArrayList<>();
    List<Recipes> monAnThu6 = new ArrayList<>();
    List<Recipes> monAnThu7 = new ArrayList<>();
    List<Recipes> monAnChuNhat = new ArrayList<>();

    private void resetList(){
        monAnThu2.clear();
        monAnThu3.clear();
        monAnThu4.clear();
        monAnThu5.clear();
        monAnThu6.clear();
        monAnThu7.clear();
        monAnChuNhat.clear();
    }


    private void setDataList(List<Recipes > a, List <Recipes> b){
        a.clear();
        a.addAll(b);
    }


    public List<Recipes> selectListRecipes(String date){
        switch (date){
            case "Thu2":
                return monAnThu2;
            case "Thu3":
                return monAnThu3;
            case "Thu4":
                return monAnThu4;
            case "Thu5":
                return monAnThu5;
            case "Thu6":
                return monAnThu6;
            case "Thu7":
                return monAnThu7;
            default:
                return monAnChuNhat;

        }
    }

    private void addRe( List<String> list ,String date){

        List<Recipes> recipesList = selectListRecipes(date);

        for (String id : list){
            db.collection("Recipes").document(id).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        String image = documentSnapshot.getString("image");
                        String name = documentSnapshot.getString("name");
                        String save = documentSnapshot.get("save").toString();
                        String time = documentSnapshot.get("timecook").toString();

                        Recipes recipes = new Recipes(id, image, name, save, time);
                        recipesList.add(recipes);

                        selectRecycleView(date).getAdapter().notifyDataSetChanged();

                        if (id.equals(list.get(list.size()-1))){
                            selectRecycleView(date).setMinimumHeight(RECIPE_HEIGHT * numberSize);
                        }


                    });
        }



    }

    private void displayDay(List<String> tenRecipe, String date) {

        List<Recipes> recipesList = selectListRecipes(date);

        recyclerView = selectRecycleView(date);
        recyclerView.setVisibility(View.VISIBLE);


//        ConstraintLayout setting = selectBtnSetting(date);
//        setting.setVisibility(View.VISIBLE);

        AdapterPlanListRecipes myAdapter = new AdapterPlanListRecipes();

        myAdapter.setData(recipesList, new IClickOnItemRecipe() {
            @Override
            public void onClickItemRecipe(Recipes recipes) {
                onClickGoToDetailFood(recipes);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();

                String ID = recipesList.get(position).getId();
                String week = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
                String name = recipesList.get(position).getName();
                StyleableToast.makeText(binding.getRoot().getContext(), "Đã xóa món " + name,R.style.mytoast).show();
                db.collection("PlanList").document(auth.getUid()).collection(week).document(date).update("recipes", FieldValue.arrayRemove(ID));

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        recipesList.remove(position);
                        viewHolder.getBindingAdapter().notifyDataSetChanged();
                        if (!recipesList.isEmpty()){
                            selectRecycleView(date).setMinimumHeight(selectRecycleView(date).getHeight() - RECIPE_HEIGHT - PADDING_RECIPE_HEIGHT);

                        }
                        if (recipesList.isEmpty()){
                            //ConstraintLayout setting = selectBtnSetting(date);
                            selectRecycleView(date).setVisibility(View.GONE);
//                            setting.setVisibility(View.GONE);
                        }
                    }
                }, 200); // 5 seconds

            }
        });

        LinearLayoutManager linearLayout = new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayout);

        itemTouchHelper.attachToRecyclerView(recyclerView);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),0);
        // linearLayout.getOrientation());

//        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(myAdapter);
        recyclerView.setNestedScrollingEnabled(true);

        addRe(tenRecipe,date);
    }


    private void turnOffRecyclerView() {
        List<String> date = new ArrayList<>();
        date.add("Thu2");
        date.add("Thu3");
        date.add("Thu4");
        date.add("Thu5");
        date.add("Thu6");
        date.add("Thu7");
        date.add("ChuNhat");

        for (String dateOfWeek : date){
//            ConstraintLayout setting = selectBtnSetting(dateOfWeek);
//            setting.setVisibility(View.GONE);

            recyclerView = selectRecycleView(dateOfWeek);
            recyclerView.setVisibility(View.GONE);
        }
    }


    private void onClickGoToDetailFood(Recipes recipes){
        bottomNavigationCustomActivity.gotoFoodDetail(recipes);
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



    private void InitUI() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setDateTime();
        }
        lightCurrentDate(binding.getRoot());
    }

    private void lightCurrentDate(View view) {

        Calendar cur = Calendar.getInstance();

        cur.setTime(calendar.getTime());
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
    private void setDateTime() {

        TextView datetime = binding.DateTime;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

        Calendar cur = Calendar.getInstance();
        cur.setTimeInMillis(calendar.getTimeInMillis());

        while (cur.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cur.add(Calendar.DATE, -1);
        }

        String date = dateFormat.format(cur.getTime());

        cur.add(Calendar.DATE, 6);
        String nextWeek = dateFormat.format(cur.getTime());

        datetime.setText(date + " - " + nextWeek);
    }


    private RecyclerView selectRecycleView(String a) {
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