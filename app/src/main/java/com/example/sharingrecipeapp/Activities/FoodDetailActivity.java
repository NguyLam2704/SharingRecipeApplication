package com.example.sharingrecipeapp.Activities;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharingrecipeapp.Adapters.AddToGrocery.AdapterAddToGro;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.Ingre.ListIngreInDetailAdapterDonVi;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.Ingre.ListIngreInDetailAdapterName;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.Ingre.ListIngreInDetailAdapterSoLuong;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.ListMethodInDetailAdapter;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.ViewPagerImageFoodAdapter;
import com.example.sharingrecipeapp.Adapters.DetailRecipe.ViewPagerImagerAvtAdapter;
import com.example.sharingrecipeapp.Classes.AutoScrollTask;
import com.example.sharingrecipeapp.Classes.Ingredient;
import com.example.sharingrecipeapp.Classes.Method;
import com.example.sharingrecipeapp.Classes.NguyenLieu;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.Classes.SoLuongIngre;
import com.example.sharingrecipeapp.Fragments.NonUserFragment;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.github.muddz.styleabletoast.StyleableToast;

public class FoodDetailActivity extends AppCompatActivity {
    ListIngreInDetailAdapterName ingreAdapter;
    ListIngreInDetailAdapterDonVi donviAdapter;
    FirebaseFirestore firebaseFirestore;
    List<Ingredient> IngreList;
    List<String> ingres;
    ImageView FdDetail_like_btn, FdSDetail_save_btn;
    String idRecipe;
    ViewPager2 viewPager2, viewPager2Avt;
    BottomNavigationCustomActivity bottomNavigationCustomActivity;
    ImageView back, btncook;
    TextView username, titlefood, heart, save, timecook, note;
    RecyclerView recycIngre, recycSoLuong, recycDonVi, recycMethod;
    FirebaseUser current_user;
    SharedPreferences sharedPreferences;

    ImageView btnAddGro;
    SharedPreferences.Editor editor_like, editor_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        Intent intent = getIntent();
        idRecipe = intent.getStringExtra("id");
        sharedPreferences = getSharedPreferences("pref",0);
        boolean check_like = sharedPreferences.getBoolean("like"+idRecipe,false);
        editor_like = sharedPreferences.edit();
        FdDetail_like_btn = findViewById(R.id.like_btn);
        FdDetail_like_btn.setSelected(check_like);

        boolean check_save = sharedPreferences.getBoolean("save"+idRecipe,false);
        editor_save = sharedPreferences.edit();
        FdSDetail_save_btn = findViewById(R.id.detail_save_btn);
        FdSDetail_save_btn.setSelected(check_save);

        back = findViewById(R.id.backImg);
        viewPager2 = findViewById(R.id.viewpagerImage);
        viewPager2Avt = findViewById(R.id.viewPagerAvt);
        username = findViewById(R.id.nameUser);
        titlefood = findViewById(R.id.TitleFood_Detail);
        heart = findViewById(R.id.like_text);
        save = findViewById(R.id.text_save);
        btncook = findViewById(R.id.text_cook);
        timecook = findViewById(R.id.text_time_detail);
        note = findViewById(R.id.text_Note);
        recycIngre = findViewById(R.id.recyNguyenLieu);
        recycMethod = findViewById(R.id.recyMethod);
        recycSoLuong = findViewById(R.id.recySoLuong);
        recycDonVi = findViewById(R.id.recyDonVi);
        btnAddGro = findViewById(R.id.btnAddGro);


