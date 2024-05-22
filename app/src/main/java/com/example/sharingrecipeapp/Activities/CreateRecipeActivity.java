package com.example.sharingrecipeapp.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharingrecipeapp.Adapters.AddRecipe.AddMethodAdapter;
import com.example.sharingrecipeapp.Adapters.AddRecipe.AddNguyenLieuAdapter;
import com.example.sharingrecipeapp.Adapters.NguyenLieu.NewRcpIngreAdapter;
import com.example.sharingrecipeapp.Classes.AddMethod;
import com.example.sharingrecipeapp.Classes.AddNguyenLieu;
import com.example.sharingrecipeapp.Classes.NewRcpIngre;
import com.example.sharingrecipeapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import io.github.muddz.styleabletoast.StyleableToast;

public class CreateRecipeActivity extends AppCompatActivity {

    EditText method, nameNL, soluongNl, donviNL;

    AddMethodAdapter methodAdapter;
    List<AddMethod> methodList;

    AddNguyenLieuAdapter nguyenLieuAdapter;
    List<AddNguyenLieu> nguyenLieuList;
    RecyclerView recy_method, recy_nguyenlieu;
    ImageView btnBack;
    Button btnAddMethod, btnAddNguyenLieu, NewRcp_btn_upload;
    ImageView NewRcp_img_imgRcp;
    EditText NewRcp_edt_nameRcp, NewRcp_edt_time, NewRcp_edt_note;
    FirebaseFirestore NewRcp_db;
    FirebaseStorage NewRcp_stg;
    FirebaseAuth NewRcp_auth;
    Uri uri;
    Spinner NewRcp_spinnerIngre;
    ArrayList<NewRcpIngre> NewRcp_IngreList;
    CheckBox Chaua,Chauau,Vietnam,Thailan;
    int pos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        recy_method = findViewById(R.id.recyclerViewMedthod);
        recy_method.setLayoutManager(new LinearLayoutManager(this));
        recy_nguyenlieu = findViewById(R.id.recy_NL);
        recy_nguyenlieu.setLayoutManager(new LinearLayoutManager(this));

        nameNL = findViewById(R.id.editTextNameNL);
        soluongNl = findViewById(R.id.editTextslnl);
        donviNL = findViewById(R.id.editTextDonVi);
        Chaua = findViewById(R.id.chaua);
        Chauau = findViewById(R.id.chauau);
        Vietnam = findViewById(R.id.vietnam);
        Thailan = findViewById(R.id.thaiLan);

        method = findViewById(R.id.editTextMethod);
        btnBack = findViewById(R.id.btnback);
        btnAddMethod = findViewById(R.id.btnAddMethod);
        btnAddNguyenLieu = findViewById(R.id.btnAddNguyenLieu);

        methodList = new ArrayList<>();
        methodAdapter = new AddMethodAdapter();
        methodAdapter.setData(methodList);
        recy_method.setAdapter(methodAdapter);


        nguyenLieuList = new ArrayList<>();
        nguyenLieuAdapter = new AddNguyenLieuAdapter();
        nguyenLieuAdapter.setData(nguyenLieuList);
        recy_nguyenlieu.setAdapter(nguyenLieuAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();

                nguyenLieuList.remove(position);
                nguyenLieuAdapter.notifyDataSetChanged();
            }
        });

        itemTouchHelper.attachToRecyclerView(recy_nguyenlieu);

        NewRcp_edt_nameRcp = findViewById(R.id.newRcp_edt_name);
        NewRcp_img_imgRcp = findViewById(R.id.newRcp_img_imgRcp);
        NewRcp_btn_upload = findViewById(R.id.newRcp_btn_upload);
        NewRcp_edt_time = findViewById(R.id.newRcp_edt_time);
        NewRcp_edt_note = findViewById(R.id.newRcp_edt_note);

        NewRcp_spinnerIngre = findViewById(R.id.newRcp_spinner_ingre);
        NewRcp_IngreList = new ArrayList<>();

