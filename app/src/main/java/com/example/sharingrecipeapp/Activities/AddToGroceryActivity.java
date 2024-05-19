package com.example.sharingrecipeapp.Activities;

import static android.content.ContentValues.TAG;

import static com.example.sharingrecipeapp.Activities.CreateRecipeActivity.unAccent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharingrecipeapp.Adapters.CheckBoxAdapter;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.Ingre.ListIngreInDetailAdapterDonVi;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.Ingre.ListIngreInDetailAdapterName;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.Ingre.ListIngreInDetailAdapterSoLuong;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.ViewPagerImageFoodAdapter;
import com.example.sharingrecipeapp.CheckBoxListener;
import com.example.sharingrecipeapp.Classes.AddNguyenLieu;
import com.example.sharingrecipeapp.Classes.AutoScrollTask;
import com.example.sharingrecipeapp.Classes.Ingredient;
import com.example.sharingrecipeapp.Classes.Method;
import com.example.sharingrecipeapp.Classes.SoLuongIngre;
import com.example.sharingrecipeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class AddToGroceryActivity extends AppCompatActivity implements CheckBoxListener {

    int i = 0;

    FirebaseFirestore NewIngre_db;
    FirebaseStorage NewIngre_stg;
    FirebaseAuth NewIngre_auth;

    FirebaseFirestore firebaseFirestore;

    String idRecipe;

    List<String> ingres;
    List<Ingredient> IngreList;

    List<SoLuongIngre> soLuongIngreList;

    ListIngreInDetailAdapterName ingreAdapter;
    ListIngreInDetailAdapterDonVi donviAdapter;

    CheckBoxAdapter checkBoxAdapter;

    RecyclerView recyNguyenLieu, recySoLuong, recyDonVi, recyCheckBox;

    ImageView btnClose;

    TextView btnAdd;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_grocery);

        firebaseFirestore = FirebaseFirestore.getInstance();


        Intent intent = getIntent();
        idRecipe = intent.getStringExtra("id");
        btnClose = findViewById(R.id.btnClose);
        btnAdd = findViewById(R.id.btnAdd);

        recyNguyenLieu = findViewById(R.id.recyAddNguyenLieu);
        recyNguyenLieu.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(), RecyclerView.VERTICAL, false));

        recySoLuong = findViewById(R.id.recyAddSL);
        recySoLuong.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(), RecyclerView.VERTICAL, false));

        recyDonVi = findViewById(R.id.recyAddDonVi);
        recyDonVi.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(), RecyclerView.VERTICAL, false));

        recyCheckBox = findViewById(R.id.recyCheckBox);
        recyCheckBox.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(), RecyclerView.VERTICAL, false));

        getRecipes(idRecipe);
        getSoLuongIngre(idRecipe);
        //CheckBox(idRecipe);

        NewIngre_db = FirebaseFirestore.getInstance();
        NewIngre_stg = FirebaseStorage.getInstance();
        NewIngre_auth = FirebaseAuth.getInstance();
        FirebaseUser NewIngreUser = NewIngre_auth.getCurrentUser();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            pushNguyenLieu(ingres);
                        } else {
                            Log.d(TAG, "Current data: null");
                        }
                    }



                    private void pushNguyenLieu(List<String> ingres) {
                        soLuongIngreList = new ArrayList<>();

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

                                        Random rd = new Random();
                                        int songuyen = rd.nextInt(999999999);
                                        String id = (String) snapshot.get("id");
                                        String name = (String) snapshot.get("name");
                                        String image = (String) snapshot.get("img");
                                        String dv = (String) snapshot.get("donvi");
                                        String docName = id + (String.valueOf(songuyen));

                                        Map<String, Object> NewIngre = new HashMap<>();
                                        NewIngre.put("name", name);
                                        NewIngre.put("donvi",dv);
                                        NewIngre.put("img", image);
                                        NewIngre.put("idUser",NewIngreUser.getUid());
                                        NewIngre.put("SL",convertslIngre("0"));
                                        DocumentReference CreateNewBuyIngre  = NewIngre_db.collection("ListNguyenLieuMua").document(docName);
                                        CreateNewBuyIngre.set(NewIngre).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(AddToGroceryActivity.this, "ok", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        firebaseFirestore.collection("Recipes").document(idRecipe).get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if(task.isSuccessful()){
                                                            ArrayList<Number> doc = (ArrayList<Number>) task.getResult().get("SoLuong");
                                                            if(!doc.isEmpty()){
                                                                Number soluong = doc.get(i);
                                                                DocumentReference documentReference = firebaseFirestore.collection("ListNguyenLieuMua").document(docName);
                                                                documentReference.update("SL",soluong);
                                                                i++;
                                                            }
                                                        }
                                                    }
                                                });
                                    } else {
                                        Log.d(TAG, "Current data: null");
                                    }
                                }
                            });

                        }
                    }

                });
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
                            ArrayList<Number> doc = (ArrayList<Number>) task.getResult().get("SoLuong");
                            if(!doc.isEmpty()){
                                List<SoLuongIngre> soLuongIngreList = new ArrayList<SoLuongIngre>();
                                for (int i = 0;i<doc.size();i++){
                                    Number sl = doc.get(i);
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
                            ArrayList<Number> doc = (ArrayList<Number>) task.getResult().get("SoLuong");
                            if(!doc.isEmpty()){
                                for (int i = 0;i<doc.size();i++){
                                    Number sl =  doc.get(i);
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

    private Number convertslIngre (String number)
    {
        Number ingres ;
        ingres = Integer.valueOf(number);
        Number arrSlngre = ingres;
        return arrSlngre;
    }
}