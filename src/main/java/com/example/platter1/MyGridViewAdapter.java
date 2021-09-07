package com.example.platter1;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<MediaBean> mList;

    public MyGridViewAdapter(Context context,List<MediaBean> mList){
        this.mContext=context;
        mLayoutInflater=LayoutInflater.from(context);
        this.mList=mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public MediaBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

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

    @Override
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
        imageView= ViewHolder.get(convertView,R.id.iv_grid);
        textView= ViewHolder.get(convertView,R.id.tv_title);
        imageView.setImageBitmap(getItem(position).getThumbImg());
        if(getItem(position).getMediaName().length()<10){
            textView.setText(getItem(position).getMediaName());
        }
        else{
            textView.setText(getItem(position).getMediaName().substring(0,9)+"...");
        }
        return convertView;
    }
}
