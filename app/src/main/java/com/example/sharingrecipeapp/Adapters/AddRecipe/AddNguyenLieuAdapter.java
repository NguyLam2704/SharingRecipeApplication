package com.example.sharingrecipeapp.Adapters.AddRecipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Classes.AddNguyenLieu;
import com.example.sharingrecipeapp.R;

import java.util.List;

public class AddNguyenLieuAdapter extends RecyclerView.Adapter<AddNguyenLieuAdapter.MyViewHolder> implements Filterable {

    List<AddNguyenLieu> addNguyenLieuList;

    List<AddNguyenLieu> addNguyenLieuListOld;

    public void setData(List<AddNguyenLieu> list){
        this.addNguyenLieuList = list;
        this.addNguyenLieuListOld = list;
        notifyDataSetChanged();
    }
    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public AddNguyenLieuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_nguyenlieu,parent,false);
        return new AddNguyenLieuAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddNguyenLieuAdapter.MyViewHolder holder, int position) {
        if (getItemCount() != 0){
            holder.name.setText(addNguyenLieuList.get(position).getNewRcpIngre().getName());
            holder.sl.setText(addNguyenLieuList.get(position).getSoluong().toString());
            holder.donvi.setText(addNguyenLieuList.get(position).getNewRcpIngre().getDv());
        }

    }

    @Override
    public int getItemCount() {
        return addNguyenLieuList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, sl, donvi;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sothutu);
            name = itemView.findViewById(R.id.name);
            sl = itemView.findViewById(R.id.sl);
            donvi = itemView.findViewById(R.id.donvi);
        }
    }
}
