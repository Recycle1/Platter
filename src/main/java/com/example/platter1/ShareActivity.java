package com.example.platter1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ShareActivity extends AppCompatActivity {

    EditText editText;
    Button ksjs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        editText=findViewById(R.id.et_share);
        ksjs=findViewById(R.id.btn_ksjs);
        ksjs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(editText.getText());
                Intent intent=new Intent();
                intent.setClass(ShareActivity.this,LoadingActivity.class);
                intent.putExtra("ip",String.valueOf(editText.getText()));
                startActivity(intent);
            }
        });
    }
}