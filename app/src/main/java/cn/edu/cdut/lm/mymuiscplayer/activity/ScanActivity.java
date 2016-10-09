package cn.edu.cdut.lm.mymuiscplayer.activity;

import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by LimiaoMaster on 2016/10/6 18:08
 */
public class ScanActivity extends AppCompatActivity{

    private ImageView search_glass ;
    private TextView textViewNumber;
    private LinearLayout linearLayout;
    private TextView textViewScanning;
    private Button button_FullScan;
    private int i = 1;
    private Button button_custom;
    private ValueAnimator animator;
    private String TAG = "ScanActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreate方法得到执行！");
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

        button_FullScan = (Button) findViewById(R.id.bt_fulldisk_scan);
        button_custom = (Button) findViewById(R.id.bt_custom_scan);
        button_FullScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (i){
                    case 1:
                        button_FullScan.setClickable(false);

                        textViewScanning.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.INVISIBLE);
                        startPropertyAnim();
                        new Thread(){
                            @Override
                            public void run() {
                                Looper.prepare();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                                    MediaUtil.createMyDatabase(ScanActivity.this);
                                }else MediaUtil.createMyDatabaseLowSystem(ScanActivity.this);
                                List<Mp3Info>  mp3InfoList = MediaUtil.getMp3ListFromMyDatabase(ScanActivity.this,0);
                                int size = mp3InfoList.size();
                                Message message = new Message();
                                message.arg1 = size;
                                handler_getMusicNUM.sendMessage(message);
                                Log.e(TAG,"发送歌曲数目的消息给handler");
                                Looper.loop();
                            }
                        }.start();
                        break;
                    case 2:
                        onBackPressed();
                        break;
                }
            }
        });

        search_glass = (ImageView) findViewById(R.id.iv_search_glass);

        textViewScanning = (TextView) findViewById(R.id.tv_scanning);
        linearLayout = (LinearLayout) findViewById(R.id.ll_result);
        textViewNumber = (TextView) findViewById(R.id.tv_show_number);

        textViewScanning.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);
    }

    Handler handler_getMusicNUM = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int number = msg.arg1;
            textViewNumber.setText(number+"首");
            linearLayout.setVisibility(View.VISIBLE);
            textViewScanning.setVisibility(View.INVISIBLE);
            button_FullScan.setClickable(true);
            button_FullScan.setText("返回本地音乐");
            i = 2;
            button_custom.setText("一键获取封面歌词");
            //search_glass.clearAnimation();
            animator.end();
            //FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(220,220);
            //layoutParams.gravity = Gravity.CENTER;
            //search_glass.setLayoutParams(layoutParams);
            search_glass.setImageResource(R.drawable.local_scan_ok);
            search_glass.setTranslationX(0);
            search_glass.setTranslationY(0);
        }
    };


    // 动画实际执行
    private void startPropertyAnim() {
        animator = ValueAnimator.ofFloat(0, 60);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(18000);
        animator.setRepeatCount(-1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float angle = (Float) animation.getAnimatedValue();
                float x = (float) (60*cos(angle));
                float y = (float) (60*sin(angle));

                search_glass.setTranslationX(x);
                search_glass.setTranslationY(y);
            }
        });
        animator.start();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.e(TAG, "onSaveInstanceState方法得到执行！");
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("username", "initphp"); //这里保存一个用户名
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.e(TAG,"onRestoreInstanceState方法得到执行");
        super.onRestoreInstanceState(savedInstanceState);
    }
}
