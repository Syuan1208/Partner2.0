package com.example.partner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.partner.Adapter.ChatAdapter;
import com.example.partner.Adapter.CourseAdapter;
import com.example.partner.Adapter.HomeAdapter;
import com.example.partner.Adapter.TeacherCourseAdapter;
import com.example.partner.Model.User;
import com.example.partner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherActivity extends AppCompatActivity {
//    CircleImageView profile_image;
//    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    BottomNavigationView bottomNavigationView;
    String Url="https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        //toolbar設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //滾動頁面
        //滾動頁面(個人檔案為首頁)
        ViewPager2 viewPager2 = findViewById(R.id.view_paper);
        TeacherCourseAdapter courseAdapter = new TeacherCourseAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(courseAdapter);
        viewPager2.setCurrentItem(0);
        //底下導覽條
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_profile:
                        item.setChecked(true);
                        View profileView = item.getActionView();
                        if (profileView != null) {
                            profileView.setBackground(getResources().getDrawable(R.drawable.bottom_nav_item_icon));
                        }
                        viewPager2.setCurrentItem(2);
                        break;
                    case R.id.navigation_chat:
                        item.setChecked(true);
                        View profileView2 = item.getActionView();
                        if (profileView2 != null) {
                            profileView2.setBackground(getResources().getDrawable(R.drawable.bottom_nav_item_icon));
                        }
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.navigation_news:
                        item.setChecked(true);
                        View profileView3 = item.getActionView();
                        if (profileView3 != null) {
                            profileView3.setBackground(getResources().getDrawable(R.drawable.bottom_nav_item_icon));
                        }
                        viewPager2.setCurrentItem(0);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(TeacherActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            //finish();
            return true;
        }
            if (item.getItemId() == R.id.creatgroup) {
                RequestNewGroup();
                return true;
            }

        return false;
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder=new AlertDialog.Builder(TeacherActivity.this,R.style.AlertDialog);
        builder.setTitle("新增群組名稱:");
        final EditText groupNameField=new EditText(TeacherActivity.this);
        groupNameField.setHint("ex:classA");
        builder.setView(groupNameField);
        builder.setPositiveButton("新增", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName=groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(TeacherActivity.this,"請輸入群組名稱",Toast.LENGTH_SHORT).show();
                }else{
                    CreateNewGroup(groupName);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void CreateNewGroup(String groupName) {
        String userid = firebaseUser.getUid();
        reference=FirebaseDatabase.getInstance(Url).getReference("Groups").child(groupName);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", groupName);
        hashMap.put("creater", userid);
        hashMap.put("imageURL", "default");
        hashMap.put("rollcall","off");
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(TeacherActivity.this,groupName+"群組創建成功",Toast.LENGTH_SHORT).show();
                }
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
}