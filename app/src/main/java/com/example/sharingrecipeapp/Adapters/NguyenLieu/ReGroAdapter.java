package com.example.sharingrecipeapp.Adapters.NguyenLieu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Classes.ReGro;
import com.example.sharingrecipeapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReGroAdapter extends RecyclerView.Adapter<ReGroAdapter.ReGroViewHolder> {
    private List<ReGro> regroList;
    private List<ReGro> regroListOld;
    IClickOnItemSavedRecipe iClickOnItemRecipe;

    public ReGroAdapter() {
    }

    public void setData(List<ReGro> list, IClickOnItemSavedRecipe listener ){
        this.regroList = list;
        this.regroListOld = list;
        this.iClickOnItemRecipe = listener;
        notifyDataSetChanged();
    }

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
        Picasso.get().load(regro.getImgReGr()).into(holder.imgRe);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickOnItemRecipe.onClickItemSavedRecipe(regro.getId());
            }
        });
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
        ConstraintLayout constraintLayout;
        public ReGroViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.SavedRecipeLayout);
            nameRe= itemView.findViewById(R.id.txtGroRe);
            imgRe=itemView.findViewById(R.id.imgGrRe);

        }
    }
}