//      firebase init
        NewRcp_db = FirebaseFirestore.getInstance();
        NewRcp_stg = FirebaseStorage.getInstance();
        NewRcp_auth = FirebaseAuth.getInstance();
        FirebaseUser NewRcp_user = NewRcp_auth.getCurrentUser();
//      click img
        NewRcp_img_imgRcp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImg();
            }
        });

//        fetch list nguyenlieu
        NewRcp_db.collection("NguyenLieu").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null)
                {
                    return;
                }
                for (QueryDocumentSnapshot queryDocumentSnapshot : value)
                {
                    String ingre_name = queryDocumentSnapshot.getString("name");
                    String ingre_dv = queryDocumentSnapshot.getString("donvi");
                    String ingre_id = queryDocumentSnapshot.getString("id");
                    String ingre_img = queryDocumentSnapshot.getString("img");
                    NewRcp_IngreList.add(new NewRcpIngre(ingre_img,ingre_name,ingre_dv,ingre_id));
                }
                NewRcp_IngreList.add(new NewRcpIngre("Khac","Thêm...","add","add"));
                NewRcpIngreAdapter newRcpIngreAdapter = new NewRcpIngreAdapter(CreateRecipeActivity.this,R.layout.item_newrcpingre,NewRcp_IngreList);
                NewRcp_spinnerIngre.setAdapter(newRcpIngreAdapter);
                NewRcp_spinnerIngre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        donviNL.setText( NewRcp_IngreList.get(position).getDv());
                        pos = position;

                        if(position == NewRcp_IngreList.size()-1)
                        {
                            donviNL.setEnabled(true);
                            donviNL.setText("");
                            nameNL.setVisibility(View.VISIBLE);
                        }
                        else {
                            donviNL.setEnabled(false);
                            nameNL.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        });
////////////////////////////////////////////////////////////////////////////upload
        NewRcp_btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isChaua = Chaua.isSelected();
                boolean isChauau = Chauau.isSelected();
                boolean isVietnam = Vietnam.isSelected();
                boolean isThailan = Thailan.isSelected();

                String NewRcp_name = NewRcp_edt_nameRcp.getText().toString();
                if(TextUtils.isEmpty(NewRcp_name))
                {
                    NewRcp_edt_nameRcp.setError("Vui lòng nhập tên món ăn");
                    return;
                }
