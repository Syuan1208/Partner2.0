package com.example.partner.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.partner.Fragment.DrawStrawFragment;
import com.example.partner.Fragment.GroupingFragment;
import com.example.partner.Fragment.HistoryFragment;
import com.example.partner.Fragment.HomeworkFragment;
import com.example.partner.Fragment.QuickyTestFragment;
import com.example.partner.Fragment.RollCallFragment;
import com.example.partner.Fragment.TeacherTestFragment;

public class TestAdapter extends FragmentStateAdapter {
    public TestAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 3:
                return new DrawStrawFragment();
            case 2:
                return new HistoryFragment();
            case 1:
                return new HomeworkFragment();
            default:
                return new QuickyTestFragment();
        }
    }


    @Override
    public int getItemCount() {
        return 4;
    }


}
