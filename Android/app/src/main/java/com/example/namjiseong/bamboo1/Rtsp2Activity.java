package com.example.namjiseong.bamboo1;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class Rtsp2Activity extends Activity {

    /****************************
     안씀이거
     ****************************/
    VideoView video;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtsp3);

        video = (VideoView)findViewById(R.id.video);
        MediaController mc = new MediaController(this);

        Uri uri = Uri.parse("http://192.168.43.39:8091/?action=stream");

        video.setMediaController(mc);
        video.setVideoURI(uri);
        video.start();

    }

    @Override
    public void onBackPressed(){
        video.pause();
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onBackPressed();
    }
}
