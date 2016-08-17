package cn.edu.cdut.lm.mymuiscplayer.layout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.edu.cdut.lm.mymuiscplayer.R;

/**
 * Created by LimiaoMaster on 2016/8/17 9:22
 */

public class BottomControlBar extends RelativeLayout {

    private static TextView tv_title_of_music;
    private static TextView tv_artist_of_music;

    private UpdateBarReceiver updateBarReceiver;

    public static final String UPDATE_TITLE_ARTIST = "cn.edu.cdut.lm.mymusicplayer.UPDATE_TITLE_ARTIST";    //  设置曲名和艺术家


    public BottomControlBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_control_bar,this);

        tv_title_of_music = (TextView) view.findViewById(R.id.title_of_music);
        tv_artist_of_music = (TextView) view.findViewById(R.id.artist_of_music);

        //updateBarReceiver = new UpdateBarReceiver(tv_title_of_music,tv_artist_of_music);
        //updateBarReceiver = new UpdateBarReceiver(view);

        updateBarReceiver = new UpdateBarReceiver();

    }


    public static class UpdateBarReceiver extends BroadcastReceiver {

        public UpdateBarReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(UPDATE_TITLE_ARTIST)) {
                Log.e("onReceive()", "是更新曲目和艺术家的Action");
                String title = intent.getStringExtra("title");
                String artist = intent.getStringExtra("artist");
                Log.e("onReceive()", "准备更新，曲目是：" + title + "艺术家是：" + artist);
                tv_title_of_music.setText(title);
                tv_artist_of_music.setText(artist);
                Log.e("onReceive()", "更新完成，，，，，，，");
            }
        }

    }
}
