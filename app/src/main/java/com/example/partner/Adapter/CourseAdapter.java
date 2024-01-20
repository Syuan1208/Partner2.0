package com.example.partner.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.partner.Fragment.ChatroomFragment;
import com.example.partner.Fragment.CourseNewsFragment;
import com.example.partner.Fragment.CourseTestsFragment;
import com.example.partner.Fragment.ProfileFragment;
import com.example.partner.Fragment.TeacherCourseFragment;

public class CourseAdapter extends FragmentStateAdapter {
    public CourseAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 2:
                return new ProfileFragment();
            case 1:
                return new ChatroomFragment();
            default:
                return  new CourseNewsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
