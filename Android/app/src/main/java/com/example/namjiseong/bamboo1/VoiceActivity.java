package com.example.namjiseong.bamboo1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class VoiceActivity extends AppCompatActivity {

    private Button startButton,stopButton;

    public byte[] buffer;
    public static DatagramSocket socket;
    private int port=5005;
    String ip="192.168.43.39";

    AudioRecord recorder;

    private int sampleRate = 16000 ; // 44100 for music
    //private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
    private boolean status = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        startButton = (Button) findViewById (R.id.start_button);
        stopButton = (Button) findViewById (R.id.stop_button);

        startButton.setOnClickListener (startListener);
        stopButton.setOnClickListener (stopListener);
    }

    private final OnClickListener stopListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            status = false;
            try{
                recorder.release();
                Log.d("VS","Recorder released");
            }catch (Exception e){
                Log.d("VS","No Recorder");
            }
            Toast.makeText(getApplicationContext(),"Stop Recording..",Toast.LENGTH_LONG).show();
        }
    };

    private final OnClickListener startListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            Toast.makeText(getApplicationContext(),"StartRecording!!",Toast.LENGTH_LONG).show();
            try{
                status = true;
                startStreaming();
            }
            catch(Exception e){
                Toast.makeText(getApplicationContext(),"Record error..",Toast.LENGTH_LONG).show();
            }
        }
    };
    public void startStreaming() {
        Thread streamThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    DatagramSocket socket = new DatagramSocket();
                    Log.d("VS", "Socket Created");

                    byte[] buffer = new byte[minBufSize];

                    Log.d("VS","Buffer created of size " + minBufSize);
                    DatagramPacket packet;

                    final InetAddress destination = InetAddress.getByName("192.168.43.39");
                    Log.d("VS", "Address retrieved");

                    recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRate,channelConfig,audioFormat,minBufSize*10);
                    Log.d("VS", "Recorder initialized");

                    recorder.startRecording();
                    //Toast.makeText(getApplicationContext(),"StartRecording!!",Toast.LENGTH_LONG).show();
                    Log.d("VS", "Start Recording");

                    while(status == true) {
                        //reading data from MIC into buffer
                        minBufSize = recorder.read(buffer, 0, buffer.length);

                        //putting buffer in the packet
                        packet = new DatagramPacket (buffer,buffer.length,destination,port);
                        socket.send(packet);

                        System.out.println("MinBufferSize: " +minBufSize);
                        Log.d("VS", "Send Packet");
                    }
                } catch(UnknownHostException e) {
                    Log.e("VS", "UnknownHostException");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("VS", "IOException");
                }
            }
        });
        streamThread.start();
    }
}
