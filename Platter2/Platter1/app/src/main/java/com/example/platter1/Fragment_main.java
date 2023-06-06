package com.example.platter1;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Outline;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.platter1.Game.Watch_WAN_Activity;
import com.example.platter1.Game.Watch_WAN_R_Activity;
import com.example.platter1.Public.Video_info;
import com.example.platter1.Public.WebTool;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.util.Constant;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.transformer.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class Fragment_main  extends Fragment {

    RecyclerView rv1;
    RecyclerView rv2;
    Grid_adapter_main adapter_main1;
    Grid_adapter_main adapter_main2;
    ArrayList<Video_info> video_hot;
    ArrayList<Video_info> video_reco;

    private Button btn_scan;

    private RelativeLayout rl_1;
    private LinearLayout ll_2;

    private Banner banner;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    adapter_main1.setList(video_hot);
                    rv1.setAdapter(adapter_main1);
                    break;
                case 2:
                    adapter_main2.setList(video_reco);
                    rv2.setAdapter(adapter_main2);
                    break;
            }
            return false;
        }
    });

    public static Fragment_main newInstance() {
        Fragment_main fragment = new Fragment_main();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_scan=view.findViewById(R.id.btn_scan);
        rv1=view.findViewById(R.id.rv1);
        rv2=view.findViewById(R.id.rv2);
        rl_1=view.findViewById(R.id.rl1);
        ll_2=view.findViewById(R.id.ll2);
        rv1.setLayoutManager(new GridLayoutManager(getActivity(),2));
        rv2.setLayoutManager(new GridLayoutManager(getActivity(),2));
        banner=view.findViewById(R.id.banner);
        initBanner();
        adapter_main1=new Grid_adapter_main(getActivity(), new Grid_adapter_main.OnItemClickListener() {
            @Override
            public void onClick(int pos) {

                Intent intent=new Intent(getActivity(), Watch_WAN_Activity.class);
                intent.putExtra("video_id",video_hot.get(pos).video_id);
                startActivity(intent);

            }
        },1);
        adapter_main2=new Grid_adapter_main(getActivity(), new Grid_adapter_main.OnItemClickListener() {
            @Override
            public void onClick(int pos) {

                Intent intent=new Intent(getActivity(), Watch_WAN_Activity.class);
                intent.putExtra("video_id",video_reco.get(pos).video_id);
                startActivity(intent);

            }
        },2);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    video_hot= WebTool.get_video_hot();
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    video_reco=WebTool.get_video_reco();
                    handler.sendEmptyMessage(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrCode();
            }
        });
        rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Search_videoActivity.class);
                startActivity(intent);
            }
        });
        ll_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Search_videoActivity.class);
                startActivity(intent);
            }
        });
    }

    void initBanner(){
        ArrayList list=new ArrayList();
        list.add("https://www.recycle11.top/notify/ad/1.png");
        list.add("https://www.recycle11.top/notify/ad/2.png");
        list.add("https://www.recycle11.top/notify/ad/3.png");
//        banner.setImageLoader(new GlideImageLoader());
//        banner.setImages(list);
        banner.setDatas(list);
        banner.setAdapter(new ImageAdapter(list));
        banner.setPageTransformer(new DepthPageTransformer());
        banner.setIndicator(new RectangleIndicator(getActivity()));
        banner.start();
    }

    public class ImageAdapter extends BannerAdapter<String, ImageAdapter.BannerViewHolder> {

        public ImageAdapter(List<String> mDatas) {
            //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
            super(mDatas);
        }

        //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
        @Override
        public ImageAdapter.BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
            ImageView imageView = new ImageView(parent.getContext());
            //注意，必须设置为match_parent，这个是viewpager2强制要求的
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return new ImageAdapter.BannerViewHolder(imageView);
        }

        @Override
        public void onBindView(ImageAdapter.BannerViewHolder holder, String data, int position, int size) {
//            holder.imageView.setImageResource(data.imageRes);
            Glide.with(getActivity()).load(mDatas.get(position)).into(holder.imageView);
            holder.imageView.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(80, 0, view.getWidth()-80, view.getHeight(), 40);
                }
            });
            holder.imageView.setClipToOutline(true);
            holder.imageView.setPadding(80,0,80,0);
        }

        class BannerViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public BannerViewHolder(@NonNull ImageView view) {
                super(view);
                this.imageView = view;
            }
        }
    }

    // 开始扫码
    private void startQrCode() {
        // 申请相机权限
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission
                    .CAMERA)) {
                Toast.makeText(getActivity(), "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 申请文件读写权限（部分朋友遇到相册选图需要读写权限的情况，这里一并写一下）
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(getActivity(), "请至权限中心打开本应用的文件读写权限", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.REQ_PERM_EXTERNAL_STORAGE);
            return;
        }
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            Intent intent=new Intent();
            intent.setClass(getActivity(), Watch_WAN_R_Activity.class);
            String cc_and_tel[]=scanResult.split("#");
            String connect_code=cc_and_tel[0];
            String video_id=cc_and_tel[1];
            intent.putExtra("connect_code",connect_code);
            intent.putExtra("video_id",video_id);
            startActivity(intent);
        }
    }

}
