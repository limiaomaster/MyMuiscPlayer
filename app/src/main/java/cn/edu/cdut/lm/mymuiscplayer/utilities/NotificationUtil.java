package cn.edu.cdut.lm.mymuiscplayer.utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
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
    private final int CODE_NEXT = 333;
    private static final int CLOSE_INTENT = -1;
    private static final int NEXT_INTENT = -2;
    private static final int PRE_INTENT = -3;



    private static final int CODE_RESET_PLAY_PAUSE = 444;

    private List<Mp3Info> mp3InfoList;
    private NotificationManager manger;
    private static final int NOTIFICATION_ID = 5709;
    private int lastPosition = -1;
    private int listPosition ;
    public  boolean isPlaying = false;
    private Context context;
    private RemoteViews remoteViews;
    private NotificationCompat.Builder builder;
    private Notification notification;


    public static final String UPDATE_PROGRESS_BAR = "cn.edu.cdut.lm.mymusicplayer.UPDATE_PROGRESS_BAR";    //  设置播放和暂停按钮的图片
    public static final String RESET_PLAY_PAUSE = "cn.edu.cdut.lm.mymusicplayer.RESET_PLAY_PAUSE";
    private int nextPosition;

    public NotificationUtil(Context context) {
        this.context = context;
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_notification);
        manger = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
        builder.setTicker("网易云音乐正在播放");
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.stat_notify);
        builder.setContent(remoteViews);    //设置小布局显示内容。
        builder.setOngoing(true);
        notification = builder.build();
        notification.priority=Notification.PRIORITY_MAX;

        /**
         *设置Notification点击关闭的动作。
         */
        //  只能通过PlayerService来停止发送更新进度条的广播。
        Intent intent_close = new Intent();
        intent_close.putExtra("position", CLOSE_INTENT);
        intent_close.setClass(context, PlayerService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, CODE_CLOSE, intent_close, FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_close_notification, pendingIntent);
        /**
         *设置Note下一首
         */
        Intent intent_next = new Intent();
        intent_next.putExtra("position", NEXT_INTENT);
        intent_next.setClass(context, PlayerService.class);
        PendingIntent pendingIntent_next = PendingIntent.getService(context, CODE_NEXT, intent_next, FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_next_notification, pendingIntent_next);

        mp3InfoList = MediaUtil.getMp3List(context);
        Log.e("Note()","获取到了本机歌曲列表---------");
    }

    public void updateNoteMusicInfo(int position) {
        listPosition = position;
        if (listPosition != lastPosition) {     //如果不是同一行，一定是点击了新的行，或者下一首按钮。
            Log.e("Note()","不是同一个listposition"+listPosition);
            //设置Notification专辑封面
            long albumId = mp3InfoList.get(position).getAlbumId();
            Bitmap bitmap_art_work = MediaUtil.getAlbumArtByPath(albumId, context);
            remoteViews.setImageViewBitmap(R.id.iv_albumArt_Notification, bitmap_art_work);
            Log.e("Note()","专辑封面已更新！"+listPosition);
            //设置Notification歌名
            String title = mp3InfoList.get(position).getTitle();
            remoteViews.setTextViewText(R.id.tv_audio_title_notification, title);
            Log.e("Note()","歌名已更新！"+listPosition);
            //设置Notification歌手
            String artist = mp3InfoList.get(position).getArtist();
            remoteViews.setTextViewText(R.id.tv_artist_notification, artist);
            remoteViews.setTextViewTextSize(R.id.tv_artist_notification, COMPLEX_UNIT_SP, 17);
            Log.e("Note()","歌手已更新！"+listPosition);
            //设置Notification专辑名称
            String album = mp3InfoList.get(position).getAlbum();
            remoteViews.setTextViewText(R.id.tv_album_notification, album);
            remoteViews.setTextViewTextSize(R.id.tv_album_notification, COMPLEX_UNIT_SP, 17);
            Log.e("Note()","专辑名称已更新！"+listPosition);
            //设置Notification播放按钮
            remoteViews.setImageViewResource(R.id.iv_pause_play_notification,R.drawable.note_btn_pause_white);
            Log.e("Note()","设为暂停键了，，，"+listPosition);
            isPlaying = true;
            Log.e("Note()","播放状态已更新！"+"isPlaying为"+isPlaying);
        }else {            //  如果是相同的行，肯定是点击了ControlBar的播放暂停键，只需更新Notification的按键的图标。
            Log.e("Note()","是相同行的listposition"+listPosition);
            //设置自己播放暂停键的图标
            if (isPlaying){
                remoteViews.setImageViewResource(R.id.iv_pause_play_notification,R.drawable.note_btn_play_white);
                Log.e("Note()","设为播放键了，，，"+listPosition+isPlaying);
                isPlaying = false;
            }else {
                remoteViews.setImageViewResource(R.id.iv_pause_play_notification,R.drawable.note_btn_pause_white);
                Log.e("Note()","设为暂停键了，，，"+listPosition+isPlaying);
                isPlaying = true;
            }
        }
        lastPosition = listPosition;
        //nextPosition = (listPosition+1)%mp3InfoList.size();
        /**
         *设置Note播放和暂停键
         */
        Intent intent_pause_play = new Intent();
        intent_pause_play.putExtra("position", listPosition);
        intent_pause_play.setClass(context, PlayerService.class);
        PendingIntent pendingIntent_play_pause = PendingIntent.getService(context, CODE_PAUSE, intent_pause_play, FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_pause_play_notification, pendingIntent_play_pause);

        notification.bigContentView=remoteViews;   //设置大布局显示内容。
        manger.notify(NOTIFICATION_ID, notification);
        Log.e("Note()","所有视图已更新完毕-------------------");

    }


}

