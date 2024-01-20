package com.example.partner.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partner.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class UserGroupAdapter extends RecyclerView.Adapter<UserGroupAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mUsers;//學生名單
    private ArrayList<String> group;
    int group_num;
    String mgroup_num;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";


    public UserGroupAdapter(Context mContext, ArrayList<String> mUsers, ArrayList<String> group, int group_num) {
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.group = group;
        this.group_num = group_num;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.grouping_item, parent, false);
        Log.i("成員名單", mUsers.toString());
        mgroup_num = Integer.toString(group_num);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.group_num.setText(mgroup_num + "位成員");
        if (position < group.size()) {
            holder.group_number.setText("第" + group.get(position) + "組");
        }
        int startIndex = position * group_num;
        int endIndex = Math.min(startIndex + group_num, mUsers.size());
        // 設定成員名單
        for (int i = startIndex; i < endIndex; i++) {
            String student = mUsers.get(i);
            switch (i - startIndex) {
                case 0:
                    holder.member1.setText(student);
                    break;
                case 1:
                    holder.member2.setText(student);
                    break;
                case 2:
                    holder.member3.setText(student);
                    break;
                case 3:
                    holder.member4.setText(student);
                    break;
                case 4:
                    holder.member5.setText(student);
                    break;
            }
        }

    }


    @Override
    public int getItemCount() {
        return group.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView member1, member2, member3, member4, member5;
        public TextView group_number, group_num;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            member1 = itemView.findViewById(R.id.member1);
            member2 = itemView.findViewById(R.id.member2);
            member3 = itemView.findViewById(R.id.member3);
            member4 = itemView.findViewById(R.id.member4);
            member5 = itemView.findViewById(R.id.member5);
            group_number = itemView.findViewById(R.id.group_number);
            group_num = itemView.findViewById(R.id.group_num);
        }
    }

}
