package com.example.partner.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.partner.Fragment.ChatroomFragment;
import com.example.partner.Fragment.CourseNewsFragment;
import com.example.partner.Fragment.MultipleChoiceFragment;
import com.example.partner.Fragment.ProfileFragment;
import com.example.partner.Fragment.TrueFalseTestFragment;

public class QuickyTestAdapter extends FragmentStateAdapter {
    public QuickyTestAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new TrueFalseTestFragment();
            default:
                return  new MultipleChoiceFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
