package com.example.sharingrecipeapp.Adapters.AddToGrocery;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.R;

public class AddToGroViewHolder extends RecyclerView.ViewHolder {
    ImageView imgAddGro;
    TextView nameAddGro,slAddGro,dvAddGro;
    CheckBox checkAddGro;

    public AddToGroViewHolder(@NonNull View itemView) {
        super(itemView);
        imgAddGro = itemView.findViewById(R.id.imgAddGro);
        nameAddGro = itemView.findViewById(R.id.nameAddGro);
        slAddGro = itemView.findViewById(R.id.slAddGro);
        dvAddGro = itemView.findViewById(R.id.dvAddGro);
        checkAddGro = itemView.findViewById(R.id.checkAddGro);
    }
}
