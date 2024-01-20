package com.example.partner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partner.R;

import java.util.ArrayList;

public class StudentHistorySeenAdapter extends RecyclerView.Adapter<StudentHistorySeenAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> question;
    private ArrayList<String> question_number;
    private ArrayList<String> history_Answer;
    ArrayList<String> mAnswer;

    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public StudentHistorySeenAdapter(Context mContext, ArrayList<String> question,ArrayList<String> history_Answer) {
        this.mContext = mContext;
        this.question = question;
        this.question_number = new ArrayList<>();
        this.mAnswer = new ArrayList<>();
        this.history_Answer=history_Answer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.studenthistoryseen_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int currentPosition = holder.getAdapterPosition();
        holder.quiz_number.setText(question_number.get(position));
        holder.enter_quiz.setText(question.get(position));
        holder.enter_answer.setText(history_Answer.get(position));
    }

    @Override
    public int getItemCount() {
        return question.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView enter_quiz;
        public EditText enter_answer;
        public TextView quiz_number;
        public ImageButton confirm_btn, cancel_btn;
        public TextView confirm_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            enter_quiz = itemView.findViewById(R.id.enter_quiz);
            enter_answer = itemView.findViewById(R.id.enter_answer);
            quiz_number = itemView.findViewById(R.id.quiz_number);
            for (int i = 1; i <= question.size(); i++) {
                String num = "第" + Integer.toString(i) + "題";
                question_number.add(num);
            }
            confirm_btn = itemView.findViewById(R.id.confirm_btn);
            cancel_btn = itemView.findViewById(R.id.cancel_btn);
            confirm_text = itemView.findViewById(R.id.confirm_text);
            //確定按鈕
            confirm_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel_btn.setEnabled(true);
                    mAnswer.add(enter_answer.getText().toString());
                    enter_answer.setEnabled(false);
                    Toast.makeText(mContext, "已鎖定答案", Toast.LENGTH_SHORT).show();
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
                    mAnswer.remove(mAnswer.size() - 1);
                    enter_answer.setEnabled(true);
                    confirm_text.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
                    Toast.makeText(mContext, "已開啟重複編輯", Toast.LENGTH_SHORT).show();
                    cancel_btn.setEnabled(false);
                }
            });
        }
    }
    public ArrayList<String> getmAnswer() {
        return mAnswer;
    }
}
