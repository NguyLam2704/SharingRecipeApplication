package com.example.sharingrecipeapp.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sharingrecipeapp.Adapters.CheckBoxAdapter;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.Ingre.ListIngreInDetailAdapterDonVi;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.Ingre.ListIngreInDetailAdapterName;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.Ingre.ListIngreInDetailAdapterSoLuong;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.ViewPagerImageFoodAdapter;
import com.example.sharingrecipeapp.CheckBoxListener;
import com.example.sharingrecipeapp.Classes.AutoScrollTask;
import com.example.sharingrecipeapp.Classes.Ingredient;
import com.example.sharingrecipeapp.Classes.Method;
import com.example.sharingrecipeapp.Classes.SoLuongIngre;
import com.example.sharingrecipeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class AddToGroceryActivity extends AppCompatActivity implements CheckBoxListener {

    FirebaseFirestore firebaseFirestore;

    String idRecipe;

    List<String> ingres;
    List<Ingredient> IngreList;

    ListIngreInDetailAdapterName ingreAdapter;
    ListIngreInDetailAdapterDonVi donviAdapter;

    CheckBoxAdapter checkBoxAdapter;

    RecyclerView recyNguyenLieu, recySoLuong, recyDonVi, recyCheckBox;

    ImageView btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_grocery);

        firebaseFirestore = FirebaseFirestore.getInstance();


        Intent intent = getIntent();
        idRecipe = intent.getStringExtra("id");
        btnClose = findViewById(R.id.btnClose);

        recyNguyenLieu = findViewById(R.id.recyAddNguyenLieu);
        recyNguyenLieu.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(),RecyclerView.VERTICAL, false));

        recySoLuong = findViewById(R.id.recyAddSL);
        recySoLuong.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(),RecyclerView.VERTICAL, false));

        recyDonVi = findViewById(R.id.recyAddDonVi);
        recyDonVi.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(),RecyclerView.VERTICAL, false));

        recyCheckBox = findViewById(R.id.recyCheckBox);
        recyCheckBox.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(),RecyclerView.VERTICAL, false));

        getRecipes(idRecipe);
        getSoLuongIngre(idRecipe);
        CheckBox(idRecipe);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void CheckBox(String idRecipe) {
            checkBoxAdapter = new CheckBoxAdapter(this,getIn(idRecipe),getSL(idRecipe), this);
            recyCheckBox.setAdapter(checkBoxAdapter);
    }


    private void getRecipes(String idRecipe) {
        final DocumentReference docRef = firebaseFirestore.collection("Recipes").document(idRecipe);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.w(TAG,"Listen failed",e );
                    return;
                }
                if (snapshot != null && snapshot.exists()){
                    Log.d(TAG,"Current data: " + snapshot.getData());
                    ingres = (List<String>) snapshot.get("NguyenLieu");
                    getIngre(ingres);

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    private void getIngre(List<String> ingres) {
        IngreList = new ArrayList<>();
        ingreAdapter = new ListIngreInDetailAdapterName();
        donviAdapter = new ListIngreInDetailAdapterDonVi();
        for (String ingre : ingres){
            final DocumentReference docRef = firebaseFirestore.collection("NguyenLieu").document(ingre);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: " + snapshot.getData());
                        String id = (String) snapshot.get("id");
                        String name = (String) snapshot.get("name");
                        String image = (String) snapshot.get("img");
                        String dv = (String) snapshot.get("donvi");
                        Ingredient ingreList = new Ingredient(id, name, image, dv);
                        IngreList.add(ingreList);
                        ingreAdapter.setData(IngreList);
                        recyNguyenLieu.setAdapter(ingreAdapter);
                        donviAdapter.setData(IngreList);
                        recyDonVi.setAdapter(donviAdapter);
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        }
    }

    private void getSoLuongIngre(String idRecipe) {
        firebaseFirestore.collection("Recipes").document(idRecipe).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<String> doc = (ArrayList<String>) task.getResult().get("SoLuong");
                            if(!doc.isEmpty()){
                                List<SoLuongIngre> soLuongIngreList = new ArrayList<SoLuongIngre>();
                                for (int i = 0;i<doc.size();i++){
                                    String sl = String.valueOf(doc.get(i));
                                    soLuongIngreList.add(new SoLuongIngre(sl));
                                }
                                recySoLuong.setAdapter(new ListIngreInDetailAdapterSoLuong(getApplicationContext(),soLuongIngreList));
                            }
                        }
                    }
                });
    }


    private ArrayList<SoLuongIngre> getSL(String idRecipe) {
        ArrayList<SoLuongIngre> soLuongIngres = new ArrayList<>();
        firebaseFirestore.collection("Recipes").document(idRecipe).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<String> doc = (ArrayList<String>) task.getResult().get("SoLuong");
                            if(!doc.isEmpty()){
                                for (int i = 0;i<doc.size();i++){
                                    String sl =  String.valueOf(doc.get(i));
                                    soLuongIngres.add(new SoLuongIngre(sl));
                                }

                            }
                        }
                    }
                });
        return soLuongIngres;
    }

    private ArrayList<Ingredient> getIn(String idRecipe) {
        ArrayList<Ingredient> Ingres = new ArrayList<>();
        firebaseFirestore.collection("Recipes").document(idRecipe).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<String> doc = (ArrayList<String>) task.getResult().get("NguyenLieu");
                            if(!doc.isEmpty()){
                                for (int i = 0;i<doc.size();i++){
                                    String id =  String.valueOf(doc.get(i));
                                    Ingres.add(new Ingredient(id));
                                }

                            }
                        }
                    }
                });
        return Ingres;
    }

    @Override
    public void onCheckBoxChange(ArrayList<Ingredient> ingredients, ArrayList<SoLuongIngre> soLuongIngres) {
            Toast.makeText(this, "Đã thêm" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}