package com.example.partner.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.partner.Adapter.QuizAdapter;
import com.example.partner.ClassNewsActivity;
import com.example.partner.Model.Quiz;
import com.example.partner.R;
import com.example.partner.TeacherActivity;
import com.example.partner.TeacherQuizActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class HomeworkFragment extends Fragment {
    View view;
    ArrayList<String> mItems;
    EditText test_name;
    Button read_btn, publish_btn;
    ArrayList<String> mQuestion;
    ArrayList<String> mAnswer;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    String topic;
    String topic_name;
    String str;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_homework, container, false);
        RecyclerView recyclerView;
        //定義
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //
        mItems = new ArrayList<>();
        mItems.add("第1題");
        QuizAdapter quizAdapter = new QuizAdapter(getContext(), mItems);
        recyclerView.setAdapter(quizAdapter);
        //定義
        test_name = view.findViewById(R.id.test_name);
        publish_btn = view.findViewById(R.id.publish_btn);
        mQuestion = new ArrayList<>();
        mQuestion = quizAdapter.getmQuestion();
        mAnswer = new ArrayList<>();
        mAnswer = quizAdapter.getmAnswer();
//        //讀取資料
//        read_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("問題總和", mQuestion.toString());
//                Log.i("答案總和", mAnswer.toString());
//            }
//        });
        //寫進資料庫
        publish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topic_name = test_name.getText().toString();
                if (!topic_name.equals("")) {
                    writequestion(topic_name);
                    writequizopentime();
                    writeanswer(topic_name);
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        topic = ((TeacherQuizActivity) activity).getcoursetest();
    }

    private void getcurrenttime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
        str = formatter.format(curDate);
    }

    private void writequestion(String topic_name) {
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference(topic).child(topic_name);
        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 1; i <= mQuestion.size(); i++) {
            String num = "問題" + Integer.toString(i);
            hashMap.put(num, mQuestion.get(i - 1));
        }
        reference.setValue(hashMap);
    }

    private void writeanswer(String topic_name) {
        String topic2 = topic + "答案";
        DatabaseReference reference1 = FirebaseDatabase.getInstance(Url).getReference(topic2).child(topic_name);
        HashMap<String, String> hashMap2 = new HashMap<>();
        for (int i = 1; i <= mAnswer.size(); i++) {
            String num2 = "答案" + Integer.toString(i);
            hashMap2.put(num2, mAnswer.get(i - 1));
        }
        reference1.setValue(hashMap2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), "已上傳題目", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), TeacherActivity.class);
                startActivity(intent);
            }
        });
    }

    private void writequizopentime() {
        getcurrenttime();
        DatabaseReference reference2 = FirebaseDatabase.getInstance(Url).getReference("題目名單").child(topic_name);
        HashMap<String, String> hashMap3 = new HashMap<>();
        hashMap3.put("發布日期", str);
        reference2.setValue(hashMap3);
    }
}