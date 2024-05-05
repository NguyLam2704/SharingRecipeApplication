package com.example.sharingrecipeapp.Adapters.DetailRecipe.Ingre;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Activities.FoodDetailActivity;
import com.example.sharingrecipeapp.Classes.Ingredient;
import com.example.sharingrecipeapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListIngreInDetailAdapterDonVi extends RecyclerView.Adapter<ListIngreInDetailAdapterDonVi.DonViViewHolder> implements Filterable {

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

    public ListIngreInDetailAdapterDonVi(){

    }



    public ListIngreInDetailAdapterDonVi(Context context, List<Ingredient> list){
        this.context = context;
        this.ingredientList = list;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public ListIngreInDetailAdapterDonVi.DonViViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ingre_donvi,parent,false);
        return new ListIngreInDetailAdapterDonVi.DonViViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListIngreInDetailAdapterDonVi.DonViViewHolder holder, int position) {
        if (getItemCount() != 0) {
            holder.donvi.setText(ingredientList.get(position).getDonvi());
        }

    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public class DonViViewHolder extends RecyclerView.ViewHolder {
        TextView donvi;
        public DonViViewHolder(@NonNull View itemView) {
            super(itemView);
            donvi = itemView.findViewById(R.id.donvi_Ingre);
        }

        public void setDonvi(TextView donvi) {
            this.donvi = donvi;

        }
    }
}
