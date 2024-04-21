package com.example.sharingrecipeapp.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharingrecipeapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ListInAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<ListIngredient> ingList;

    public ListInAdapter(Context context, int layout, List<ListIngredient> ingList) {
        this.context = context;
        this.layout = layout;
        this.ingList = ingList;
    }

    @Override
    public int getCount() {
        return ingList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);
        // anh xa view
        TextView txtName = (TextView) convertView.findViewById(R.id.namein);
        TextView txtQuant= (TextView) convertView.findViewById(R.id.quantityin);
        ImageView imgIn= (ImageView) convertView.findViewById(R.id.imgin);
        CheckBox cbIn = (CheckBox) convertView.findViewById(R.id.checkIn);
        //Gan gia tri
        ListIngredient ingredient = ingList.get(position);
        txtName.setText(ingredient.getNameIn());
        txtQuant.setText(ingredient.getQuantityIn());
        imgIn.setImageResource(ingredient.getImageIn());
        cbIn.setChecked(ingredient.isCheck());
        return convertView;
    }
}
