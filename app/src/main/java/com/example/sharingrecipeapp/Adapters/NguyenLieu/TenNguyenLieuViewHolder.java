package com.example.sharingrecipeapp.Adapters.NguyenLieu;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.R;

public class TenNguyenLieuViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        View view;
        public TenNguyenLieuViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tenNL);
            imageView = itemView.findViewById(R.id.imgNL);
            view = itemView.findViewById(R.id.addNL);
        }

}

