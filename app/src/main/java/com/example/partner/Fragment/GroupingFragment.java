package com.example.partner.Fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.partner.Adapter.UserGroupAdapter;
import com.example.partner.ClassNewsActivity;
import com.example.partner.Model.User;
import com.example.partner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class GroupingFragment extends Fragment {
    EditText eachgroup_num_people;
    Button grouping_confirm;
    ArrayList<String> studentlist = new ArrayList<>();
    ArrayList<String> group = new ArrayList<>();
    RecyclerView recyclerView;
    String coursename;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    String coursegroup;
    int num2;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grouping, container, false);
        //定義
        eachgroup_num_people = view.findViewById(R.id.eachgroup_num_people);
        grouping_confirm = view.findViewById(R.id.grouping_confirm);
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //按鈕
        grouping_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隨機打散學生順序
                Collections.shuffle(studentlist);
                //得到輸入框中的數字
                int num = Integer.valueOf(eachgroup_num_people.getText().toString());
                //得到總共組別
                int total_group = (studentlist.size() / num) + 1;
                //已分過組人數
                int past_group = 0;
                if (num <= 5 && num >= 2) {
                    group.clear();
                    for (int i = 1; i <= total_group; i++) {
                        group.add(Integer.toString(i));
                    }
                    Log.i("成員名單(前)", studentlist.toString());
                    UserGroupAdapter userGroupAdapter = new UserGroupAdapter(getContext(), studentlist, group, num);
                    recyclerView.setAdapter(userGroupAdapter);
                    grouping_confirm.setEnabled(false);
                    grouping_confirm.setBackgroundColor(Color.parseColor("#E8E4E4"));
                    grouping_confirm.setText("已分組");
                } else {
                    Toast.makeText(getActivity(), "請輸入小於5的人數", Toast.LENGTH_SHORT).show();
                }
                //分組寫入資料庫
                writegrouping(num,total_group,studentlist);
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        coursename = ((ClassNewsActivity) activity).getcoursename();
        //分組紀錄
        coursegroup = coursename +"分組紀錄";
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
                    if (user.getCareer().equals("學生")) {
                        studentlist.add(user.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //檢查分組過了沒
        ArrayList<String> studentlist2 = new ArrayList<>();
        ArrayList<String> group2 = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference(coursegroup).child("學生名單");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    studentlist2.add(dataSnapshot.getValue(String.class));
                }
                if(snapshot.exists()){
                    grouping_confirm.setEnabled(false);
                    grouping_confirm.setBackgroundColor(Color.parseColor("#E8E4E4"));
                    grouping_confirm.setText("已分組");
                    DatabaseReference reference3=FirebaseDatabase.getInstance(Url).getReference(coursegroup).child("第1組");
                    reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                num2++;
                            }
                            for(int f=1;f<=(studentlist2.size()/num2)+1;f++){
                                group2.add(Integer.toString(f));
                                Log.i("幾組",group2.toString());
                            }
                            //設定adapter
                            UserGroupAdapter userGroupAdapter = new UserGroupAdapter(getContext(), studentlist2, group2, num2);
                            recyclerView.setAdapter(userGroupAdapter);
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
    public void writegrouping(int group_num, int total_group,ArrayList<String> studentlist) {
        DatabaseReference reference2=FirebaseDatabase.getInstance(Url).getReference(coursegroup).child("學生名單");
        HashMap<String, Object> hashMap2 = new HashMap<>();
        for(int k=0;k<studentlist.size();k++){
            String b=Integer.toString(k);
            hashMap2.put(b,studentlist.get(k));
        }
        reference2.updateChildren(hashMap2);
        int count=0;
        for(int i=1;i<=total_group;i++){
            String groupname="第"+Integer.toString(i)+"組";
            DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference(coursegroup).child(groupname);
            HashMap<String, Object> hashMap = new HashMap<>();
            for (int j = 1; j <= group_num; j++) {
                if(count<studentlist.size()) {
                    String a = Integer.toString(j);
                    String member_name = "第" + a + "成員";
                    hashMap.put(member_name, studentlist.get(count));
                    count++;
                    reference.updateChildren(hashMap);
                }
            }
        }
    }

}