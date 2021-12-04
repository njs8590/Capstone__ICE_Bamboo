package com.example.namjiseong.bamboo1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button bt_setting;
    Button bt_streaming;
    Button bt_rtspstreaming;
    Button bt_message;
    Button bt_controller;
    Button bt_voice;


    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Button
        bt_message = (Button)findViewById(R.id.messagebtn);
        bt_streaming= (Button)findViewById(R.id.streamingbtn);
        bt_rtspstreaming = (Button)findViewById(R.id.rtspbtn);
        bt_setting = (Button)findViewById(R.id.settingbtn);
        bt_controller = (Button)findViewById(R.id.controllerbtn);
        bt_voice = (Button)findViewById(R.id.voicebtn);


        bt_streaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeActivity.this, StreamingActivity.class);
                startActivity(intent);
            }
        });

        bt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        bt_rtspstreaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeActivity.this, RtspActivity.class);
                startActivity(intent);
            }
        });

        bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        bt_controller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeActivity.this, ControllerActivity.class);
                startActivity(intent);
            }
        });

        bt_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeActivity.this, VoiceActivity.class);
                startActivity(intent);
            }
        });
    }
}