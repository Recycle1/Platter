package com.example.platter1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.util.Constant;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    private char Vision[] = null;
    static int width;
    static int height;
    static int vis;
    private Button send;
    private Button fasong;
    private Button receive;
    private Button jieshou;
    //private Switch net;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        send=findViewById(R.id.btn_s);
        receive=findViewById(R.id.btn_r);
        fasong=findViewById(R.id.btn_send);
        jieshou=findViewById(R.id.btn_receive);
        //net=findViewById(R.id.switch1);
        WindowManager windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        display.getRealSize(outPoint);
        Public_data.screen_width=outPoint.x;
        Public_data.screen_height=outPoint.y;
        if(Public_data.screen.size()==0){
            Public_data.screen.add(Public_data.screen_width+"#"+Public_data.screen_height);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        Vision = Build.VERSION.RELEASE.toCharArray();
        for (int i = 0, j = 1; (i < Vision.length) && (Vision[i] != '.'); i++, j = j * 10) {
            vis = vis * j + ((int) Vision[i] - 48);
        }
        Public_data.vis=vis;
        setlistener();
    }
    private void setlistener(){
        OnClick onClick=new OnClick();
        send.setOnClickListener(onClick);
        receive.setOnClickListener(onClick);
        fasong.setOnClickListener(onClick);
        jieshou.setOnClickListener(onClick);
//        net.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    Public_data.net_way="通用";
//                }else{
//                    Public_data.net_way="WIFI";
//                }
//            }
//        });
    }
    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_s:
                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this,GridRecyclerViewActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_r:
                    P_Client.chuanshu=false;
//                    Intent intent1=new Intent();
//                    intent1.setClass(MainActivity.this,ShareActivity.class);
//                    startActivity(intent1);
                    startQrCode();
                    break;
                case R.id.btn_send:
                    send.setVisibility(VISIBLE);
                    receive.setVisibility(GONE);
                    fasong.setBackgroundColor(Color.parseColor("#C1C5EA"));
                    jieshou.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    break;
                case R.id.btn_receive:
                    send.setVisibility(GONE);
                    receive.setVisibility(VISIBLE);
                    fasong.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    jieshou.setBackgroundColor(Color.parseColor("#C1C5EA"));
                    break;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(0!=(Intent.FLAG_ACTIVITY_CLEAR_TOP&intent.getFlags())){
            finish();
            System.exit(0);
        }
    }

    // 开始扫码
    private void startQrCode() {
        // 申请相机权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .CAMERA)) {
                Toast.makeText(this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 申请文件读写权限（部分朋友遇到相册选图需要读写权限的情况，这里一并写一下）
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请至权限中心打开本应用的文件读写权限", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.REQ_PERM_EXTERNAL_STORAGE);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,LoadingActivity.class);
            intent.putExtra("ip",scanResult);
            startActivityForResult(intent,123);
        }

        else if(resultCode==123){
            receive.setPressed(true);
            receive.setPressed(false);
        }
    }

}