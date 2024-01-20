package com.example.partner.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.partner.Adapter.CourseAdapter;
import com.example.partner.Adapter.QuickyTestAdapter;
import com.example.partner.R;


public class QuickyTestFragment extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_quicky_test, container, false);
        //視窗
        ViewPager2 viewPager2 = view.findViewById(R.id.view_paper);
        QuickyTestAdapter quickyTestAdapter=new QuickyTestAdapter(getChildFragmentManager(),getLifecycle());
        viewPager2.setAdapter(quickyTestAdapter);
        //選擇題點選
        ImageView multiplechoice_imageview=view.findViewById(R.id.multiplechoice_imageview);
        multiplechoice_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(0);
            }
        });
        //是非題點選
        LinearLayout truefalse_linearlayout=view.findViewById(R.id.truefalse_linearlayout);
        truefalse_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(1);
            }
        });
        return view;
    }
}