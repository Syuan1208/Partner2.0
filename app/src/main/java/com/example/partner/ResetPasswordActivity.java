package com.example.partner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText send_email;
    Button btn_reset, btn_retrun;
    FirebaseAuth firebaseAuth;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        //定義
        send_email = findViewById(R.id.send_email);
        btn_reset = findViewById(R.id.btn_reset);
        firebaseAuth = FirebaseAuth.getInstance();
        btn_retrun = findViewById(R.id.btn_return);
        imageView=findViewById(R.id.reset_image);
        //重製密碼按鈕
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = send_email.getText().toString();
                if (email.equals("")) {
                    Toast.makeText(ResetPasswordActivity.this, "請輸入郵件", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                imageView.setImageResource(R.drawable.success);
                                Toast.makeText(ResetPasswordActivity.this, "請去信箱確認郵件", Toast.LENGTH_SHORT).show();
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(ResetPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        //返回按鈕
        btn_retrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPasswordActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });
    }
}