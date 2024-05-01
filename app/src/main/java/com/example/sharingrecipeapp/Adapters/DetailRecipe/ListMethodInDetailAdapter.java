package com.example.sharingrecipeapp.Adapters.DetailRecipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Classes.Ingredient;
import com.example.sharingrecipeapp.Classes.Method;
import com.example.sharingrecipeapp.R;

import java.util.List;

public class ListMethodInDetailAdapter extends RecyclerView.Adapter<ListMethodInDetailAdapter.MethodViewHolder> implements Filterable {

    List<Method> methodList;

    Context context;

    public ListMethodInDetailAdapter(Context context, List<Method> list){
        this.context = context;
        this.methodList = list;

    }
    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public ListMethodInDetailAdapter.MethodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_method,parent,false);
        return new ListMethodInDetailAdapter.MethodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListMethodInDetailAdapter.MethodViewHolder holder, int position) {
        if(getItemCount() != 0){
            holder.method.setText(methodList.get(position).getStep());
        }

    }

    @Override
    public int getItemCount() {
        return methodList.size();
    }

    public class MethodViewHolder extends RecyclerView.ViewHolder {
        TextView method;
        public MethodViewHolder(@NonNull View itemView) {
            super(itemView);
            method = itemView.findViewById(R.id.method_text);
        }
    }
}
