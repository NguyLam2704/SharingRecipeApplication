package com.example.sharingrecipeapp.Adapters.PlanList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPlanListRecipes extends RecyclerView.Adapter<PlanListRecipesViewHolder> {
    Context context;

    List<Recipes> recipesList;
    List<Recipes> recipesListOld;

    IClickOnItemRecipe iClickOnItemRecipe;

    public AdapterPlanListRecipes() {
    }

    public AdapterPlanListRecipes(Context context, List<Recipes> recipesList) {
        this.context = context;
        this.recipesList = recipesList;
    }

    @NonNull
    @Override
    public PlanListRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_list_recipe,parent,false);
        return new PlanListRecipesViewHolder(view); //new PlanListRecipesViewHolder(LayoutInflater.from(context).inflate(R.layout.plan_list_recipe,parent,false));
    }

    public void setData(List<Recipes> list, IClickOnItemRecipe listener){
        this.recipesList = list;
        this.recipesListOld = list;
        this.iClickOnItemRecipe = listener;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull PlanListRecipesViewHolder holder, int position) {

        Recipes recipes = recipesList.get(position);

        if (recipes != null){
            holder.textView.setText(recipes.getName());
            Picasso.get().load(recipes.getImage()).into(holder.imageView );
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iClickOnItemRecipe.onClickItemRecipe(recipes);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }
}

