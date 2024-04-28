package com.example.sharingrecipeapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sharingrecipeapp.Classes.Theme;
import com.example.sharingrecipeapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ThemeAdapter extends FirestoreRecyclerAdapter<Theme, ThemeAdapter.ThemeViewHolder> {

    public ThemeAdapter(@NonNull FirestoreRecyclerOptions<Theme> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ThemeAdapter.ThemeViewHolder holder, int position, @NonNull Theme theme) {
        holder.name.setText(theme.getName());
        Glide.with(holder.img.getContext()).load(theme.getImage()).into(holder.img);
    }

    @NonNull
    @Override
    public ThemeAdapter.ThemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme, parent, false);

        return new ThemeViewHolder(view);
    }

    public class ThemeViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView img;
        public ThemeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView_title_theme);
            img = itemView.findViewById(R.id.imageView_theme);

        }
    }
}