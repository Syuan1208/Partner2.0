package com.example.partner.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.partner.Adapter.UserAdapter;
import com.example.partner.Model.Chatlist;
import com.example.partner.Model.User;
import com.example.partner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUser;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private List<Chatlist>userList;//版本1 private List<String>userList;
    String Url="https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView=view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        userList=new ArrayList<>();
        reference=FirebaseDatabase.getInstance(Url).getReference("Chatlist").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Chatlist chatlist=dataSnapshot.getValue(Chatlist.class);
                    userList.add(chatlist);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //聊天版本1
//        reference=FirebaseDatabase.getInstance().getReference("Chats");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userList.clear();
//                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    Chat chat=dataSnapshot.getValue(Chat.class);
//                    if(chat.getSender().equals(firebaseUser.getUid())){
//                        userList.add(chat.getReceiver());
//                    }
//                    if(chat.getReceiver().equals(firebaseUser.getUid())){
//                        userList.add(chat.getSender());
//                    }
//                }
//                readChats();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        //mUser=new ArrayList<>();
//        //readUser();
        return view;
    }
    private void chatList(){
        mUser=new ArrayList<>();
        reference=FirebaseDatabase.getInstance(Url).getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUser.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user=dataSnapshot.getValue(User.class);
                    for(Chatlist chatlist:userList){
                        if(user.getId().equals(chatlist.getId())){
                            mUser.add(user);
                        }
                    }
                }
                userAdapter =new UserAdapter(getContext(),mUser,true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//聊天版本1
//    private void readChats(){
//        mUser=new ArrayList<>();
//        reference=FirebaseDatabase.getInstance().getReference("Users");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mUser.clear();
//                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    User user=dataSnapshot.getValue(User.class);
//                    for (String id:userList){
//                        if(user.getId().equals(id)){
//                            if(mUser.size()!=0){
//                                //for(User user1 :mUser){
//                                for(int i=0;i<mUser.size();i++) {
//                                    User user1=mUser.get(i);
//                                    if (!user.getId().equals(user1.getId())) {
//                                        mUser.add(user);
//                                    }
//                                }
//                               // }
//                            }
//                            else{
//                                mUser.add(user);
//                            }
//                        }
//                    }
//                }
//                userAdapter =new UserAdapter(getContext(),mUser,false);
//                recyclerView.setAdapter(userAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

}