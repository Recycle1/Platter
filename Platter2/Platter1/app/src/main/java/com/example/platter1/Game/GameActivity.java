package com.example.platter1.Game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.platter1.LoadingActivity;
import com.example.platter1.MainActivity;
import com.example.platter1.Public.Public_data;
import com.example.platter1.Public.Public_method;
import com.example.platter1.Public.WebTool;
import com.example.platter1.R;
import com.example.platter1.WatchActivity;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.util.Constant;
import com.google.zxing.util.QrCodeGenerator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private EditText editText;
    private Button btn_begin;
    private Button btn_back;
    private ImageView imgQrcode;
    private TextView tv_number;
    int request_number=1;
    int current_number=0;
    String connect_code;

    private Button btn_connect;

    String select_member=null;

    int game_number;

    //得到的二维码解析，即connect_code
    String get_qr_code=null;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println(Public_data.url+"select_member.php?connect_code="+connect_code);
                                select_member=WebTool.touchHtml(Public_data.url+"select_member.php?connect_code="+connect_code);
                                handler.sendEmptyMessage(3);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case 1:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(true){
                                try {
                                    String result=WebTool.touchHtml(Public_data.url+"get_select_member.php?connect_code="+get_qr_code);
                                    if(!result.equals("无")){
                                        select_member=result;
                                        handler.sendEmptyMessage(2);
                                        break;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                    break;
                case 2:
                    Intent intent1=new Intent(GameActivity.this,Search_best_Activity.class);
                    intent1.putExtra("select_member",select_member);
                    System.out.println("current_number:"+current_number);
                    intent1.putExtra("person_number",current_number);
                    intent1.putExtra("connect_code",connect_code);
                    intent1.putExtra("identity",1);
                    startActivity(intent1);
                    break;
                case 3:
                    Intent intent=new Intent(GameActivity.this,Search_best_Activity.class);
                    intent.putExtra("select_member",select_member);
                    System.out.println("current_number:"+current_number);
                    intent.putExtra("person_number",current_number);
                    intent.putExtra("connect_code",connect_code);
                    intent.putExtra("identity",0);
                    startActivityForResult(intent,1);
                    break;
            }

            return false;
        }
    });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        editText=findViewById(R.id.et_person_num);
        btn_begin=findViewById(R.id.btn_begin);
        btn_back=findViewById(R.id.btn_back);
        btn_connect=findViewById(R.id.btn_connect);
        imgQrcode=findViewById(R.id.iv_imgQrcode);
        tv_number=findViewById(R.id.tv_number);
        game_number=getIntent().getIntExtra("game_number",2);
        editText.setText(String.valueOf(game_number));
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity.this.finish();
            }
        });
        btn_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将人数选择的结果给person_num
                int number=Integer.valueOf(editText.getText().toString());
                request_number=number;
                //更新UI界面
                editText.setVisibility(View.GONE);
                Random rand = new Random();
                connect_code="cc_"+Public_method.getRandom();
                imgQrcode.setVisibility(View.VISIBLE);
                generateQrCode(connect_code);
                tv_number.setText("等待加入");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result=WebTool.touchHtml(Public_data.url+"insert_search_connect.php?connect_code="+connect_code+"&tel="+Public_data.tel);
                            if(!result.equals("failed")){
                                while(true){
                                    current_number=Integer.valueOf(WebTool.touchHtml(Public_data.url+"get_current_number.php?connect_code="+connect_code));
                                    if(current_number==request_number){
                                        handler.sendEmptyMessage(0);
                                        break;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrCode();
            }
        });
    }

    private void generateQrCode(String msg) {
        Bitmap bitmap = QrCodeGenerator.getQrCodeImage(msg, imgQrcode.getWidth(), imgQrcode.getHeight());
        if (bitmap == null) {
            Toast.makeText(this, "生成二维码出错", Toast.LENGTH_SHORT).show();
            imgQrcode.setImageBitmap(null);
        } else {
            imgQrcode.setImageBitmap(bitmap);
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
            ActivityCompat.requestPermissions(GameActivity.this, new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 申请文件读写权限（部分朋友遇到相册选图需要读写权限的情况，这里一并写一下）
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请至权限中心打开本应用的文件读写权限", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(GameActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.REQ_PERM_EXTERNAL_STORAGE);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(GameActivity.this, CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            //将扫描出的信息显示出来
            get_qr_code= bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Public_data.url+"add_member.php?connect_code="+get_qr_code+"&tel="+Public_data.tel);
                        String result=WebTool.touchHtml(Public_data.url+"add_member.php?connect_code="+get_qr_code+"&tel="+Public_data.tel);
                        connect_code=get_qr_code;
                        if(result.equals("加入成功")){
                            handler.sendEmptyMessage(1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        else if(requestCode==1){
            tv_number.setText("设置人数");
            editText.setText(String.valueOf(game_number));
            editText.setVisibility(View.VISIBLE);
            imgQrcode.setVisibility(View.INVISIBLE);
        }
    }

}