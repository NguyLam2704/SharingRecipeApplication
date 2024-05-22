package com.example.sharingrecipeapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.sharingrecipeapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import io.github.muddz.styleabletoast.StyleableToast;

public class Forgotpass extends AppCompatActivity {

    ImageButton Forgot_return ;
    EditText Forgot_edt_email;
    Button Forgot_btn;
    ProgressBar Forgot_prgbar;
    FirebaseAuth Forgot_auth;
    String Forgot_getemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        Forgot_return = findViewById(R.id.forgotpass_returns);
        Forgot_edt_email = findViewById(R.id.forgotpass_edt_email);
        Forgot_btn = findViewById(R.id.forgotpass_btn);
        Forgot_prgbar = findViewById(R.id.forgotpass_prgbar);
        Forgot_auth = FirebaseAuth.getInstance();
        Forgot_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Forgot_edt_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) Forgot_edt_email.setBackgroundResource(R.drawable.query_bound);
                else  Forgot_edt_email.setBackgroundResource(R.drawable.edittext_bound);
            }
        });
        Forgot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Forgot_getemail = Forgot_edt_email.getText().toString();
                if(TextUtils.isEmpty(Forgot_getemail))
                {
                    Forgot_edt_email.setError("Vui lòng nhập email");
                    return;
                }
                else
                {
                    ResetPassword();
                }
            }
        });


    }

    private void ResetPassword()
    {
        Forgot_prgbar.setVisibility(View.VISIBLE);

        Forgot_auth.sendPasswordResetEmail(Forgot_getemail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Forgot_prgbar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(Forgotpass.this);
                builder.setTitle("Thông báo");
                builder.setMessage("Vui lòng kiểm tra email và xác nhận mật khẩu mới trước khi đăng nhập");
                builder.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Forgotpass.this,LoginActivity.class);
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
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Forgot_prgbar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Forgotpass.this);
                        builder.setTitle("Thông báo");
                        builder.setMessage("Không tìm thấy tài khoản");
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
                });
    }
}