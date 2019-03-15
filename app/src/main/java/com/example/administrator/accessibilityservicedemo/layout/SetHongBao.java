package com.example.administrator.accessibilityservicedemo.layout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.NumberKeyListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.administrator.accessibilityservicedemo.Other.SetOtherHB;
import com.example.administrator.accessibilityservicedemo.R;


/**
 * Created by Administrator on 2018/2/15 0015.
 */

public class SetHongBao extends AppCompatActivity {
    private Button btsave;
    private Switch huLuv, spQHB, huFU;
    private EditText delayTime;
    private EditText huifutext;
    private EditText huluetext;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_hongbao);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        readData();

        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hlsw = huLuv.isChecked();
                boolean spsw = spQHB.isChecked();
                boolean hfsw = huFU.isChecked();
                String time = (delayTime.getText().toString());
                String hf = huifutext.getText().toString();
                String hl = huluetext.getText().toString();

                boolean hlEnable =huluetext.isEnabled();
                boolean hfEnable =huifutext.isEnabled();
                if (time.equals("")||time.equals(".")){
                    time="0";
                }
                SetOtherHB.hufutext = hf;
                SetOtherHB.huluecitext = hl;
                SetOtherHB.hulueci = hlsw;
                SetOtherHB.hufu = hfsw;
                SetOtherHB.delayTime = time;
                SetOtherHB.isAutoQHB = spsw;
                SetOtherHB.hlEnable=hlEnable;
                SetOtherHB.hfEnable=hfEnable;
                saveData(time, hlsw, hfsw, spsw, hf, hl,hlEnable,hfEnable);

                readData();
                Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        huLuv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (huLuv.isChecked()){
                    huluetext.setEnabled(true);
                }else {
                    huluetext.setEnabled(false);
                }
            }
        });

        huFU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (huFU.isChecked()){
                    huifutext.setEnabled(true);
                }else {
                    huifutext.setEnabled(false);
                }
            }
        });
    }


    private void readData() {
        SharedPreferences pref = getSharedPreferences("setOtherHB", MODE_PRIVATE);
        boolean ishlc = pref.getBoolean("ishlc", false);
        String time = pref.getString("time", "0");
        boolean ishf = pref.getBoolean("ishf", false);
        boolean ispq = pref.getBoolean("isspq", true);
        String hfc = pref.getString("hfc", "");
        String hlc = pref.getString("hlc", "");
        boolean hlEnable=pref.getBoolean("hlEnable",false);
        boolean hfEnable=pref.getBoolean("hfEnable",false);

        huLuv.setChecked(ishlc);
        spQHB.setChecked(ispq);
        huFU.setChecked(ishf);
        delayTime.setText(time);
        huluetext.setText(hlc);
        huifutext.setText(hfc);
        huluetext.setEnabled(hlEnable);
        huifutext.setEnabled(hfEnable);
    }

    private void init() {
        btsave = (Button) findViewById(R.id.saveall);
        huFU = (Switch) findViewById(R.id.hufu);
        huLuv = (Switch) findViewById(R.id.hulue);
        delayTime = (EditText) findViewById(R.id.delaytime);
        spQHB = (Switch) findViewById(R.id.spqhb);
        huifutext = (EditText) findViewById(R.id.huifutext);
        huluetext = (EditText) findViewById(R.id.testhb);
    }

    public void saveData(String time, boolean ishlc, boolean ishf, boolean isspq, String hfc, String hlc,boolean hlEnable,boolean hfEnable) {
        SharedPreferences.Editor editor = getSharedPreferences("setOtherHB", MODE_PRIVATE).edit();
        editor.putString("time", time);
        editor.putBoolean("ishlc", ishlc);
        editor.putBoolean("ishf", ishf);
        editor.putBoolean("isspq", isspq);
        editor.putString("hfc", hfc);
        editor.putString("hlc", hlc);
        editor.putBoolean("hlEnable",hlEnable);
        editor.putBoolean("hfEnable",hfEnable);
        editor.apply();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
