package cn.edu.cdut.lm.mymuiscplayer.service;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by LimiaoMaster on 2016/8/15 22:47
 */

public class PlayerService extends Service {

    private MediaPlayer mediaPlayer;

    private String path;
    private String title;
    private String artist;



    private int lastPosition = -1;  //  这首曲目之前的曲目位置
    private int position ;  // 从列表传来的新的曲目位置

    private int duration;	//播放长度


    private boolean isPause;                    //暂停状态

    public static final String MUSIC_DURATION = "cn.edu.cdut.lm.mymusicplayer.MUSIC_DURATION";//新音乐长度更新动作
    public static final String UPDATE_TITLE_ARTIST = "cn.edu.cdut.lm.mymusicplayer.UPDATE_TITLE_ARTIST";    //  设置曲名和艺术家
    public static final String UPDATE_PLAY_PAUSE = "cn.edu.cdut.lm.mymusicplayer.UPDATE_PLAY_PAUSE";    //  设置播放和暂停按钮的图片
    public static final String UPDATE_BOTTO_MBAR = "cn.edu.cdut.lm.mymusicplayer.UPDATE_BOTTO_MBAR";    //  设置播放和暂停按钮的图片






    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e("onCreate()","-----------执行onCreate()方法----------");

        mediaPlayer = new MediaPlayer();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("onStartCommand()","-----------执行onStartCommand()方法----------");

        path = intent.getStringExtra("url");
        position = intent.getIntExtra("position",0);
        title = intent.getStringExtra("title");
        artist = intent.getStringExtra("artist");

        if( position == lastPosition ){         //首先判断这次点击和上次点击的列表中的项目是不是一个
            if( mediaPlayer.isPlaying()){       // 如果是同一个，表示想要暂停该文件，
                this.pause();
                Log.e("onStartCommand()","已经把音乐暂停！！！");
                Intent sendIntent = new Intent();
                sendIntent.setAction(UPDATE_BOTTO_MBAR);
                sendIntent.putExtra("title",title);
                sendIntent.putExtra("artist",artist);
                sendIntent.putExtra("playOrpause","play");
                sendBroadcast(sendIntent);
                Log.e("onStartCommand()","已经发送intent，请更新播放按钮！！");
            } else {
                mediaPlayer.start();                // 继续播放,用系统的方法start()
                Log.e("onStartCommand()","已经把音乐继续！！！");
                Intent sendIntent = new Intent();
                sendIntent.setAction(UPDATE_BOTTO_MBAR);
                sendIntent.putExtra("title",title);
                sendIntent.putExtra("artist",artist);
                sendIntent.putExtra("playOrpause","pause");
                sendBroadcast(sendIntent);
                Log.e("onStartCommand()","已经发送intent，请更新播放按钮！！");
            }
        } else {                                            //  如果不是一个，那肯定是要播放新的文件了。
            this.playAnotherMusic(0);
            Intent sendIntent = new Intent();
            sendIntent.setAction(UPDATE_BOTTO_MBAR);
            sendIntent.putExtra("title",title);
            sendIntent.putExtra("artist",artist);
            sendIntent.putExtra("playOrpause","pause");
            sendBroadcast(sendIntent);

        }

        lastPosition = position;
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
            } else mediaPlayer.start();    //开始播放

            /*duration = mediaPlayer.getDuration();
            Intent intent = new Intent();
            intent.setAction(MUSIC_DURATION);
            intent.putExtra("duration",duration);
            sendBroadcast(intent);*/


        }
    }









}
