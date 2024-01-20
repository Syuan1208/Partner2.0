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
import com.example.partner.MessageActivity;
import com.example.partner.Model.Chat;
import com.example.partner.Model.User;
import com.example.partner.R;
import com.example.partner.StudentQuickyTestModeActivity;
import com.example.partner.TeacherHistoryActivity;
import com.example.partner.TeacherQuickyTestActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> data;
    private ArrayList<String> data_time;
    private String topic;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    //
    public static final int TEST_TYPE_HOMEWORK = 0;
    public static final int TEST_TYPE_QUICKYTEST = 1;
    private FirebaseUser firebaseUser;
    public HistoryAdapter(Context mContext, ArrayList<String> data, ArrayList<String> data_time,String topic) {
        this.mContext = mContext;
        this.data = data;
        this.data_time = data_time;
        this.topic=topic;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==TEST_TYPE_HOMEWORK){
            View view = LayoutInflater.from(mContext).inflate(R.layout.history_item, parent, false);
            return new ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.history_quickytest_item, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int currentPosition = holder.getAdapterPosition();
        String name2 = data_time.get(currentPosition) + " | " + data.get(currentPosition);
        holder.name.setText(name2);
        String type_of_test = data.get(currentPosition);
        char first_char = type_of_test.charAt(0);
        if (first_char == '快') {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TeacherQuickyTestActivity.class);
                    intent.putExtra("題目名單", data.get(currentPosition));
                    intent.putExtra("某某歷史題目", topic);
                    mContext.startActivity(intent);
                }
            });
        }else{
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TeacherHistoryActivity.class);
                    intent.putExtra("題目名單",data.get(currentPosition));
                    intent.putExtra("某某歷史題目",topic);
                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textview1);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String type_of_test = data.get(position);
        char first_char = type_of_test.charAt(0);
        if (first_char == '快') {
            return TEST_TYPE_QUICKYTEST;
        } else {
            return TEST_TYPE_HOMEWORK;
        }
    }
}
