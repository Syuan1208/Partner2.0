package com.example.partner;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.partner.Adapter.HomeAdapter;
import com.example.partner.Adapter.StartAdapter;
import com.example.partner.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager2;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (firebaseUser != null) {
//            if (firebaseUser.getUid().equals("kYL8AzCRiFPYi6DBioC1W632r9f1")) {
//                Intent intent = new Intent(StartActivity.this, TeacherActivity.class);
//                startActivity(intent);
//                finish();
//            } else {
//                Intent intent = new Intent(StartActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }
        if (firebaseUser != null) {
            reference = FirebaseDatabase.getInstance(Url).getReference("Users").child(firebaseUser.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        if (user.getCareer().equals("老師")) {
                            Intent intent = new Intent(StartActivity.this, TeacherActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(StartActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        viewPager2=findViewById(R.id.viewpager2);
        //初始化viewpager2
        StartAdapter startAdapter2 = new StartAdapter(getSupportFragmentManager(), getLifecycle(),0);
        viewPager2.setAdapter(startAdapter2);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int position = 0;
                switch (item.getItemId()) {
                    case R.id.navigation_login:
                        position=0;
                        item.setChecked(true);
                        View loginview = item.getActionView();
                        if(loginview!=null) {
                            loginview.setBackgroundResource(R.color.catch_darkblue);
                        }
                        break;
                    case R.id.navigation_register:
                        item.setChecked(true);
                        View registerview = item.getActionView();
                        if(registerview!=null) {
                            registerview.setBackgroundResource(R.color.catch_darkblue);
                        }
                        position=1;
                        break;
                }
                StartAdapter startAdapter2 = new StartAdapter(getSupportFragmentManager(), getLifecycle(),position);
                viewPager2.setAdapter(startAdapter2);
                return true;
            }
        });
    }
}