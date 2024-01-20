package com.example.partner.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.partner.MainActivity;
import com.example.partner.R;
import com.example.partner.RegisterActivity;
import com.example.partner.TeacherActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterFragment extends Fragment {
    EditText username, email, password,studentID,classname,department,academic;
    Button btn_register;
    FirebaseAuth auth;
    DatabaseReference reference;
    Spinner spinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_register, container, false);
        //註冊資料定義
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        email = view.findViewById(R.id.email);
        academic=view.findViewById(R.id.academic);
        department=view.findViewById(R.id.department);
        classname=view.findViewById(R.id.classname);
        studentID=view.findViewById(R.id.studentID);
        spinner=view.findViewById(R.id.spinner);
        auth = FirebaseAuth.getInstance();
        //註冊按鈕定義
        btn_register = view.findViewById(R.id.register);
        //註冊按鈕
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(getActivity(), "請填寫全部資料", Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_username, txt_email, txt_password);
                }

            }
        });

        return view;
    }
    //註冊功能
    private void register(String username, String email, String password) {
        String spinner_item=(String)spinner.getSelectedItem();
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
                    hashMap.put("rollcall", "未簽到");
                    if(!spinner_item.equals("帳戶身分")){
                        hashMap.put("career", spinner_item);
                    }
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
                                if (spinner_item.equals("學生")) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(getActivity(), TeacherActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(getActivity(), "註冊請使用email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}