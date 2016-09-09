package cn.edu.cdut.lm.mymuiscplayer.service;


import android.app.NotificationManager;
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

import static cn.edu.cdut.lm.mymuiscplayer.adapter.SingleSongRVAdapter.notificationUtil;


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

    public static final String UPDATE_PROGRESS_BAR = "cn.edu.cdut.lm.mymusicplayer.UPDATE_PROGRESS_BAR";    //  设置播放和暂停按钮的图片
    public static final String UPDATE_UI_ON_COMPLETION = "cn.edu.cdut.lm.mymusicplayer.UPDATE_UI_ON_COMPLETION";    //  设置播放和暂停按钮的图片
    public static final String STOP_PLAY_BY_NOTE = "cn.edu.cdut.lm.mymusicplayer.STOP_PLAY_BY_NOTE";
    public static final String UPDATE_UI_ON_BUTTON_CLICK = "cn.edu.cdut.lm.mymusicplayer.UPDATE_UI_ON_BUTTON_CLICK";


    private  int currentPosition;

    private static final String CLOSE_NOTIFICATION = "close_notification";
    private NotificationManager manager;
    private static final int NOTIFICATION_ID = 5709;
    private boolean isPlaying = false;
    private boolean isStop = true;
    //private NotificationUtil notificationUtil;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = new MediaPlayer();
        mp3InfoList = MediaUtil.getMp3List(this);
        //notificationUtil = new NotificationUtil(getApplicationContext());

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //停止更新进度条
                handler.removeMessages(1);
                //获取下一首歌的路径，并播放，并继续发送更新进度条。
                path = mp3InfoList.get(recycleListPosition).getUrl();
                playAnotherMusic(0);
                handler.sendEmptyMessage(1);
                //发送广播，更新BottomControlBar的UI。
                Intent sendIntent = new Intent();
                sendIntent.setAction(UPDATE_UI_ON_COMPLETION);
                sendIntent.putExtra("position",recycleListPosition);
                sendBroadcast(sendIntent);
                //更新Notification的UI
                notificationUtil.updateNoteMusicInfo(recycleListPosition);
                //位置重新设置。
                listPosition = recycleListPosition;
                listLastPosition = recycleListPosition;
                //产生新的下一首歌的position。
                recycleListPosition = (recycleListPosition+1)%mp3InfoList.size();
            }
        });

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int position = intent.getIntExtra("position",0);
        if(position == -1){ //点击了关闭按钮，关闭notification、停止播放歌曲、进度条复位。
            stopPlayingMusic();
        }else {
            listPosition = position;
            path = mp3InfoList.get(listPosition).getUrl();
            if( listPosition == listLastPosition ){ //  点击同一条曲目，表示要暂停，或者继续播放该曲目。
                if(isStop){
                    playAnotherMusic(0);
                }else if( mediaPlayer.isPlaying()){          //   如果此时为正在播放，表示要暂停。
                    pausePlayingMusic();
                } else {                                            //  如果此时为暂停，表示要继续播放。
                    Log.e("onStartCommand","将要继续播放！"+listPosition);
                    continuePlayingMusic();                //  继续播放,用系统的方法start()
                }
            } else {                                               //  如果不是一个，那肯定是要播放新的文件了。
                playAnotherMusic(0);
            }
            listLastPosition = listPosition;
            recycleListPosition = (listPosition+1)%mp3InfoList.size();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void stopPlayingMusic() {
        if(mediaPlayer != null){
            //停止播放
            mediaPlayer.stop();
            //关闭notification
            manager.cancel(NOTIFICATION_ID);
            //进度条复位
            Intent intent_resetBar = new Intent();
            intent_resetBar.setAction(UPDATE_PROGRESS_BAR);
            intent_resetBar.putExtra("duration",duration);
            intent_resetBar.putExtra("currentPosition",0);
            sendBroadcast(intent_resetBar);
            handler.removeMessages(1);
            //控制条的播放按键复位
            Intent intent_resetPlayButton = new Intent();
            intent_resetPlayButton.setAction(STOP_PLAY_BY_NOTE);
            sendBroadcast(intent_resetPlayButton);
            //设为停止播放状态
            notificationUtil.isPlaying = false;
            isStop = true;
            isPlaying = false;
        }

    }


    private void pausePlayingMusic() {
        if (mediaPlayer != null ) {
            mediaPlayer.pause();
            handler.removeMessages(1);

            Intent intent = new Intent();
            intent.setAction(UPDATE_UI_ON_BUTTON_CLICK);
            intent.putExtra("position",listPosition);
            sendBroadcast(intent);

            notificationUtil.updateNoteMusicInfo(listPosition);
            isStop = false;
            isPlaying = false;
        }
    }
    private void continuePlayingMusic() {
        if (mediaPlayer != null ) {
            mediaPlayer.start();
            handler.sendEmptyMessage(1);

            Intent intent = new Intent();
            intent.setAction(UPDATE_UI_ON_BUTTON_CLICK);
            intent.putExtra("position",listPosition);
            sendBroadcast(intent);

            notificationUtil.updateNoteMusicInfo(listPosition);

            isStop = false;
            isPlaying = true;
        }
    }

    private void playAnotherMusic (int currentTime) {
        Intent intent = new Intent();
        intent.setAction(UPDATE_UI_ON_BUTTON_CLICK);
        intent.putExtra("position",listPosition);
        sendBroadcast(intent);
        handler.removeMessages(1);
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
        isStop = false;
        isPlaying = true;
    }

    private final class PreparedListener implements MediaPlayer.OnPreparedListener {

        private int currentTime;

        PreparedListener(int currentTime) {
            this.currentTime = currentTime;
        }
        @Override
        public void onPrepared(MediaPlayer mp) {
            if(currentTime > 0) {    //如果音乐不是从头播放
                mediaPlayer.seekTo(currentTime);
            } else {
                mediaPlayer.start();    //开始播放
                handler.sendEmptyMessage(1);
            }
        }
    }


    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                if (mediaPlayer != null){
                    currentPosition = mediaPlayer.getCurrentPosition();
                    duration = mp3InfoList.get(listPosition).getDuration();
                    Intent intent = new Intent();
                    intent.setAction(UPDATE_PROGRESS_BAR);
                    intent.putExtra("duration",duration);
                    intent.putExtra("currentPosition",currentPosition);
                    sendBroadcast(intent);
                    handler.sendEmptyMessageDelayed(1,1000);
                }
            }
        }
    };







}
