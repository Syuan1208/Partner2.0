package com.example.partner.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partner.Adapter.GroupAdapter;
import com.example.partner.ClassNewsActivity;
import com.example.partner.MainActivity;
import com.example.partner.Model.Group;
import com.example.partner.Model.Grouplist;
import com.example.partner.R;
import com.example.partner.StartActivity;
import com.example.partner.TeacherActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private GroupAdapter groupAdapter;
    private List<Group> mGroup;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private List<Grouplist> GroupchatList;//版本1 private List<String>userList;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group, container, false);
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mGroup = new ArrayList<>();
        readGroupUser();
//        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
//        GroupchatList=new ArrayList<>();
//        reference=FirebaseDatabase.getInstance().getReference("Grouplist").child(firebaseUser.getUid());
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                GroupchatList.clear();
//                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    Grouplist grouplist=dataSnapshot.getValue(Grouplist.class);
//                    GroupchatList.add(grouplist);
//                }
//                groupList();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        return view;
    }

    private void readGroupUser() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance(Url).getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mGroup.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Group Groupuser = dataSnapshot.getValue(Group.class);//firebaseDatabase裡的Groups都在Groupuser
                    assert Groupuser != null;
                    assert firebaseUser != null;
                    mGroup.add(Groupuser);
                }
                groupAdapter = new GroupAdapter(getContext(), mGroup,true);//mGroup裡有ClassA B C
                recyclerView.setAdapter(groupAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void groupList() {
        mGroup = new ArrayList<>();
        reference = FirebaseDatabase.getInstance(Url).getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mGroup.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Group groupcreater = dataSnapshot.getValue(Group.class);
                    for (Grouplist grouplist : GroupchatList) {
                        if (groupcreater.getCreater().equals(grouplist.getId())) {
                            mGroup.add(groupcreater);
                        }
                    }
                }
                groupAdapter = new GroupAdapter(getContext(), mGroup,true);
                recyclerView.setAdapter(groupAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}