package com.example.platter1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.platter1.Public.Public_data;
import com.example.platter1.Public.Record;
import com.example.platter1.Public.Video_info;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.videoHolder> {

    private Context context;
    private OnItemClickListener listener;
    public ArrayList<Record> list;

    public RecordAdapter(Context context, OnItemClickListener listener){
        this.context=context;
        this.listener=listener;
        list= new ArrayList<>();
    }

    public void setRecordlist(ArrayList<Record> list) {
        this.list = list;
    }

    @Override
    public videoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new videoHolder(LayoutInflater.from(context).inflate(R.layout.layout_record_item,parent,false));
    }

    @Override
    public void onBindViewHolder(videoHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_name.setText(list.get(position).video_info.name);
        holder.tv_datetime.setText(list.get(position).datetime);
        Glide.with(context).load(Public_data.url+"cloud_img/"+list.get(position).video_info.video_id+".jpg").into(holder.iv);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class videoHolder extends RecyclerView.ViewHolder{

        private ImageView iv;
        private TextView tv_name;
        private TextView tv_datetime;

        public videoHolder(View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.iv);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_datetime=itemView.findViewById(R.id.tv_datetime);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }
}