        //Recycler không scroll
        recycIngre.setNestedScrollingEnabled(false);
        recycMethod.setNestedScrollingEnabled(false);
        recycSoLuong.setNestedScrollingEnabled(false);
        recycDonVi.setNestedScrollingEnabled(false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        if(current_user == null)
        {
            FdSDetail_save_btn.setSelected(false);
            FdDetail_like_btn.setSelected(false);
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext(), RecyclerView.VERTICAL, false);
        recycIngre.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManagerMethod = new LinearLayoutManager(this.getApplicationContext(), RecyclerView.VERTICAL, false);
        recycMethod.setLayoutManager(linearLayoutManagerMethod);
        recycSoLuong.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(),RecyclerView.VERTICAL,false));
        recycDonVi.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(),RecyclerView.VERTICAL,false));


        getSoluongLike(idRecipe);
        getSoluongSave(idRecipe);
        getUsers(idRecipe);
        getRecipes(idRecipe);
        getSoLuongIngre(idRecipe);
        getListMethod(idRecipe);



        btncook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FoodDetailActivity.this, Method_Silder_Activity.class);
                intent.putExtra("id", idRecipe);
                startActivity(intent);
            }
        });

        btnAddGro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_user != null) {
                    Dialog dialog = new Dialog(FoodDetailActivity.this);
                    dialog.setContentView(R.layout.dialog_addtogrocery);
                    displayDialogAddToGro(dialog);
                    dialog.show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FoodDetailActivity.this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Vui lòng đăng nhập để tiếp tục");
                    builder.setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(FoodDetailActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface abc) {
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.color_primary));
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.color_primary));
                        }
                    });
                    dialog.show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent turnHomeFrag = new Intent();
                turnHomeFrag.putExtra("id",idRecipe);
                turnHomeFrag.putExtra("save",save.getText().toString());
                setResult(111,turnHomeFrag);
                finish();
                //reload();

            }
        });

        FdSDetail_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_user == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(FoodDetailActivity.this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Vui lòng đăng nhập để tiếp tục");
                    builder.setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(FoodDetailActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface abc) {
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.color_primary));
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.color_primary));
                        }
                    });
                    dialog.show();

                }
                else {
                    if (FdSDetail_save_btn.isSelected()) {
//                        int update_text_save = Integer.parseInt(save.getText().toString()) - 1;
//                        save.setText(String.valueOf(update_text_save));
                        //save state
                        FdSDetail_save_btn.setSelected(false);
                        editor_save.putBoolean("save" + idRecipe, false);
                        //update firestore
                        DocumentReference minus_save = firebaseFirestore.collection("SaveRecipes").document(idRecipe);
                        minus_save.update("idUsers", FieldValue.arrayRemove(String.valueOf(current_user.getUid())));
                    } else {
//                        int update_text_save = Integer.parseInt(save.getText().toString()) + 1;
//                        save.setText(String.valueOf(update_text_save));
                        //save state
                        FdSDetail_save_btn.setSelected(true);
                        editor_save.putBoolean("save" + idRecipe, true);
                        //update firestore
                        Map<String, Object> saverecipe = new HashMap<>();
                        saverecipe.put("Recipes", idRecipe);
                        firebaseFirestore.collection("SaveRecipes").document(idRecipe).update(saverecipe)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                    Log.d(TAG, "update saverecipes successfully");
                                    DocumentReference plus_save = firebaseFirestore.collection("SaveRecipes").document(idRecipe);
                                    plus_save.update("idUsers", FieldValue.arrayUnion(String.valueOf(current_user.getUid())));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        firebaseFirestore.collection("SaveRecipes").document(idRecipe).set(saverecipe).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                DocumentReference plus_save = firebaseFirestore.collection("SaveRecipes").document(idRecipe);
                                                plus_save.update("idUsers", FieldValue.arrayUnion(String.valueOf(current_user.getUid())));
                                            }
                                        });
                                    }
                                });
                    }
                    editor_save.commit();
                }
            }
        });

        FdDetail_like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_user == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(FoodDetailActivity.this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Vui lòng đăng nhập để tiếp tục");
                    builder.setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(FoodDetailActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface abc) {
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.color_primary));
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.color_primary));
                        }
                    });
                    dialog.show();
                } else {
                    if (FdDetail_like_btn.isSelected()) {
//                    int update_text_like = Integer.parseInt(heart.getText().toString()) - 1;
//                    heart.setText(String.valueOf(update_text_like));
                        //save state
                        FdDetail_like_btn.setSelected(false);
                        editor_like.putBoolean("like" + idRecipe, false);
                        //update Likes firestore
                        DocumentReference minus_like = firebaseFirestore.collection("Likes").document(idRecipe);
                        minus_like.update("idUsers", FieldValue.arrayRemove(String.valueOf(current_user.getUid())));
                    } else {
//                    int update_text_like = Integer.parseInt(heart.getText().toString()) + 1;
//                    heart.setText(String.valueOf(update_text_like));
                        //save state
                        FdDetail_like_btn.setSelected(true);
                        editor_like.putBoolean("like" + idRecipe, true);
                        //update Likes firestore
                        Map<String, Object> Likes = new HashMap<>();
                        Likes.put("Recipes", idRecipe);
//
                        firebaseFirestore.collection("Likes").document(idRecipe).update(Likes).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "update like successfully");
                                DocumentReference plus_like = firebaseFirestore.collection("Likes").document(idRecipe);
                                plus_like.update("idUsers", FieldValue.arrayUnion(String.valueOf(current_user.getUid())));
                            }

                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        firebaseFirestore.collection("Likes").document(idRecipe).set(Likes).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                DocumentReference plus_like = firebaseFirestore.collection("Likes").document(idRecipe);
                                                plus_like.update("idUsers", FieldValue.arrayUnion(String.valueOf(current_user.getUid())));
                                            }
                                        });
                                    }
                                });
                    }
                    editor_like.commit();
                }
            }
        });
    }

    List<NguyenLieu> listNL_AddToGro;
    List<String> tenNL;
    private void displayDialogAddToGro(Dialog dialog) {
        //anh xa va khoi tao
        RecyclerView rvAddToGro = dialog.findViewById(R.id.recyAddtoGro);
        listNL_AddToGro = new ArrayList<>();
        ArrayList<Boolean> check = new ArrayList<>();
        AdapterAddToGro adapterAddToGro = new AdapterAddToGro(listNL_AddToGro);
        adapterAddToGro.setCheck(check);
        rvAddToGro.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        rvAddToGro.setAdapter(adapterAddToGro);

        //lay du lieu tu database
        for (int i = 0; i < soLuongIngreList.size();i++){
            check.add(new Boolean(true));
            String name = IngreList.get(i).getName();
            String donvi = IngreList.get(i).getDonvi();
            String img = IngreList.get(i).getImg();
            double sl = soLuongIngreList.get(i).getSoluong().doubleValue();
            String id = IngreList.get(i).getId();

            NguyenLieu nl = new NguyenLieu(sl,donvi,id,name,img);
            listNL_AddToGro.add(nl);
            adapterAddToGro.notifyDataSetChanged();
        }

        dialog.findViewById(R.id.btnAddToGro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean empty = true;
                for (int i = 0; i < check.size(); i++){
                    if (check.get(i)){
                        updateFireStore(listNL_AddToGro.get(i));
                        empty = false;
                    }
                }
                if (!empty){
                    StyleableToast.makeText(getBaseContext(),"Thêm nguyên liệu đã chọn thành công",R.style.mytoast).show();
                }
                dialog.dismiss();
            }
        });
    }

    private void updateFireStore(NguyenLieu nguyenLieu) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Map<String,Object> data = new HashMap<>();
        data.put("name",nguyenLieu.getName());
        data.put("img",nguyenLieu.getImg());
        data.put("donvi",nguyenLieu.getDonvi());
        data.put("SL",nguyenLieu.getSL());
        data.put("idUser",auth.getUid());
        firebaseFirestore.collection("ListNguyenLieuMua").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Map<String,Object> updateID = new HashMap<>();
                updateID.put("id",documentReference.getId());
                documentReference.update(updateID);
            }
        });


    }


    private void getSoluongLike(String idRecipe)
    {
        firebaseFirestore.collection("Likes").whereEqualTo("Recipes",idRecipe).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error !=  null )
                {
                    return;
                }
                ArrayList<String> idUsers = new ArrayList<>();
                for (QueryDocumentSnapshot doc :value)
                {

                    if(doc.get("idUsers") != null && doc != null)
                    {
                        idUsers = (ArrayList<String>) doc.get("idUsers");
                        heart.setText((String.valueOf(idUsers.size())));
                    }
                }


            }
        });


    }


    public void getSoluongSave(String idRecipe)
    {
        firebaseFirestore.collection("SaveRecipes").whereEqualTo("Recipes",idRecipe).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error !=  null )
                {
                    return;
                }

                ArrayList<String> idUser = new ArrayList<>();
                for (QueryDocumentSnapshot doc :value)
                {
                    if(doc.get("idUsers") != null)
                    {
                        idUser = (ArrayList<String>) doc.get("idUsers");
                    }

                    save.setText((String.valueOf(idUser.size())));
                }
            }
        });
    }


    List<SoLuongIngre> soLuongIngreList;
    private void getSoLuongIngre(String idRecipe) {
        firebaseFirestore.collection("Recipes").document(idRecipe).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<Number> doc = (ArrayList<Number>) task.getResult().get("SoLuong");
                            if(!doc.isEmpty()){
                                soLuongIngreList = new ArrayList<SoLuongIngre>();
                                for (int i = 0;i<doc.size();i++){
                                    Number sl = doc.get(i);
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
                                  String step = doc.get(i);
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
                    timecook.setText(snapshot.getString("timecook")+" phút");
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
                        ingreAdapter.setData(IngreList);
                        recycIngre.setAdapter(ingreAdapter);
                        donviAdapter.setData(IngreList);
                        recycDonVi.setAdapter(donviAdapter);
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        }
    }

    public void gotoStepSlider(String idRecipe) {
        Intent intent = new Intent(FoodDetailActivity.this, Method_Silder_Activity.class);
        intent.putExtra("id", idRecipe);
        startActivity(intent);
    }


}