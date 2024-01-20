package com.example.partner.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partner.R;

import java.util.ArrayList;

public class RollcallRecord_Adapter extends RecyclerView.Adapter<RollcallRecord_Adapter.MyViewHolder> {
    private ArrayList<String> mData;
    private String lastmdata;

    public RollcallRecord_Adapter(ArrayList<String> data) {
        mData = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rollcallrecord, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewDescription.setText("");
        holder.textViewTitle.setText("點名紀錄");
        for(int i=0;i<mData.size();i++){
            if(i==0) {
                lastmdata =mData.get(i) + "\n";
            }else{
                lastmdata =lastmdata+mData.get(i) + "\n";
            }
        }
        holder.textViewDescription.setText(lastmdata);
        lastmdata="";
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;

        MyViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }
}
