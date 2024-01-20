package com.example.partner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.partner.Adapter.GroupMessageAdapter;
import com.example.partner.Model.Chat;
import com.example.partner.Model.Group;
import com.example.partner.Model.NoNamechat;
import com.example.partner.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMessageActivity extends AppCompatActivity {
    TextView groupname;//群組名稱
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;
    ImageButton btn_send;//送出按鈕
    EditText text_send;//要送出的文字
    GroupMessageAdapter groupmessageAdapter;
    List<Chat> mchat;//聊天紀錄
    RecyclerView recyclerView;
    ValueEventListener seenListener;//已讀 未讀
    String classname;//選到的群組名稱
    String senderID;
    private List<Chat> sender;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    String mCarrer;
    ArrayList<String> mID = new ArrayList();
    ArrayList<String> mID_Name = new ArrayList();
    private Context mContext;
    String classrollcall;
    String opentime;
    String username;
    String whether_rollcall;
    ArrayList<String> studentlist = new ArrayList();
    //設計我的dialog
    ImageButton mydialog;
    Dialog dialog;
    private TextToSpeech mTts;
    //得到全部學生點名沒
    ArrayList<String> studentlist_rollcall = new ArrayList();
    ArrayList<String> all_announcement= new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);
        //聊天室介面設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //
        judgecareer();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                if(mCarrer.equals("學生")){
                    startActivity(new Intent(GroupMessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }else{
                    startActivity(new Intent(GroupMessageActivity.this, TeacherActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }
        });
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        groupname = findViewById(R.id.groupname);
        //進入聊天室後
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        intent = getIntent();
        classname = intent.getStringExtra("groupname");

        //所有公告內容
        AllAnnouncement();

        //傳送訊息
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(firebaseUser.getUid(), classname, msg);
                } else {
                    Toast.makeText(GroupMessageActivity.this, "無法傳送訊息", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });
        // 初始化 TextToSpeech
        mTts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    mTts.setLanguage(Locale.getDefault()); // 設置語言
                }
            }
        });
        //呼叫mydialog
        mydialog = findViewById(R.id.mydialog);
        dialog = new Dialog(this);
        mydialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //傳送匿名訊息
                sendNonameText(text_send.getText().toString());
                text_send.setText("");
                Toast.makeText(GroupMessageActivity.this, "訊息將在3秒後廣播", Toast.LENGTH_SHORT).show();
            }
        });

        reference = FirebaseDatabase.getInstance(Url).getReference("Groups").child(classname);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Group group = snapshot.getValue(Group.class);//得到classA或B或C
                groupname.setText(group.getName());
                readMessages(firebaseUser.getUid(), classname, group.getImageURL());
                //測試是否大家都能收到跳訊息
                recievedialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        seenMessage(classname);
        judgecareer();
        judgeid();
        judgename();
        //丟給後面fragment用
        Ifclassrollcall();
        Openttime();
        getStudentUsername();
        getUserrollcall();
        getStudentlist();
        getAllUserrollcall();

    }

    //
    private void AllAnnouncement() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference("公告");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                all_announcement.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(classname)) {
                        all_announcement.add(chat.getMessage());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //接收小視窗
    private void recievedialog() {
        // 監聽 Firebase 中的新數據
//        DatabaseReference messagesRef = FirebaseDatabase.getInstance(Url).getReference("NonameChat");
//        messagesRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                // 獲取最新的聊天訊息
//                String latestMessage = "";
//                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
//                    latestMessage = messageSnapshot.getValue(String.class);
//                }
//                // 彈出 dialog 顯示最新的聊天訊息
//                if (!latestMessage.equals("") & latestMessage.equals(latestMessage)) {
//                    callmydialog(latestMessage);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // 處理錯誤
//            }
//        });
        // 監聽 Firebase 中的新數據 版本2
        DatabaseReference messagesRef = FirebaseDatabase.getInstance(Url).getReference("NonameChat");
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                NoNamechat noNamechat = snapshot.getValue(NoNamechat.class);
                if (noNamechat.getStatus().equals("未廣播")) {//如果訊息是未廣播
                    callmydialog(noNamechat.getMessage());//呼叫小視窗
                }
                //}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNonameText(String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference("NonameChat");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("message", message);
        hashMap.put("sender", username);
        hashMap.put("status", "未廣播");
        reference.setValue(hashMap);
    }

    private void callmydialog(String message) {
        dialog.setContentView(R.layout.mydialog);
        TextView mydialog_textview = dialog.findViewById(R.id.mydialog_textview);
        mydialog_textview.setText(message);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //準備音效
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.mydialog_sound_effect);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //dialog.show();
        final Handler handler = new Handler();
        // 延遲顯示 Dialog
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.start();
                dialog.show();
//                if(dialog.isShowing()){
//                    mTts.speak(message, TextToSpeech.QUEUE_FLUSH, null); // 播報語音
//                }
                // 設定 Dialog 自動消失
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        mediaPlayer.release();
                    }
                }, 3000);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                    mTts.speak(message, TextToSpeech.QUEUE_FLUSH, null); // 播報語音

                    }
                });
            }
        }, 3000);
        broadcaststatus();
    }

    private void getStudentlist() {
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentlist.clear();
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
    }

    //判斷學生或老師
    private void judgecareer() {
        DatabaseReference reference1 = FirebaseDatabase.getInstance(Url).getReference("Users");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getId().equals(firebaseUser.getUid())) {
                        mCarrer=user.getCareer();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //儲存所有使用者ID
    private void judgeid() {
        DatabaseReference reference1 = FirebaseDatabase.getInstance(Url).getReference("Users");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mID.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    mID.add(user.getId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //儲存所有使用者ID
    private void judgename() {
        DatabaseReference reference1 = FirebaseDatabase.getInstance(Url).getReference("Users");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mID_Name.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    mID_Name.add(user.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void seenMessage(String classname) {
        reference = FirebaseDatabase.getInstance(Url).getReference("GroupChats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (!(chat.getSender().equals(firebaseUser.getUid()))) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        dataSnapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference();
        HashMap<String, Object> haspMap = new HashMap<>();
        haspMap.put("sender", sender);
        haspMap.put("receiver", receiver);//這邊的rerceiver是classname
        haspMap.put("message", message);
        haspMap.put("isseen", false);
        reference.child("GroupChats").push().setValue(haspMap);
        //新增使用者到聊天室
        final DatabaseReference chatRef = FirebaseDatabase.getInstance(Url).getReference("Grouplist").child(classname);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRef.child("senderid").setValue(firebaseUser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessages(final String myid, final String groupname, final String imageURL) {
        mchat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance(Url).getReference("GroupChats");//全部的group
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mchat.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Chat chat = snapshot1.getValue(Chat.class);
                    if (chat.getReceiver().equals(classname)) {
                        mchat.add(chat);
                    }
                    groupmessageAdapter = new GroupMessageAdapter(GroupMessageActivity.this, mchat, mID, mID_Name, imageURL, classname,all_announcement);
                    recyclerView.setAdapter(groupmessageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //判斷課程是否開啟並傳到StudentRollCallFragment
    private void Ifclassrollcall() {
        reference = FirebaseDatabase.getInstance(Url).getReference("Groups").child(classname);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Group group = snapshot.getValue(Group.class);
                classrollcall = group.getRollcall();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //得到開啟點名時間並傳到StudentRollCallFragment
    private void Openttime() {
        reference = FirebaseDatabase.getInstance(Url).getReference("Groups").child(classname);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Group group = snapshot.getValue(Group.class);
                opentime = group.getCurrenttime();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //得到使用者名稱
    public void getStudentUsername() {
        reference = FirebaseDatabase.getInstance(Url).getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getId().equals(firebaseUser.getUid())) {
                        username = user.getUsername();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //得到使用者簽到沒
    private void getUserrollcall() {
        reference = FirebaseDatabase.getInstance(Url).getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                whether_rollcall = user.getRollcall();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAllUserrollcall() {
        reference = FirebaseDatabase.getInstance(Url).getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentlist_rollcall.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (!user.getCareer().equals("老師"))
                        studentlist_rollcall.add(user.getUsername() + " : " + user.getRollcall());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void broadcaststatus() {
        HashMap<String, Object> hashMap = new HashMap<>();
        reference = FirebaseDatabase.getInstance(Url).getReference("NonameChat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                NoNamechat noNamechat = snapshot.getValue(NoNamechat.class);
                if (noNamechat.getStatus().equals("未廣播")) {
                    hashMap.put("status", "已廣播");
                    reference.updateChildren(hashMap);
                }
                // }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        //status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //reference.removeEventListener(seenListener);
        //status("offline");
    }

}