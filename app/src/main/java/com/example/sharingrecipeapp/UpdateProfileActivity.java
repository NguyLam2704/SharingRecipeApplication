package com.example.sharingrecipeapp;

import static com.example.sharingrecipeapp.Fragments.UserFragment.MY_REQUEST_CODE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;

public class UpdateProfileActivity extends AppCompatActivity {
    ImageButton return1;
    Button update;
    ImageView image_user_update;
    EditText edit_name, edit_email, edit_pass;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseUser currentUser;
    Uri uri;
    ActivityResultLauncher<Intent> activityResultLauncher =registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if(o.getResultCode() == RESULT_OK){
                Intent intent = o.getData();
                if(intent == null)
                    return;
                uri = intent.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    setBitmap(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        return1=findViewById(R.id.returns1);
        image_user_update = findViewById(R.id.image_user_upd);
        edit_name = findViewById(R.id.edit_name);
        edit_email = findViewById(R.id.edt_email);
        edit_pass = findViewById(R.id.edit_pass);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        return1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showInfo();
//        init_setListener();
    }
    public void showInfo(){
        if (currentUser != null) {
            String userID = currentUser.getUid();
            firestore = FirebaseFirestore.getInstance();
            DocumentReference userRef = firestore.collection("Users").document(userID);
            userRef.addSnapshotListener((value, error) -> {
                if(value!=null && value.exists()){
                    String name = value.getString("username");
                    String avatarURL = value.getString("avatar");
                    String email = value.getString("email");
                    String pass = value.getString("password");
                    edit_name.setText(name);
                    edit_email.setText(email);
                    edit_pass.setText(pass);
                    Glide.with(this).load(avatarURL).error(R.drawable.round_account_circle).into(image_user_update);
                }
            });
        }
    }
    private void init_setListener(){
        image_user_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();
            }

        });
    }
    private void onClickRequestPermission(){
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            openGallery();
        }else{
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission,MY_REQUEST_CODE);
        }
    }



    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String full_name = edit_name.getText().toString().trim();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(full_name)
                .setPhotoUri(uri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showInfo();
                            Toast.makeText(UpdateProfileActivity.this,"thanh cong",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }
    public void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent,"Chọn ảnh"));

    }
    private  void setBitmap(Bitmap bitmap){
        image_user_update.setImageBitmap(bitmap);
    }

}