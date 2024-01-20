package com.example.partner.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.partner.Fragment.GroupFragment;
import com.example.partner.Fragment.ProfileFragment;
import com.example.partner.Fragment.UsersFragment;

public class HomeAdapter extends FragmentStateAdapter {
    public HomeAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
//            case 1:
//                return new ProfileFragment();
            default:
                return  new ProfileFragment();
        }
    }
    public String getPageTitle(int position) {
        // 返回對應位置的標題文字
        switch (position) {
            default:
                return "";
        }
    }
    @Override
    public int getItemCount() {
        return 1;
    }
}
