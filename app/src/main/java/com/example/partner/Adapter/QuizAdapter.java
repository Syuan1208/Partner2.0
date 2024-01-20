package com.example.partner.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.partner.MessageActivity;
import com.example.partner.Model.Chat;
import com.example.partner.Model.Quiz;
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

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {
    private ArrayList<String> mItems;
    private Context mContext;
    ArrayList<String> mQuestion;
    ArrayList<String> mAnswer;

    public QuizAdapter(Context mContext, ArrayList<String> mItems) {
        this.mContext = mContext;
        this.mItems = mItems;
        this.mQuestion = new ArrayList<>();
        this.mAnswer = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.teachertset_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.quiz_number.setText(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton addTestBtn;
        public ImageButton deleteTestBtn;
        public EditText enter_quiz;
        public EditText enter_answer;
        public TextView quiz_number;
        public ImageButton confirm_btn, cancel_btn;
        public TextView confirm_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addTestBtn = itemView.findViewById(R.id.addtest_btn);
            deleteTestBtn = itemView.findViewById(R.id.deletetset_btn);
            enter_quiz = itemView.findViewById(R.id.enter_quiz);
            enter_answer = itemView.findViewById(R.id.enter_answer);
            quiz_number = itemView.findViewById(R.id.quiz_number);
            confirm_btn = itemView.findViewById(R.id.confirm_btn);
            cancel_btn = itemView.findViewById(R.id.cancel_btn);
            confirm_text = itemView.findViewById(R.id.confirm_text);
            //新增按鈕
            addTestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItem();
                }
            });
            //刪除按鈕
            deleteTestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 判斷資料集合是否為空，如果不是，移除最後一個項目
                    if (mItems.size() > 1) {
                        mItems.remove(mItems.size() - 1);
                        mQuestion.remove(mQuestion.size() - 1);
                        mAnswer.remove(mAnswer.size() - 1);
                        // 通知資料已經更新，讓 RecyclerView 重新顯示
                        notifyDataSetChanged();
                    }
                }
            });
            //確定按鈕
            confirm_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel_btn.setEnabled(true);
//                    Log.i("問題",enter_quiz.getText().toString());
                    mQuestion.add(enter_quiz.getText().toString());
                    mAnswer.add(enter_answer.getText().toString());
                    enter_quiz.setEnabled(false);
                    enter_answer.setEnabled(false);
                    Toast.makeText(mContext, "已鎖定題目及答案", Toast.LENGTH_SHORT).show();
                    confirm_text.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
                    confirm_btn.setEnabled(false);
                }
            });
            //關閉取消
            cancel_btn.setEnabled(false);
            //取消
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirm_btn.setEnabled(true);
                    mQuestion.remove(mQuestion.size() - 1);
                    mAnswer.remove(mAnswer.size() - 1);
                    enter_quiz.setEnabled(true);
                    enter_answer.setEnabled(true);
                    confirm_text.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
                    Toast.makeText(mContext, "已開啟重複編輯", Toast.LENGTH_SHORT).show();
                    cancel_btn.setEnabled(false);
                }
            });
        }
    }

    public void addItem() {
        int num1 = mItems.size() + 1;
        String num2 = "第" + Integer.toString(num1) + "題";
        // 新增一個項目資料到資料集合中
        mItems.add(num2);
        // 通知資料已經更新，讓 RecyclerView 重新顯示
        notifyDataSetChanged();
    }

    public ArrayList<String> getmQuestion() {
        return mQuestion;
    }
    public ArrayList<String> getmAnswer() {
        return mAnswer;
    }
}
