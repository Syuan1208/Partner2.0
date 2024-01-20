package com.example.partner.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.partner.Fragment.LoginFragment;
import com.example.partner.Fragment.ProfileFragment;
import com.example.partner.Fragment.RegisterFragment;
public class StartAdapter extends FragmentStateAdapter {
    private int position;

    public StartAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,int position) {
        super(fragmentManager, lifecycle);
        this.position=position;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (this.position == 0) {
            return new LoginFragment();
        } else {
            return new RegisterFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
    @Override
    public long getItemId(int position) {
        // 這邊必須覆寫 getItemId()，讓 ViewPager2 知道不同的位置對應到不同的 item
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        // 這邊必須覆寫 getItemViewType()，讓 ViewPager2 知道不同的位置對應到不同的 item
        return position;
    }
}
