package com.example.sharingrecipeapp.Adapters.Explore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Classes.NewRcpIngre;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemSearchIngreAdapter extends RecyclerView.Adapter<ItemSearchIngreAdapter.ItemSearchIngreViewHolder> {
    Context context;
    List<NewRcpIngre> IngreList;
    List<NewRcpIngre> IngreListOld;
    public ItemSearchIngreAdapter()
    {

    }

    public ItemSearchIngreAdapter(Context context, List<NewRcpIngre> ingreList, List<NewRcpIngre> ingreListOld) {
        this.context = context;
        IngreList = ingreList;
        IngreListOld = ingreListOld;
    }

    @NonNull
    @Override
    public ItemSearchIngreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_ingre,parent,false);
        return new ItemSearchIngreViewHolder(view);
    }

    public void setData(List<NewRcpIngre> Ingre)
    {
        this.IngreList = Ingre;
        this.IngreListOld = Ingre;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemSearchIngreViewHolder holder, int position) {
            NewRcpIngre newRcpIngre = IngreList.get(position);
            if(newRcpIngre != null)
            {
                holder.name_ingre.setText(newRcpIngre.getName());
                Picasso.get().load(newRcpIngre.getImg()).into(holder.icon_ingre);
                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.relativeLayout.setBackgroundResource(R.drawable.query_bound);
                        if(holder.name_ingre.isChecked())
                        {
                            holder.relativeLayout.setBackgroundResource(R.drawable.query_bound);
                            holder.name_ingre.setChecked(false);
                        }
                        else {
                            holder.relativeLayout.setBackgroundResource(R.drawable.edittext_bound);
                            holder.name_ingre.setChecked(true);

                        }
                    }
                });


            }

    }
    @Override
    public int getItemCount() {
        return IngreList.size();
    }

    public class ItemSearchIngreViewHolder extends RecyclerView.ViewHolder
    {
        CheckedTextView name_ingre ;
        ImageView icon_ingre;
        RelativeLayout relativeLayout;

        public ItemSearchIngreViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name_ingre = itemView.findViewById(R.id.name_ingre);
            this.icon_ingre = itemView.findViewById(R.id.img_ingre);
            this.relativeLayout = itemView.findViewById(R.id.layout_ingre);
        }
    }
}
