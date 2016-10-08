package cn.edu.cdut.lm.mymuiscplayer.service;


import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;
import cn.edu.cdut.lm.mymuiscplayer.utilities.NotificationUtil;


/**
 * Created by LimiaoMaster on 2016/8/15 22:47
 */

public class PlayerService extends Service {

    private MediaPlayer mediaPlayer;
    private List<Mp3Info> mp3InfoList;
    private String path;
    private String title;
    private String artist;

    private int albumId;

    private int listLastPosition = -1;  //  这首曲目之前的曲目位置
    private int listPosition = -1;  // 从列表传来的新的曲目位置
    private int recycleListPosition;

    private int duration;	//播放长度

    public static final String UPDATE_PROGRESS_BAR = "cn.edu.cdut.lm.mymusicplayer.UPDATE_PROGRESS_BAR";    //  设置播放和暂停按钮的图片
    public static final String STOP_PLAY_BY_NOTE = "cn.edu.cdut.lm.mymusicplayer.STOP_PLAY_BY_NOTE";
    public static final String UPDATE_CONTROL_BAR = "cn.edu.cdut.lm.mymusicplayer.UPDATE_CONTROL_BAR";
    public static final String UPDATE_SPEAKER_LIST_POSITION = "cn.edu.cdut.lm.mymusicplayer.UPDATE_SPEAKER_LIST_POSITION";
    public static final String UPDATE_SORT_ORDER = "cn.edu.cdut.lm.mymusicplayer.UPDATE_SORT_ORDER";


    public static NotificationManager manager;
    private static final int NOTIFICATION_ID = 5709;
    private boolean isPlaying = false;
    private boolean isStop = true;
    //private NotificationUtil notificationUtil;
    private static final int CLOSE_INTENT = -1;
    private static final int PAUSE_PLAY_INTENT = -2;
    private static final int NEXT_INTENT = -3;
    private static final int PRE_INTENT = -4;
    private static final int CHANGE_SORT_ORDER_INTENT = -5;

    private NotificationUtil notificationUtil;
    private int sortOrder;

    private NotificationUtil.UpdateSortOrderReceiver updateSortOrderReceiver;




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        getUpdatedMp3List();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationUtil = new NotificationUtil(getApplicationContext());

        updateSortOrderReceiver = notificationUtil.new UpdateSortOrderReceiver();
        IntentFilter intentFilter_order = new IntentFilter();
        intentFilter_order.addAction(UPDATE_SORT_ORDER);
        getApplicationContext().registerReceiver(updateSortOrderReceiver,intentFilter_order);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //获取下一首歌的路径，并播放，并继续发送更新进度条。
                duration = mp3InfoList.get(recycleListPosition).getDuration();
                path = mp3InfoList.get(recycleListPosition).getUrl();
                title = mp3InfoList.get(recycleListPosition).getTitle();
                artist = mp3InfoList.get(recycleListPosition).getArtist();
                albumId = mp3InfoList.get(recycleListPosition).getAlbumId();

                playAnotherMusic(recycleListPosition);

