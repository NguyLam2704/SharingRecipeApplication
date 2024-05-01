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

public class ViewPagerImagerAvtAdapter extends RecyclerView.Adapter<ViewPagerImagerAvtAdapter.AvtViewHolder> implements Filterable {

    private String imgage;
    private Context context;

    public ViewPagerImagerAvtAdapter(Context c, String s){
        this.imgage = s;
        this.context = c;
    }
    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public ViewPagerImagerAvtAdapter.AvtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avt_detail,parent,false);
        return new ViewPagerImagerAvtAdapter.AvtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerImagerAvtAdapter.AvtViewHolder holder, int position) {
        Picasso.get().load(imgage).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class AvtViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public AvtViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.avt_user_detail);
        }
    }
}
