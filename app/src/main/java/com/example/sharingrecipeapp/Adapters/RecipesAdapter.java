package com.example.sharingrecipeapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipesAdapter extends  RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> implements Filterable {

    private List<Recipes> mRecipes;

    private List<Recipes> mRecipesOld;

    private IClickOnItemRecipe iClickOnItemRecipe;

    public RecipesAdapter() {

    }

    public void setData(List<Recipes> list, IClickOnItemRecipe listener){
        this.mRecipes = list;
        this.mRecipesOld = list;
        this.iClickOnItemRecipe = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipe,parent,false);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        Recipes recipes = mRecipes.get(position);
        if(recipes == null) return;

        holder.title.setText(recipes.getName());
        holder.time.setText(recipes.getTimecook()+ " phút");
        holder.save.setText(recipes.getSave());
        Glide.with(holder.img.getContext()).load(recipes.getImage()).into(holder.img);

        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickOnItemRecipe.onClickItemRecipe(recipes);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mRecipes != null){
            return mRecipes.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    public static class RecipesViewHolder extends RecyclerView.ViewHolder{
        TextView title, time, save;
        ImageView img;

        MaterialCardView materialCardView;
        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView_title);
            time = itemView.findViewById(R.id.textView_time);
            save = itemView.findViewById(R.id.textView_save);
            img = itemView.findViewById(R.id.imageView_food);
            materialCardView = itemView.findViewById(R.id.random_list_container);

        }
    }
}
