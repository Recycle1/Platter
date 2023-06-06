package com.example.platter1;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.platter1.Public.Public_data;

public class Fragment_home extends Fragment {

    private RelativeLayout rl_login;
    private RelativeLayout rl_2;
    private RelativeLayout rl_3;
    private RelativeLayout rl_5;
    private TextView tv_name;

    public static Fragment_home newInstance() {
        Fragment_home fragment = new Fragment_home();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rl_login=view.findViewById(R.id.rl_login);
        rl_2=view.findViewById(R.id.rl_2);
        rl_3=view.findViewById(R.id.rl_3);
        rl_5=view.findViewById(R.id.rl_5);
        tv_name=view.findViewById(R.id.tv_name);
        if(Public_data.tel.equals("")){
            tv_name.setText("点击登录");
            rl_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),LoginActivity.class);
                    startActivityForResult(intent,1);
                }
            });
        }
        else{
            tv_name.setText(Public_data.name);
        }
        rl_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Public_data.tel.equals("")){
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent=new Intent(getActivity(),PartyActivity.class);
                    startActivity(intent);
                }
            }
        });
        rl_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Public_data.tel.equals("")){
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent=new Intent(getActivity(),RecordActivity.class);
                    startActivity(intent);
                }
            }
        });
        rl_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Public_data.tel="";
                tv_name.setText("点击登录");
                rl_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),LoginActivity.class);
                        startActivityForResult(intent,1);
                    }
                });
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            tv_name.setText(Public_data.name);
            rl_login.setOnClickListener(null);
        }
    }
}
