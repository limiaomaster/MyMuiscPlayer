package cn.edu.cdut.lm.mymuiscplayer.layout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.service.PlayerService;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by LimiaoMaster on 2016/8/17 9:22
 */

public class BottomControlBar extends RelativeLayout implements View.OnClickListener{

    private static Context context;

    private static List<Mp3Info> mp3InfoList;
    private static int listSize;

    private static TextView tv_title_of_music;
    private static TextView tv_artist_of_music;

    private static ImageView iv_play_pause;
    private static ImageView iv_next_song;
    private static ImageView iv_art_work;

    private static Bitmap bitmap_art_work;
    private static ProgressBar progressBar;

    private static String title;
    private static String artist;

    private static long musicId;
    private static long albumId;

    private static int lastPosition;
    private static int listPosition;
    private static int  nextPosition;

    private static long duration;
    private static int currentPisition;

    private static boolean isPlaying;

    private UpdateBarReceiver updateBarReceiver;

    public static final String UPDATE_PROGRESS_BAR = "cn.edu.cdut.lm.mymusicplayer.UPDATE_PROGRESS_BAR";    //  设置播放和暂停按钮的图片
    public static final String UPDATE_UI_ON_LIST_CLICK = "cn.edu.cdut.lm.mymusicplayer.UPDATE_UI_ON_LIST_CLICK";
    public static final String UPDATE_UI_ON_COMPLETION = "cn.edu.cdut.lm.mymusicplayer.UPDATE_UI_ON_COMPLETION";    //  设置播放和暂停按钮的图片

    public BottomControlBar(Context context, AttributeSet attrs) {

        super(context, attrs);  //  必须放在第一行
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_control_bar,this);

        tv_title_of_music = (TextView) view.findViewById(R.id.title_of_music);
        tv_artist_of_music = (TextView) view.findViewById(R.id.artist_of_music);
        iv_play_pause = (ImageView) view.findViewById(R.id.play_pause_btn);
        iv_next_song = (ImageView) view.findViewById(R.id.next_song);
        iv_art_work = (ImageView) view.findViewById(R.id.art_work);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        updateBarReceiver = new UpdateBarReceiver();

        iv_play_pause.setOnClickListener(this);
        iv_next_song.setOnClickListener(this);

