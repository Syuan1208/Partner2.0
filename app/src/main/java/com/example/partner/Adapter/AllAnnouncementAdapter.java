package com.example.partner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partner.R;

import java.util.ArrayList;

public class AllAnnouncementAdapter extends RecyclerView.Adapter<AllAnnouncementAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mAnnouncements;
    String Url = "https://catch-763a4-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public AllAnnouncementAdapter(Context mContext, ArrayList<String> mAnnouncements) {
        this.mAnnouncements = mAnnouncements;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_announcement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.show_message.setText(mAnnouncements.get(position));
    }

    @Override
    public int getItemCount() {
        return mAnnouncements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
        }
    }
}
