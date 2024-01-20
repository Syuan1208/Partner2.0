package com.example.partner;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText username, email, password,studentID,classname,department,academic;
    Button btn_register;
    FirebaseAuth auth;
    DatabaseReference reference;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button confirm;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //得到toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("註冊");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //註冊資料
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        academic=findViewById(R.id.academic);
        department=findViewById(R.id.department);
        classname=findViewById(R.id.classname);
        studentID=findViewById(R.id.studentID);
        //註冊按鈕
        btn_register = (Button) findViewById(R.id.register);
        //選擇按鈕
        radioGroup = findViewById(R.id.radioGroup);
        auth = FirebaseAuth.getInstance();
        //註冊按鈕
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                //身分選擇
                int career = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(career);
                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || radioButton.getText().equals("")) {
                    Toast.makeText(RegisterActivity.this, "請填寫全部資料", Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_username, txt_email, txt_password);
                }

            }
        });
    }

    //註冊功能
    private void register(String username, String email, String password) {
        String career = radioButton.getText().toString();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();
                    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
                    reference = FirebaseDatabase.getInstance(Url).getReference("Users").child(userid);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", username);
                    hashMap.put("imageURL", "default");
                    hashMap.put("status", "offline");
                    hashMap.put("search", username.toLowerCase());
                    hashMap.put("career", radioButton.getText().toString());
                    hashMap.put("rollcall", "未簽到");
                    if(!academic.getText().equals("")){
                        hashMap.put("academic",academic.getText().toString());
                    }else{
                        hashMap.put("academic","未填寫");
                    }
                    if(!department.getText().equals("")){
                        hashMap.put("department",department.getText().toString());
                    }else{
                        hashMap.put("academic","未填寫");
                    }
                    if(!classname.getText().equals("")){
                        hashMap.put("classname",classname.getText().toString());
                    }else{
                        hashMap.put("classname","未填寫");
                    }
                    if(!studentID.getText().equals("")){
                        hashMap.put("studentID",studentID.getText().toString());
                    }else{
                        hashMap.put("studentID","未填寫");
                    }
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (career.equals("學生")) {
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Intent intent = new Intent(RegisterActivity.this, TeacherActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this, "註冊請使用email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}