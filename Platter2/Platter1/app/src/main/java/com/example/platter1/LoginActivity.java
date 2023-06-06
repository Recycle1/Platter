package com.example.platter1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.platter1.Public.Public_data;
import com.example.platter1.Public.User;
import com.example.platter1.Public.WebTool;

public class LoginActivity extends AppCompatActivity {

    private EditText et_tel;
    private EditText et_password;
    private Button btn_login;
    private Button btn_register;
    private User user;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(LoginActivity.this, "请联网", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this, "手机号或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    String tel=et_tel.getText().toString();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                user=WebTool.get_user(tel);
                                handler.sendEmptyMessage(4);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case 4:
                    Public_data.tel=user.tel;
                    Public_data.name=user.name;
                    setResult(1);
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    LoginActivity.this.finish();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_tel=findViewById(R.id.et_tel);
        et_password=findViewById(R.id.et_password);
        btn_login=findViewById(R.id.btn_login);
        btn_register=findViewById(R.id.btn_register);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_tel.getText().toString().trim().length()==0){
                    Toast.makeText(LoginActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                }
                else if(et_password.getText().toString().trim().length()==0){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
                else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String tel=et_tel.getText().toString().trim();
                            String password=et_password.getText().toString().trim();
                            try {
                                String result=WebTool.touchHtml(Public_data.url+"login.php?tel="+tel+"&password="+password);
                                if(result.equals("failed")){
                                    handler.sendEmptyMessage(1);
                                }
                                else if(result.equals("0")){
                                    handler.sendEmptyMessage(2);
                                }
                                else{
                                    handler.sendEmptyMessage(3);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}