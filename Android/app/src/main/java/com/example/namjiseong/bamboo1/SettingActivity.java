package com.example.namjiseong.bamboo1;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends Activity {

    Button bt_save;
    Button bt_clear;
    EditText ed_ip;
    EditText ed_port;
    EditText ed_ip_st;
    EditText ed_port_st;
    SharedPreferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        bt_save = (Button)findViewById(R.id.buttonSave);
        bt_clear = (Button)findViewById(R.id.buttonClear);
        ed_ip = (EditText)findViewById(R.id.edit_ip);
        ed_port = (EditText)findViewById(R.id.edit_port);
        ed_ip_st = (EditText)findViewById(R.id.edit_ip_st);
        ed_port_st = (EditText)findViewById(R.id.edit_port_st);

        pref = getSharedPreferences("pref",MODE_PRIVATE);

        String strtemp = pref.getString("ip","IP");
        ed_ip.setText(strtemp);
        strtemp = pref.getString("port","PORT");
        ed_port.setText(strtemp);
        strtemp = pref.getString("ip_st","IP_streaming");
        ed_ip_st.setText(strtemp);
        strtemp = pref.getString("port_st","PORT_streaming");
        ed_port_st.setText(strtemp);

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences("ip",ed_ip.getText().toString());
                savePreferences("port",ed_port.getText().toString());
                savePreferences("ip_st",ed_ip_st.getText().toString());
                savePreferences("port_st",ed_port_st.getText().toString());
                Toast.makeText(getApplicationContext(),"Save..",Toast.LENGTH_LONG).show();
            }
        });

        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_ip.setText("");
                ed_port.setText("");
                ed_ip_st.setText("");
                ed_port_st.setText("");
                removeAllPreferences();
                Toast.makeText(getApplicationContext(),"Clear..",Toast.LENGTH_LONG).show();
            }
        });

    }

    // 값 저장하기
    private void savePreferences(String key, String value){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    // 값(ALL Data) 삭제하기
    private void removeAllPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

}
