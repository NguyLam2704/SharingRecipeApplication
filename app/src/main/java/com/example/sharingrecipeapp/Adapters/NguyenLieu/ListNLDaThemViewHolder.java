package com.example.sharingrecipeapp.Adapters.NguyenLieu;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.R;

public class ListNLDaThemViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView name, donvi;
    EditText editText;
    CheckBox checkBox;
    public ListNLDaThemViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imgin);
        name = itemView.findViewById(R.id.namein);
        editText = itemView.findViewById(R.id.quantityin);
        donvi = itemView.findViewById(R.id.donvi_dathem);
        checkBox = itemView.findViewById(R.id.checkIn);
    }
}
