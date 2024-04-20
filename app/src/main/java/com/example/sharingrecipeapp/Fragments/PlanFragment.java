package com.example.sharingrecipeapp.Fragments;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.sharingrecipeapp.R;
import com.example.sharingrecipeapp.databinding.FragmentPlanBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class PlanFragment extends Fragment {
    private  FragmentPlanBinding binding;
    private Calendar calendar;

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
        binding = FragmentPlanBinding.inflate(inflater,container,false);

        InitUI();

        prev = binding.prevWeek;
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE,-7);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setDateTime(binding.DateTime);
                }
            }
        });

        next = binding.nextWeek;

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE,7);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setDateTime(binding.DateTime);
                }
            }
        });

        return binding.getRoot();
    }

    private void InitUI() {
        calendar = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setDateTime(binding.getRoot());
        }
        lightCurrentDate(binding.getRoot());
    }


    private void lightCurrentDate(View view) {
        Calendar cur = calendar;
        cur.add(Calendar.HOUR,-24);
        int color = requireContext().getColor(R.color.color_primary);
        TextView date;
        switch (cur.get(Calendar.DAY_OF_WEEK)){
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

        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY){
            calendar.add(Calendar.DATE,-1);
        }

        String date = dateFormat.format(calendar.getTime());

        calendar.add(Calendar.DATE,6);
        String nextWeek = dateFormat.format(calendar.getTime());

        datetime.setText(date +" - " + nextWeek );
    }

    public void setCalendar(Calendar cal){
        this.calendar = cal;
    }

    public Calendar getCalendar(){
        return calendar;
    }

}

