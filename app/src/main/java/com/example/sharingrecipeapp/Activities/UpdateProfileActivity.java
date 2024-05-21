package com.example.sharingrecipeapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.sharingrecipeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {
    BottomNavigationCustomActivity bottomNavigationCustomActivity;
    ImageButton return1;
    Button update;
    ImageView image_user_update;
    EditText edit_name, edit_pass;
    TextView edit_email;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseUser currentUser;
    FirebaseStorage firebaseStorage;
    Uri uri;
    String old_name, old_pass, old_avatarURL;
    ProgressBar progressBar;
    StorageReference img_user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        update = findViewById(R.id.update_btn);
        return1 = findViewById(R.id.returns1);
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
        edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateProfileActivity.this,"Không thể chỉnh sửa thông tin này",Toast.LENGTH_SHORT).show();
            }
        });
        showInfo();
        init_setListener();

    }

    public void showInfo() {
        if (currentUser != null) {
            String userID = currentUser.getUid();
            firestore = FirebaseFirestore.getInstance();
            DocumentReference userRef = firestore.collection("Users").document(userID);
            userRef.addSnapshotListener((value, error) -> {
                if (value != null && value.exists()) {
                    old_name = value.getString("username");
                    old_avatarURL = value.getString("avatar");
                    String old_email = value.getString("email");
                    old_pass = value.getString("password");
                    edit_name.setText(old_name);
                    edit_email.setText(old_email);
                    edit_pass.setText(old_pass);
                    Glide.with(this).load(old_avatarURL).error(R.drawable.round_account_circle).into(image_user_update);
                }
            });
        }
    }

    private void init_setListener() {
        image_user_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImg();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();

            }

        });
    }

    private void ChooseImg()
    {
        Intent chooseimg = new Intent();
        chooseimg.setAction(Intent.ACTION_GET_CONTENT);
        chooseimg.setType("image/*");
        ac.launch(Intent.createChooser(chooseimg,"Select picture"));
    }
    private final ActivityResultLauncher<Intent> ac = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>(){

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
                        image_user_update.setImageBitmap(bitmap);
                    }  catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    });
    private void onClickUpdateProfile() {
        String full_name = edit_name.getText().toString();
        String pass = edit_pass.getText().toString();
        if (full_name.isEmpty()) {
            Toast.makeText(UpdateProfileActivity.this, "Vui lòng nhập tên người dùng!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.isEmpty() || pass.length() < 6) {
            Toast.makeText(UpdateProfileActivity.this, "Mật khẩu tối thiểu 6 kí tự!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!full_name.equals(old_name) || uri!=null || !pass.equals(old_pass)){
            if(!full_name.equals(old_name)){
                updateName();
            }
            if(uri!=null){
                updateImage();
            }
            if(!pass.equals(old_pass)) {
                updatePass();
            }
            startActivity(new Intent(UpdateProfileActivity.this,LoginActivity.class));
        }else{
            Toast.makeText(UpdateProfileActivity.this, "Thông tin không có thay đổi", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateImage(){
        StorageReference img_user = FirebaseStorage.getInstance().getReference().child("user/"+uri.getLastPathSegment());
        UploadTask upload_img = img_user.putFile(uri);

        upload_img.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> downloadURL = taskSnapshot.getStorage().getDownloadUrl();
                downloadURL.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Map<String,Object> NewImg = new HashMap<>();
                        NewImg.put("avatar", uri.toString());
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Users")
                                .whereEqualTo("id", currentUser.getUid())
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                            String documentID = documentSnapshot.getId();
                                            db.collection("Users")
                                                    .document(documentID)
                                                    .update(NewImg)
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(UpdateProfileActivity.this, "Thất bại, vui lòng tử lại sau!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    private void updateName(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String full_name = edit_name.getText().toString();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(full_name)
                .build();
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> update_user = new HashMap<>();
                    update_user.put("username", full_name);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Users")
                            .whereEqualTo("id", user.getUid())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                        String documentID = documentSnapshot.getId();
                                        db.collection("Users")
                                                .document(documentID)
                                                .update(update_user)
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(UpdateProfileActivity.this, "Thất bại, vui lòng tử lại sau!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });
    }
    private void updatePass() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String pass = edit_pass.getText().toString();
        user.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Map<String, Object> update_pass = new HashMap<>();
                    update_pass.put("password", pass);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Users").whereEqualTo("id", user.getUid()).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                        String documentID = documentSnapshot.getId();
                                        db.collection("Users").document(documentID)
                                                .update(update_pass);
                                    }
                                }
                            });
                }else{
                    Toast.makeText(UpdateProfileActivity.this,"Xảy ra lỗi, vui lòng thử lại sau!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}