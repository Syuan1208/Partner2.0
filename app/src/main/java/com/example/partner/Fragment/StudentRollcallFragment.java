package com.example.partner.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.partner.Adapter.UserRollcallAdapter;
import com.example.partner.ClassNewsActivity;
import com.example.partner.Model.Group;
import com.example.partner.Model.User;
import com.example.partner.R;
import com.example.partner.StudentClassNewsActivity;
import com.example.partner.TeacherActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StudentRollcallFragment extends Fragment {
    //點名按鈕
    Button rollcall;
    //
    FirebaseUser firebaseUser;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    String coursename;
    String rollcalltime;
    String studentname;
    ImageView imageView;
    TextView textView;
    String rollcall_staus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_rollcall, container, false);
        rollcall = view.findViewById(R.id.rollcall);
        imageView = view.findViewById(R.id.rollcall_image);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        textView = view.findViewById(R.id.rollcall_word);
        rollcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollcall();
            }
        });
        return view;
    }

    //簽到的人狀態要改成已簽到
    private void rollcall() {
        //得到點名開啟的時間
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference("課程名稱").child(coursename);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    rollcalltime = dataSnapshot.getValue(String.class);
                }
                //根據得到的時間找出資料庫資料 寫入recycleview
                DatabaseReference reference2 = FirebaseDatabase.getInstance(Url).getReference(coursename).child(rollcalltime);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(studentname, "已到");
                reference2.updateChildren(hashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//更換圖片
        imageView.setImageResource(R.drawable.high_five);
        textView.setText("恭喜!點名成功");
        rollcall.setBackgroundColor(Color.parseColor("#E8E4E4"));
        Toast.makeText(getActivity(), "點名成功!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        coursename = ((StudentClassNewsActivity) activity).getcoursename();
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        //獲取放入學生名單
        DatabaseReference reference2 = FirebaseDatabase.getInstance(Url).getReference("Users");
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getId().equals(firebaseUser.getUid())) {
                        studentname = user.getUsername();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //檢查是否點名過
        rollcalltime = "";
        DatabaseReference reference3 = FirebaseDatabase.getInstance(Url).getReference(coursename).child("rollcall");
        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    rollcall_staus = snapshot.getValue(String.class);
                if(rollcall_staus.equals("未開啟")){
                    textView.setText("未開放點名");
                    rollcall.setEnabled(false);
                    rollcall.setBackgroundColor(Color.parseColor("#E8E4E4"));
                    rollcall.setText("未開放");
                }else{
                    DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference("課程名稱").child(coursename);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                rollcalltime = dataSnapshot.getValue(String.class);
                            }
                            //根據得到的時間找出資料庫資料 寫入recycleview
                            if (!rollcalltime.equals("")) {
                                DatabaseReference reference2 = FirebaseDatabase.getInstance(Url).getReference(coursename).child(rollcalltime);
                                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            if (dataSnapshot.getKey().equals(studentname)) {
                                                if (!dataSnapshot.getValue().equals("未到")) {
                                                    //更換圖片
                                                    imageView.setImageResource(R.drawable.high_five);
                                                    textView.setText("恭喜!點名成功");
                                                    rollcall.setEnabled(false);
                                                    rollcall.setBackgroundColor(Color.parseColor("#E8E4E4"));
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

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        super.onStart();

    }

}