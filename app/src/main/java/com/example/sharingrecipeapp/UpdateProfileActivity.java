package com.example.sharingrecipeapp;

import static com.example.sharingrecipeapp.Fragments.UserFragment.MY_REQUEST_CODE;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.sharingrecipeapp.Fragments.UserFragment;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UpdateProfileActivity extends AppCompatActivity {
    ImageButton return1;
    Button update;
    ImageView image_user_update;
    EditText edit_name, edit_pass;
    TextView edit_email;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseUser currentUser;
    Uri uri;
    String old_name, old_pass, old_avatarURL;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == RESULT_OK) {
                Intent intent = o.getData();
                if (intent != null) {
                    uri = intent.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        image_user_update.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    });


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
                openGallery();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();

            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent, "Select picture"));
    }

    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String full_name = edit_name.getText().toString();
        String email = edit_email.getText().toString();
        String pass = edit_pass.getText().toString();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(full_name)
                .setPhotoUri(uri)
                .build();
        if (full_name.isEmpty()) {
            Toast.makeText(UpdateProfileActivity.this, "Vui lòng nhập tên người dùng!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(UpdateProfileActivity.this, "Vui lòng nhập email dùng!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.isEmpty() || pass.length() < 6) {
            Toast.makeText(UpdateProfileActivity.this, "Mật khẩu tối thiểu 6 kí tự!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(UpdateProfileActivity.this, "Email không tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!full_name.equals(old_name) || uri != null){
            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Map<String, Object> update_user = new HashMap<>();
                        if (full_name != old_name)
                            update_user.put("username", full_name);
                        if (uri != null) {
                            update_user.put("avatar", uri);
                        }
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Users")
                                .whereEqualTo("username", old_name).whereEqualTo("avatar", old_avatarURL)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                            String documentID = documentSnapshot.getId();
                                            db.collection("Users")
                                                    .document(documentID)
                                                    .update(update_user)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(UpdateProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
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
        if(!pass.equals(old_pass)) {
            user.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Map<String, Object> update_pass = new HashMap<>();
                    update_pass.put("password", pass);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Users").whereEqualTo("password", old_pass).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                        String documentID = documentSnapshot.getId();
                                        db.collection("Users").document(documentID)
                                                .update(update_pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        finish();
                                                    }
                                                });
                                    }
                                }
                            });
                }
            });
        }
    }

}