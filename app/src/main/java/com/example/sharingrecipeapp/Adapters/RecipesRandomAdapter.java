package com.example.sharingrecipeapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RecipesRandomAdapter extends FirestoreRecyclerAdapter<Recipes, RecipesRandomAdapter.RecipesViewHolder> {



    public RecipesRandomAdapter(@NonNull FirestoreRecyclerOptions<Recipes> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecipesViewHolder holder, int position, @NonNull Recipes recipes) {

        holder.title.setText(recipes.getName());
        holder.time.setText(recipes.getTimecook()+" phút");
        holder.save.setText(recipes.getSave());
        Glide.with(holder.img.getContext()).load(recipes.getImage()).into(holder.img);

    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipe,parent,false);
        return new RecipesViewHolder(view);
    }


    public static class RecipesViewHolder extends RecyclerView.ViewHolder{
        TextView title, time, save;
        ImageView img;


        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView_title);
            time = itemView.findViewById(R.id.textView_time);
            save = itemView.findViewById(R.id.textView_save);
            img = itemView.findViewById(R.id.imageView_food);

        }
    }
}