//                ImageView[] imgRcp = {NewRcp_img_imgRcp};
//                if(imgRcp.length == 0){
//                    Toast.makeText(CreateRecipeActivity.this, "Vui lòng thêm ảnh!!", Toast.LENGTH_LONG).show();
//                }


                if(nguyenLieuList.isEmpty())
                {
                    soluongNl.setError("Vui lòng nhập thông tin nguyên liệu");
                    return;
                }
                if(methodList.isEmpty())
                {
                    method.setError("Vui lòng nhập các bước thực hiện món ăn");
                    return;
                }
                String NewRcp_time = NewRcp_edt_time.getText().toString();
                if(TextUtils.isEmpty(NewRcp_time))
                {
                    NewRcp_edt_time.setError("Vui lòng nhập thời gian ước tính hoàn thành");
                    return;
                }
                String NewRcp_note = NewRcp_edt_note.getText().toString();
                StorageReference img_stg = NewRcp_stg.getReference().child("user/"+uri.getLastPathSegment());
                DocumentReference user = NewRcp_db.collection("Users").document(NewRcp_user.getUid());
                UploadTask upload_NewRcp_img = img_stg.putFile(uri);
                upload_NewRcp_img.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                StyleableToast.makeText(CreateRecipeActivity.this,"Vui lòng nhập đầy đủ thông tin",R.style.errortoast).show();
                                return;
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> dowloadURL = taskSnapshot.getStorage().getDownloadUrl();
                                dowloadURL.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Map<String,Object> NewRcp = new HashMap<>();
                                        NewRcp.put("name",NewRcp_name);
                                        NewRcp.put("id",unAccent(NewRcp_name.replace(" ","")));
                                        NewRcp.put("method",Arrays.asList(convertMethod(methodList)));
                                        NewRcp.put("NguyenLieu",Arrays.asList(convertNameIngre(nguyenLieuList)));
                                        NewRcp.put("SoLuong",Arrays.asList(convertslIngre(nguyenLieuList)));
                                        NewRcp.put("timecook",NewRcp_time);
                                        NewRcp.put("note",NewRcp_note);
                                        NewRcp.put("Users", user);
                                        NewRcp.put("chaua",isChaua);
                                        NewRcp.put("chauau",isChauau);
                                        NewRcp.put("vietnam",isVietnam);
                                        NewRcp.put("thailan",isThailan);
                                        NewRcp.put("image",uri.toString());
                                        if(TextUtils.isEmpty(uri.toString()))
                                        {
                                            Toast.makeText(CreateRecipeActivity.this, "Vui lòng nhập hình ảnh món ăn", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        DocumentReference CreateNewRcp = NewRcp_db.collection("Recipes").document(unAccent(NewRcp_name.replace(" ","")));
                                        CreateNewRcp.set(NewRcp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(CreateRecipeActivity.this, "successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        ///add to nguyenlieu
                                        NewRcp_stg.getReference().child("ingredients_icon/logo_gro.png").getDownloadUrl()
                                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        for (AddNguyenLieu ingre : nguyenLieuList) {
                                                            Map<String,Object> Newingre = new HashMap<>();
                                                            Newingre.put("id", unAccent(ingre.getName().replace(" ","")));
                                                            Newingre.put("donvi", ingre.getDonvi().toString());
                                                            Newingre.put("name",ingre.getName().toString());

                                                            DocumentReference CreateIngre = NewRcp_db.collection("NguyenLieu").document(unAccent(ingre.getName().replace(" ", "")));
                                                            CreateIngre.update(Newingre).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Toast.makeText(CreateRecipeActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Newingre.put("img",uri.toString());
                                                                            CreateIngre.set(Newingre).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {

                                                                                }
                                                                            });
                                                                        }
                                                                    });

                                                        }
                                                    }
                                                });

                                    }
                                });
                            }
                        });
                    Toast.makeText(CreateRecipeActivity.this, "success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateRecipeActivity.this, BottomNavigationCustomActivity.class);
                    startActivity(intent);
                    Map<String ,Object> NewRcp_addsave = new HashMap<>();
                    NewRcp_addsave.put("Recipes",unAccent(NewRcp_name.replace(" ","")));
                    NewRcp_addsave.put("idUsers",Arrays.asList());
                    NewRcp_db.collection("SaveRecipes").document(unAccent(NewRcp_name.replace(" ",""))).set(NewRcp_addsave)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        });

                }
        });
        btnAddNguyenLieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNguyenLieu();
            }
        });

        btnAddMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addmethod();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void addNguyenLieu() {
        String strSL = soluongNl.getText().toString().trim();
        if(TextUtils.isEmpty(strSL))
        {
            soluongNl.setError("Vui lòng nhập số lượng");
            return;
        }
        if(pos != NewRcp_IngreList.size()-1)
        {
            nguyenLieuList.add(new AddNguyenLieu(NewRcp_IngreList.get(pos),Double.parseDouble(strSL)));
        }
        else
        {
            String strName = nameNL.getText().toString().trim();
            if(TextUtils.isEmpty(strName))
            {
                nameNL.setError("Vui lòng nhập tên nguyên liệu");
                return;
            }
            String strDv = donviNL.getText().toString().trim();
            if (TextUtils.isEmpty(strDv))
            {
                donviNL.setText("Vui lòng nhập đơn vị");
                return;
            }
            String img = "https://firebasestorage.googleapis.com/v0/b/fantafood-3ea80.appspot.com/o/ingredients_icon%2Flogo_gro.png?alt=media&token=3deb24f9-1edb-4a88-8963-308278a9e9ee";
            nguyenLieuList.add(new AddNguyenLieu(new NewRcpIngre(img,strName,strDv,"add"),Double.parseDouble(strSL)));
        }
        nguyenLieuAdapter.notifyDataSetChanged();
        recy_nguyenlieu.scrollToPosition(nguyenLieuList.size() - 1);

        if(pos == NewRcp_IngreList.size()-1)
        {

            donviNL.setText("");
            nameNL.setText("");
            soluongNl.setText("");
//            nameNL.setVisibility(View.GONE);
        }
        else {
            nameNL.setText("");
            soluongNl.setText("");
            donviNL.setText( NewRcp_IngreList.get(pos).getDv());
        }
    }

    private void addmethod() {
        String strMethod = method.getText().toString().trim();
        if(TextUtils.isEmpty(strMethod)){
            method.setError("Vui lòng nhập các bước thực hiện");
            return;
        }

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();

                methodList.remove(position);
                methodAdapter.notifyDataSetChanged();
            }
        });

        itemTouchHelper.attachToRecyclerView(recy_method);
        methodList.add(new AddMethod(strMethod));
        methodAdapter.notifyDataSetChanged();
        recy_method.scrollToPosition(methodList.size() - 1);

        method.setText("");

    }
