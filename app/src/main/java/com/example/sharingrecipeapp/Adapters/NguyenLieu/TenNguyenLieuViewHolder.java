package com.example.sharingrecipeapp.Adapters.NguyenLieu;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.R;

public class TenNguyenLieuViewHolder extends RecyclerView.ViewHolder {
        TextView tenNL, donvi;
        EditText editText;
        ImageView imageView;
        View view;
        public TenNguyenLieuViewHolder(@NonNull View itemView) {
            super(itemView);
            tenNL = itemView.findViewById(R.id.tenNL);
            imageView = itemView.findViewById(R.id.imgNL);
            view = itemView.findViewById(R.id.addNL);
            editText = itemView.findViewById(R.id.editTextNumber);
            donvi = itemView.findViewById(R.id.donvi);
        }

}

