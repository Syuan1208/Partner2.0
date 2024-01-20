package com.example.partner.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.partner.Fragment.BlankCourseTestsFragment;
import com.example.partner.Fragment.DrawStrawFragment;
import com.example.partner.Fragment.HistoryFragment;
import com.example.partner.Fragment.HomeworkFragment;
import com.example.partner.Fragment.QuickyTestFragment;
import com.example.partner.Fragment.StudentTestFragment;

public class StudentTestAdapter extends FragmentStateAdapter {
    public StudentTestAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new StudentTestFragment();
            default:
                return new BlankCourseTestsFragment();
        }
    }


    @Override
    public int getItemCount() {
        return 2;
    }


}
