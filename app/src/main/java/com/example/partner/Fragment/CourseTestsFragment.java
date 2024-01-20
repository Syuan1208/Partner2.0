package com.example.partner.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.partner.Adapter.StudentTestAdapter;
import com.example.partner.R;
import com.example.partner.StudentClassNewsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CourseTestsFragment extends Fragment {
    View view;
    ViewPager2 viewPager2;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    String coursename;
    String historytest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_course_tests, container, false);
        viewPager2 =view.findViewById(R.id.view_paper);
        StudentTestAdapter studentTestAdapter=new StudentTestAdapter(getChildFragmentManager(),getLifecycle());
        viewPager2.setAdapter(studentTestAdapter);
        //
        DatabaseReference reference= FirebaseDatabase.getInstance(Url).getReference(historytest);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    viewPager2.setCurrentItem(1);
                }else{
                    viewPager2.setCurrentItem(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        coursename = ((StudentClassNewsActivity) activity).getcoursename();
        historytest=coursename+"歷史題目";
        super.onAttach(activity);
    }
}