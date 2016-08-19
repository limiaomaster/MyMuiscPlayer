package cn.edu.cdut.lm.mymuiscplayer.service;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;


/**
 * Created by LimiaoMaster on 2016/8/15 22:47
 */

public class PlayerService extends Service {

    private MediaPlayer mediaPlayer;
    private List<Mp3Info> mp3InfoList;
    private String path;
    private String title;
    private String artist;

    private long musicId;
    private long albumId;

    private int listLastPosition = -1;  //  这首曲目之前的曲目位置
    private int listPosition ;  // 从列表传来的新的曲目位置
    private int recycleListPosition;

    private long duration;	//播放长度

    private boolean isPause;                    //暂停状态

    public static final String UPDATE_PROGRESS_BAR = "cn.edu.cdut.lm.mymusicplayer.UPDATE_PROGRESS_BAR";    //  设置播放和暂停按钮的图片
    public static final String UPDATE_UI_ON_COMPLETION = "cn.edu.cdut.lm.mymusicplayer.UPDATE_UI_ON_COMPLETION";    //  设置播放和暂停按钮的图片

    private int currentPosition;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e("onCreate()","-----------执行onCreate()方法----------");
        super.onCreate();

        mediaPlayer = new MediaPlayer();
        mp3InfoList = MediaUtil.getMp3List(this);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                path = mp3InfoList.get(recycleListPosition).getUrl();
                playAnotherMusic(0);

                Intent sendIntent = new Intent();
                sendIntent.setAction(UPDATE_UI_ON_COMPLETION);
                sendIntent.putExtra("position",recycleListPosition);
                sendBroadcast(sendIntent);
                listPosition = recycleListPosition;
                listLastPosition = recycleListPosition;
                recycleListPosition = (recycleListPosition+1)%mp3InfoList.size();
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("onStartCommand()","-----------执行onStartCommand()方法----------");


        listPosition = intent.getIntExtra("position",0);
        path = mp3InfoList.get(listPosition).getUrl();

        if( listPosition == listLastPosition ){ //  点击同一条曲目，表示要暂停，或者继续播放该曲目。
            if( mediaPlayer.isPlaying()){          //   如果此时为正在播放，表示要暂停。
                this.pause();
            } else {                                            //  如果此时为暂停，表示要继续播放。
                mediaPlayer.start();                  //   继续播放,用系统的方法start()
                Log.e("onStartCommand()","已经把音乐继续！！！");
            }
        } else {                                               //  如果不是一个，那肯定是要播放新的文件了。
            this.playAnotherMusic(0);
        }

        listLastPosition = listPosition;
        recycleListPosition = (listPosition+1)%mp3InfoList.size();
        return super.onStartCommand(intent, flags, startId);
    }


    private void playAnotherMusic (int currentTime) {
        try {
            mediaPlayer.reset();//把各项参数恢复到初始状态
            //重新初始化
            mediaPlayer.setDataSource(path);    //  设置播放地址
            mediaPlayer.prepare();  //进行缓冲
            //注册一个监听器
            mediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
        }
    }



    private void stop(){
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private final class PreparedListener implements MediaPlayer.OnPreparedListener {
        private int currentTime;

        public PreparedListener(int currentTime) {
            this.currentTime = currentTime;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.e("onPrepared","播放器已经就绪，可以开始播放！");

            if(currentTime > 0) {    //如果音乐不是从头播放
                mediaPlayer.seekTo(currentTime);
            } else {
                mediaPlayer.start();    //开始播放
                Log.e("onPrepared","已经开始播放！！！！！！！！");
                handler.sendEmptyMessage(1);
                Log.e("onPrepared","sendEmptyMessage(1)");
            }





        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.i("handleMessage()","进入handleMessage()方法。。。。。");
            if (msg.what == 1){
                if (mediaPlayer != null){
                    currentPosition = mediaPlayer.getCurrentPosition();
                    duration = mp3InfoList.get(listPosition).getDuration();

                    Log.i("handleMessage()","当前歌曲长度为："+duration);
                    Log.i("handleMessage()","当前播放进度为："+currentPosition);

                    Intent intent = new Intent();
                    intent.setAction(UPDATE_PROGRESS_BAR);
                    intent.putExtra("duration",duration);
                    intent.putExtra("currentPosition",currentPosition);
                    sendBroadcast(intent);
                    Log.i("handleMessage()","这是发送更新progressbar的intent");
                    handler.sendEmptyMessageDelayed(1,1000);
                }
            }
        }
    };







}
