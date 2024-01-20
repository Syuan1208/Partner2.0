package com.example.partner.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.partner.MessageActivity;
import com.example.partner.Model.Chat;
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

public class UserRollcallAdapter extends RecyclerView.Adapter<UserRollcallAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mUsers;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public UserRollcallAdapter(Context mContext, ArrayList<String> mUsers) {
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_rollcall_item, parent, false);
        Log.i("學生cle", mUsers.toString());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.profile_image.setImageResource(R.drawable.negative_vote);
        //載入名子
        holder.username.setText(mUsers.get(position));
        holder.rollcalltime.setText("未簽到");
        //備用方案
//        DatabaseReference reference=FirebaseDatabase.getInstance(Url).getReference("Users");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    User user=dataSnapshot.getValue(User.class);
//                    if(user.getUsername().equals(mUsers.get(position))){
//                        Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
//                    }else{
//                        holder.profile_image.setImageResource(R.mipmap.ic_launcher);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        String imageUrl = "https://example.com/user/" + mUsers.get(position) + ".jpg";
//
//        Glide.with(mContext)
//                .load(imageUrl)
//                .placeholder(R.mipmap.ic_launcher)
//                .into(holder.profile_image);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profile_image;
        public TextView rollcalltime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            rollcalltime = itemView.findViewById(R.id.rollcall_time);
        }
    }
}
