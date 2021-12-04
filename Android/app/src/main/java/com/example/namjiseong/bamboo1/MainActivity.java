package com.example.namjiseong.bamboo1;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    TextView receiveText;
    EditText messageText;
    Button connectBtn;
    SharedPreferences pref;
    Boolean ipcheck = true;

    Socket socket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("pref",MODE_PRIVATE);

        //앱 기본 스타일 설정
        getSupportActionBar().setElevation(0);

        connectBtn = (Button) findViewById(R.id.buttonConnect);
        receiveText = (TextView) findViewById(R.id.textViewReceive);
        messageText = (EditText) findViewById(R.id.messageText);

        String ip, port;
        ip = pref.getString("ip","");
        port = pref.getString("port","");

        //ip, port 설정 되어있는지 확인
        if(ip=="" || port ==""){
            ipcheck = false;
        }

        //connect 버튼 클릭
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ipcheck == true) {
                    MyClientTask myClientTask = new MyClientTask(ip, Integer.parseInt(port), messageText.getText().toString());
                    myClientTask.execute();
                }else{
                    receiveText.setText("Check ip, port..");
                }
            }
        });
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
            receiveText.setText(response);
            super.onPostExecute(result);
        }
    }

}