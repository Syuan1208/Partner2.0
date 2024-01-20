package com.example.partner.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.partner.ClassNewsActivity;
import com.example.partner.R;
import com.example.partner.StudentClassNewsActivity;


public class TeacherCourseFragment extends Fragment {
    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4;
    TextView textView1,textView2,textView3,textView4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_teacher_course, container, false);
        //定義
        linearLayout1 = view.findViewById(R.id.class1);
        linearLayout2 = view.findViewById(R.id.class2);
        linearLayout3 = view.findViewById(R.id.class3);
        linearLayout4 = view.findViewById(R.id.class4);
        //textview文字丟到後面
        textView1=view.findViewById(R.id.textview1);
        textView2=view.findViewById(R.id.textview2);
        textView3=view.findViewById(R.id.textview3);
        textView4=view.findViewById(R.id.textview4);
        //點擊
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClassNewsActivity.class);
                intent.putExtra("課程名稱",textView1.getText());
                startActivity(intent);
            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClassNewsActivity.class);
                intent.putExtra("課程名稱",textView2.getText());
                startActivity(intent);
            }
        });
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClassNewsActivity.class);
                intent.putExtra("課程名稱",textView3.getText());
                startActivity(intent);
            }
        });
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClassNewsActivity.class);
                intent.putExtra("課程名稱",textView4.getText());
                startActivity(intent);
            }
        });
        return view;
    }
}