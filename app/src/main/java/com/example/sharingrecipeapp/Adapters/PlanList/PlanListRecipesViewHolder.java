package com.example.sharingrecipeapp.Adapters.PlanList;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.R;

public class PlanListRecipesViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textView;
    ConstraintLayout constraintLayout;

    public PlanListRecipesViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imgRecipe);
        textView = itemView.findViewById(R.id.titleRecipe);
        constraintLayout = itemView.findViewById(R.id.plan_recipe);
    }

}
