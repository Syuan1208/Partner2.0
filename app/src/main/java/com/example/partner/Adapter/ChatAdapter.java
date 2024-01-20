package com.example.partner.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.partner.Fragment.ChatFragment;
import com.example.partner.Fragment.GroupFragment;
import com.example.partner.Fragment.ProfileFragment;
import com.example.partner.Fragment.UsersFragment;

public class ChatAdapter extends FragmentStateAdapter {
    public ChatAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new GroupFragment();
            default:
                return  new UsersFragment();
        }
    }
    public String getPageTitle(int position) {
        // 返回對應位置的標題文字
        switch (position) {
            case 0:
                return "好友";
            default:
                return "群組";
        }
    }
    @Override
    public int getItemCount() {
        return 2;
    }
}
