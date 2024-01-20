package com.example.partner.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.partner.GroupMessageActivity;
import com.example.partner.Model.Chat;
import com.example.partner.Model.Group;
import com.example.partner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private Context mContext;
    private List<Group> mGroups;
    private boolean is_chat;
    String thelastMessage;
    String Url="https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public GroupAdapter(Context mContext, List<Group> mGroups,boolean is_chat) {
        this.mGroups = mGroups;
        this.mContext = mContext;
        this.is_chat=is_chat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Group groupuser = mGroups.get(position);//groupuser是被選到的群組
        holder.groupname.setText(groupuser .getName());
        if (groupuser .getImageURL().equals("default")) {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(groupuser .getImageURL()).into(holder.profile_image);
        }
        //檢查是否有新訊息 從有沒有已讀判斷
        if(is_chat){
            lastMessage(groupuser.getName(),holder.last_msg);
        }else {
            holder.last_msg.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupMessageActivity.class);
                intent.putExtra("groupname", groupuser.getName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView groupname;
        public ImageView profile_image;
//        private ImageView img_on;
//        private ImageView img_off;
       private TextView last_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupname = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
//            img_on = itemView.findViewById(R.id.img_on);
//            img_off = itemView.findViewById(R.id.img_off);
           last_msg=itemView.findViewById(R.id.last_msg);
        }
    }
    //檢查訊息
    private void lastMessage(String userid,TextView last_msg){
        thelastMessage="default";
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance(Url).getReference("GroupChats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Chat chat=dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(userid)){
                        thelastMessage=chat.getMessage();
                    }
                }
                switch (thelastMessage){
                    case "default":
                        last_msg.setText("");
                        break;
                    default:
                        last_msg.setText(thelastMessage);
                        break;
                }
                thelastMessage="default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
