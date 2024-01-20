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

import com.example.partner.Adapter.UserGroupAdapter;
import com.example.partner.Model.User;
import com.example.partner.R;
import com.example.partner.StudentClassNewsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupingNewsFragment extends Fragment {
    View view;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    FirebaseUser firebaseUser;
    String username;
    String coursename;
    String group_record;
    String group;
    ArrayList<String> grouplist = new ArrayList<>();
    ArrayList<String> studentlist = new ArrayList<>();
    RecyclerView recyclerView;
    int count;
    int num;
    @Override
    public void onStart() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getId().equals(firebaseUser.getUid())) {
                        username = user.getUsername();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_grouping_news, container, false);
        //定義
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference(group_record);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(!(dataSnapshot.getKey().equals("學生名單"))){
                        count++;
                        HashMap<String, Object> data = (HashMap<String, Object>) dataSnapshot.getValue();
                        Log.i("測試", data.toString());
                        for (int i = 1; i <= data.size(); i++) {
                            String now_name = "第" + Integer.toString(i) + "成員";
                            String name = (String) data.get(now_name);
                            if (name.equals(username)) {
                                for(int j=1;j<= data.size();j++){
                                    String now_name2 = "第" + Integer.toString(j) + "成員";
                                    String name2 = (String) data.get(now_name2);
                                    studentlist.add(name2);
                                }
                                grouplist.add(Integer.toString(count));
                                num=data.size();
                                break;
                            }
                        }
                    }
                }
                //
                UserGroupAdapter userGroupAdapter = new UserGroupAdapter(getContext(), studentlist, grouplist, num);
                recyclerView.setAdapter(userGroupAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        coursename = ((StudentClassNewsActivity) activity).getcoursename();
        group_record = coursename + "分組紀錄";
        super.onAttach(activity);
    }
}