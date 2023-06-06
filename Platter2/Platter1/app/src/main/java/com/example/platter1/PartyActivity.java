package com.example.platter1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.platter1.Game.GameActivity;

public class PartyActivity extends AppCompatActivity {

    private Button btn_two;
    private Button btn_more;
    private Button btn_back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);
        btn_back=findViewById(R.id.btn_back);
        btn_two=findViewById(R.id.btn_two);
        btn_more=findViewById(R.id.btn_more);
        btn_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PartyActivity.this, GameActivity.class);
                intent.putExtra("game_number",2);
                startActivity(intent);
            }
        });
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PartyActivity.this, GameActivity.class);
                intent.putExtra("game_number",3);
                startActivity(intent);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PartyActivity.this.finish();
            }
        });
    }
}