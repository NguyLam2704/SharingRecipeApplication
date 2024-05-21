package com.example.sharingrecipeapp.Adapters.NguyenLieu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharingrecipeapp.Classes.Ingredient;
import com.example.sharingrecipeapp.Classes.NewRcpIngre;
import com.example.sharingrecipeapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewRcpIngreAdapter extends BaseAdapter {
    Context contex;
    int mylayout;
    List<NewRcpIngre> IngreList;



    public NewRcpIngreAdapter(Context contex, int layout, List<NewRcpIngre> ingreList) {
        this.contex = contex;
        mylayout = layout;
        IngreList = ingreList;
    }

    @Override
    public int getCount() {
        return IngreList.size() ;
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
        LayoutInflater inflater = (LayoutInflater) contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(mylayout,null);
        NewRcpIngre newRcpIngre = IngreList.get(position);
        TextView name_ingre = (TextView) convertView.findViewById(R.id.NewRcp_name_ingre);
        ImageView img_ingre = (ImageView) convertView.findViewById(R.id.img_ingre);
        Picasso.get().load(newRcpIngre.getImg()).into(img_ingre);
        name_ingre.setText(newRcpIngre.getName());
        if (newRcpIngre.getId().equals("add"))
        {
            img_ingre.setImageResource(R.drawable.baseline_add);
        }
        return convertView;
    }
}
