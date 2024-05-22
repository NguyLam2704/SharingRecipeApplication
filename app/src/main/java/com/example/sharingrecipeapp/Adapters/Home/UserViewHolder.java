package com.example.sharingrecipeapp.Adapters.Home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.R;

public class UserViewHolder extends RecyclerView.ViewHolder {
    ImageView avt;
    TextView tenUser;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        avt = itemView.findViewById(R.id.avt_User);
        tenUser = itemView.findViewById(R.id.ten_User);
    }
}
