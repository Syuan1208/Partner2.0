package com.example.partner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.partner.Adapter.ChatAdapter;
import com.example.partner.Adapter.ClassNewsAdapter;
import com.example.partner.Adapter.CourseAdapter;
import com.example.partner.Adapter.HomeAdapter;
import com.example.partner.Adapter.StudentClassNewsAdapter;
import com.example.partner.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentClassNewsActivity extends AppCompatActivity {
    //    CircleImageView profile_image;
//    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager2;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_class_news);
        //toolbar設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                startActivity(new Intent(StudentClassNewsActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //滾動頁面
        viewPager2 = findViewById(R.id.view_paper);
        StudentClassNewsAdapter studentclassNewsAdapter = new StudentClassNewsAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(studentclassNewsAdapter);
        viewPager2.setCurrentItem(0);
        //底下導覽條
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_student_test:
                        item.setChecked(true);
                        View profileView = item.getActionView();
                        if (profileView != null) {
                            profileView.setBackground(getResources().getDrawable(R.drawable.bottom_nav_item_icon));
                        }
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.navigation_student_rollcall:
                        item.setChecked(true);
                        View profileView2 = item.getActionView();
                        if (profileView2 != null) {
                            profileView2.setBackground(getResources().getDrawable(R.drawable.bottom_nav_item_icon));
                        }
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.navigation_groupinformation:
                        item.setChecked(true);
                        View profileView3 = item.getActionView();
                        if (profileView3 != null) {
                            profileView3.setBackground(getResources().getDrawable(R.drawable.bottom_nav_item_icon));
                        }
                        viewPager2.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void status(String status) {
        reference = FirebaseDatabase.getInstance(Url).getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    public String getcoursename(){
        Intent intent=getIntent();
        return intent.getStringExtra("課程名稱");
    }
}