        mp3InfoList = MediaUtil.getMp3List(getContext());  //调用工具包中的getMp3Infos()方法，获取Mp3Info对象的列表。
        listSize = mp3InfoList.size();  //  获取歌曲总数
        Log.e("BottomControlBar()","您的手机上一共有"+listSize+"首歌曲！！！");
    }





    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("onAttachedToWindow()","onAttachedToWindow方法得到执行！");
        getDataAndUpdateBarOnAttachedToWindow();
    }

    public void getDataAndUpdateBarOnAttachedToWindow(){
        SharedPreferences pref = getContext().getSharedPreferences("data", MODE_PRIVATE);
        /**
         * 1
         * 判断歌曲状态，更新  播放暂停  按钮。
         */
        if(isPlaying) {
            iv_play_pause.setImageResource(R.drawable.playbar_btn_pause);
        } else iv_play_pause.setImageResource(R.drawable.playbar_btn_play);

        /**
         * 2
         * 更新歌名和艺术家
         */
        String title_pref = pref.getString("title", "");
        String artist_pref = pref.getString("artist", "");
        tv_title_of_music.setText(title_pref);
        tv_artist_of_music.setText(artist_pref);
        //设置跑马灯，滚动显示歌名。
        tv_title_of_music.setSingleLine(true);
        tv_title_of_music.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv_title_of_music.setSelected(true);
        tv_title_of_music.setMarqueeRepeatLimit(-1);
        title = title_pref;           //    务必注意把从xml文件中获取的内容再赋给当前变量
        artist = artist_pref;       //    否则第二次显示控制条的时候的内容都为空，，，

        /**
         * 3
         * 更新专辑封面
         */
        long musicId_pref = pref.getLong("music_id",0);
        long albumId_pref = pref.getLong("album_id",0);
        bitmap_art_work = MediaUtil.getArtwork(context,musicId_pref,albumId_pref,true,true);
        iv_art_work.setImageBitmap(bitmap_art_work);
        musicId = musicId_pref;
        albumId = albumId_pref;
        /**
         * 4
         * 更新进度条
         */
        Long duration_pref = pref.getLong("duration",0);
        int currentPisition_pref = pref.getInt("currentPisition",0);
        progressBar.setMax(Integer.parseInt(String.valueOf(duration_pref)));
        progressBar.setProgress(currentPisition_pref);
        duration = duration_pref;
        currentPisition = currentPisition_pref;
    }





    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("onDetachedFromWindow()","onDetachedFromWindow方法得到执行！");
        saveDataOnDetachedFromWindow();
    }

    public static void saveDataOnDetachedFromWindow(){
        SharedPreferences.Editor editor = context.getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("title", title);
        editor.putString("artist", artist);
        editor.putBoolean("isplaying", isPlaying);
        editor.putLong("duration",duration);
        editor.putInt("currentPisition",currentPisition);
        editor.putLong("music_id",musicId);
        editor.putLong("album_id",albumId);
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_pause_btn:
                Log.e("onClick","点击了play_pause按钮，，，");
                    Intent intent = new Intent();
                    intent.putExtra("position", listPosition);
                    intent.setClass(getContext(), PlayerService.class);
                    getContext().startService(intent);  //  注意是调用getContext()，不是SingleSongFragment中的getActivity()
                if( !isPlaying ){                     //    如果处于暂停或者停止状态，表示要播放歌曲了，要把图标置为暂停！！
                    Log.e("onClick","此时处于暂停或者停止状态");
                    iv_play_pause.setImageResource(R.drawable.playbar_btn_pause);
                    isPlaying = true;
                }else {                                //    如果处于播放状态，表示要暂停歌曲，要把图标置为播放！！
                    Log.e("onClick","此时处于播放状态");
                    iv_play_pause.setImageResource(R.drawable.playbar_btn_play);
                    isPlaying = false;
                }
           break;

            case R.id.next_song:
                Intent intent1 = new Intent();
                intent1.putExtra("position", nextPosition);
                intent1.setClass(getContext(), PlayerService.class);
                getContext().startService(intent1);

                title = mp3InfoList.get(nextPosition).getTitle();
                artist = mp3InfoList.get(nextPosition).getArtist();
                tv_title_of_music.setText(title);
                tv_artist_of_music.setText(artist);

                musicId = mp3InfoList.get(nextPosition).getId();
                albumId = mp3InfoList.get(nextPosition).getAlbumId();
                bitmap_art_work = MediaUtil.getArtwork(context,musicId,albumId,true,true);
                iv_art_work.setImageBitmap(bitmap_art_work);

                iv_play_pause.setImageResource(R.drawable.playbar_btn_pause);
                isPlaying = true;

                listPosition = nextPosition;                           //   也要注意更新当前位置listPosition
                lastPosition = nextPosition;                          //   也要注意更新上一个位置lastPosition
                nextPosition = (nextPosition+1)%listSize;   //  注意更新nextPosition，，，
            break;
        }
    }

    //  内部类，广播接收器，更新底部控制条的UI。
    //  要为static类型的，要在Manifest文件中注册，并设置过滤器。
    public static class UpdateBarReceiver extends BroadcastReceiver {

        public UpdateBarReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(UPDATE_UI_ON_LIST_CLICK) || action.equals(UPDATE_UI_ON_COMPLETION)) {

                listPosition = intent.getIntExtra("position",0);
                /**
                 * 1
                 * 判断歌曲状态，更新  播放暂停  按钮。
                 */
                if(listPosition == lastPosition){   // 点击同一条曲目，表示要暂停，或者继续播放该曲目。
                    if(isPlaying ){                         //  如果此时为正在播放，表示要暂停。
                        isPlaying = false;
                        iv_play_pause.setImageResource(R.drawable.playbar_btn_play);
                    }else{                                    //    如果此时为暂停，表示要继续播放。
                        isPlaying = true;
                        iv_play_pause.setImageResource(R.drawable.playbar_btn_pause);
                    }
                }else{                                       //  点击不同的曲目，一定是播放新的歌曲。
                    isPlaying = true;
                    iv_play_pause.setImageResource(R.drawable.playbar_btn_pause);
                }
                lastPosition = listPosition;
                nextPosition = (listPosition+1)%listSize;
                /**
                 * 2
                 * 更新歌名和艺术家
                 */
                title = mp3InfoList.get(listPosition).getTitle();
                artist = mp3InfoList.get(listPosition).getArtist();
                tv_title_of_music.setText(title);
                tv_artist_of_music.setText(artist);
                //设置跑马灯，滚动显示歌名。
                tv_title_of_music.setSingleLine(true);
                tv_title_of_music.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                tv_title_of_music.setSelected(true);
                tv_title_of_music.setMarqueeRepeatLimit(-1);

                /**
                 * 3
                 * 更新专辑封面
                 */
                musicId = mp3InfoList.get(listPosition).getId();
                albumId = mp3InfoList.get(listPosition).getAlbumId();
                bitmap_art_work = MediaUtil.getArtwork(context,musicId,albumId,true,true);
                iv_art_work.setImageBitmap(bitmap_art_work);

            }   else if (action.equals(UPDATE_PROGRESS_BAR)){

                currentPisition = intent.getIntExtra("currentPosition", 0);
                duration = intent.getLongExtra("duration",0);

                progressBar.setMax(Integer.parseInt(String.valueOf(duration)));
                progressBar.setProgress(currentPisition);
            }
            saveDataOnDetachedFromWindow();
        }
    }
}
