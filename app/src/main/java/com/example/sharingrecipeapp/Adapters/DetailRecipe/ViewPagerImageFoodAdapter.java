package com.example.sharingrecipeapp.Adapters.DetailRecipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.R;
import com.squareup.picasso.Picasso;

public class ViewPagerImageFoodAdapter extends RecyclerView.Adapter<ViewPagerImageFoodAdapter.ImageViewHolder> implements Filterable {

    private String imgUrl;
    private Context context;


    public ViewPagerImageFoodAdapter(Context c, String s){
        this.imgUrl = s;
        this.context = c;
    }


    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public ViewPagerImageFoodAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_recipes,parent,false);
        return new ViewPagerImageFoodAdapter.ImageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerImageFoodAdapter.ImageViewHolder holder, int position) {
        Picasso.get().load(imgUrl).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgFood);
        }
    }
}
