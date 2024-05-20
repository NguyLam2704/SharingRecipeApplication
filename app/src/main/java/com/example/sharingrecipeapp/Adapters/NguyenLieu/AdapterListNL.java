package com.example.sharingrecipeapp.Adapters.NguyenLieu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Classes.NguyenLieu;
import com.example.sharingrecipeapp.Fragments.GroceriesFragment;
import com.example.sharingrecipeapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterListNL extends BaseAdapter {
    List<NguyenLieu> list;
    AdapterListNLDaMua adapterDaMua;
    List<NguyenLieu> daMua;


    public AdapterListNL(List<NguyenLieu> list) {
        this.list = list;
    }

    public AdapterListNL(List<NguyenLieu> list, AdapterListNLDaMua adapterDaMua, List<NguyenLieu> daMua) {
        this.list = list;
        this.adapterDaMua = adapterDaMua;
        this.daMua = daMua;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ingredients,parent,false);
        NguyenLieu nguyenLieu = list.get(position);

        ImageView imageView = view.findViewById(R.id.imgin);
        TextView name = view.findViewById(R.id.namein);
        EditText editText = view.findViewById(R.id.quantityin);
        TextView donvi = view.findViewById(R.id.donvi_dathem);
        CheckBox checkBox_NL = view.findViewById(R.id.checkIn);
        checkBox_NL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                NguyenLieu nl  = list.get(position);
                int daMuaPosition = 0;
                boolean biTrung = false;
                if (isChecked){
                    for (int i = 0; i < daMua.size(); i++){
                        if (daMua.get(i).getName().equals(nl.getName())){
                            daMua.get(i).setSL(daMua.get(i).getSL() + nl.getSL());
                            daMuaPosition = i;
                            biTrung = true;
                        }
                    }

                    if (!biTrung){
                        daMua.add(nl);
                        updateFireStore(list.get(position));
                    } else {
                        updateNL_Da_co(daMua.get(daMuaPosition));
                    }

                    deleteNguyenLieuDaThem(list.get(position));

                    adapterDaMua.notifyDataSetChanged();
                    list.remove(position);
                    notifyDataSetChanged();
                }
            }
        });

        donvi.setText(nguyenLieu.getDonvi());
        Picasso.get().load(nguyenLieu.getImg()).into(imageView);
        name.setText(nguyenLieu.getName());

        String sl;
        if (nguyenLieu.getSL() == (int) nguyenLieu.getSL()){
            sl = String.valueOf((int) nguyenLieu.getSL());
        } else {
            sl = String.valueOf(nguyenLieu.getSL());
        }
        editText.setText(sl);
        return view;
    }

    private void deleteNguyenLieuDaThem(NguyenLieu nguyenLieu) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        db.collection("ListNguyenLieuMua").whereEqualTo("name",nguyenLieu.getName()).whereEqualTo("idUser",auth.getUid())
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots){
                            db.collection("ListNguyenLieuMua").document(doc.getId()).delete();
                        }
                    }
                });
        db.collection("ListNguyenLieuMua").document(nguyenLieu.getId()).delete();
    }

    private void updateNL_Da_co(NguyenLieu nguyenLieu) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> data = new HashMap<>();
        data.put("SL",nguyenLieu.getSL());
        db.collection("ListNguyenLieuDaMua").document(nguyenLieu.getId()).update(data);
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
        db.collection("ListNguyenLieuDaMua").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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
