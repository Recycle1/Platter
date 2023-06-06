package com.example.platter1;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.platter1.Game.Watch_WAN_R_Activity;
import com.example.platter1.Public.Public_data;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.util.Constant;

public class Fragment_storage extends Fragment {

    private Button send;
    private Button receive;
    private Switch switch_net;

    public static Fragment_storage newInstance() {
        Fragment_storage fragment = new Fragment_storage();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storage, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        send=view.findViewById(R.id.btn_s);
        receive=view.findViewById(R.id.btn_r);
        switch_net=view.findViewById(R.id.switch_net);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),GridRecyclerViewActivity.class);
                startActivity(intent);
            }
        });
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P_Client.chuanshu=false;
                startQrCode();
            }
        });
        switch_net.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Public_data.net="流量";
                }
                else{
                    Public_data.net="WIFI";
                }
            }
        });
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
            if(Public_data.net.equals("WIFI")){
                //将扫描出的信息显示出来
                Intent intent=new Intent();
                intent.setClass(getActivity(),LoadingActivity.class);
                intent.putExtra("ip",scanResult);
                startActivityForResult(intent,123);
            }else if(Public_data.net.equals("流量")){
                //将扫描出的信息显示出来
                Intent intent=new Intent();
                intent.setClass(getActivity(), Watch_WAN_R_Activity.class);
                String cc_and_tel[]=scanResult.split("#");
                String connect_code=cc_and_tel[0];
                String tel=cc_and_tel[1];
                intent.putExtra("connect_code",connect_code);
                intent.putExtra("tel",tel);
                startActivity(intent);
            }
        }

        else if(resultCode==123){

        }
    }

}
