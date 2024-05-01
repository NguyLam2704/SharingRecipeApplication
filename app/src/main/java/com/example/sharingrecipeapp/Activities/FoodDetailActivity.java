package com.example.sharingrecipeapp.Activities;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharingrecipeapp.Adapters.DetailRecipe.Ingre.ListIngreInDetailAdapterDonVi;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.Ingre.ListIngreInDetailAdapterName;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.Ingre.ListIngreInDetailAdapterSoLuong;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.ListMethodInDetailAdapter;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.ViewPagerImageFoodAdapter;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.ViewPagerImagerAvtAdapter;
import com.example.sharingrecipeapp.Classes.AutoScrollTask;
import com.example.sharingrecipeapp.Classes.Ingredient;
import com.example.sharingrecipeapp.Classes.Method;
import com.example.sharingrecipeapp.Classes.SoLuongIngre;
import com.example.sharingrecipeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class FoodDetailActivity extends AppCompatActivity {

    private int countSL = 0;
    private int countIngre = -1;

    ListIngreInDetailAdapterName ingreAdapter;
    ListIngreInDetailAdapterSoLuong soluongAdapter;

    ListIngreInDetailAdapterDonVi donviAdapter;

    FirebaseFirestore firebaseFirestore;

    List<Ingredient> IngreList;
    List<Ingredient> IngreDVList;
    List<String> ingres;
    List<String> sl;


    String idRecipe;

    ViewPager2 viewPager2, viewPager2Avt;
    ImageView back;

    TextView username, titlefood, heart, save, add, cook, note;

    RecyclerView recycIngre, recycSoLuong, recycDonVi, recycMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        back = findViewById(R.id.backImg);
        viewPager2 = findViewById(R.id.viewpagerImage);
        viewPager2Avt = findViewById(R.id.viewPagerAvt);
        username = findViewById(R.id.nameUser);
        titlefood = findViewById(R.id.TitleFood_Detail);
        heart = findViewById(R.id.like_text);
        save = findViewById(R.id.text_save);
        add = findViewById(R.id.text_add);
        cook = findViewById(R.id.text_time_detail);
        note = findViewById(R.id.text_Note);
        recycIngre = findViewById(R.id.recyNguyenLieu);
        recycMethod = findViewById(R.id.recyMethod);
        recycSoLuong = findViewById(R.id.recySoLuong);
        recycDonVi = findViewById(R.id.recyDonVi);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext(), RecyclerView.VERTICAL, false);
        recycIngre.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManagerMethod = new LinearLayoutManager(this.getApplicationContext(), RecyclerView.VERTICAL, false);
        recycMethod.setLayoutManager(linearLayoutManagerMethod);
        recycSoLuong.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(),RecyclerView.VERTICAL,false));
        recycDonVi.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(),RecyclerView.VERTICAL,false));

        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        idRecipe = intent.getStringExtra("id");


        getUsers(idRecipe);
        getRecipes(idRecipe);
        getSoLuongIngre(idRecipe);
        getListMethod(idRecipe);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodDetailActivity.this, BottomNavigationCustomActivity.class);
                startActivity(intent);
            }
        });
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
                                recycSoLuong.setAdapter(new ListIngreInDetailAdapterSoLuong(getApplicationContext(),soLuongIngreList));
                            }
                        }
                    }
                });
    }


    private void getListMethod(String idRecipe) {

        firebaseFirestore.collection("Recipes").document(idRecipe).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<String> doc = (ArrayList<String>) task.getResult().get("method");
                            if(!doc.isEmpty()){
                                List<Method> methodList = new ArrayList<Method>();
                                for (int i = 0;i<doc.size();i++){
                                  String step = ("• Bước ")+ (i+1)+ ": " +doc.get(i);
                                  methodList.add(new Method(step));
                                }
                               recycMethod.setAdapter(new ListMethodInDetailAdapter(getApplicationContext(),methodList));
                            }
                        }
                    }
                });
    }


    private void getUsers(String idRecipe) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Recipes").document(idRecipe).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentReference doc = (DocumentReference) task.getResult().get("Users");
                            doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot snapshot) {
                                    username.setText(snapshot.getString("username"));
                                    String avt = snapshot.getString("avatar");
                                    ViewPagerImagerAvtAdapter avtAdapter = new ViewPagerImagerAvtAdapter(getApplicationContext(), avt);
                                    viewPager2Avt.setAdapter(avtAdapter);
                                }
                            });
                        }
                    }
                });
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
                    String tenRecipe = snapshot.getString("name");
                    titlefood.setText(tenRecipe);


                    String picRecipe = snapshot.getString("image");

                    ViewPagerImageFoodAdapter imageAdapter = new ViewPagerImageFoodAdapter(getApplicationContext(), picRecipe);
                    viewPager2.setAdapter(imageAdapter);

                    TimerTask autoScrollTask = new AutoScrollTask(viewPager2);
                    Timer timer = new Timer();
                    timer.schedule(autoScrollTask, 2000, 2000);

                    heart.setText(String.valueOf(snapshot.get("like")));
                    save.setText(String.valueOf(snapshot.get("save")));
                    cook.setText(snapshot.getString("timecook")+" phút");
                    note.setText(snapshot.getString("note"));

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
                        ingreAdapter.setData(IngreList, FoodDetailActivity.this);
                        recycIngre.setAdapter(ingreAdapter);

                        donviAdapter.setData(IngreList, FoodDetailActivity.this);
                        recycDonVi.setAdapter(donviAdapter);

                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        }
    }

}