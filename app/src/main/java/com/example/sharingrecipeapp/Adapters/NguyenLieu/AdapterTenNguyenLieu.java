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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;

public class AdapterTenNguyenLieu extends RecyclerView.Adapter<TenNguyenLieuViewHolder> {

    Context context;
    FirebaseFirestore db;
    FirebaseAuth auth;
    List<NguyenLieu> tenNL;
    List<NguyenLieu> NL_Da_Them;
    AdapterListNLDaThem adapterListNL;

    public AdapterTenNguyenLieu(Context context, List<NguyenLieu> tenNL) {
        this.context = context;
        this.tenNL = tenNL;
    }

    public AdapterTenNguyenLieu() {
        tenNL = new ArrayList<>();
    }

    public void setData(AdapterListNLDaThem adapterListNL, List<NguyenLieu> NL){
        this.adapterListNL = adapterListNL;
        this.NL_Da_Them = NL;
    }
    @NonNull
    @Override
    public TenNguyenLieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tennguyenlieu,parent,false);
        return new TenNguyenLieuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TenNguyenLieuViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        NguyenLieu nguyenLieu = tenNL.get(position);
        if (nguyenLieu != null){
            // Set giao dien
            holder.tenNL.setText(nguyenLieu.getName());
            holder.donvi.setText(nguyenLieu.getDonvi());
            Picasso.get().load(nguyenLieu.getImg()).into(holder.imageView);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //lay so luong tu edit text
                    EditText sl = holder.editText;

                    //Check xem edit text co rong hay khong
                    if (!sl.getText().toString().isEmpty() && !sl.getText().toString().equals("0") && !sl.getText().toString().equals(".")){
                        Double soluong = Double.valueOf(sl.getText().toString());
                        nguyenLieu.setSL(soluong);

                        //Them nguyen lieu vao list nguyen lieu da them
                        boolean biTrung = false;
                        for (int i = 0; i < NL_Da_Them.size(); i++){
                            if (NL_Da_Them.get(i).getName().equals(nguyenLieu.getName())){
                                NL_Da_Them.get(i).setSL(NL_Da_Them.get(i).getSL() + nguyenLieu.getSL());
                                biTrung = true;
                            }
                        }

                        // Cap nhat lai list nguyen lieu
                        if (!biTrung) {
                            if (NL_Da_Them.isEmpty()){
                                adapterListNL.turnOffBtnEditDone();
                                adapterListNL.turnOffBtnEdit();
                            }
                            else {
                                adapterListNL.turnOnBtnEdit();
                            }
                            NL_Da_Them.add(nguyenLieu);
                        }

                        //Cap nhat lai ListView
                        adapterListNL.dataClear();
                        adapterListNL.notifyDataSetChanged();

                        //Day du lieu len FireStore
                        db = FirebaseFirestore.getInstance();
                        auth = FirebaseAuth.getInstance();
                        if (!sl.getText().toString().equals("")){
                            Map<String, Object> data = new HashMap<>();
                            data.put("name",nguyenLieu.getName());
                            data.put("donvi",nguyenLieu.getDonvi());
                            data.put("idUser",auth.getUid());
                            data.put("img",nguyenLieu.getImg());
                            data.put("SL",soluong);

                            db.collection("ListNguyenLieuMua").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Map<String,Object> data = new HashMap<>();
                                    data.put("id",documentReference.getId());
                                    nguyenLieu.setId(documentReference.getId());
                                    StyleableToast.makeText(context,"Thêm nguyên liệu thành công", R.style.mytoast).show();
                                }
                            });
                        }
                    } else {
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