//    //////////////////////////////////upload img
    private void ChooseImg()
    {
        Intent chooseimg = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(chooseimg,"Select picture"));
    }
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>(){

        @Override
        public void onActivityResult(ActivityResult o) {
            if(o.getResultCode() == RESULT_OK)
            {
                Intent intent = o.getData();
                if(intent != null)
                {
                    uri = intent.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        NewRcp_img_imgRcp.setImageBitmap(bitmap);
                    }  catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else
                {
                    StyleableToast.makeText(CreateRecipeActivity.this,"Vui lòng nhập hình ảnh món ăn",R.style.errortoast).show();
                    return;
                }
            }
        }
    });

//    chuan hoa chuoi
    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        if (s.equals("Đ") || s.equals("đ"))
        {
            return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "d").replace(" ","");
        }
        return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D').replace(" ","");

    }

    //get array method
    private String[] convertMethod (List<AddMethod> methodList)
    {
        ArrayList<String>  methods = new ArrayList<String>();
        for (AddMethod singlemethod : methodList)
        {
            methods.add(singlemethod.getMethod().toString());
        }
        String[] arrayMethod = methods.toArray(new String[0]);
        return arrayMethod;
    }
    /////////////////////////////////////////////////////////////////
    // get array name ingre
    private String[] convertNameIngre (List<AddNguyenLieu> ingreList)
    {
        ArrayList<String> ingres = new ArrayList<String>();
        for (AddNguyenLieu ingre : ingreList)
        {
            ingres.add(unAccent(ingre.getName().toString()));
        }
        String[] arrNameingre = ingres.toArray(new String[0]);
        return arrNameingre;
    }
    //////////////////////////////////////////////////////////////
    // get array donvi ingre
    private String[] convertDonviIngre (List<AddNguyenLieu> ingreList)
    {
        ArrayList<String> ingres = new ArrayList<String>();
        for (AddNguyenLieu ingre : ingreList)
        {
            ingres.add(ingre.getDonvi().toString());
        }
        String[] arrDonvingre = ingres.toArray(new String[0]);
        return arrDonvingre;
    }
    //////////////////////////////////////////////////////////////
    // get array sl ingre
    private Number[] convertslIngre (List<AddNguyenLieu> ingreList)
    {
        ArrayList<Number> ingres = new ArrayList<Number>();
        for (AddNguyenLieu ingre : ingreList)
        {
            ingres.add(ingre.getSoluong());
        }
        Number[] arrSlngre = ingres.toArray(new Number[0]);
        return arrSlngre;
    }

    private String convertImageViewToString(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}

