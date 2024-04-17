package com.example.sharingrecipeapp.Fragments;

import static java.time.LocalDate.now;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.sharingrecipeapp.R;


import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class PlanFragment extends Fragment {

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

        setDateTime(view);
        lightCurrentDate(view);

    }

    private void lightCurrentDate(View view) {
        Calendar calendar = Calendar.getInstance();
        int color = requireContext().getColor(R.color.colorPrimary);
        TextView date;
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
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

        Calendar calendar = Calendar.getInstance();

        TextView datetime = view.findViewById(R.id.DateTime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY){
            calendar.add(Calendar.DATE,-1);
        }

        String date = dateFormat.format(calendar.getTime());

        calendar.add(Calendar.DATE,7);
        String nextWeek = dateFormat.format(calendar.getTime());

        datetime.setText(date +" - " + nextWeek );

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }
}