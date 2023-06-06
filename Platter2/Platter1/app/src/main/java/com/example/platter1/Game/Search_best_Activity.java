package com.example.platter1.Game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.platter1.Public.Public_data;
import com.example.platter1.Public.WebTool;
import com.example.platter1.R;
import com.google.zxing.util.QrCodeGenerator;

import java.util.Random;

public class Search_best_Activity extends AppCompatActivity {

    private ImageView imgQrcode;
    private FrameLayout fl;

    String select_member_list;
    int person_number;
    int identity;
    String connect_code;
    String member_order;
    String member_list[];

    //接收端得到的顺序
    String get_member_order[];

    int num;

    Handler handler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    for(int i=0;i<member_list.length;i++){

                    }

                    if(select_member_list.contains(Public_data.tel)){
                        Intent intent=new Intent(Search_best_Activity.this,TugActivity.class);
                        if(select_member_list.split("#")[0].equals(Public_data.tel)){
                            intent.putExtra("number",1);
                        }
                        else{
                            intent.putExtra("number",2);
                        }
                        intent.putExtra("connect_code",connect_code);
                        startActivityForResult(intent,123);
                    }
                    else{
                        Search_best_Activity.this.finish();
                    }
                    break;
                case 2:

                    System.out.println("member_order:"+member_list.length);
                    System.out.println("get_member_order:"+get_member_order.length);
                    System.out.println(get_member_order[0]);

                    for(int i=0;i<member_list.length;i++){
                        if(member_list[i].equals(Public_data.tel)){
//                            for(int j=0;j<get_member_order.length;j++){
//                                fl.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                                if(Integer.valueOf(get_member_order[j])==i){
//                                    fl.setBackgroundColor(Color.parseColor("#000000"));
//                                }
//
//                            }
                            break;
                        }
                        System.out.println(Public_data.tel);
                        if(select_member_list.contains(Public_data.tel)){
                            Intent intent1=new Intent(Search_best_Activity.this,TugActivity.class);
                            if(select_member_list.split("#")[0].equals(Public_data.tel)){
                                intent1.putExtra("number",1);
                            }
                            else{
                                intent1.putExtra("number",2);
                            }
                            intent1.putExtra("connect_code",connect_code);
                            startActivityForResult(intent1,123);
                        }
                        else{
                            Search_best_Activity.this.finish();
                        }
                    }
                    break;
                case 3:
                    if(identity==0){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Random random=new Random();
                                    num=random.nextInt(5)+3;
                                    for(int i=0;i<num;i++){
                                        if(i==0){
                                            member_order=String.valueOf(random.nextInt(person_number))+"-";
                                        }
                                        else if(i==num-1){
                                            member_order+=String.valueOf(random.nextInt(person_number));
                                        }
                                        else{
                                            member_order+=String.valueOf(random.nextInt(person_number))+"-";
                                        }
                                    }
                                    System.out.println(Public_data.url+"send_member_order.php?connect_code="+connect_code+"&member_order="+member_order);
                                    WebTool.touchHtml(Public_data.url+"send_member_order.php?connect_code="+connect_code+"&member_order="+member_order);
                                    handler.sendEmptyMessage(1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    else if(identity==1){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    System.out.println(Public_data.url+"get_member_order.php?connect_code="+connect_code);
                                    get_member_order=WebTool.touchHtml(Public_data.url+"get_member_order.php?connect_code="+connect_code).split("-");
                                    handler.sendEmptyMessage(2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    break;
            }
            return false;
        }
    });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serarch_best);

        fl=findViewById(R.id.fl);

        select_member_list=getIntent().getStringExtra("select_member");
        person_number=getIntent().getIntExtra("person_number",1);

        connect_code=getIntent().getStringExtra("connect_code");
        identity=getIntent().getIntExtra("identity",0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    member_list=WebTool.touchHtml(Public_data.url+"get_member_list.php?connect_code="+connect_code).split("#",person_number);
                    handler.sendEmptyMessage(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();





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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123){
            Search_best_Activity.this.finish();
        }
    }
}