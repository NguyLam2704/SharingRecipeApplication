

package com.example.sharingrecipeapp.Fragments;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.makeText;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sharingrecipeapp.Activities.BottomNavigationCustomActivity;
import com.example.sharingrecipeapp.Activities.FoodDetailActivity;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.Ingre.ListIngreInDetailAdapterName;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.Ingre.ListIngreInDetailAdapterSoLuong;
import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Adapters.Home.RecipesRandomAdapter;
import com.example.sharingrecipeapp.Adapters.ListInAdapter;
import com.example.sharingrecipeapp.Adapters.NguyenLieu.AdapterListNL;
import com.example.sharingrecipeapp.Adapters.NguyenLieu.AdapterListNLDaMua;
import com.example.sharingrecipeapp.Adapters.NguyenLieu.AdapterTenNguyenLieu;
import com.example.sharingrecipeapp.Adapters.NguyenLieu.IClickOnItemSavedRecipe;
import com.example.sharingrecipeapp.Adapters.NguyenLieu.ReGroAdapter;
import com.example.sharingrecipeapp.Adapters.PlanList.AdapterPlanListRecipes;
import com.example.sharingrecipeapp.Classes.Ingredient;
import com.example.sharingrecipeapp.Classes.ListIngredient;
import com.example.sharingrecipeapp.Classes.NguyenLieu;
import com.example.sharingrecipeapp.Classes.ReGro;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.Classes.SoLuongIngre;
import com.example.sharingrecipeapp.databinding.FragmentGroceriesBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroceriesFragment extends Fragment {
    static int HEIGHT = 0;
    BottomNavigationCustomActivity bottomNavigationCustomActivity;

    FragmentGroceriesBinding binding;
    ImageView plus;

    RecyclerView tenNL;
    List<NguyenLieu> nguyenLieuList;
    List<NguyenLieu> listNL_da_mua;
    AdapterListNL adapterListNL;
    AdapterListNLDaMua adapterListNLDaMua;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String userID;


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
        adapterListNLDaMua = new AdapterListNLDaMua(listNL_da_mua);

        displayNguyenLieu();
        displayNguyenLieuDaMua();

        plus = binding.plus;
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAdd();
            }
        });




        return binding.getRoot();
    }

    private void displayNguyenLieuDaMua() {
        ListView listNLDaMua = binding.listNlDaMua;
        adapterListNLDaMua.setData(adapterListNL,nguyenLieuList);
        listNLDaMua.setAdapter(adapterListNLDaMua);

        db.collection("ListNguyenLieuDaMua").whereEqualTo("idUser","HmY48QhzdQSzLoDFDSaaMGzDa8c2").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots){
                        String name = doc.getString("name");
                        String img = doc.getString("img");
                        double SL = doc.getDouble("SL");
                        String id = doc.getId();
                        String donvi = doc.getString("donvi");

                        listNL_da_mua.add(new NguyenLieu(SL,donvi,id,name,img));

//                        if (listNL_da_mua.size() < 7){
//                            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) listNLDaMua.getLayoutParams();
//                            HEIGHT = listNL_da_mua.size()* 150;
//                            lp.height = HEIGHT;
//                            listNLDaMua.setLayoutParams(lp);
//                        }
                        adapterListNLDaMua.notifyDataSetChanged();
                        listNLDaMua.setAdapter(adapterListNLDaMua);
                    }
                });
    }

    private void displayNguyenLieu() {
        ListView listViewNL = binding.listGroceries;
        //listViewNL.setEnabled(false);
        adapterListNL = new AdapterListNL(nguyenLieuList, adapterListNLDaMua, listNL_da_mua );

        db.collection("ListNguyenLieuMua").whereEqualTo("idUser","HmY48QhzdQSzLoDFDSaaMGzDa8c2").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots){
                        String name = doc.getString("name");
                        String img = doc.getString("img");
                        double SL = doc.getDouble("SL");
                        String id = doc.getId();
                        String donvi = doc.getString("donvi");
                        boolean biTrung = false;

                        //Toast.makeText(binding.getRoot().getContext(),String.valueOf(SL),Toast.LENGTH_SHORT).show();


                        for (int i = 0; i < nguyenLieuList.size();i++){
                            if (nguyenLieuList.get(i).getName().equals(name)){
                                nguyenLieuList.get(i).setSL(SL + nguyenLieuList.get(i).getSL());
                                adapterListNL.notifyDataSetChanged();
                                biTrung = true;
                                break;
                            }
                        }

                        if (!biTrung){
                            nguyenLieuList.add(new NguyenLieu(SL,donvi,id,name,img));
//                            if (nguyenLieuList.size() < 7){
//                                ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) listViewNL.getLayoutParams();
//                                HEIGHT = nguyenLieuList.size()* 150;
//                                lp.height = HEIGHT;
//                                listViewNL.setLayoutParams(lp);
//                            }

                            adapterListNL.notifyDataSetChanged();
                            listViewNL.setAdapter(adapterListNL);
                        }
                    }
                });

//        db.collection("ListNguyenLieuMua").whereEqualTo("idUser","HmY48QhzdQSzLoDFDSaaMGzDa8c2")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        for (DocumentChange dc : value.getDocumentChanges()) {
//                            switch (dc.getType()) {
//                                case ADDED:
//                                adapterListNL.notifyDataSetChanged();
//                                    break;
//                                case MODIFIED:
//
//                                    break;
//                                case REMOVED:
//
//                                    break;
//                            }
//                        }
//                    }
//
//                });

    }

    private void DialogAdd(){
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_list_add);
        displayNguyenLieu(dialog);
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
        db.collection("NguyenLieu").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    private void onClickGoToDetailFood(String id) {
        bottomNavigationCustomActivity.gotoFoodDetail(id);
    }
}