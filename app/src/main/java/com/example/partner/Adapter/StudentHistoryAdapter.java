package com.example.partner.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partner.MainActivity;
import com.example.partner.StudentQuickyTestModeActivity;
import com.example.partner.R;
import com.example.partner.StudentHistoryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentHistoryAdapter extends RecyclerView.Adapter<StudentHistoryAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> data;
    private ArrayList<String> data_time;
    private String topic;
    private String coursename;
    private DatabaseReference reference;
    private String topic_answer;
    private FirebaseUser firebaseUser;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    //
    public static final int TEST_TYPE_HOMEWORK = 0;
    public static final int TEST_TYPE_QUICKYTEST = 1;

    public StudentHistoryAdapter(Context mContext, ArrayList<String> data, ArrayList<String> data_time, String topic, String coursename) {
        this.mContext = mContext;
        this.data = data;
        this.data_time = data_time;
        this.topic = topic;
        this.coursename = coursename;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TEST_TYPE_HOMEWORK) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.history_item, parent, false);
            return new ViewHolder(view);
        } else {
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
                    topic_answer = data.get(currentPosition) + "回答紀錄";
                    DatabaseReference reference=FirebaseDatabase.getInstance(Url).getReference(topic_answer).child(firebaseUser.getUid());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.i("主題",topic_answer);
                            Log.i("使用者",firebaseUser.getUid());
                            Log.d("FirebaseData", "Snapshot exists: " + snapshot.exists());
                            if(snapshot.exists()){
                                Toast.makeText(mContext,"已作答過",Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent = new Intent(mContext, StudentQuickyTestModeActivity.class);
                                intent.putExtra("題目名單", data.get(currentPosition));
                                intent.putExtra("某某歷史題目", topic);
                                mContext.startActivity(intent);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, StudentHistoryActivity.class);
                    intent.putExtra("題目名單", data.get(currentPosition));
                    intent.putExtra("某某歷史題目", topic);
                    intent.putExtra("課程名稱", coursename);
                    mContext.startActivity(intent);
                }
            });
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
}
