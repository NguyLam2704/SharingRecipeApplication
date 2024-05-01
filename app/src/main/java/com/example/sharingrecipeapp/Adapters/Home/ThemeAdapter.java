package com.example.sharingrecipeapp.Adapters.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sharingrecipeapp.Classes.Theme;
import com.example.sharingrecipeapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder> implements Filterable {

    private List<Theme> mTheme;
    private List<Theme> mThemeOld;

    iClickOnItemTheme iClickOnItemTheme;

    public ThemeAdapter(){}

    public void setData(List<Theme> list, iClickOnItemTheme click){
        this.mTheme = list;
        this.mThemeOld = list;
        this.iClickOnItemTheme = click;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ThemeAdapter.ThemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme, parent, false);

        return new ThemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeViewHolder holder, int position) {
        Theme theme = mTheme.get(position);
        if (theme == null) return;
        holder.name.setText(theme.getName());
        Glide.with(holder.img.getContext()).load(theme.getImage()).into(holder.img);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickOnItemTheme.onClickItemTheme(theme);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mThemeOld != null){
            return mTheme.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ThemeViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView img;

        MaterialCardView card;
        public ThemeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView_title_theme);
            img = itemView.findViewById(R.id.imageView_theme);
            card = itemView.findViewById(R.id.random_list_theme);
        }
    }
}