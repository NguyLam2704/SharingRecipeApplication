package com.example.sharingrecipeapp.Adapters.AddToGrocery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Classes.NguyenLieu;
import com.example.sharingrecipeapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterAddToGro extends RecyclerView.Adapter<AddToGroViewHolder> {

    List<NguyenLieu> listNL_AddGro;

    ArrayList<Boolean> check;

    public void setCheck(ArrayList<Boolean> check) {
        this.check = check;
    }

    public AdapterAddToGro(List<NguyenLieu> listNL_AddGro) {
        this.listNL_AddGro = listNL_AddGro;
    }

    @NonNull
    @Override
    public AddToGroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_addtogrocery,parent,false);
        return new AddToGroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddToGroViewHolder holder, int position) {
        NguyenLieu nguyenLieu = listNL_AddGro.get(position);

        holder.nameAddGro.setText(nguyenLieu.getName());
        Picasso.get().load(nguyenLieu.getImg()).into(holder.imgAddGro);

        String sl;
        if (nguyenLieu.getSL() == (int) nguyenLieu.getSL()){
            sl = String.valueOf((int)nguyenLieu.getSL());
        }else {
            sl = String.valueOf(nguyenLieu.getSL());
        }
        holder.slAddGro.setText(sl);
        holder.dvAddGro.setText(nguyenLieu.getDonvi());
        holder.checkAddGro.setChecked(true);
        holder.checkAddGro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    check.set(position,true);
                }else {
                    check.set(position,false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listNL_AddGro.size();
    }
}
