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
import com.example.sharingrecipeapp.Classes.Method;
import com.example.sharingrecipeapp.Classes.SoLuongIngre;
import com.example.sharingrecipeapp.R;

import java.util.List;

public class ListIngreInDetailAdapterSoLuong extends RecyclerView.Adapter<ListIngreInDetailAdapterSoLuong.SoLuongViewHolder> implements Filterable {

    List<SoLuongIngre> soLuongIngreList;
    List<SoLuongIngre> soLuongIngreListOld;

    FoodDetailActivity foodDetailActivity;
    Context context;

    public ListIngreInDetailAdapterSoLuong(Context context, List<SoLuongIngre> list){
        this.context = context;
        this.soLuongIngreList = list;

    }

    public void setData(List<SoLuongIngre> list, FoodDetailActivity ac){
        this.soLuongIngreList = list;
        this.soLuongIngreList=list;
        this.foodDetailActivity = ac;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListIngreInDetailAdapterSoLuong.SoLuongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ingre_soluong,parent,false);
        return new ListIngreInDetailAdapterSoLuong.SoLuongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListIngreInDetailAdapterSoLuong.SoLuongViewHolder holder, int position) {
        if(getItemCount() != 0){
            holder.soluong.setText(soLuongIngreList.get(position).getSoluong());
        }
    }

    @Override
    public int getItemCount() {
        return soLuongIngreList.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class SoLuongViewHolder extends RecyclerView.ViewHolder {
        TextView soluong;
        public SoLuongViewHolder(@NonNull View itemView) {
            super(itemView);
            soluong = itemView.findViewById(R.id.soluong_Ingre);
        }
    }
}