                //位置重新设置。
                listPosition = recycleListPosition;
                listLastPosition = (mp3InfoList.size()+(recycleListPosition-1))%mp3InfoList.size();
                //产生新的下一首歌的position。
                recycleListPosition = (recycleListPosition+1)%mp3InfoList.size();
                saveDataOnCompletion();
            }
        });
    }

    private void getUpdatedMp3List() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        sortOrder = pref.getInt("sort_order_check_position",0);
        mp3InfoList = MediaUtil.getMp3ListFromMyDatabase(this,sortOrder);
    }

    public void saveDataOnCompletion(){
        Log.e("Service()","执行保存数据，，，saveDataOnCompletion");
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("title", title);
        editor.putString("artist", artist);
        editor.putInt("album_id",albumId);

        editor.putInt("duration",duration);
        //editor.putInt("currentPisition",currentPisition);
        editor.putBoolean("isplaying", isPlaying);
        editor.putInt("listPosition",listPosition);
        editor.commit();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null){

            int position = intent.getIntExtra("position",0);
            Log.e("Service()","获取发送的intent中的position"+position);


            if(position == CLOSE_INTENT){                       //点击了Note中的关闭按钮，关闭notification、停止播放歌曲、进度条复位。
                Log.e("Service()","这是关闭的intent"+position);
                stopPlayingMusic();
            }else if (position == PAUSE_PLAY_INTENT){ //点击了Note中的播放和暂停按钮。
                if(isPlaying){
                    pausePlayingMusic();
                    isPlaying = false;
                }else continuePlayingMusic();
            }else if (position == NEXT_INTENT){              //点击了Note中的下一首按钮。
                Log.e("Service()","这是下一首的intent"+position);
                duration = mp3InfoList.get(recycleListPosition).getDuration();
                path = mp3InfoList.get(recycleListPosition).getUrl();
                playAnotherMusic(recycleListPosition);

                listPosition = recycleListPosition;
                listLastPosition = (mp3InfoList.size()+(recycleListPosition-1))%mp3InfoList.size();
                Log.e("Service()","上一首为："+listLastPosition);
                recycleListPosition = (recycleListPosition+1)%mp3InfoList.size();
                Log.e("Service()","下一首为："+recycleListPosition);

            }else if (position == PRE_INTENT) {               //点击了Note中的上一首按钮。
                Log.e("Service()","这是上一首的intent"+position);
                duration = mp3InfoList.get(listLastPosition).getDuration();
                path = mp3InfoList.get(listLastPosition).getUrl();
                playAnotherMusic(listLastPosition);

                listPosition = listLastPosition;
                recycleListPosition = (listLastPosition+1)%mp3InfoList.size();
                Log.e("Service()","下一首为："+recycleListPosition);
                listLastPosition = (mp3InfoList.size()+(listLastPosition-1))%mp3InfoList.size();
                Log.e("Service()","上一首为："+listLastPosition);
            }else if (position == CHANGE_SORT_ORDER_INTENT){
                int orderType = intent.getIntExtra("orderType",0);
                changeSortOrder(orderType);
            }


                else {                                                             //点击列表中的项目item。
                path = mp3InfoList.get(position).getUrl();
                duration = mp3InfoList.get(position).getDuration();
                if( position == listPosition ){                      //  点击同一条曲目，表示要暂停，或者继续播放该曲目。
                    if(isStop){                                              //  如果是停止状态，表示要播放新的歌曲。
                        Log.e("Service()","是同一行，停止状态，要播放新文件，，，playAnotherMusic "+position);
                        playAnotherMusic(position);
                    }else if( mediaPlayer.isPlaying()){       //   如果此时为正在播放，表示要暂停。
                        Log.e("Service()","是同一行，播放状态，要暂停播放该文件，，，playAnotherMusic "+position);
                        pausePlayingMusic();
                    } else {                                                  //  如果此时为暂停，表示要继续播放。
                        Log.e("Service()","是同一行，暂停状态，要继续播放该文件，，，playAnotherMusic "+position);
                        continuePlayingMusic();                      //  继续播放,用系统的方法start()
                    }
                }

                else {                                                      //  如果不是一个，那肯定是要播放新的文件了。
                    Log.e("Service()","不是同一行，播放新文件，，，playAnotherMusic "+position);
                    playAnotherMusic(position);
                }
                listPosition = position;
                listLastPosition = (mp3InfoList.size()+(position-1))%mp3InfoList.size();
                recycleListPosition = (position+1)%mp3InfoList.size();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void changeSortOrder(int orderType) {
        //1
        Intent intent_change_order = new Intent();
        intent_change_order.setAction(UPDATE_SORT_ORDER);
        intent_change_order.putExtra("orderType",orderType);
        sendBroadcast(intent_change_order);
        //2
        getUpdatedMp3List();
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
            //取消注册
            getApplicationContext().unregisterReceiver(updateSortOrderReceiver);
            //自身播放状态
            isStop = true;
            isPlaying = false;
            //停止服务
            stopSelf();
        }
    }


    private void pausePlayingMusic() {
        if (mediaPlayer != null ) {
            mediaPlayer.pause();
            handler.removeMessages(1);

            Intent intent = new Intent();
            intent.setAction(UPDATE_CONTROL_BAR);
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
            intent.setAction(UPDATE_CONTROL_BAR);
            intent.putExtra("position",listPosition);
            sendBroadcast(intent);

            notificationUtil.updateNoteMusicInfo(listPosition);

            isStop = false;
            isPlaying = true;
        }
    }

    private void playAnotherMusic (final int position) {
        title = mp3InfoList.get(position).getTitle();
        artist = mp3InfoList.get(position).getArtist();
        albumId = mp3InfoList.get(position).getAlbumId();
        duration = mp3InfoList.get(position).getDuration();
        listPosition = position;
        //更新controlBar
        Intent intent = new Intent();
        intent.setAction(UPDATE_CONTROL_BAR);
        intent.putExtra("position",position);
        sendBroadcast(intent);
        //更新RecyclerView的小喇叭
        Intent intent_speaker = new Intent();
        intent_speaker.setAction(UPDATE_SPEAKER_LIST_POSITION);
        intent_speaker.putExtra("position",position);
        sendBroadcast(intent_speaker);
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putInt("speakerPosition",position);
        editor.commit();
        //
        handler.removeMessages(1);
        //
        try {
            mediaPlayer.reset();                           //   把各项参数恢复到初始状态
            mediaPlayer.setDataSource(path);    //   设置播放地址
            mediaPlayer.prepareAsync();             //  进行缓冲
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    Log.e("Service()","文件准备就绪，要开始播放该文件，，， ");
                    handler.sendEmptyMessage(1);
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //更新notification
        new Thread(){       //  务必开启线程更新note，否则按键动画会有卡顿。
            @Override
            public void run() {
                notificationUtil.updateNoteMusicInfo(position);
            }
        }.start();

        isStop = false;
        isPlaying = true;
        //把信息写入sharedPreference
        saveDataOnCompletion();
    }


    private final class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        MyOnPreparedListener() {
        }
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();                     //开始播放
            Log.e("Service()","文件准备就绪，要开始播放该文件，，， ");
            handler.sendEmptyMessage(1);
        }
    }





    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                if (mediaPlayer != null){
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    Intent intent = new Intent();
                    intent.setAction(UPDATE_PROGRESS_BAR);
                    intent.putExtra("duration",duration);
                    intent.putExtra("currentPosition", currentPosition);
                    sendBroadcast(intent);
                    handler.sendEmptyMessageDelayed(1,1000);
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        if(mediaPlayer!=null){
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        //关闭notification
        manager.cancel(NOTIFICATION_ID);
        super.onDestroy();
    }
}
