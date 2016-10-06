package cn.edu.cdut.lm.mymuiscplayer.activity;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;

/**
 * Created by LimiaoMaster on 2016/10/6 18:08
 */
public class ScanActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_scan_activity);
        toolbar.setTitle("扫描音乐");
        toolbar.setTitleTextColor(getResources().getColor(R.color.toolbarTextColor));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button button = (Button) findViewById(R.id.bt_fulldisk_scan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        Looper.prepare();
                        MediaUtil.createMyDatabase(ScanActivity.this);
                        List<Mp3Info>  mp3InfoList = MediaUtil.getMp3ListFromMyDatabase(ScanActivity.this,0);
                        int size = mp3InfoList.size();
                        Looper.loop();
                    }
                }.start();
            }
        });
    }
}
