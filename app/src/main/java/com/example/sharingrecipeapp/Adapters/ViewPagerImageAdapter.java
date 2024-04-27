package com.example.sharingrecipeapp.Adapters;

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

public class ViewPagerImageAdapter extends RecyclerView.Adapter<ViewPagerImageAdapter.ImageViewHolder> implements Filterable {

    private String imgUrl;
    private Context context;


    public ViewPagerImageAdapter(Context c, String s){
        this.imgUrl = s;
        this.context = c;
    }


    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public ViewPagerImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_recipes,parent,false);
        return new ViewPagerImageAdapter.ImageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerImageAdapter.ImageViewHolder holder, int position) {
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
