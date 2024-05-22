package com.example.sharingrecipeapp.Adapters.NguyenLieu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Classes.NguyenLieu;
import com.example.sharingrecipeapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterListNLDaMua extends RecyclerView.Adapter<ListNLDaMuaViewHolder> {
    List<NguyenLieu> daMua;
    List<NguyenLieu> list;
    AdapterListNLDaThem adapterListNL;



    public AdapterListNLDaMua(List<NguyenLieu> daMua) {
        this.daMua = daMua;

    }

    public void setData(AdapterListNLDaThem adapterListNL, List<NguyenLieu> list){
        this.adapterListNL = adapterListNL;
        this.list = list;
    }

    @NonNull
    @Override
    public ListNLDaMuaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nguyenlieudamua,parent,false);
        return new ListNLDaMuaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListNLDaMuaViewHolder holder, int position) {
        NguyenLieu nguyenLieu = daMua.get(position);

        holder.donvi.setText(nguyenLieu.getDonvi());
        Picasso.get().load(nguyenLieu.getImg()).into(holder.imageView);
        holder.name.setText(nguyenLieu.getName());
        String sl;
        if (nguyenLieu.getSL() == (int) nguyenLieu.getSL()){
            sl = String.valueOf( (int) nguyenLieu.getSL());
        } else {
            sl = String.valueOf(nguyenLieu.getSL());
        }

        holder.editText.setText(sl);
        holder.editText.setEnabled(false);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                NguyenLieu nl = daMua.get(position);
                boolean biTrung = false;
                int nlDaThemPosition = 0;

                if (!isChecked){
                    for (int i = 0; i < list.size(); i++){
                        if (list.get(i).getName().equals(nl.getName())){
                            list.get(i).setSL(list.get(i).getSL() + nl.getSL());
                            nlDaThemPosition = i;
                            biTrung = true;
                        }
                    }

                    if (!biTrung){
                        list.add(nl);
                        updateFireStore(nl);
                    } else {
                        updateNL_Da_co(list.get(nlDaThemPosition));
                    }

                    deleteNguyenLieuDaThem(daMua.get(position));



                    adapterListNL.notifyDataSetChanged();
                    daMua.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
        holder.checkBox.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return daMua.size();
    }



    private void deleteNguyenLieuDaThem(NguyenLieu nguyenLieu) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ListNguyenLieuDaMua").document(nguyenLieu.getId()).delete();
    }

    private void updateNL_Da_co(NguyenLieu nguyenLieu) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> data = new HashMap<>();
        data.put("SL",nguyenLieu.getSL());
        db.collection("ListNguyenLieuMua").document(nguyenLieu.getId()).update(data);
    }

    private void updateFireStore(NguyenLieu nguyenLieu) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("name",nguyenLieu.getName());
        data.put("donvi",nguyenLieu.getDonvi());
        data.put("idUser",auth.getUid());
        data.put("img",nguyenLieu.getImg());
        data.put("SL",nguyenLieu.getSL());
        db.collection("ListNguyenLieuMua").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Map<String,Object> data = new HashMap<>();
                data.put("id",documentReference.getId());
                nguyenLieu.setId(documentReference.getId());
                documentReference.update(data);
            }
        });


    }
}
