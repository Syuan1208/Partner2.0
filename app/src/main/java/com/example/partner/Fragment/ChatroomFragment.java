package com.example.partner.Fragment;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.partner.Adapter.ChatAdapter;
import com.example.partner.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class ChatroomFragment extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_chatroom, container, false);
        //滾動頁面(個人檔案為首頁)
        //滑動視窗
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager2 viewPager2 = view.findViewById(R.id.viewpager);
        ChatAdapter chatAdapter=new ChatAdapter(getChildFragmentManager(),getLifecycle());
        viewPager2.setAdapter(chatAdapter);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.black));
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(chatAdapter.getPageTitle(position))
        ).attach();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }
}