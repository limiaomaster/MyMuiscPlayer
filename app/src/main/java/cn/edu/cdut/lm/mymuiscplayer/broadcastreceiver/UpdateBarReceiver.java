/*
package cn.edu.cdut.lm.mymuiscplayer.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cn.edu.cdut.lm.mymuiscplayer.R;

*/
/**
 * Created by LimiaoMaster on 2016/8/17 9:58
 *//*


public class UpdateBarReceiver extends BroadcastReceiver {


    private TextView tv_title_of_music;
    private TextView tv_artist_of_music;

    private  View view;

    public UpdateBarReceiver() {
    }

    public UpdateBarReceiver(View view) {
        this.view = view;
    }

    */
/*public UpdateBarReceiver(TextView tv_title_of_music, TextView tv_artist_of_music) {
        this.tv_title_of_music = tv_title_of_music;
        this.tv_artist_of_music = tv_artist_of_music;
    }*//*


    public static final String UPDATE_TITLE_ARTIST = "cn.edu.cdut.lm.mymusicplayer.UPDATE_TITLE_ARTIST";    //  设置曲名和艺术家


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("onReceive()","收到Service发来的广播！");


        tv_title_of_music = (TextView) view.findViewById(R.id.title_of_music);
        tv_artist_of_music = (TextView) view.findViewById(R.id.artist_of_music);



        String action = intent.getAction();
        if (action.equals(UPDATE_TITLE_ARTIST)){
            Log.e("onReceive()","是更新曲目和艺术家的Action");
            String title = intent.getStringExtra("title");
            String artist = intent.getStringExtra("artist");
            Log.e("onReceive()","准备更新，曲目是："+title+"艺术家是："+artist);
            tv_title_of_music.setText(title);
            tv_artist_of_music.setText(artist);
            Log.e("onReceive()","更新完成，，，，，，，");
        }
    }
}
*/
