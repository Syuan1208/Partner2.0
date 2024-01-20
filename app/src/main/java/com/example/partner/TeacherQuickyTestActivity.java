package com.example.partner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeacherQuickyTestActivity extends AppCompatActivity {
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    TextView quickytest_question, quickytest_answer_a, quickytest_answer_b, quickytest_answer_c, quickytest_answer_d;
    LinearLayout linearLayout_answer_a, linearLayout_answer_b, linearLayout_answer_c, linearLayout_answer_d;
    Button confirm_btn;
    Intent intent;
    String topic, course_history_list;//從intent拿到的
    DatabaseReference reference;
    ArrayList<String> quickytestlist;
    String correct_answer;
    String student_answer = "";
    TextView timer_number;
    String timer_times;
    FirebaseUser firebaseUser;
    String username;
    String topic_answer;
    CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_quicky_test);
        //資料定義
                quickytest_question = findViewById(R.id.quickytest_question);
        quickytest_answer_a = findViewById(R.id.quickytest_answer_a);
        quickytest_answer_b = findViewById(R.id.quickytest_answer_b);
        quickytest_answer_c = findViewById(R.id.quickytest_answer_c);
        quickytest_answer_d = findViewById(R.id.quickytest_answer_d);
        linearLayout_answer_a = findViewById(R.id.linearLayout_answer_a);
        linearLayout_answer_b = findViewById(R.id.linearLayout_answer_b);
        linearLayout_answer_c = findViewById(R.id.linearLayout_answer_c);
        linearLayout_answer_d = findViewById(R.id.linearLayout_answer_d);
        confirm_btn = findViewById(R.id.confirm_btn);
        timer_number = findViewById(R.id.timer_number);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //拿到體育(四)歷史題目和快速測測驗名稱
        intent = getIntent();
        topic = intent.getStringExtra("題目名單");
        topic_answer = topic + "回答紀錄";
        course_history_list = intent.getStringExtra("某某歷史題目");
        quickytestlist = new ArrayList<>();
        //toolbar設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                startActivity(new Intent(TeacherQuickyTestActivity.this, TeacherActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        //得到選擇題資料
        reference = FirebaseDatabase.getInstance(Url).getReference(course_history_list).child(topic);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    quickytestlist.add(dataSnapshot.getValue(String.class));
                    //設定textview資料
                }
                //設定textview資料
                quickytest_answer_a.setText(quickytestlist.get(0));
                quickytest_answer_b.setText(quickytestlist.get(1));
                quickytest_answer_c.setText(quickytestlist.get(2));
                quickytest_answer_d.setText(quickytestlist.get(3));
                quickytest_question.setText(quickytestlist.get(6));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}