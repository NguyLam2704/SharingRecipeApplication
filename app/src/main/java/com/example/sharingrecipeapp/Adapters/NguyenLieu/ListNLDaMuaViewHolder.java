package com.example.sharingrecipeapp.Adapters.NguyenLieu;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.R;

public class ListNLDaMuaViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView name, donvi;
    EditText editText;
    CheckBox checkBox;
    public ListNLDaMuaViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.img_damua);
        name = itemView.findViewById(R.id.name_damua);
        editText = itemView.findViewById(R.id.SL_damua);
        donvi = itemView.findViewById(R.id.donvi_damua);
        checkBox = itemView.findViewById(R.id.checkIn_damua);
    }
}
