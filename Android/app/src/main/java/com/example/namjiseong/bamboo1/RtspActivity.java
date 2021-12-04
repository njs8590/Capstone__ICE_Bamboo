package com.example.namjiseong.bamboo1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

public class RtspActivity extends AppCompatActivity {

    private SimpleExoPlayer player;
    private PlayerView exoPlayerView;

    private Boolean playWhenReady = true;
    private int currentWindow = 0;
    private Long playbackPosition = 0L;

    SharedPreferences pref;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtsp);

        webView = (WebView)findViewById(R.id.webView);
        webView.setPadding(0,0,0,0);
        //webView.setInitialScale(100);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        //webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        pref = getSharedPreferences("pref",MODE_PRIVATE);
        String ip_st, port_st;
        ip_st = pref.getString("ip_st","");
        port_st = pref.getString("port_st","");

        String url ="http://"+ip_st+":"+port_st+"/?action=stream";

        webView.loadUrl(url);

    }

    @Override
    public void onBackPressed(){
        webView.onPause();
        webView.destroy();
        super.onBackPressed();
    }
}
