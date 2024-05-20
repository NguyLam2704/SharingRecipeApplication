package com.example.sharingrecipeapp.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.reflect.Array;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CreateRecipeActivity extends AppCompatActivity {

    EditText method, nameNL, soluongNl, donviNL;

    AddMethodAdapter methodAdapter;
    List<AddMethod> methodList;

    AddNguyenLieuAdapter nguyenLieuAdapter;
    List<AddNguyenLieu> nguyenLieuList;
    RecyclerView recy_method, recy_nguyenlieu;
    ImageView btnBack;
    TextView btnAddMethod, btnAddNguyenLieu;
    Button NewRcp_btn_upload;
    ImageView NewRcp_img_imgRcp;
    EditText NewRcp_edt_nameRcp, NewRcp_edt_time, NewRcp_edt_note;
    FirebaseFirestore NewRcp_db;
    FirebaseStorage NewRcp_stg;
    FirebaseAuth NewRcp_auth;
    Uri uri;
    Spinner NewRcp_spinnerIngre;
    ArrayList<NewRcpIngre> NewRcp_IngreList;
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
                NewRcpIngreAdapter newRcpIngreAdapter = new NewRcpIngreAdapter(CreateRecipeActivity.this,R.layout.item_newrcpingre,NewRcp_IngreList);
                NewRcp_spinnerIngre.setAdapter(newRcpIngreAdapter);

            }
        });
//////////////////////////////////////////////////////////////////////////upload
        NewRcp_btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NewRcp_name = NewRcp_edt_nameRcp.getText().toString();
                String NewRcp_time = NewRcp_edt_time.getText().toString();
                String NewRcp_note = NewRcp_edt_note.getText().toString();
                StorageReference img_stg = NewRcp_stg.getReference().child("user/"+uri.getLastPathSegment());
                DocumentReference user = NewRcp_db.collection("Users").document(NewRcp_user.getUid());
                UploadTask upload_NewRcp_img = img_stg.putFile(uri);
                upload_NewRcp_img.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

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
                                        NewRcp.put("image",uri.toString());
                                        DocumentReference CreateNewRcp = NewRcp_db.collection("Recipes").document(unAccent(NewRcp_name.replace(" ","")));
                                        CreateNewRcp.set(NewRcp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(CreateRecipeActivity.this, "successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        ///add to nguyenlieu
                                        for (AddNguyenLieu ingre : nguyenLieuList) {
                                            Map<String,Object> Newingre = new HashMap<>();
                                            Newingre.put("id", unAccent(ingre.getName().replace(" ","")));
                                            Newingre.put("donvi", ingre.getDonvi().toString());
                                            Newingre.put("name",ingre.getName().toString());
                                            DocumentReference CreateIngre = NewRcp_db.collection("NguyenLieu").document(unAccent(ingre.getName().replace(" ", "")));
                                            CreateIngre.update(Newingre).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(CreateRecipeActivity.this, "ok", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
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
                                Toast.makeText(CreateRecipeActivity.this, "success", Toast.LENGTH_SHORT).show();
                            }
                        });
//                img_stg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        String NewRcp_name = NewRcp_edt_nameRcp.getText().toString();
//                        Map<String,Object> NewRcp = new HashMap<>();
//                        NewRcp.put("name",NewRcp_name);
//                        NewRcp.put("image",uri.toString());
//                        DocumentReference CreateNewRcp = NewRcp_db.collection("Recipes").document(unAccent(NewRcp_name));
//                        CreateNewRcp.set(NewRcp).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(CreateRecipeActivity.this, "success", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                });







//        ///////////////////////////////////////////////////////////////////////
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
        String strName = nameNL.getText().toString().trim();
        String strSL = soluongNl.getText().toString().trim();
        String strDonVi = donviNL.getText().toString().trim();

        if(TextUtils.isEmpty(strName) || TextUtils.isEmpty(strSL) || TextUtils.isEmpty(strDonVi)){
            return;
        }

        nguyenLieuList.add(new AddNguyenLieu(strName, Double.parseDouble(strSL), strDonVi));
        nguyenLieuAdapter.notifyDataSetChanged();
        recy_nguyenlieu.scrollToPosition(nguyenLieuList.size() - 1);

        nameNL.setText("");
        soluongNl.setText("");
        donviNL.setText("");
    }

    private void addmethod() {
        String strMethod = method.getText().toString().trim();
        if(TextUtils.isEmpty(strMethod)){
            return;
        }

        methodList.add(new AddMethod(strMethod));
        methodAdapter.notifyDataSetChanged();
        recy_method.scrollToPosition(methodList.size() - 1);

        method.setText("");

    }
    //////////////////////////////////upload img
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
            }
        }
    });
    ////////////////////////////////////////////////////////////////////
//    chuan hoa chuoi
    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        if (s.equals("Đ") || s.equals("đ"))
        {
            return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "d");
        }
        return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D').replace(" ","").replace("A","a")
                .replace("Z","z").replace("B","b").replace("C","c").replace("D","d").replace("I","i").replace("F","f")
                .replace("G","g").replace("H","h").replace("E","e").replace("J","j").replace("K","k").replace("Y","y")
                .replace("L","l").replace("M","m").replace("N","n").replace("O","o").replace("P","p").replace("Q","q").replace("R","r")
                .replace("S","s").replace("T","t").replace("U","u").replace("V","v").replace("W","w").replace("X","x");

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

}

////////
