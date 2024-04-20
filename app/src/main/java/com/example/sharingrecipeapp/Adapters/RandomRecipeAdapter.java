package com.example.sharingrecipeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Models.Recipe;
import com.example.sharingrecipeapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RandomRecipeAdapter extends RecyclerView.Adapter<RandomeRecipeViewHolder> {
    Context context;
    List<Recipe> list;

    public RandomRecipeAdapter(Context context, java.util.List<Recipe> list) {
        this.context = context;
        list = list;
    }

    @NonNull
    @Override
    public RandomeRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RandomeRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RandomeRecipeViewHolder holder, int position) {
        holder.textView_title.setText(list.get(position).title);
        holder.textView_title.setSelected(true);
        holder.textView_like.setText(list.get(position).aggregateLikes);
        holder.textView_time.setText(list.get(position).readyInMinutes+" minutes");
        Picasso.get().load(list.get(position).image).into(holder.imageView_food);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class RandomeRecipeViewHolder extends RecyclerView.ViewHolder{
    CardView random_list_container;
    TextView textView_title, textView_time,textView_like;
    ImageView imageView_food;
    public RandomeRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        random_list_container = itemView.findViewById(R.id.random_list_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_time = itemView.findViewById(R.id.textView_time);
        textView_like = itemView.findViewById(R.id.textView_like);
        imageView_food = itemView.findViewById(R.id.imageView_food);
    }
}