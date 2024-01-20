package com.example.partner.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.partner.ClassNewsActivity;
import com.example.partner.MessageActivity;
import com.example.partner.R;
import com.example.partner.TeacherQuizActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.view.Change;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class MultipleChoiceFragment extends Fragment {
    View view;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    String correct_answer = "";
    ArrayList<String> check = new ArrayList();
    EditText quickytest_question, quickytest_answer_a, quickytest_answer_b, quickytest_answer_c, quickytest_answer_d;
    LinearLayout linearLayout_answer_a, linearLayout_answer_b, linearLayout_answer_c, linearLayout_answer_d;
    String question, answer_a, answer_b, answer_c, answer_d;
    Intent intent;
    Button send_button;
    Spinner time_spinner;
    String topic;//
    Boolean isnull = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_multiple_choice, container, false);
        //資料定義
        quickytest_question = view.findViewById(R.id.quickytest_question);
        quickytest_answer_a = view.findViewById(R.id.quickytest_answer_a);
        quickytest_answer_b = view.findViewById(R.id.quickytest_answer_b);
        quickytest_answer_c = view.findViewById(R.id.quickytest_answer_c);
        quickytest_answer_d = view.findViewById(R.id.quickytest_answer_d);
        linearLayout_answer_a = view.findViewById(R.id.linearLayout_answer_a);
        linearLayout_answer_b = view.findViewById(R.id.linearLayout_answer_b);
        linearLayout_answer_c = view.findViewById(R.id.linearLayout_answer_c);
        linearLayout_answer_d = view.findViewById(R.id.linearLayout_answer_d);
        time_spinner = view.findViewById(R.id.time_spinner);
        //答案選項變色
        linearLayout_answer_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_answer_a.setBackground(getResources().getDrawable(R.drawable.groupitem_border2));
                linearLayout_answer_b.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                linearLayout_answer_c.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                linearLayout_answer_d.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                correct_answer = "A";
            }
        });
        linearLayout_answer_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_answer_b.setBackground(getResources().getDrawable(R.drawable.groupitem_border2));
                linearLayout_answer_a.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                linearLayout_answer_c.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                linearLayout_answer_d.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                correct_answer = "B";
            }
        });
        linearLayout_answer_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_answer_c.setBackground(getResources().getDrawable(R.drawable.groupitem_border2));
                linearLayout_answer_a.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                linearLayout_answer_b.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                linearLayout_answer_d.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                correct_answer = "C";
            }
        });
        linearLayout_answer_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_answer_d.setBackground(getResources().getDrawable(R.drawable.groupitem_border2));
                linearLayout_answer_a.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                linearLayout_answer_b.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                linearLayout_answer_c.setBackground(getResources().getDrawable(R.drawable.groupitem_border));
                correct_answer = "D";
            }
        });

        //發布考試
        send_button = view.findViewById(R.id.send_button);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取得選取的項目內容
                String timer_select = time_spinner.getSelectedItem().toString();
                //資料取得
                question = quickytest_question.getText().toString();
                answer_a = quickytest_answer_a.getText().toString();
                answer_b = quickytest_answer_b.getText().toString();
                answer_c = quickytest_answer_c.getText().toString();
                answer_d = quickytest_answer_d.getText().toString();
                check.add(question);
                check.add(answer_a);
                check.add(answer_b);
                check.add(answer_c);
                check.add(answer_d);
                check.add(correct_answer);
                //檢查是否空白並執行
                for (int i = 0; i < check.size(); i++) {
                    if (check.get(i).equals("")) {
                        Toast.makeText(getActivity(), "尚有資料未填", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (i == check.size() - 1) {
                        HashMap<String, Object> haspMap = writeHaspHap(question, answer_a, answer_b, answer_c, answer_d, correct_answer, timer_select);
                        sendQuickytest(haspMap, question);
                        Toast.makeText(getActivity(), "已開啟快速測驗", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getActivity(), ClassNewsActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
        //寫進資料庫

        return view;
    }

    private void sendQuickytest(HashMap<String, Object> haspMap, String question) {
        String quicky_question = "快速測驗題目:" + question;
        writequizopentime(question);//寫到快速測驗題目名單
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference(topic).child(quicky_question);
        reference.setValue(haspMap);
    }

    private String getcurrenttime() {
        String str;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
        str = formatter.format(curDate);
        return str;
    }

    private String getcurrenttime2() {
        String str;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
        str = formatter.format(curDate);
        return str;
    }

    private HashMap<String, Object> writeHaspHap(String question, String answer_a, String answer_b, String answer_c, String answer_d, String correct_answer, String timer) {
        //建立一個HashMap
        HashMap<String, Object> haspMap = new HashMap<>();
        haspMap.put("題目類型", "選擇題");
        haspMap.put("題目", question);
        haspMap.put("A選項", answer_a);
        haspMap.put("B選項", answer_b);
        haspMap.put("C選項", answer_c);
        haspMap.put("D選項", answer_d);
        haspMap.put("答案", correct_answer);
        haspMap.put("計時秒數", timer);
        return haspMap;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        topic = ((TeacherQuizActivity) activity).getcoursetest();
    }

    private void writequizopentime(String question) {
        String quicky_question = "快速測驗題目:" + question;
        String currenttime = getcurrenttime2();
        DatabaseReference reference2 = FirebaseDatabase.getInstance(Url).getReference("題目名單").child(quicky_question);
        HashMap<String, String> hashMap3 = new HashMap<>();
        hashMap3.put("發布日期", currenttime);
        reference2.setValue(hashMap3);
    }
}