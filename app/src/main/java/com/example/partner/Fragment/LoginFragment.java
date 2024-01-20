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
import android.widget.TextView;
import android.widget.Toast;

import com.example.partner.LoginActivity;
import com.example.partner.MainActivity;
import com.example.partner.Model.User;
import com.example.partner.R;
import com.example.partner.ResetPasswordActivity;
import com.example.partner.TeacherActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {
    EditText email, password;
    Button btn_login;
    FirebaseAuth auth;
    TextView forget_password;
    DatabaseReference reference;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    Intent intent;
    String mUserCareer;
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        //登入需要的定義
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        btn_login = view.findViewById(R.id.login);
        auth = FirebaseAuth.getInstance();
        //登入按鈕
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(getActivity(), "帳號密碼有誤", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                judgecareer(task.getResult().getUser().getUid());
                            } else {
                                Toast.makeText(getActivity(), "連線錯誤", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        //忘記密碼
        forget_password=view.findViewById(R.id.forget_password);
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ResetPasswordActivity.class));
            }
        });
        return view;


    }

    //判斷學生或老師
    private void judgecareer(String id) {
        DatabaseReference reference1 = FirebaseDatabase.getInstance(Url).getReference("Users");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user.getId().equals(id)) {
                            mUserCareer = user.getCareer();
                            if (mUserCareer.equals("老師")) {
                                intent = new Intent(getActivity(), TeacherActivity.class);
                            } else {
                                intent = new Intent(getActivity(), MainActivity.class);
                            }
                            //原始碼intent=new Intent(LoginActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}