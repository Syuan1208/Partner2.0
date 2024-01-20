package com.example.partner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.partner.Adapter.TestAdapter;
import com.example.partner.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeacherQuizActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    int btn_positon;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    ArrayList<String> studentlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_quiz);
        //toolbar設定
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                startActivity(new Intent(TeacherQuizActivity.this, TeacherActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        //得到被點擊的位置
        Intent intent = getIntent();
        btn_positon=intent.getIntExtra("點擊位置",0);
        //定義
        viewPager2 = findViewById(R.id.view_paper);
        TestAdapter testAdapter = new TestAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(testAdapter);
        viewPager2.setCurrentItem(btn_positon);
        //底下導覽條
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(btn_positon).getItemId());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_quickytest:
                        item.setChecked(true);
                        View profileView = item.getActionView();
                        if (profileView != null) {
                            profileView.setBackground(getResources().getDrawable(R.drawable.bottom_nav_item_icon));
                        }
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.navigation_homework:
                        item.setChecked(true);
                        View profileView2 = item.getActionView();
                        if (profileView2 != null) {
                            profileView2.setBackground(getResources().getDrawable(R.drawable.bottom_nav_item_icon));
                        }
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.navigation_history:
                        item.setChecked(true);
                        View profileView3 = item.getActionView();
                        if (profileView3 != null) {
                            profileView3.setBackground(getResources().getDrawable(R.drawable.bottom_nav_item_icon));
                        }
                        viewPager2.setCurrentItem(2);
                        break;
                    case R.id.navigation_drawstraw:
                        item.setChecked(true);
                        View profileView4 = item.getActionView();
                        if (profileView4 != null) {
                            profileView4.setBackground(getResources().getDrawable(R.drawable.bottom_nav_item_icon));
                        }
                        viewPager2.setCurrentItem(3);
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

    public String getcoursetest() {
        Intent intent = getIntent();
        return intent.getStringExtra("歷史題目");
    }
    public ArrayList<String> getstudentlist(){
        DatabaseReference reference2 = FirebaseDatabase.getInstance(Url).getReference("Users");
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getCareer().equals("學生")) {
                        studentlist.add(user.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return studentlist;
    }
}