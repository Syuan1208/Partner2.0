package com.example.partner.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.partner.Fragment.CourseTestsFragment;
import com.example.partner.Fragment.DrawStrawFragment;
import com.example.partner.Fragment.GroupingFragment;
import com.example.partner.Fragment.GroupingNewsFragment;
import com.example.partner.Fragment.RollCallFragment;
import com.example.partner.Fragment.StudentRollcallFragment;

public class StudentClassNewsAdapter extends FragmentStateAdapter {
    public StudentClassNewsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 2:
                return new GroupingNewsFragment();
            case 1:
                return new StudentRollcallFragment();
            default:
                return new CourseTestsFragment();
        }
    }
    public String getPageTitle(int position) {
        // 返回對應位置的標題文字
        switch (position) {
            case 0:
                return "分組資訊";
            default:
                return "點名";
        }
    }
    @Override
    public int getItemCount() {
        return 3;
    }
}
