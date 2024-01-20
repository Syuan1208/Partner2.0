package com.example.partner.Fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.partner.ClassNewsActivity;
import com.example.partner.Model.User;
import com.example.partner.R;
import com.example.partner.TeacherQuizActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrawStrawFragment extends Fragment {
    View view;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    TextView textView1;
    TextView textView2;
    TextView textView3;
    Button button;
    //private List<User> mUser;
    ArrayList<String> mUser = new ArrayList();
    ArrayList<String> studentlist = new ArrayList<>();
    int i = 0;
    int click_times;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_draw_straw, container, false);
        textView1 = view.findViewById(R.id.textview1);
        textView2 = view.findViewById(R.id.textview2);
        button = view.findViewById(R.id.drawstraw);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roll();
            }
        });
        return view;
    }

    private void roll() {
        int number = (int) (Math.random() * studentlist.size());
        textView1.setText(studentlist.get(number));
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        studentlist = ((TeacherQuizActivity) activity).getstudentlist();
    }
}