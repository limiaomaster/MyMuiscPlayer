package cn.edu.cdut.lm.mymuiscplayer.utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.service.PlayerService;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Created by LimiaoMaster on 2016/9/7 10:22
 */

public class NotificationUtil {
    private static final int CODE_CLOSE = 111;
    private static final int CODE_PAUSE_PLAY = 222;
    private static final int CODE_NEXT = 333;
    private static final int CODE_PRE = 444;

    private static final int CLOSE_INTENT = -1;
    private static final int PAUSE_PLAY_INTENT = -2;
    private static final int NEXT_INTENT = -3;
    private static final int PRE_INTENT = -4;

    private List<Mp3Info> mp3InfoList;
    private NotificationManager manger;
    private static final int NOTIFICATION_ID = 5709;

    private int listPosition = -1 ;
    public  boolean isPlaying = false;
    private Context context;
    private RemoteViews remoteViews;
    private NotificationCompat.Builder builder;
    private Notification notification;

    public static final String UPDATE_SORT_ORDER = "cn.edu.cdut.lm.mymusicplayer.UPDATE_SORT_ORDER";




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
        getOrUpdateMp3List();



        /**
         *设置Note点击关闭的动作。
         */
        //  只能通过PlayerService来停止发送更新进度条的广播。
        Intent intent_close = new Intent();
        intent_close.putExtra("position", CLOSE_INTENT);
        intent_close.setClass(context, PlayerService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, CODE_CLOSE, intent_close, FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_close_notification, pendingIntent);
        /**
         *设置Note播放和暂停键
         */
        Intent intent_pause_play = new Intent();
        intent_pause_play.putExtra("position", PAUSE_PLAY_INTENT);
        intent_pause_play.setClass(context, PlayerService.class);
        PendingIntent pendingIntent_play_pause = PendingIntent.getService(context, CODE_PAUSE_PLAY, intent_pause_play, FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_pause_play_notification, pendingIntent_play_pause);
        /**
         *设置Note下一首
         */
        Intent intent_next = new Intent();
        intent_next.putExtra("position", NEXT_INTENT);
        intent_next.setClass(context, PlayerService.class);
        PendingIntent pendingIntent_next = PendingIntent.getService(context, CODE_NEXT, intent_next, FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_next_notification, pendingIntent_next);

        /**
         *设置Note下一首
         */
        Intent intent_pre = new Intent();
        intent_pre.putExtra("position",PRE_INTENT);
        intent_pre.setClass(context,PlayerService.class);
        PendingIntent pendingIntent_pre = PendingIntent.getService(context,CODE_PRE,intent_pre,FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_pre_notification,pendingIntent_pre);
    }

    public void getOrUpdateMp3List(){
        SharedPreferences pref = context.getSharedPreferences("data", MODE_PRIVATE);
        int sortOrder = pref.getInt("sort_order_check_position", 0);
        mp3InfoList = MediaUtil.getMp3ListFromMyDatabase(context,sortOrder);
    }

    public void updateNoteMusicInfo(int position) {
        if (position != listPosition) {     //如果不是同一行，一定是点击了新的行，或者下一首按钮。
            Log.e("Note()","不是同一个listposition"+position);
            //设置Notification专辑封面
            long albumId = mp3InfoList.get(position).getAlbumId();
            Bitmap bitmap_art_work = MediaUtil.getAlbumArtByPath(albumId, context);
            remoteViews.setImageViewBitmap(R.id.iv_albumArt_Notification, bitmap_art_work);
            Log.e("Note()","专辑封面已更新！"+position);
            //设置Notification歌名
            String title = mp3InfoList.get(position).getTitle();
            remoteViews.setTextViewText(R.id.tv_audio_title_notification, title);
            Log.e("Note()","歌名已更新！"+position);
            //设置Notification歌手
            String artist = mp3InfoList.get(position).getArtist();
            remoteViews.setTextViewText(R.id.tv_artist_notification, artist);
            remoteViews.setTextViewTextSize(R.id.tv_artist_notification, COMPLEX_UNIT_SP, 17);
            Log.e("Note()","歌手已更新！"+position);
            //设置Notification专辑名称
            String album = mp3InfoList.get(position).getAlbum();
            remoteViews.setTextViewText(R.id.tv_album_notification, album);
            remoteViews.setTextViewTextSize(R.id.tv_album_notification, COMPLEX_UNIT_SP, 17);
            Log.e("Note()","专辑名称已更新！"+position);
            //设置Notification播放按钮
            remoteViews.setImageViewResource(R.id.iv_pause_play_notification,R.drawable.note_btn_pause_white);
            Log.e("Note()","设为暂停键了，，，"+position);
            isPlaying = true;
            Log.e("Note()","播放状态已更新！"+"isPlaying为"+isPlaying);
        }else {            //  如果是相同的行，肯定是点击了ControlBar的播放暂停键，只需更新Notification的按键的图标。
            Log.e("Note()","是相同行的listposition"+position);
            //设置自己播放暂停键的图标
            if (isPlaying){
                remoteViews.setImageViewResource(R.id.iv_pause_play_notification,R.drawable.note_btn_play_white);
                Log.e("Note()","设为播放键了，，，"+position+isPlaying);
                isPlaying = false;
            }else {
                remoteViews.setImageViewResource(R.id.iv_pause_play_notification,R.drawable.note_btn_pause_white);
                Log.e("Note()","设为暂停键了，，，"+position+isPlaying);
                isPlaying = true;
            }
        }
        listPosition = position;
        notification.bigContentView=remoteViews;   //设置大布局显示内容。
        Log.e("Note()","设置notification的大布局为remoteViews");

        manger.notify(NOTIFICATION_ID, notification);
        Log.e("Note()","所有视图已更新完毕-------------------");
    }

    public class UpdateSortOrderReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(UPDATE_SORT_ORDER)){
                getOrUpdateMp3List();
            }
        }
    }
}

