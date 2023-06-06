package com.example.platter1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.platter1.Public.Public_data;
import com.example.platter1.Public.WebTool;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_tel;
    private EditText et_user_name;
    private EditText et_password;
    private EditText et_confirm;

    private RadioButton man;
    private RadioButton woman;

    String tel;
    String user_name;
    String gender;
    String password;
    String confirm;

    private Button btn_back;
    private Button btn_register;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(RegisterActivity.this, "手机号已注册", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String result=WebTool.touchHtml(Public_data.url+"register.php?tel="+tel+"&name="+user_name+"&gender="+gender+"&password="+password);
                                if(result.equals("注册成功")){
                                    handler.sendEmptyMessage(3);
                                }
                                else{
                                    handler.sendEmptyMessage(4);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case 3:
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    RegisterActivity.this.finish();
                    break;
                case 4:
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_tel=findViewById(R.id.et_tel);
        et_user_name=findViewById(R.id.et_user_name);
        et_password=findViewById(R.id.et_password);
        et_confirm=findViewById(R.id.et_confirm);
        man=findViewById(R.id.man);
        woman=findViewById(R.id.woman);
        btn_back=findViewById(R.id.btn_back);
        btn_register=findViewById(R.id.btn_register);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tel=et_tel.getText().toString().trim();
                user_name=et_user_name.getText().toString().trim();
                password=et_password.getText().toString().trim();
                confirm=et_confirm.getText().toString().trim();
                if(man.isChecked()){
                    gender="男";
                }
                else if(woman.isChecked()){
                    gender="女";
                }
                if(tel.length()==0){
                    Toast.makeText(RegisterActivity.this, "请输入电话号", Toast.LENGTH_SHORT).show();
                }
                else if(user_name.length()==0){
                    Toast.makeText(RegisterActivity.this, "请输入昵称", Toast.LENGTH_SHORT).show();
                }
                else if(password.length()==0){
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
                else if(confirm.length()==0){
                    Toast.makeText(RegisterActivity.this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!password.equals(confirm)){
                        Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String result = WebTool.touchHtml(Public_data.url+"get_user_used.php?tel="+tel);
                                    if(result.equals("1")){
                                        handler.sendEmptyMessage(1);
                                    }
                                    else{
                                        handler.sendEmptyMessage(2);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            }
        });
    }
}