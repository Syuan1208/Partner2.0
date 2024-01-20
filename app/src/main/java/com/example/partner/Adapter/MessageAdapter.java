package com.example.partner.Adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.partner.Model.Chat;
import com.example.partner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chat> mChat;
    private String imageURL;
    private String receiverName;
    FirebaseUser firebaseUser;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";
    String thelastannouncement;
    ArrayList<String> all_announcement;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageURL, String receiverName,ArrayList<String> all_announcement) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageURL = imageURL;
        this.receiverName = receiverName;
        this.all_announcement=all_announcement;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());

        if (imageURL.equals("default")) {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(imageURL).into(holder.profile_image);
        }
        holder.username.setText(receiverName);
        if (position == mChat.size() - 1) {
            if (chat.getIsseen()) {
                holder.txt_seen.setText("已讀");
            } else {
                holder.txt_seen.setText("未讀");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;
        public TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            username = itemView.findViewById(R.id.username);
            //最新公告
            PopupWindow popupWindow = new PopupWindow(itemView.getContext());
            View popupView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.popup_menu, null);
            popupWindow.setContentView(popupView);
            //
            int hight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, itemView.getContext().getResources().getDisplayMetrics());
            popupWindow.setHeight(hight);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.announcement));
            // 設置 PopupWindow 的顯示位置
            popupWindow.showAtLocation(itemView, Gravity.NO_GRAVITY, 0, 310);
            // 設置 PopupWindow 的菜單項點擊事件
            TextView announcementTextView = popupView.findViewById(R.id.announcement);
            ImageButton cancel_btn=popupView.findViewById(R.id.cancel_btn);
            ImageButton expand_btn=popupView.findViewById(R.id.expand_btn);
            //設定公告
            lastAnnouncement(announcementTextView);
            //展開所有公告
            expand_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow.isShowing()) {
                        //全部公告
                        PopupWindow popupWindow2 = new PopupWindow(itemView.getContext());
                        View popupView2 = LayoutInflater.from(itemView.getContext()).inflate(R.layout.all_announcement, null);
                        popupWindow2.setContentView(popupView2);
                        //
                        int hight2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 630, itemView.getContext().getResources().getDisplayMetrics());
                        popupWindow2.setHeight(hight2);
                        popupWindow2.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                        Drawable drawable2 = new ColorDrawable(ContextCompat.getColor(popupView2.getContext(), android.R.color.transparent));
                        popupWindow2.setBackgroundDrawable(drawable2);
                        // 設置 PopupWindow 的顯示位置
                        popupWindow2.showAtLocation(itemView, Gravity.NO_GRAVITY, 0, 310);
                        RecyclerView recyclerView = popupView2.findViewById(R.id.recycle_view);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(popupView2.getContext()));
                        AllAnnouncementAdapter allAnnouncementAdapter = new AllAnnouncementAdapter(popupView2.getContext(), all_announcement);
                        recyclerView.setAdapter(allAnnouncementAdapter);
                        //
                        ImageButton cancel_all=popupView2.findViewById(R.id.cancel_all);
                        cancel_all.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow2.dismiss();
                            }
                        });
                    }
                }
            });
            //取消公告欄
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            //訊息長按功能
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // 創建PopupMenu對象
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    // 設置PopupMenu的菜單佈局文件
                    popupMenu.inflate(R.menu.popup_menu);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        popupMenu.setGravity(Gravity.END);
                    }
                    // 設置PopupMenu的菜單項點擊事件
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            // 根據菜單項的id處理對應的事件
                            switch (menuItem.getItemId()) {
                                case R.id.announcement:
                                    setannouncement(firebaseUser.getUid(), receiverName, show_message.getText().toString());
//                                    Log.i("測試",show_message.getText().toString());
                                default:
                                    return false;
                            }
                        }
                    });
                    // 顯示PopupMenu
                    popupMenu.show();
                    return true;
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
    private void setannouncement(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance(Url).getReference();
        HashMap<String, Object> haspMap = new HashMap<>();
        haspMap.put("sender", sender);
        haspMap.put("receiver", receiver);
        haspMap.put("message", message);
        haspMap.put("isseen", false);
        reference.child("公告").push().setValue(haspMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //可以做最新的公告
                Log.i("測試", "上傳資料庫成功");
            }
        });

    }
    //檢查訊息
    private void lastAnnouncement(TextView last_msg){
        thelastannouncement="default";
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance(Url).getReference("公告");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Chat chat=dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(receiverName)){
                        thelastannouncement=chat.getMessage();
                    }
                }
                switch (thelastannouncement){
                    case "default":
                        last_msg.setText("無公告");
                        break;
                    default:
                        last_msg.setText(thelastannouncement);
                        break;
                }
                thelastannouncement="無公告";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
