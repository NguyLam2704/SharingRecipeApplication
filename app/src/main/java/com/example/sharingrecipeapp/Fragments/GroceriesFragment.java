

package com.example.sharingrecipeapp.Fragments;

import static android.widget.Toast.makeText;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharingrecipeapp.Activities.BottomNavigationCustomActivity;
import com.example.sharingrecipeapp.Activities.LoginActivity;
import com.example.sharingrecipeapp.Adapters.NguyenLieu.AdapterListNLDaMua;
import com.example.sharingrecipeapp.Adapters.NguyenLieu.AdapterListNLDaThem;
import com.example.sharingrecipeapp.Adapters.NguyenLieu.AdapterTenNguyenLieu;
import com.example.sharingrecipeapp.Classes.NguyenLieu;
import com.example.sharingrecipeapp.databinding.FragmentGroceriesBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;

public class GroceriesFragment extends Fragment {
    static int HEIGHT = 0;
    BottomNavigationCustomActivity bottomNavigationCustomActivity;

    FragmentGroceriesBinding binding;
    ImageView plus, write;
    Button AddNL;
    EditText editName, editSl, editDv;
    TextView textView;
    RecyclerView tenNL;

    RecyclerView listViewNL;
    List<NguyenLieu> nguyenLieuList;
    AdapterListNLDaThem adapterListNL;

    RecyclerView listNLDaMua;
    List<NguyenLieu> listNL_da_mua;
    AdapterListNLDaMua adapterListNLDaMua;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String userID;
    ImageView btn_edit;
    ImageView btn_edit_done;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGroceriesBinding.inflate(inflater,container,false);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userID = auth.getUid();

        nguyenLieuList = new ArrayList<>();
        listNL_da_mua = new ArrayList<>();
        btn_edit = binding.btnEdit;
        btn_edit_done = binding.btnEditDone;

        adapterListNLDaMua = new AdapterListNLDaMua(listNL_da_mua);

