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

import com.example.partner.Adapter.HistoryAdapter;
import com.example.partner.Adapter.StudentHistoryAdapter;
import com.example.partner.R;
import com.example.partner.StudentClassNewsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class StudentTestFragment extends Fragment {
    View view;
    String coursename;
    RecyclerView recyclerView;
    ArrayList<String> data;
    ArrayList<String> data_time;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    String topic;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_student_test, container, false);
        //定義recyclerView
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //寫入recycleview
        //得到題目名單
        data = new ArrayList<>();
        data_time = new ArrayList<>();
        getData();
        return view;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        coursename = ((StudentClassNewsActivity) activity).getcoursename();
        topic=coursename+"歷史題目";
        super.onAttach(activity);
    }
    private void getData() {
        data.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference(topic);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    data.add(dataSnapshot.getKey());
                }
                getDatatime(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDatatime(ArrayList<String> data) {
        for (int i = 0; i < data.size(); i++) {
            DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference("題目名單").child(data.get(i));
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        data_time.add(dataSnapshot.getValue(String.class));
                        if (data.size() == data_time.size()) {
                            StudentHistoryAdapter historyAdapter = new StudentHistoryAdapter(getContext(), data, data_time, topic,coursename);
                            recyclerView.setAdapter(historyAdapter);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}