package com.example.partner.Fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.partner.Adapter.UserRollcall2Adapter;
import com.example.partner.Adapter.UserRollcallAdapter;
import com.example.partner.ClassNewsActivity;
import com.example.partner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Rollcall_TFragment extends Fragment {
    //firebase位置
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    //點選班級名子
    String coursename;
    //得到開啟點名的時間
    String rollcalltime;
    ArrayList<String> studentlist = new ArrayList();
    ArrayList<String> studentlist2 = new ArrayList();
    RecyclerView recyclerView;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_rollcall__t, container, false);
        //定義
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //得到點名開啟的時間
        rollcalltime="";
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference("課程名稱").child(coursename);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    rollcalltime = dataSnapshot.getValue(String.class);
                }
                //根據得到的時間找出資料庫資料 寫入recycleview
                if (!rollcalltime.equals("")){
                    DatabaseReference reference3 = FirebaseDatabase.getInstance(Url).getReference(coursename).child("rollcall");
                    reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue(String.class).equals("已開啟")) {
                                DatabaseReference reference2 = FirebaseDatabase.getInstance(Url).getReference(coursename).child(rollcalltime);
                                reference2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        studentlist2.clear();
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            if (dataSnapshot.getValue(String.class).equals("已到")) {
                                                studentlist2.add(dataSnapshot.getKey());
                                            }
                                        }
                                        UserRollcall2Adapter userAdapter = new UserRollcall2Adapter(getContext(), studentlist2);
                                        recyclerView.setAdapter(userAdapter);
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

        return view;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        coursename = ((ClassNewsActivity) activity).getcoursename();
    }

    @Override
    public void onStart() {

        super.onStart();
    }
}