        listViewNL = binding.listGroceries;
        adapterListNL = new AdapterListNLDaThem(nguyenLieuList, adapterListNLDaMua, listNL_da_mua );
        adapterListNL.setEditBtn(btn_edit);
        adapterListNL.setEditBtnDone(btn_edit_done);
        listViewNL.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));
        listViewNL.setAdapter(adapterListNL);

        listNLDaMua = binding.listNlDaMua;
        adapterListNLDaMua.setData(adapterListNL,nguyenLieuList);
        adapterListNLDaMua.addBtn(btn_edit,btn_edit_done);
        listNLDaMua.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));
        listNLDaMua.setAdapter(adapterListNLDaMua);

        displayNguyenLieu();
        displayNguyenLieuDaMua();


        plus = binding.plus;
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAdd();
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_edit.setVisibility(View.GONE);
                btn_edit_done.setVisibility(View.VISIBLE);
                adapterListNL.turnOnEdit();
                duringActiveBtnEdit();
            }
        });



        btn_edit_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapterListNL.listEditIsEmpty()){
                    StyleableToast.makeText(binding.getRoot().getContext(),"Bạn cần nhập số lượng",R.style.mytoast).show();
                } else if (adapterListNL.listHasEditIs0()) {
                    StyleableToast.makeText(binding.getRoot().getContext(),"Số lượng phải lớn hơn 0",R.style.mytoast).show();
                } else {
                    adapterListNL.updateEditSL();
                    btn_edit_done.setVisibility(View.GONE);
                    if (!nguyenLieuList.isEmpty()){
                        btn_edit.setVisibility(View.VISIBLE);
                    }

                    adapterListNL.turnOffEdit();
                }

            }
        });

        return binding.getRoot();
    }

    private void duringActiveBtnEdit() {

        ArrayList<View> view = new ArrayList<>();
        view.add(binding.getRoot().findViewById(R.id.grocery_container));
        view.add(binding.getRoot().findViewById(R.id.list_nl_da_mua));
        view.add(binding.getRoot().findViewById(R.id.plus));
        //view.add(binding.getRoot().findViewById(R.id.checkIn_damua));
        //view.add(binding.getRoot().findViewById(R.id.SL_damua));


        for ( View v : view){
            v.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    btn_edit.setVisibility(View.VISIBLE);
                    btn_edit_done.setVisibility(View.GONE);
                    adapterListNL.turnOffEdit();
                    return false;
                }
            });
        }

        View v = binding.getRoot().findViewById(R.id.checkIn);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_edit.setVisibility(View.VISIBLE);
                btn_edit_done.setVisibility(View.GONE);
                adapterListNL.turnOffEdit();
                return false;
            }
        });
    }


    private void displayNguyenLieuDaMua() {
        db.collection("ListNguyenLieuDaMua").whereEqualTo("idUser",userID).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots){
                        String name = doc.getString("name");
                        String img = doc.getString("img");
                        double SL = doc.getDouble("SL");
                        String id = doc.getId();
                        String donvi = doc.getString("donvi");

                        listNL_da_mua.add(new NguyenLieu(SL,donvi,id,name,img));
                        adapterListNLDaMua.notifyDataSetChanged();
                    }
                });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();

                deleteNLDaMua(listNL_da_mua.get(position));
                listNL_da_mua.remove(position);
                adapterListNLDaMua.notifyDataSetChanged();
            }
        });

        itemTouchHelper.attachToRecyclerView(listNLDaMua);
    }

    private void deleteNLDaMua(NguyenLieu nguyenLieu) {
        db.collection("ListNguyenLieuDaMua").document(nguyenLieu.getId()).delete();
    }

    private void displayNguyenLieu() {
        db.collection("ListNguyenLieuMua").whereEqualTo("idUser",userID).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots){
                        String name = doc.getString("name");
                        String img = doc.getString("img");
                        double SL = doc.getDouble("SL");
                        String id = doc.getId();
                        String donvi = doc.getString("donvi");
                        boolean biTrung = false;

                        for (int i = 0; i < nguyenLieuList.size();i++){
                            if (nguyenLieuList.get(i).getName().equals(name)){
                                nguyenLieuList.get(i).setSL(SL + nguyenLieuList.get(i).getSL());
                                adapterListNL.notifyDataSetChanged();
                                biTrung = true;
                                break;
                            }
                        }

                        if (!biTrung){
                            adapterListNL.turnOnBtnEdit();

                            nguyenLieuList.add(new NguyenLieu(SL,donvi,id,name,img));
                            adapterListNL.notifyDataSetChanged();

                        }
                    }
                    if (!nguyenLieuList.isEmpty()){
                        adapterListNL.turnOnBtnEdit();
                    }
                });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();

                deleteNLDaThem(nguyenLieuList.get(position));
                nguyenLieuList.remove(position);
                adapterListNL.dataClear();
                adapterListNL.notifyDataSetChanged();

                if (nguyenLieuList.isEmpty()){
                    adapterListNL.turnOffBtnEdit();
                    adapterListNL.turnOffBtnEditDone();
                }



            }
        });

        itemTouchHelper.attachToRecyclerView(listViewNL);

    }

    private void deleteNLDaThem(NguyenLieu nguyenLieu) {
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

    private void DialogAdd(){
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_list_add);
        displayNguyenLieu(dialog);
        write = dialog.findViewById(R.id.write);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialoga = new Dialog(requireContext());
                dialoga.setContentView(R.layout.dialog_nhapnguyenlieu);
                editName = dialoga.findViewById(R.id.editTenNL);
                editDv = dialoga.findViewById(R.id.editDV);
                editSl = dialoga.findViewById(R.id.editSL);
                AddNL = dialoga.findViewById(R.id.NhapNL);
                AddNL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNguyenLieu();
                        dialoga.dismiss();
                    }
                });
                dialoga.show();
            }
        });
        dialog.show();
        dialog.findViewById(R.id.btn_hoan_tat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void displayNguyenLieu(Dialog dialog) {
        tenNL = dialog.findViewById(R.id.recyNL);
        List<NguyenLieu> tenNguyenLieuList = new ArrayList<>();
        AdapterTenNguyenLieu adapterTenNguyenLieu = new AdapterTenNguyenLieu(requireContext(),tenNguyenLieuList);

        adapterTenNguyenLieu.setData(adapterListNL,nguyenLieuList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        tenNL.setLayoutManager(linearLayoutManager);
        tenNL.setAdapter(adapterTenNguyenLieu);
        db.collection("NguyenLieu").orderBy("name").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot doc : task.getResult()){
                        String name = doc.getString("name");
                        String img = doc.getString("img");
                        String donvi  = doc.getString("donvi");

                        NguyenLieu nl = new NguyenLieu(donvi,name,img);
                        tenNguyenLieuList.add(nl);
                        adapterTenNguyenLieu.notifyDataSetChanged();
                    }
                }
            }
        });
    }
    private void addNguyenLieu() {
        String strName = editName.getText().toString().trim();
        String strSL = editSl.getText().toString().trim();
        String strDonVi = editDv.getText().toString().trim();
        String img = "https://firebasestorage.googleapis.com/v0/b/fantafood-3ea80.appspot.com/o/ingredients_icon%2Flogo_gro.png?alt=media&token=3deb24f9-1edb-4a88-8963-308278a9e9ee";
        if(TextUtils.isEmpty(strName) || TextUtils.isEmpty(strSL) || TextUtils.isEmpty(strDonVi) || strSL.equals(".")){
            StyleableToast.makeText(requireActivity(),"Vui lòng nhập đầy đủ thông tin",R.style.mytoast).show();
        }else {
            double soluong = Double.valueOf(strSL);
            //ktra xem co trung ten NL khong
            db.collection("NguyenLieu").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    boolean biTrung = false;
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String dbName = doc.getString("name").toLowerCase();
                        if (strName.toLowerCase().equals(dbName)) {
                            StyleableToast.makeText(binding.getRoot().getContext(), "Nguyên liệu đã tồn tại",R.style.mytoast).show();
                            biTrung = true;
                            break;
                        }
                    }

                    if (!biTrung) {
                        NguyenLieu nl = new NguyenLieu(soluong, strDonVi, "", strName, img);

                        Map<String, Object> data = new HashMap<>();
                        data.put("name", strName);
                        data.put("donvi", strDonVi);
                        data.put("SL", soluong);
                        data.put("idUser", auth.getUid());
                        data.put("img", img);

                        db.collection("ListNguyenLieuMua").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("id", documentReference.getId());
                                nl.setId(documentReference.getId());
                                documentReference.update(data);
                            }
                        });

                        nguyenLieuList.add(nl);
                        adapterListNL.dataClear();
                        adapterListNL.notifyDataSetChanged();
                        Toast.makeText(binding.getRoot().getContext(),"Thêm nguyên liệu thành công",Toast.LENGTH_SHORT).show();

                        if (nguyenLieuList.isEmpty()){
                            adapterListNL.turnOffBtnEdit();
                        }
                        else {
                            adapterListNL.turnOnBtnEdit();
                        }
                    }
                }
            });
        }
    }

}