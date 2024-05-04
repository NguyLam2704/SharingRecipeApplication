package com.example.sharingrecipeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Classes.ListIngredient;
import com.example.sharingrecipeapp.Classes.ReGro;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.R;

import java.util.List;

public class ReGroAdapter extends RecyclerView.Adapter<ReGroAdapter.ReGroViewHolder> {
    private List<ReGro> regroList;
    public ReGroAdapter(List<ReGro> regroList) {
        this.regroList = regroList;
    }



    @NonNull

    @Override
    public ReGroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.groceries_recipe, parent, false);

        return new ReGroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReGroViewHolder holder, int position) {
        ReGro regro= regroList.get(position);
        if (regro == null){
            return ;
        }
        holder.nameRe.setText(regro.getNameReGr());
        holder.imgRe.setImageResource(regro.getImgReGr());
    }

    @Override
    public int getItemCount() {
        if(regroList!=null){
            return regroList.size();
        }
        return 0;
    }

    class ReGroViewHolder extends RecyclerView.ViewHolder{
        private TextView nameRe;
        private ImageView imgRe;
        public ReGroViewHolder(@NonNull View itemView) {
            super(itemView);
            nameRe= itemView.findViewById(R.id.txtGroRe);
            imgRe=itemView.findViewById(R.id.imgGrRe);

        }
    }
}