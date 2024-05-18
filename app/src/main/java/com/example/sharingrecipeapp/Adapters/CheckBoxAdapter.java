package com.example.sharingrecipeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.CheckBoxListener;
import com.example.sharingrecipeapp.Classes.Ingredient;
import com.example.sharingrecipeapp.Classes.SoLuongIngre;
import com.example.sharingrecipeapp.R;

import java.util.ArrayList;

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.MyViewHolder> implements Filterable {

    Context context;

    CheckBoxListener checkBoxListener;
    ArrayList<Ingredient> ingredients;
    ArrayList<Ingredient> ingredientsAdd = new ArrayList<>();
    ArrayList<SoLuongIngre> soLuongIngres;
    ArrayList<SoLuongIngre> soLuongIngresAdd = new ArrayList<>();

    public CheckBoxAdapter(Context context, ArrayList<Ingredient> ingredients, ArrayList<SoLuongIngre> soLuongIngres, CheckBoxListener checkBoxListener ) {
        this.context = context;
        this.checkBoxListener = checkBoxListener;
        this.ingredients = ingredients;
        this.soLuongIngres = soLuongIngres;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public CheckBoxAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkbox, parent, false);
        return new CheckBoxAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckBoxAdapter.MyViewHolder holder, int position) {

        if (getItemCount() != 0){
            holder.check_Box.setText("");
            if (holder.check_Box.isChecked()){
                ingredientsAdd.add(ingredients.get(position));
                soLuongIngresAdd.add(soLuongIngres.get(position));
            } else {
                ingredientsAdd.remove(ingredients.get(position));
                soLuongIngresAdd.remove(soLuongIngres.get(position));
            }
            checkBoxListener.onCheckBoxChange(ingredientsAdd,soLuongIngresAdd);
        }
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox check_Box;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            check_Box = itemView.findViewById(R.id.checkBoxAdd);
        }
    }
}
