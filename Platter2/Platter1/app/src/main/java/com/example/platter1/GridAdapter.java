package com.example.platter1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.platter1.Game.Watch_WAN_Activity;
import com.example.platter1.Public.Public_data;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.mViewHolder> {

    private Context mContext;
    private OnItemClickListener mlistener;
    private LayoutInflater mLayoutInflater;
    private List<MediaBean> mList;

    public GridAdapter(Context context,List<MediaBean> mList){
        this.mContext=context;
        mLayoutInflater=LayoutInflater.from(context);
        this.mList=mList;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new mViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_grid_item,parent,false));
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imageView.setImageBitmap(getItem(position).getThumbImg());
//        holder.textView.setText(getItem(position).getMediaName());
//        if(getItem(position).getMediaName().length()<10){
//            holder.textView.setText(getItem(position).getMediaName());
//        }
//        else{
//            holder.textView.setText(getItem(position).getMediaName().substring(0,9)+"...");
//        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                if(Public_data.net.equals("WIFI")){
                    intent.setClass(mContext,WatchActivity.class);

                }
                else if(Public_data.net.equals("流量")){
                    intent.setClass(mContext, Watch_WAN_Activity.class);
                }
                intent.putExtra("path",mList.get(position).getPath());
                mContext.startActivity(intent);
            }
        });
    }

    class mViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView textView;
        public mViewHolder(View itemView) {
            super(itemView);
            imageView=  itemView.findViewById(R.id.iv_grid);
            textView= itemView.findViewById(R.id.tv_title);
        }
    }

//    @Override
//    public void onBindViewHolder(linearViewHolder holder, int position) {
//        holder.textView.setText("Hello");
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mlistener.onClick(position);
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public MediaBean getItem(int position) {
        return mList.get(position);
    }

//    class linearViewHolder extends RecyclerView.ViewHolder{
//
//        private TextView textView;
//
//        public linearViewHolder(View itemView) {
//            super(itemView);
//            textView=itemView.findViewById(R.id.tv_title);
//        }
//    }

    static class ViewHolder{
        public static <T extends View> T get(View view, int id) {
            SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<View>();
                view.setTag(viewHolder);
            }
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return (T) childView;
        }
    }


    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder=null;
//        if(convertView==null){
//            convertView=mLayoutInflater.inflate(R.layout.layout_grid_item,null);
//            holder=new ViewHolder();
//            holder.imageView=(ImageView)convertView.findViewById(R.id.iv_grid);
//            holder.textView=(TextView)convertView.findViewById(R.id.tv_title);
//            convertView.setTag(holder);
//        }else{
//            holder=(ViewHolder)convertView.getTag();
//        }
//        //赋值
//        holder.textView.setText("花");
//        //Glide.with(mContext).load("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3357165169,2293375246&fm=26&gp=0.jpg").into(holder.imageView);
//        holder.imageView.setImageBitmap(getItem(position).getThumbImg());
        convertView=mLayoutInflater.inflate(R.layout.layout_grid_item,null);
        ImageView imageView;
        TextView textView;
        imageView= MyGridViewAdapter.ViewHolder.get(convertView,R.id.iv_grid);
        textView= MyGridViewAdapter.ViewHolder.get(convertView,R.id.tv_title);
        imageView.setImageBitmap(getItem(position).getThumbImg());
        if(getItem(position).getMediaName().length()<10){
            textView.setText(getItem(position).getMediaName());
        }
        else{
            textView.setText(getItem(position).getMediaName().substring(0,9)+"...");
        }
        textView.setText("11");
        return convertView;
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setMlistener(OnItemClickListener mlistener) {
        this.mlistener = mlistener;
    }
}
