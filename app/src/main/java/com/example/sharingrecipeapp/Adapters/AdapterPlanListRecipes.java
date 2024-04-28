package com.example.sharingrecipeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Models.Recipes;
import com.example.sharingrecipeapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPlanListRecipes extends RecyclerView.Adapter<PlanListRecipesViewHolder> {
    Context context;

    List<Recipes> recipesList;

    public AdapterPlanListRecipes(Context context, List<Recipes> recipesList) {
        this.context = context;
        this.recipesList = recipesList;
    }

    @NonNull
    @Override
    public PlanListRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlanListRecipesViewHolder(LayoutInflater.from(context).inflate(R.layout.plan_list_recipe,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlanListRecipesViewHolder holder, int position) {
        if (getItemCount() != 0){
            holder.textView.setText(recipesList.get(position).getName());
            Picasso.get().load(recipesList.get(position).getImg()).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }
}
