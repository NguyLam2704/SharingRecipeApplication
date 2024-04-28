package com.example.sharingrecipeapp.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.R;

public class PlanListRecipesViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textView;

    public PlanListRecipesViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imgRecipe);
        textView = itemView.findViewById(R.id.titleRecipe);
    }
}
