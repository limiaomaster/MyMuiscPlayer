package cn.edu.cdut.lm.mymuiscplayer.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import cn.edu.cdut.lm.mymuiscplayer.R;

/**
 * Created by LimiaoMaster on 2016/9/20 8:45
 */

public class PlayingActivity extends AppCompatActivity {
    private static final String TAG = "PlayingActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreate方法得到执行。");
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_player_2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart方法得到执行。");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume方法得到执行。");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause方法得到执行。");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop方法得到执行。");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart方法得到执行。");
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG,"onDestroy方法得到执行。");
        //关闭notification
        //PlayerService.manager.cancel(NOTIFICATION_ID);
        super.onDestroy();
    }
}
