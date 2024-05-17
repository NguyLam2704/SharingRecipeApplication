package com.example.sharingrecipeapp.Adapters.NguyenLieu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Classes.NguyenLieu;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.R;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.List;

public class AdapterTenNguyenLieu extends RecyclerView.Adapter<TenNguyenLieuViewHolder> {

    List<NguyenLieu> tenNL;
    public List<NguyenLieu> addNL;

    public AdapterTenNguyenLieu() {
        tenNL = new ArrayList<>();
        addNL = new ArrayList<>();
    }

    public void setData(List<NguyenLieu> list){
        this.tenNL = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TenNguyenLieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tennguyenlieu,parent,false);
        return new TenNguyenLieuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TenNguyenLieuViewHolder holder, int position) {
        NguyenLieu nguyenLieu = tenNL.get(position);
        if (nguyenLieu != null){
            holder.textView.setText(nguyenLieu.getName());
            Picasso.get().load(nguyenLieu.getImg()).into(holder.imageView);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNL.add(nguyenLieu);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tenNL.size();
    }


}
