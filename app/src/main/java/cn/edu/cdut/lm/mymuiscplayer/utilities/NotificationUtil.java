package cn.edu.cdut.lm.mymuiscplayer.utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.service.PlayerService;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static android.content.Context.NOTIFICATION_SERVICE;
import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Created by LimiaoMaster on 2016/9/7 10:22
 */

public class NotificationUtil {
    private static final int CODE_CLOSE = 111;
    private static final int CODE_PAUSE = 222;
    private static final int CODE_RESET_PLAY_PAUSE = 333;

    private  List<Mp3Info> mp3InfoList;
    private  NotificationManager manger;
    private static final int NOTIFICATION_ID = 5709;
    private  int lastPosition;
    private  int listPosition;
    private static boolean isPlaying;
    private  Context context;
    private RemoteViews remoteViews;
    public static final String UPDATE_PROGRESS_BAR = "cn.edu.cdut.lm.mymusicplayer.UPDATE_PROGRESS_BAR";    //  设置播放和暂停按钮的图片
    public static final String RESET_PLAY_PAUSE = "cn.edu.cdut.lm.mymusicplayer.RESET_PLAY_PAUSE";


    public NotificationUtil(Context context ) {
        this.context = context;
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_notification);
        manger = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    }

    public  void updateNotificationUI(int position){

        listPosition = position;
        mp3InfoList = MediaUtil.getMp3List(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        /**
         *设置Notification专辑封面
         */
        //long musicId = mp3InfoList.get(position).getId();
        long albumId = mp3InfoList.get(position).getAlbumId();
        Bitmap bitmap_art_work = MediaUtil.getAlbumArtByPath(albumId,context);
        remoteViews.setImageViewBitmap(R.id.iv_albumArt_Notification,bitmap_art_work);
        /**
         *设置Notification歌名
         */
        String title = mp3InfoList.get(position).getTitle();
        remoteViews.setTextViewText(R.id.tv_audio_title_notification,title);
        /**
         *设置Notification歌手
         */
        String artist = mp3InfoList.get(position).getArtist();
        remoteViews.setTextViewText(R.id.tv_artist_notification,artist);
        remoteViews.setTextViewTextSize(R.id.tv_artist_notification,COMPLEX_UNIT_SP,17);
        /**
         *设置Notification专辑名称
         */
        String album = mp3InfoList.get(position).getAlbum();
        remoteViews.setTextViewText(R.id.tv_album_notification,album);
        remoteViews.setTextViewTextSize(R.id.tv_album_notification,COMPLEX_UNIT_SP,17);
        /**
         *设置Notification播放暂停键的图标
         */
        if(listPosition == lastPosition){
            if(isPlaying){
                remoteViews.setImageViewResource(R.id.iv_pause_play_notification,R.drawable.note_btn_play_white);
                isPlaying = false;
            }else {
                remoteViews.setImageViewResource(R.id.iv_pause_play_notification,R.drawable.note_btn_pause_white);
                isPlaying = true;
            }
        }else {
            remoteViews.setImageViewResource(R.id.iv_pause_play_notification,R.drawable.note_btn_pause_white);
            isPlaying = true;
        }
        lastPosition = listPosition;

        /**
         *设置Notification点击播放和暂停键的动作
         */
        Intent intent_pause_play = new Intent();
        intent_pause_play.putExtra("position",listPosition);
        intent_pause_play.setClass(context,PlayerService.class);
        PendingIntent pendingIntent1 = PendingIntent.getService(context,CODE_PAUSE,intent_pause_play,FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_pause_play_notification,pendingIntent1);
        /**
         *设置Notification点击关闭的动作。
         */
        //  只能通过PlayerService来停止发送更新进度条的广播。
        Intent intent_close = new Intent();
        intent_close.putExtra("position",-1);
        intent_close.setClass(context,PlayerService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context,CODE_CLOSE,intent_close,FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_close_notification,pendingIntent);

        /*Intent intent_resetPlayButton = new Intent();
        intent_resetPlayButton.setAction(RESET_PLAY_PAUSE);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context,CODE_RESET_PLAY_PAUSE,intent_resetPlayButton,FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_close_notification,pendingIntent2);*/


        builder.setTicker("网易云音乐正在播放");
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.stat_notify);
        builder.setContent(remoteViews);    //设置小布局显示内容。
        builder.setOngoing(true);

        Notification notification = builder.build();
        notification.bigContentView = remoteViews;   //设置大布局显示内容。
        notification.priority = Notification.PRIORITY_MAX;
        manger.notify(NOTIFICATION_ID,notification);

    }
}
