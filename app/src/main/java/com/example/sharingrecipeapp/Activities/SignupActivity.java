package com.example.sharingrecipeapp.Activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sharingrecipeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.github.muddz.styleabletoast.StyleableToast;

public class SignupActivity extends AppCompatActivity  {

    EditText email, username, password;
    boolean passwordVisible;

    private FirebaseAuth Signup_auth;

    FirebaseFirestore Signup_db;

    ProgressBar Signup_progressbar;
    String userID;
    Button Signup_btn;
//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.signup_returns)
//        {
//            finish();
//        }
//        if (v.getId() == R.id.Login_btn)
//        {
//            Intent login_view = new Intent(SignupActivity.this, LoginActivity.class);
//            startActivity(login_view);
//        }
//
//    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ImageButton returns = findViewById(R.id.signup_returns);
        returns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        Signup_btn = findViewById(R.id.signup_btn);

        Signup_auth = FirebaseAuth.getInstance();
        Signup_db = FirebaseFirestore.getInstance();
        email = findViewById(R.id.signup_edt_email);
        password = findViewById(R.id.signup_edt_password);
        username = findViewById(R.id.signup_edt_username);
        Signup_progressbar = findViewById(R.id.signup_progressbar);
        Signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Signup_getemail, Signup_getpassword, Signup_getusername;
                Signup_getemail = email.getText().toString();
                Signup_getpassword = password.getText().toString();
                Signup_getusername = username.getText().toString();

                if(TextUtils.isEmpty(Signup_getemail))
                {
                    email.setError("Vui lòng nhập email");
                    return;
                }

                if(TextUtils.isEmpty(Signup_getpassword))
                {
                    password.setError("Vui lòng nhập mật khẩu");
                    return;
                }

                if(TextUtils.isEmpty(Signup_getusername))
                {
                    username.setError("Vui lòng nhập tên người dùng");
                    return;
                }

                if(password.length() < 6)
                {
                    password.setError("Mật khẩu tối thiểu gồm 6 kí tự");
                    return;
                }

                Signup_progressbar.setVisibility(View.VISIBLE);

                Signup_auth.createUserWithEmailAndPassword(Signup_getemail,Signup_getpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser verify_user = Signup_auth.getCurrentUser();
                            verify_user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    StyleableToast.makeText(SignupActivity.this,"Email xác thực đã được gửi ",R.style.mytoast).show();
                                    Signup_progressbar.setVisibility(View.GONE);
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            StyleableToast.makeText(SignupActivity.this,"Gửi email xác thực thất bại",R.style.errortoast).show();
                                        }
                                    });
//                                StyleableToast.makeText(SignupActivity.this,"Xác thực thành công",R.style.mytoast).show();
                                userID = Signup_auth.getCurrentUser().getUid();
                                DocumentReference Signup_document = Signup_db.collection("Users").document(userID);
                                Map<String,Object> new_user = new HashMap<>();

                                new_user.put("email",Signup_getemail);
                                new_user.put("username",Signup_getusername);
                                new_user.put("password",Signup_getpassword);
                                new_user.put("id",userID);
                                new_user.put("avatar","");
                                Signup_document.set(new_user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Signup_progressbar.setVisibility(View.GONE);
                                        Button Login_btn = findViewById(R.id.Login_btn);
                                        Signup_auth.signOut();

                                        Intent login_view = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(login_view);
//                                        Login_btn.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                Intent login_view = new Intent(SignupActivity.this, BottomNavigationCustomActivity.class);
//                                                startActivity(login_view);
//                                            }
//                                        });
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + Signup_document.getId());
                                    }
                                });

                        }
                        else
                        {
                            StyleableToast.makeText(SignupActivity.this,"Tài khoản đã tồn tại",Toast.LENGTH_LONG,R.style.errortoast).show();
                            Signup_progressbar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    if(event.getRawX() >= password.getRight()-password.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = password.getSelectionEnd();
                        if(passwordVisible)
                        {
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        }
                        else
                        {
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

    }
}