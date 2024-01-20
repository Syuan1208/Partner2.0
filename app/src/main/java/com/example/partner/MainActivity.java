package com.example.partner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.partner.Adapter.CourseAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    //CircleImageView profile_image;
    //TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //toolbar設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //滾動頁面(個人檔案為首頁)
        ViewPager2 viewPager2 = findViewById(R.id.view_paper);
        CourseAdapter courseAdapter = new CourseAdapter(getSupportFragmentManager(), getLifecycle());
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
        //原本只有登出
//        switch (item.getItemId()) {
//            case R.id.logout:
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(MainActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                //finish();
//                return true;
//        }
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            //finish();
            return true;
        }

        if (item.getItemId() == R.id.creatgroup) {
            Toast.makeText(MainActivity.this, "只有老師可以建立群組", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
        builder.setTitle("新增群組名稱:");
        final EditText groupNameField = new EditText(MainActivity.this);
        groupNameField.setHint("ex:classA");
        builder.setView(groupNameField);
        builder.setPositiveButton("新增", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = groupNameField.getText().toString();
                if (TextUtils.isEmpty(groupName)) {
                    Toast.makeText(MainActivity.this, "請輸入群組名稱", Toast.LENGTH_SHORT).show();
                } else {
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
        reference = FirebaseDatabase.getInstance(Url).getReference("Groups").child(groupName);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", groupName);
        hashMap.put("creater", userid);
        hashMap.put("creater", userid);
        hashMap.put("imageURL", "default");
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, groupName + "群組創建成功", Toast.LENGTH_SHORT).show();
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