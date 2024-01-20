package com.example.partner.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.partner.Fragment.ChatFragment;
import com.example.partner.Fragment.CourseTestsFragment;
import com.example.partner.Fragment.DrawStrawFragment;
import com.example.partner.Fragment.GroupFragment;
import com.example.partner.Fragment.GroupingFragment;
import com.example.partner.Fragment.ProfileFragment;
import com.example.partner.Fragment.RollCallFragment;
import com.example.partner.Fragment.TeacherTestFragment;
import com.example.partner.Fragment.UsersFragment;

public class ClassNewsAdapter extends FragmentStateAdapter {
    public ClassNewsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 2:
                return new
                        GroupingFragment();
            case 1:
                return new RollCallFragment();
            default:
                return new TeacherTestFragment();
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }


}
