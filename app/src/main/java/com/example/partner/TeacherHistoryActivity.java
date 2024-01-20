package com.example.partner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.partner.Adapter.HistorySeenAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeacherHistoryActivity extends AppCompatActivity {
    String topic;//資料庫第二層
    String course_history_list;//資料庫第一層
    String course_historyanswer;
    TextView quiz_name;
    RecyclerView recyclerView;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    ArrayList<String> question;
    ArrayList<String> answer;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_history);
        //toolbar設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                startActivity(new Intent(TeacherHistoryActivity.this, TeacherActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        //定義
        Intent intent = getIntent();
        topic = intent.getStringExtra("題目名單");
        quiz_name = findViewById(R.id.quiz_name);
        quiz_name.setText(topic);
        question=new ArrayList<>();
        answer=new ArrayList<>();
        Log.i("topic", topic);
        course_history_list = intent.getStringExtra("某某歷史題目");
        Log.i("啥歷史題目", course_history_list);
        course_historyanswer = course_history_list + "答案";
        Log.i("啥答案", course_historyanswer);
        //定義recyclerView
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //
        getQuestion();
    }

    private void getQuestion(){
        DatabaseReference reference= FirebaseDatabase.getInstance(Url).getReference(course_history_list).child(topic);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    question.add(dataSnapshot.getValue(String.class));
                    Log.i("題目", question.toString());
                }
                getAnswer();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getAnswer(){
        DatabaseReference reference= FirebaseDatabase.getInstance(Url).getReference(course_historyanswer).child(topic);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    answer.add(dataSnapshot.getValue(String.class));
                    Log.i("答案",answer.toString());
                }
                HistorySeenAdapter historySeenAdapter=new HistorySeenAdapter(getApplicationContext(),question,answer);
                recyclerView.setAdapter(historySeenAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}