package com.example.partner.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partner.R;
import com.example.partner.TeacherHistoryActivity;

import java.util.ArrayList;

public class HistorySeenAdapter extends RecyclerView.Adapter<HistorySeenAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> question;
    private ArrayList<String> list_answer;
    private ArrayList<String> question_number;

    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public HistorySeenAdapter(Context mContext, ArrayList<String> question, ArrayList<String> list_answer) {
        this.mContext = mContext;
        this.question = question;
        this.list_answer = list_answer;
        this.question_number = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.historyseen_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int currentPosition = holder.getAdapterPosition();
        holder.quiz_number.setText(question_number.get(position));
        holder.enter_quiz.setText(question.get(position));
        holder.enter_answer.setText(list_answer.get(position));
    }

    @Override
    public int getItemCount() {
        return question.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView enter_quiz;
        public TextView enter_answer;
        public TextView quiz_number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            enter_quiz = itemView.findViewById(R.id.enter_quiz);
            enter_answer = itemView.findViewById(R.id.enter_answer);
            quiz_number = itemView.findViewById(R.id.quiz_number);
            for (int i = 1; i <= question.size(); i++) {
                String num = "第" + Integer.toString(i) + "題";
                question_number.add(num);
            }
        }
    }
}
