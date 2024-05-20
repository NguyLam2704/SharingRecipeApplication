package com.example.sharingrecipeapp.Adapters.NguyenLieu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharingrecipeapp.Classes.NguyenLieu;
import com.example.sharingrecipeapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterListNLDaMua extends BaseAdapter {

    List<NguyenLieu> daMua;
    List<NguyenLieu> list;
    AdapterListNL adapterListNL;

    public AdapterListNLDaMua(List<NguyenLieu> daMua) {
        this.daMua = daMua;
    }

    public AdapterListNLDaMua(List<NguyenLieu> daMua, AdapterListNL adapterListNL, List<NguyenLieu> list) {
        this.daMua = daMua;
        this.list = list;
        this.adapterListNL = adapterListNL;
    }

    public void setData(AdapterListNL adapterListNL, List<NguyenLieu> list){
        this.adapterListNL = adapterListNL;
        this.list = list;
    }


    @Override
    public int getCount() {
        return daMua.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nguyenlieudamua,parent,false);
        NguyenLieu nguyenLieu = daMua.get(position);

        ImageView imageView = view.findViewById(R.id.img_damua);
        TextView name = view.findViewById(R.id.name_damua);
        EditText editText = view.findViewById(R.id.SL_damua);
        TextView donvi = view.findViewById(R.id.donvi_damua);
        CheckBox checkBox_NL = view.findViewById(R.id.checkIn_damua);
        checkBox_NL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        donvi.setText(nguyenLieu.getDonvi());
        Picasso.get().load(nguyenLieu.getImg()).into(imageView);
        name.setText(nguyenLieu.getName());
        String sl;
        if (nguyenLieu.getSL() == (int) nguyenLieu.getSL()){
            sl = String.valueOf( (int) nguyenLieu.getSL());
        } else {
            sl = String.valueOf(nguyenLieu.getSL());
        }
        editText.setText(sl);
        checkBox_NL.setChecked(true);
        return view;
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
