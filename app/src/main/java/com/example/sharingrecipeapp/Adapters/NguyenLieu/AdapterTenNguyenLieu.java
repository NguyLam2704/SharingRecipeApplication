package com.example.sharingrecipeapp.Adapters.NguyenLieu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Classes.NguyenLieu;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdapterTenNguyenLieu extends RecyclerView.Adapter<TenNguyenLieuViewHolder> {

    Context context;
    FirebaseFirestore db;
    FirebaseAuth auth;
    List<NguyenLieu> tenNL;

    public AdapterTenNguyenLieu(Context context, List<NguyenLieu> tenNL) {
        this.context = context;
        this.tenNL = tenNL;
    }

    public AdapterTenNguyenLieu() {
        tenNL = new ArrayList<>();
    }

    public void setData(List<NguyenLieu> list){
        this.tenNL = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TenNguyenLieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tennguyenlieu,parent,false);
        return new TenNguyenLieuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TenNguyenLieuViewHolder holder, int position) {
        NguyenLieu nguyenLieu = tenNL.get(position);
        if (nguyenLieu != null){
            holder.tenNL.setText(nguyenLieu.getName());
            holder.donvi.setText(nguyenLieu.getDonvi());
            Picasso.get().load(nguyenLieu.getImg()).into(holder.imageView);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText sl = holder.editText;
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    if (!sl.getText().toString().equals("")){
                        Map<String, Object> data = new HashMap<>();
                        data.put("name",nguyenLieu.getName());
                        data.put("donvi",nguyenLieu.getDonvi());
                        data.put("idUser",auth.getUid());
                        data.put("img",nguyenLieu.getImg());
                        data.put("SL",Integer.valueOf(sl.getText().toString()));
                        db.collection("ListNguyenLieuMua").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(context,"Đã thêm nguyên liệu", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(context,"Bạn cần thêm số lượng", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tenNL.size();
    }


}