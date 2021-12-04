package com.example.namjiseong.bamboo1;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ControllerActivity extends AppCompatActivity {
    CircleMenu circleMenu;
    SharedPreferences pref;
    WebView webView;
    Boolean ipcheck = true;
    Socket socket = null;
    TextView receiveText;
    String ip, port;
    String ip_st, port_st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        MyFirebaseInstanceIDService myfire = new MyFirebaseInstanceIDService();
        myfire.onTokenRefresh();


        /******
         * video streaming
         *******/
        webView = (WebView)findViewById(R.id.webView);
        webView.setPadding(0,0,0,0);
        //webView.setInitialScale(100);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        //webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        pref = getSharedPreferences("pref",MODE_PRIVATE);

        ip_st = pref.getString("ip_st","");
        port_st = pref.getString("port_st","");
        String url ="http://"+ip_st+":"+port_st+"/?action=stream";
        webView.loadUrl(url);

        /*******
         * Socket
         *******/
        receiveText = (TextView)findViewById(R.id.textViewReceive);

        ip = pref.getString("ip","");
        port = pref.getString("port","");

        //ip, port 설정 되어있는지 확인
        if(ip=="" || port ==""){
            ipcheck = false;
        }

        /******
         * Controller
         ******/
        circleMenu = (CircleMenu) findViewById(R.id.circle_menu);
        circleMenu.openMenu();

        /**
         * 아이콘 별로 인덱스는 0 부터
         */
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.mipmap.icon_menu, R.mipmap.icon_cancel)
                .addSubMenu(Color.parseColor("#258CFF"), R.mipmap.ic_baby_bed)
                .addSubMenu(Color.parseColor("#30A400"), R.mipmap.ic_crib_toy)
                .addSubMenu(Color.parseColor("#FF4B32"), R.mipmap.icon_notify)
                .addSubMenu(Color.parseColor("#8A39FF"), R.mipmap.ic_border_all)
                .addSubMenu(Color.parseColor("#FF6A00"), R.mipmap.ic_mic)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {

                    @Override
                    public void onMenuSelected(int index) {
                        //Toast.makeText(getApplicationContext(),"index : "+index,Toast.LENGTH_LONG).show();
                        if(ipcheck==true){
                            switch (index){
                                case 0:
                                    SendMessage("task1");
                                    /**
                                     * 요람흔들기 + 노래
                                     **/
                                    break;
                                case 1:
                                    SendMessage("task2");
                                    /**
                                     * 모빌 돌리기 + 사운드
                                     **/
                                    break;
                                case 2:
                                    SendMessage("task3");
                                    /**
                                     * 녹음된 소리 틀기
                                     **/
                                    break;
                                case 3:
                                    SendMessage("task4");
                                    /**
                                     *
                                     **/
                                    break;
                                case 4:
                                    SendMessage("record");
                                    break;
                            }
                        }else{
                            receiveText.setText("Check ip, port..");
                        }
                    }

                }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

            @Override
            public void onMenuOpened(){}

            @Override
            public void onMenuClosed() {
                circleMenu.openMenu();
            }
        });
    }

    public void SendMessage(String msg){
        MyClientTask myClientTask = new MyClientTask(ip, Integer.parseInt(port), msg);
        myClientTask.execute();
    }
    //
    public class MyClientTask extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        String response = "";
        String myMessage = "";

        //constructor
        MyClientTask(String addr, int port, String message){
            dstAddress = addr;
            dstPort = port;
            myMessage = message;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            myMessage = myMessage.toString();
            try {
                socket = new Socket(dstAddress, dstPort);
                //송신
                OutputStream out = socket.getOutputStream();
                out.write(myMessage.getBytes());

                //수신
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                int bytesRead;
                InputStream inputStream = socket.getInputStream();
                while ((bytesRead = inputStream.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                }
                response = "Server Response :: " + response;

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //receiveText.setText(response);
            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
        }
    }
}
