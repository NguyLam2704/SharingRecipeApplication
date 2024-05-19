package com.example.sharingrecipeapp.Adapters.AddRecipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Activities.CreateRecipeActivity;
import com.example.sharingrecipeapp.Classes.AddMethod;
import com.example.sharingrecipeapp.R;

import java.util.List;

public class AddMethodAdapter extends RecyclerView.Adapter<AddMethodAdapter.MyViewHolder> implements Filterable {

    List<AddMethod> methodList;

    List<AddMethod> methodListOld;



    public void setData(List<AddMethod> list){
        this.methodList = list;
        this.methodListOld = list;
        notifyDataSetChanged();
    }

    public AddMethodAdapter(){}
    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public AddMethodAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_method,parent,false);
        return new AddMethodAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AddMethodAdapter.MyViewHolder holder, int position) {
        if (getItemCount() != 0){
           holder.name.setText(methodList.get(position).getMethod());
           holder.sttBuoc.setText("Bước " + (position+1) +": ");
        }

    }

    @Override
    public int getItemCount() {
        return methodList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, sttBuoc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sttBuoc = itemView.findViewById(R.id.sttBuoc);
            name = itemView.findViewById(R.id.method);

        }
    }
}
