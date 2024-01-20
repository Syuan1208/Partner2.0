package com.example.partner.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.partner.ClassNewsActivity;
import com.example.partner.MainActivity;
import com.example.partner.R;
import com.example.partner.TeacherActivity;
import com.example.partner.TeacherQuizActivity;

public class TeacherTestFragment extends Fragment {
    View view;
    ImageButton imageButton1, imageButton2, imageButton3, imageButton4;
    String coursename,coursetest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_teacher_test, container, false);
        //按鈕定義
        imageButton1 = view.findViewById(R.id.imagebutton1);
        imageButton2 = view.findViewById(R.id.imagebutton2);
        imageButton3 = view.findViewById(R.id.imagebutton3);
        imageButton4 = view.findViewById(R.id.imagebutton4);
        //按鈕
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TeacherQuizActivity.class);
                intent.putExtra("歷史題目", coursetest);
                intent.putExtra("點擊位置", 0);
                startActivity(intent);
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TeacherQuizActivity.class);
                intent.putExtra("歷史題目", coursetest);
                intent.putExtra("點擊位置", 1);
                startActivity(intent);
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TeacherQuizActivity.class);
                intent.putExtra("歷史題目", coursetest);
                intent.putExtra("點擊位置", 2);
                startActivity(intent);
            }
        });
        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TeacherQuizActivity.class);
                intent.putExtra("歷史題目", coursetest);
                intent.putExtra("點擊位置", 3);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        coursename = ((ClassNewsActivity) activity).getcoursename();
        //分組紀錄
        coursetest = coursename + "歷史題目";
    }
}