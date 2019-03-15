package com.example.administrator.accessibilityservicedemo;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.administrator.accessibilityservicedemo.Other.SetOtherHB;
import com.example.administrator.accessibilityservicedemo.layout.MenuAbout;
import com.example.administrator.accessibilityservicedemo.layout.MenuActivation;
import com.example.administrator.accessibilityservicedemo.layout.MenuHelp;
import com.example.administrator.accessibilityservicedemo.layout.SetHongBao;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button openService;
    private Button setAutoHongbao;
    private ToggleButton zongkg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();

        openService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
            }
        });

        setAutoHongbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, SetHongBao.class);
                startActivity(intent);
            }
        });

        zongkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetOtherHB.zongkg=zongkg.isChecked();
            }
        });


    }

    private void open() {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "请开启服务", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //菜单栏
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                Intent help_it = new Intent(this, MenuHelp.class);
                startActivity(help_it);
                break;
            case R.id.about:
                Intent about_it = new Intent(this, MenuAbout.class);
                startActivity(about_it);
                break;
            case R.id.share:
                String path = getResourcesUri(R.drawable.share);
                Intent imageIntent = new Intent(Intent.ACTION_SEND);
                imageIntent.setType("image/jpg");
                imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
                startActivity(Intent.createChooser(imageIntent, "分享"));
                break;
            case R.id.activation:
                Intent activation_it = new Intent(this, MenuActivation.class);
                startActivity(activation_it);
                break;
        }
        return true;
    }

    private void bindView() {
        openService = (Button) findViewById(R.id.openService);
        setAutoHongbao=(Button) findViewById(R.id.openSZ);
        zongkg=(ToggleButton) findViewById(R.id.zongkg);
    }

    //分享图片
    private String getResourcesUri(@DrawableRes int id) {
        Resources resources = getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
//        Toast.makeText(this, "Uri:" + uriPath, Toast.LENGTH_SHORT).show();
        return uriPath;
    }

}
