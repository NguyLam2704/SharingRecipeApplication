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

import com.example.sharingrecipeapp.Adapters.DetailRecipe.ListIngreInDetailAdapter;
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

    private int i = 0;

    ListIngreInDetailAdapter ingreAdapter;

    FirebaseFirestore firebaseFirestore;

    List<Ingredient> ingredientList;


    String idRecipe;

    ViewPager2 viewPager2, viewPager2Avt;
    ImageView back;

    TextView username, titlefood, heart, save, add, cook, note;

    RecyclerView recycIngre, recycMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        back = findViewById(R.id.backImg);
        viewPager2 = findViewById(R.id.viewpagerImage);
        viewPager2Avt = findViewById(R.id.viewPagerAvt);
        username = findViewById(R.id.nameUser);
        titlefood = findViewById(R.id.TitleFood_Detail);
        heart = findViewById(R.id.text_heart);
        save = findViewById(R.id.text_save);
        add = findViewById(R.id.text_add);
        cook = findViewById(R.id.text_time_detail);
        note = findViewById(R.id.text_Note);
        recycIngre = findViewById(R.id.recyNguyenLieu);
        recycMethod = findViewById(R.id.recyMethod);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext(), RecyclerView.VERTICAL, false);
        recycIngre.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManagerMethod = new LinearLayoutManager(this.getApplicationContext(), RecyclerView.VERTICAL, false);
        recycMethod.setLayoutManager(linearLayoutManagerMethod);


        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        idRecipe = intent.getStringExtra("id");


        getUsers(idRecipe);
        getRecipes(idRecipe);
        getListIngre(idRecipe);
        getListMethod(idRecipe);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodDetailActivity.this, BottomNavigationCustomActivity.class);
                startActivity(intent);
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
                                  String step = ("Bước ")+ (i+1)+ ": " +doc.get(i);
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

    private void getListIngre(String idRecipe) {


        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Recipes").document(idRecipe).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<DocumentReference> doc = (ArrayList<DocumentReference>) task.getResult().get("NguyenLieu");
                            ArrayList<Integer> SL = (ArrayList<Integer>) task.getResult().get("SL");
                            if(!doc.isEmpty()){
                                List<Ingredient> IngreList = new ArrayList<Ingredient>();
                                List<SoLuongIngre> slList = new ArrayList<>();
                                 for (DocumentReference x : doc){
                                    x.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot snapshot) {

                                            String sl = String.valueOf(SL.get(i));
                                            String id = snapshot.getString("id");
                                            String name = snapshot.getString("name");
                                            String image = snapshot.getString("img");
//                                            String donvi = snapshot.getString("donvi");
                                            IngreList.add(new Ingredient(id, name, image, " ", sl));
                                            recycIngre.setAdapter(new ListIngreInDetailAdapter(getApplicationContext(), IngreList));
                                            i++;
                                        }
                                    });

                                }
                            }
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

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

    }

}