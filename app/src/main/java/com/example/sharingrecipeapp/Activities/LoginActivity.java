package com.example.sharingrecipeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    Button Login_btn;

    ProgressBar Login_progressbar;

    private FirebaseAuth Login_auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageButton returns = findViewById(R.id.login_returns);
        returns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Login_btn = findViewById(R.id.login_btn);

        email = findViewById(R.id.login_edt_email);
        password = findViewById(R.id.login_edt_password);
        Login_progressbar = findViewById(R.id.login_progressbar);
        Login_auth = FirebaseAuth.getInstance();
        Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Login_getemail, Login_getpassword;
                Login_getemail = email.getText().toString();
                Login_getpassword = password.getText().toString();
                if(TextUtils.isEmpty(Login_getemail))
                {
                    email.setError("Vui lòng nhập địa chỉ email");
                    return;
                }

                if(TextUtils.isEmpty(Login_getpassword))
                {
                    password.setError("Vui lòng nhập mật khẩu");
                    return;
                }
                if(Login_getpassword.length() < 6)
                {
                    password.setError("Mật khẩu tối thiểu 6 kí tự");
                }

                Login_progressbar.setVisibility(View.VISIBLE);
                Login_auth.signInWithEmailAndPassword(Login_getemail,Login_getpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Login_progressbar.setVisibility(View.GONE);
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        }
                        else {
                            Login_progressbar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không dúng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

    }
}