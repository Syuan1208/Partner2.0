package com.example.partner.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.partner.Fragment.GroupFragment;
import com.example.partner.Fragment.Rollcall_FFragment;
import com.example.partner.Fragment.Rollcall_TFragment;
import com.example.partner.Fragment.UsersFragment;

public class RollcallAdapter extends FragmentStateAdapter {
    public RollcallAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new Rollcall_FFragment();
            default:
                return  new Rollcall_TFragment();
        }
    }
    public String getPageTitle(int position) {
        // 返回對應位置的標題文字
        switch (position) {
            case 1:
                return "未到";
            default:
                return "已到";
        }
    }
    @Override
    public int getItemCount() {
        return 2;
    }
}
