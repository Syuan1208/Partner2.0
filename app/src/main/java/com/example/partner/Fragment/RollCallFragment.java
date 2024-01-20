package com.example.partner.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.partner.Adapter.CourseAdapter;
import com.example.partner.Adapter.RollcallAdapter;
import com.example.partner.Adapter.RollcallRecord_Adapter;
import com.example.partner.ClassNewsActivity;
import com.example.partner.Model.User;
import com.example.partner.R;
import com.example.partner.TeacherActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RollCallFragment extends Fragment {
    View view;
    Button rollcall_on;
    Button rollcall_off;
    FirebaseUser firebaseUser;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    String str;//現在的時間
    ArrayList<String> studentlist = new ArrayList();
    String coursename;
    String rollcall = "123";

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_roll_call, container, false);
        //定義
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        rollcall_on = view.findViewById(R.id.rollcall_on);
        rollcall_off = view.findViewById(R.id.rollcall_off);
        //滾動頁面(個人檔案為首頁)
        //滑動視窗
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager2 viewPager2 = view.findViewById(R.id.viewpager);
        RollcallAdapter rollcallAdapter = new RollcallAdapter(getChildFragmentManager(), getLifecycle());

            viewPager2.setAdapter(rollcallAdapter);
            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.black));
            new TabLayoutMediator(tabLayout, viewPager2,
                    (tab, position) -> tab.setText(rollcallAdapter.getPageTitle(position))
            ).attach();
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    viewPager2.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        rollcall_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Openrollcall();
                writestudentlist();
                //設定按鈕是否能按(顏色分別)
                setButtonEnableTrue(rollcall_off);
                setButtonEnableFalse(rollcall_on);
            }
        });
        rollcall_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //關閉點名
                Closerollcall();
                //設定按鈕是否能按(顏色分別
                setButtonEnableTrue(rollcall_on);
                setButtonEnableFalse(rollcall_off);

            }
        });
        return view;
    }

    private void Closerollcall() {
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference(coursename);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("rollcall", "未開啟");
        reference.updateChildren(hashMap);
        //提示
        Toast.makeText(getActivity(), "已關閉點名", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        coursename = ((ClassNewsActivity) activity).getcoursename();
    }

    private void Openrollcall() {
        //獲取開啟時間
        getcurrenttime();
        //把開啟點名寫入資料庫
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference(coursename);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("rollcall", "已開啟");
        reference.updateChildren(hashMap);
        DatabaseReference reference2 = FirebaseDatabase.getInstance(Url).getReference("課程名稱").child(coursename);
        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("time", str);
        reference2.updateChildren(hashMap2);
        //提示
        Toast.makeText(getActivity(), "已開啟點名", Toast.LENGTH_SHORT).show();
    }

    private void writestudentlist() {
        //把開啟點名時間+學生名單寫入資料庫
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference(coursename).child(str);
        HashMap<String, Object> hashMap = new HashMap<>();
        for (int i = 0; i < studentlist.size(); i++) {
            hashMap.put(studentlist.get(i), "未到");
        }
        reference.updateChildren(hashMap);
    }

    private void getcurrenttime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
        str = formatter.format(curDate);
    }

    private void setButtonEnableTrue(Button button) {
        button.setEnabled(true);
        button.setBackgroundColor(Color.parseColor("#FF03DAC5"));
    }

    private void setButtonEnableFalse(Button button) {
        button.setEnabled(false);
        button.setBackgroundColor(Color.parseColor("#E8E4E4"));
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
        //得到是否開啟過點名
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference(coursename).child("rollcall");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    rollcall = snapshot.getValue(String.class);
                    //設定按鈕
                    if (rollcall.equals("已開啟")) {
                        //設定按鈕是否能按(顏色分別)
                        setButtonEnableTrue(rollcall_off);
                        setButtonEnableFalse(rollcall_on);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onStart();

    }
}