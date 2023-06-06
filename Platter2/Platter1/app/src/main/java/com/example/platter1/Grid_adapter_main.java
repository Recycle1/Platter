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
import com.example.platter1.Public.Video_info;

import java.util.ArrayList;

public class Grid_adapter_main extends RecyclerView.Adapter<Grid_adapter_main.videoHolder>{

    private Context mContext;
    private OnItemClickListener mlistener;
    public ArrayList<Video_info> list;
    int mode;

    public Grid_adapter_main(Context context, OnItemClickListener listener, int mode){
        this.mContext=context;
        this.mlistener=listener;
        this.mode=mode;
    }

    public void setList(ArrayList<Video_info> list) {
        this.list = list;
    }

    @Override
    public Grid_adapter_main.videoHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(mode==1){
            return new Grid_adapter_main.videoHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_grid_recyclerview_item1,parent,false));
        }
        else{
            return new Grid_adapter_main.videoHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_grid_recyclerview_item2,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(Grid_adapter_main.videoHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_name.setText(list.get(position).name);
        Glide.with(mContext).load(Public_data.url+"cloud_img/"+list.get(position).video_id+".jpg").into(holder.iv_img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.onClick(position);
                }
            });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

class videoHolder extends RecyclerView.ViewHolder{

        private ImageView iv_img;
        private TextView tv_name;

        public videoHolder(View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            iv_img=itemView.findViewById(R.id.iv_img);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }

}

