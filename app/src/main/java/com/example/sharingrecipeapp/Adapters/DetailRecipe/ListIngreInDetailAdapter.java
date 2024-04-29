package com.example.sharingrecipeapp.Adapters.DetailRecipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Classes.Ingredient;
import com.example.sharingrecipeapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListIngreInDetailAdapter extends RecyclerView.Adapter<ListIngreInDetailAdapter.IngreViewHolder> implements Filterable {

    List<Ingredient> ingredientList;


    Context context;

    public ListIngreInDetailAdapter(Context context, List<Ingredient> list){
        this.context = context;
        this.ingredientList = list;

    }

    public void setData(List<Ingredient> ingreList){

        this.ingredientList = ingreList;
        notifyDataSetChanged();
    }
    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public ListIngreInDetailAdapter.IngreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ingre,parent,false);
        return new ListIngreInDetailAdapter.IngreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngreViewHolder holder, int position) {
        if (getItemCount() != 0) {
            holder.nameIngre.setText(ingredientList.get(position).getName());
            holder.donvi.setText(ingredientList.get(position).getDonvi());
            holder.soluong.setText(ingredientList.get(position).getSL());
            Picasso.get().load(ingredientList.get(position).getImg()).into(holder.icon);
        }
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public class IngreViewHolder extends RecyclerView.ViewHolder {
        TextView nameIngre, donvi, soluong;
        ImageView icon;

        public IngreViewHolder(@NonNull View itemView) {
            super(itemView);
            nameIngre = itemView.findViewById(R.id.name_Ingre);
            donvi = itemView.findViewById(R.id.DonViText);
            soluong = itemView.findViewById(R.id.SoLuongText);
            icon = itemView.findViewById(R.id.icon_ingre);
        }
    }
}
