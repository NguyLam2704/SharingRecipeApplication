package com.example.sharingrecipeapp.Adapters.NguyenLieu;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Classes.NguyenLieu;
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

public class AdapterListNLDaThem extends RecyclerView.Adapter<ListNLDaThemViewHolder> {
    List<NguyenLieu> list;
    AdapterListNLDaMua adapterDaMua;
    List<NguyenLieu> daMua;

    List<String> editTextListOld;
    List<EditText> editTextList;

    ImageView editBtn;
    ImageView editBtnDone;

    public ImageView getEditBtnDone() {
        return editBtnDone;
    }

    public void setEditBtnDone(ImageView editBtnDone) {
        this.editBtnDone = editBtnDone;
    }

    public ImageView getEditBtn() {
        return editBtn;
    }

    public void setEditBtn(ImageView editBtn) {
        this.editBtn = editBtn;
    }

    public AdapterListNLDaThem(List<NguyenLieu> list) {
        this.list = list;

    }

    public AdapterListNLDaThem(List<NguyenLieu> list, AdapterListNLDaMua adapterDaMua, List<NguyenLieu> daMua) {
        this.list = list;
        this.adapterDaMua = adapterDaMua;
        this.daMua = daMua;
        editTextList = new ArrayList<>();
        editTextListOld = new ArrayList<>();
    }

    @NonNull
    @Override
    public ListNLDaThemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ingredients,parent,false);
        return new ListNLDaThemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListNLDaThemViewHolder holder, int position) {
        NguyenLieu nguyenLieu = list.get(position);

        holder.name.setText(nguyenLieu.getName());
        String sl;
        if (nguyenLieu.getSL() == (int) nguyenLieu.getSL()){
            sl = String.valueOf((int) nguyenLieu.getSL());
        } else {
            sl = String.valueOf(nguyenLieu.getSL());
        }
        holder.editText.setText(sl);
        holder.editText.setEnabled(false);

        //them edittext vao list
        editTextList.add(holder.editText);
        editTextListOld.add(sl);


        holder.donvi.setText(nguyenLieu.getDonvi());
        Picasso.get().load(nguyenLieu.getImg()).into(holder.imageView);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                    editTextList.remove(position);
                    editTextListOld.remove(position);

                    dataClear();
                    notifyDataSetChanged();
                }
            }
        });
        holder.checkBox.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void dataClear(){
        editTextList.clear();
        editTextListOld.clear();
        //list.clear();
    }

    public void turnOffBtnEditDone(){
        editBtnDone.setVisibility(View.GONE);
    }
    public void turnOnBtnEditDone(){
        editBtnDone.setVisibility(View.VISIBLE);
    }

    public void turnOffBtnEdit(){
        editBtn.setVisibility(View.GONE);
    }
    public void turnOnBtnEdit(){
        editBtn.setVisibility(View.VISIBLE);
    }

    public boolean listHasEditIs0(){
        for (EditText editText : editTextList){
            if (editText.getText().toString().equals("0")){
                return true;
            }
        }
        return false;
    }

    public boolean listEditIsEmpty(){
        for (EditText editText : editTextList){
            if (editText.getText().toString().isEmpty()){
                return true;
            }
        }
        return false;
    }

    public void updateEditSL(){
        //Toast.makeText(editBtn.getContext(), String.valueOf(editTextListOld.size()),Toast.LENGTH_SHORT).show();
        for (int i = 0; i < list.size(); i++){
            if (!editTextList.get(i).getText().toString().equals(editTextListOld.get(i))){
                double sl = Double.valueOf(editTextList.get(i).getText().toString()) ;
                Map<String,Object> data = new HashMap<>();
                data.put("SL",sl);
                list.get(i).setSL(sl);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("ListNguyenLieuMua").document(list.get(i).getId()).update(data);
            }
        }
    }

    public void turnOnEdit(){
        for (EditText i : editTextList){
            i.setEnabled(true);
        }
    }

    public void turnOffEdit(){
        for (EditText i : editTextList){
            i.setEnabled(false);
        }
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
