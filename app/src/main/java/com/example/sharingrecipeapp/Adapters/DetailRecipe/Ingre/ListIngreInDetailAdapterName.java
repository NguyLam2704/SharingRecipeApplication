package com.example.sharingrecipeapp.Adapters.DetailRecipe.Ingre;

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

import com.example.sharingrecipeapp.Activities.FoodDetailActivity;
import com.example.sharingrecipeapp.Classes.Ingredient;
import com.example.sharingrecipeapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListIngreInDetailAdapterName extends RecyclerView.Adapter<ListIngreInDetailAdapterName.IngreViewHolder> implements Filterable {

    List<Ingredient> ingredientList;
    List<Ingredient> ingredientListOld;

    FoodDetailActivity foodDetailActivity;


    Context context;

    public void setData(List<Ingredient> list, FoodDetailActivity ac){
        this.ingredientList = list;
        this.ingredientListOld=list;
        this.foodDetailActivity = ac;
        notifyDataSetChanged();
    }

    public ListIngreInDetailAdapterName(){

    }
    public ListIngreInDetailAdapterName(Context context, List<Ingredient> list){
        this.context = context;
        this.ingredientList = list;

    }


    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public ListIngreInDetailAdapterName.IngreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ingre_name,parent,false);
        return new ListIngreInDetailAdapterName.IngreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngreViewHolder holder, int position) {
        if (getItemCount() != 0) {
            holder.nameIngre.setText(ingredientList.get(position).getName());
            Picasso.get().load(ingredientList.get(position).getImg()).into(holder.icon);
        }
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public class IngreViewHolder extends RecyclerView.ViewHolder {
        TextView nameIngre;
        ImageView icon;

        public IngreViewHolder(@NonNull View itemView) {
            super(itemView);
            nameIngre = itemView.findViewById(R.id.name_Ingre);
            icon = itemView.findViewById(R.id.icon_ingre);
        }
    }
}
