package cn.edu.cdut.lm.mymuiscplayer.layout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.service.PlayerService;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;

/**
 * Created by LimiaoMaster on 2016/8/17 9:22
 */

public class BottomControlBar extends RelativeLayout implements View.OnClickListener{


    private List<Mp3Info> mp3InfoList;
    private static int listSize;

    private static TextView tv_title_of_music;
    private static TextView tv_artist_of_music;

    private static ImageView iv_play_pause;
    private static ImageView iv_next_song;

    private static String path;
    private static String title;
    private static String artist;
    private static String playOrPause;
    private static int listPosition;
    private static int  nextPosition;


    private static boolean isPlaying;



    private UpdateBarReceiver updateBarReceiver;

    public static final String UPDATE_TITLE_ARTIST = "cn.edu.cdut.lm.mymusicplayer.UPDATE_TITLE_ARTIST";    //  设置曲名和艺术家
    public static final String UPDATE_PLAY_PAUSE = "cn.edu.cdut.lm.mymusicplayer.UPDATE_PLAY_PAUSE";    //  设置播放和暂停按钮的图片
    public static final String UPDATE_BOTTOM_BAR = "cn.edu.cdut.lm.mymusicplayer.UPDATE_BOTTOM_BAR";    //  设置播放和暂停按钮的图片



    public BottomControlBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_control_bar,this);

        tv_title_of_music = (TextView) view.findViewById(R.id.title_of_music);
        tv_artist_of_music = (TextView) view.findViewById(R.id.artist_of_music);
        iv_play_pause = (ImageView) view.findViewById(R.id.play_btn);
        iv_next_song = (ImageView) view.findViewById(R.id.next_song);
        //updateBarReceiver = new UpdateBarReceiver(tv_title_of_music,tv_artist_of_music);
        //updateBarReceiver = new UpdateBarReceiver(view);

        updateBarReceiver = new UpdateBarReceiver();
        iv_play_pause.setOnClickListener(this);
        iv_next_song.setOnClickListener(this);

        mp3InfoList = MediaUtil.getMp3List(getContext());  //调用工具包中的getMp3Infos()方法，获取Mp3Info对象的列表。
        listSize = mp3InfoList.size();
        Log.e("BottomControlBar()","您的手机上一共有"+listSize+"首歌曲！！！");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_btn:
                    Intent intent = new Intent();

                    intent.putExtra("url", path);
                    intent.putExtra("position", listPosition);
                    intent.putExtra("title", title);
                    intent.putExtra("artist", artist);

                    intent.setClass(getContext(), PlayerService.class);
                    getContext().startService(intent);  //  注意是调用getContext()，不是SingleSongFragment中的getActivity()
                    break;

            case R.id.next_song:
                Mp3Info mp3Info = mp3InfoList.get(nextPosition);
                Intent intent1 = new Intent();

                intent1.putExtra("url",mp3Info.getUrl());
                intent1.putExtra("position", nextPosition);
                intent1.putExtra("title",mp3Info.getTitle());
                intent1.putExtra("artist",mp3Info.getArtist());

                intent1.setClass(getContext(), PlayerService.class);
                getContext().startService(intent1);
                Log.e("onItemClick","启动了播放服务！");

        }
    }

    //  内部类，广播接收器，更新底部控制条的UI。
    //  要为static类型的，要在Manifest文件中注册，并设置过滤器。
    public  static class UpdateBarReceiver extends BroadcastReceiver {

        public UpdateBarReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(UPDATE_BOTTOM_BAR)) {
                Log.e("onReceive()", "收到广播，这是更新控制条文字和图片的Action");

                path = intent.getStringExtra("url");
                listPosition = intent.getIntExtra("listPosition",0);
                title = intent.getStringExtra("title");
                artist = intent.getStringExtra("artist");
                playOrPause = intent.getStringExtra("playOrPause");

                nextPosition = (listPosition+1)%listSize;

                Log.e("onReceive()", "准备更新控制条，，，曲目是：" + title +
                        "   艺术家是：" + artist +"   要把按钮设置为："+ playOrPause +"的状态");
                tv_title_of_music.setText(title);
                tv_artist_of_music.setText(artist);

                if (playOrPause.equals("pause")){
                    iv_play_pause.setImageResource(R.drawable.playbar_btn_pause);
                    isPlaying = true;
                } else if (playOrPause.equals("play")){
                    iv_play_pause.setImageResource(R.drawable.playbar_btn_play);
                    isPlaying = false;
                }
                Log.e("onReceive()", "更新已经全部完成！！！");
            }





        }

    }
}
