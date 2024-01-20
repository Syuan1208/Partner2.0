package com.example.partner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.partner.Adapter.HistorySeenAdapter;
import com.example.partner.Adapter.StudentHistorySeenAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentHistoryActivity extends AppCompatActivity {
    String topic;//資料庫第二層
    String course_history_list;//資料庫第一層
    TextView quiz_name;
    RecyclerView recyclerView;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    ArrayList<String> question;
    Button confirm_btn;
    ArrayList<String> mAnswer;
    ArrayList<String> history_Answer;
    StudentHistorySeenAdapter historySeenAdapter;
    StudentHistorySeenAdapter historySeenAdapter2;
    String coursename;
    String answer_record;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_history);
        //toolbar設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                startActivity(new Intent(StudentHistoryActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        //定義
        Intent intent = getIntent();
        topic = intent.getStringExtra("題目名單");
        course_history_list = intent.getStringExtra("某某歷史題目");
        coursename = intent.getStringExtra("課程名稱");
        answer_record = topic + "回答紀錄";
        //xml定義
        quiz_name = findViewById(R.id.quiz_name);
        quiz_name.setText(topic);
        question = new ArrayList<>();
        //定義recyclerView
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //得到題目
        history_Answer=new ArrayList<>();
        DatabaseReference reference2 = FirebaseDatabase.getInstance(Url).getReference(answer_record).child(firebaseUser.getUid());
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        history_Answer.add(dataSnapshot.getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference(course_history_list).child(topic);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    question.add(dataSnapshot.getValue(String.class));
                    Log.i("題目", question.toString());
                }
                if(history_Answer.isEmpty()){
                    for(int i=0;i<question.size();i++){
                        history_Answer.add("");
                    }
                }
                historySeenAdapter = new StudentHistorySeenAdapter(getApplicationContext(), question,history_Answer);
                recyclerView.setAdapter(historySeenAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //答案寫進資料庫
        confirm_btn = findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference1 = FirebaseDatabase.getInstance(Url).getReference(answer_record).child(firebaseUser.getUid());
                //得到使用者調到的答案
                historySeenAdapter.notifyDataSetChanged();
                mAnswer = historySeenAdapter.getmAnswer();
                //寫入hashmap
                HashMap<String, String> hashMap = new HashMap<>();
                for (int i = 1; i <= question.size(); i++) {
                    String answer_num = "第" + Integer.toString(i) + "題回答";
                    hashMap.put(answer_num, mAnswer.get(i - 1));
                }
                if (mAnswer.size() == question.size()) {
                    reference1.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "已完成答題", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

}