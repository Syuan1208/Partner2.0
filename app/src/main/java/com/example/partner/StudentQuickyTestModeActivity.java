package com.example.partner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.partner.Model.User;
import com.example.partner.R;
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
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class StudentQuickyTestModeActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_student_quicky_test_mode);
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
                startActivity(new Intent(StudentQuickyTestModeActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
                correct_answer = quickytestlist.get(4);
                timer_times = quickytestlist.get(5);
                quickytest_question.setText(quickytestlist.get(6));
                opentimer(timer_times);
                getusername();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//答案選項變色
        linearLayout_answer_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_answer_a.setBackground(getResources().getDrawable(R.drawable.groupitem_border2));
                linearLayout_answer_b.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                linearLayout_answer_c.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                linearLayout_answer_d.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                student_answer = "A";
            }
        });
        linearLayout_answer_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_answer_a.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                linearLayout_answer_b.setBackground(getResources().getDrawable(R.drawable.groupitem_border2));
                linearLayout_answer_c.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                linearLayout_answer_d.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                student_answer = "B";
            }
        });
        linearLayout_answer_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_answer_a.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                linearLayout_answer_b.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                linearLayout_answer_c.setBackground(getResources().getDrawable(R.drawable.groupitem_border2));
                linearLayout_answer_d.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                student_answer = "C";
            }
        });
        linearLayout_answer_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_answer_a.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                linearLayout_answer_b.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                linearLayout_answer_c.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                linearLayout_answer_d.setBackground(getResources().getDrawable(R.drawable.groupitem_border2));
                student_answer = "D";
            }
        });
//
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (correct_answer.equals(student_answer)) {
                    timer.cancel();
                    DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference(topic_answer).child(firebaseUser.getUid());
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(username, "已作答");
                    hashMap.put("回答答案", student_answer);
                    hashMap.put("回答是否正確", "是");
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "作答成功!恭喜答對", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    });
                } else if (student_answer.equals("")) {
                    Toast.makeText(getApplicationContext(), "未選擇答案!請點選答案，框變為紫色則為你得答案", Toast.LENGTH_SHORT).show();
                } else {
                    timer.cancel();
                    DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference(topic_answer).child(firebaseUser.getUid());
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(username, "已作答");
                    hashMap.put("回答答案", student_answer);
                    hashMap.put("回答是否正確", "否");
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "作答成功!但答錯了QQ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });

    }

    private void opentimer(String timer_times) {
        //計時器
        long totalTime;
        if (timer_times.equals("15")) {
            totalTime = 15000;
        } else if (timer_times.equals("30")) {
            totalTime = 30000;
        } else {
            totalTime = 60000;
        }
        long interval = 1000; // 每隔 1 秒執行一次

        timer = new CountDownTimer(totalTime, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 在每個計時間隔中執行的操作
                // 更新 UI，顯示剩餘時間等等
                String time = String.format(Locale.ENGLISH, "%02d : %02d"
                        , TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                        , TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                timer_number.setText(time);
            }

            @Override
            public void onFinish() {
                DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference(topic_answer).child(firebaseUser.getUid());
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(username, "已作答");
                hashMap.put("回答答案", "未在時間內作答完畢");
                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "時間到!停止作答", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        };
        // 開始倒數計時
        timer.start();
    }

    private void getusername() {
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if(user.getId().equals(firebaseUser.getUid()))
                        username = user.getUsername();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}