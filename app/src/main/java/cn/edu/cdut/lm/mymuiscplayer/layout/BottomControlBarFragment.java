package cn.edu.cdut.lm.mymuiscplayer.layout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.service.PlayerService;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by LimiaoMaster on 2016/9/23 15:30
 */

public class BottomControlBarFragment extends Fragment implements View.OnClickListener {
    private static String TAG = "ControlBarFragment";
    public static final String UPDATE_PROGRESS_BAR = "cn.edu.cdut.lm.mymusicplayer.UPDATE_PROGRESS_BAR";    //  设置播放和暂停按钮的图片
    public static final String UPDATE_UI_ON_LIST_CLICK = "cn.edu.cdut.lm.mymusicplayer.UPDATE_UI_ON_LIST_CLICK";
    public static final String UPDATE_UI_ON_COMPLETION = "cn.edu.cdut.lm.mymusicplayer.UPDATE_UI_ON_COMPLETION";    //  设置播放和暂停按钮的图片
    public static final String STOP_PLAY_BY_NOTE = "cn.edu.cdut.lm.mymusicplayer.STOP_PLAY_BY_NOTE";
    public static final String UPDATE_CONTROL_BAR = "cn.edu.cdut.lm.mymusicplayer.UPDATE_CONTROL_BAR";

    private int lastPosition = -1;
    private int listPosition;
    private int  nextPosition;
    private long duration;
    private int currentPisition;
    private boolean isPlaying = false;
    private boolean isStop = true;
    private ProgressBar progressBar;
    private String title;
    private String artist;
    private long albumId;
    private List<Mp3Info> mp3InfoList;
    private  int listSize;
    private TextView tv_title_of_music;
    private TextView tv_artist_of_music;
    private ImageView iv_play_pause;
    private ImageView iv_next_song;
    private ImageView iv_art_work;
    long lastClickTime = 0;
    final int MIN_CLICK_DELAY_TIME = 1000;

    public static BottomControlBarFragment newInstance(){
        Log.e(TAG,"newInstance方法得到执行！！！");

        return new BottomControlBarFragment();
    }

    @Override
    public void onAttach(Context context) {
        Log.e(TAG,"onAttach方法得到执行！");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreate方法得到执行！");
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView方法得到执行！");
        View view = inflater.inflate(R.layout.bottom_control_bar,container,false);
        tv_title_of_music = (TextView) view.findViewById(R.id.title_of_music);
        tv_artist_of_music = (TextView) view.findViewById(R.id.artist_of_music);
        iv_play_pause = (ImageView) view.findViewById(R.id.play_pause_btn);
        iv_next_song = (ImageView) view.findViewById(R.id.next_song);
        iv_art_work = (ImageView) view.findViewById(R.id.art_work);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        iv_play_pause.setOnClickListener(this);
        iv_next_song.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onActivityCreated方法得到执行！");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.e(TAG,"onStart方法得到执行！");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e(TAG,"onResume方法得到执行！");
        super.onResume();

        getDataAndUpdateBarOnAttachedToWindow();

        mp3InfoList = MediaUtil.getMp3List(getContext());  //调用工具包中的getMp3Infos()方法，获取Mp3Info对象的列表。
        listSize = mp3InfoList.size();  //  获取歌曲总数

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATE_UI_ON_LIST_CLICK);
        intentFilter.addAction(UPDATE_UI_ON_COMPLETION);
        intentFilter.addAction(STOP_PLAY_BY_NOTE);
        intentFilter.addAction(UPDATE_CONTROL_BAR);
        intentFilter.addAction(UPDATE_PROGRESS_BAR);
        getActivity().registerReceiver(broadcastReceiver,intentFilter);
    }




    @Override
    public void onPause() {
        Log.e(TAG,"onPause方法得到执行！");
        super.onPause();

        saveDataOnDetachedFromWindow();

        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onStop() {
        Log.e(TAG,"onStop方法得到执行！");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.e(TAG,"onDestroyView方法得到执行！");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG,"onDestroy方法得到执行！");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.e(TAG,"onDetach方法得到执行！");
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_pause_btn:
                // 启动播放服务
                Intent intent = new Intent();
                intent.putExtra("position", listPosition);
                intent.setClass(getContext(), PlayerService.class);
                getContext().startService(intent);  //  注意是调用getContext()，不是SingleSongFragment中的getActivity()
                break;
            case R.id.next_song:
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    //启动播放服务
                    Intent intent_next = new Intent();
                    intent_next.putExtra("position", nextPosition);
                    intent_next.setClass(getContext(), PlayerService.class);
                    getContext().startService(intent_next);
                    lastClickTime = currentTime;
                }
                break;
        }
    }


    private void getDataAndUpdateBarOnAttachedToWindow(){
        SharedPreferences pref = getContext().getSharedPreferences("data", MODE_PRIVATE);
        //1判断歌曲状态，更新  播放暂停  按钮。
        isPlaying = pref.getBoolean("isplaying",true);
        if(isPlaying) {
            iv_play_pause.setImageResource(R.drawable.playbar_btn_pause);
        } else iv_play_pause.setImageResource(R.drawable.playbar_btn_play);
        //2 更新歌名和艺术家
        title = pref.getString("title", "");
        artist = pref.getString("artist", "");
        tv_title_of_music.setText(title);
        tv_artist_of_music.setText(artist);
        //设置跑马灯，滚动显示歌名。
        tv_title_of_music.setSingleLine(true);
        tv_title_of_music.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv_title_of_music.setSelected(true);
        tv_title_of_music.setMarqueeRepeatLimit(-1);
        //3更新专辑封面
        albumId = pref.getLong("album_id",0);
        //bitmap_art_work = MediaUtil.getArtwork(context,musicId_pref,albumId_pref,true,true);
        Bitmap bitmap = MediaUtil.getAlbumArtByPath(albumId,getContext());
        iv_art_work.setImageBitmap(bitmap);
        //4更新进度条
        duration = pref.getLong("duration",0);
        currentPisition = pref.getInt("currentPisition",0);
        progressBar.setMax(Integer.parseInt(String.valueOf(duration)));
        progressBar.setProgress(currentPisition);
        //5更新正在播放歌曲的位置
        listPosition = pref.getInt("listPosition",-1);
        //6
        nextPosition = pref.getInt("nextPosition",0);
    }


    private void saveDataOnDetachedFromWindow(){
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("title", title);
        editor.putString("artist", artist);
        editor.putBoolean("isplaying", isPlaying);
        editor.putLong("duration",duration);
        editor.putInt("currentPisition",currentPisition);
        editor.putLong("album_id",albumId);
        editor.putInt("listPosition",listPosition);
        editor.putInt("nextPosition",nextPosition);
        editor.commit();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (
                    action.equals(UPDATE_UI_ON_LIST_CLICK) ||
                    action.equals(UPDATE_UI_ON_COMPLETION) ||
                    action.equals(UPDATE_CONTROL_BAR)
                    ) {
                int position = intent.getIntExtra("position",0);
                if (position != listPosition){
                    Log.e(TAG,"-------收到广播，position != listPosition，更新ControlBar中--------");
                    //更新专辑封面
                    albumId = mp3InfoList.get(position).getAlbumId();
                    Bitmap bitmapp = MediaUtil.getAlbumArtByPath(albumId,context);
                    iv_art_work.setImageBitmap(bitmapp);
                    Log.e(TAG,"-------收到广播，专辑封面已更新--------");
                    //更新  播放暂停  按钮。
                    iv_play_pause.setImageResource(R.drawable.playbar_btn_pause);
                    Log.e(TAG,"-------收到广播，播放暂停按钮已更新--------");
                    //更新歌名和艺术家
                    title = mp3InfoList.get(position).getTitle();
                    artist = mp3InfoList.get(position).getArtist();
                    tv_title_of_music.setText(title);
                    tv_artist_of_music.setText(artist);
                    Log.e(TAG,"-------收到广播，歌名和艺术家已更新--------");
                    //设置跑马灯，滚动显示歌名。
                    tv_title_of_music.setSingleLine(true);
                    tv_title_of_music.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    tv_title_of_music.setSelected(true);
                    tv_title_of_music.setMarqueeRepeatLimit(-1);

                    isPlaying = true;
                    isStop = false;
                    Log.e(TAG,"-------收到广播，isPlaying的状态为："+isPlaying+"");

                    listPosition = position;
                    nextPosition = (position+1)%listSize;
                }else {
                    Log.e(TAG,"-------收到广播，position == listPosition，更新ControlBar中--------");

                    if (isPlaying){
                        iv_play_pause.setImageResource(R.drawable.playbar_btn_play);
                        Log.e(TAG,"收到广播，，，这是相同行。。。设为了播放按钮！"+position+isPlaying);

                        isPlaying = false;
                        isStop = false;
                        Log.e(TAG,"收到广播，，，这是相同行。。。设为了播放按钮！之后的操作。。。"+position+isPlaying);

                    }else {
                        iv_play_pause.setImageResource(R.drawable.playbar_btn_pause);
                        Log.e(TAG,"收到广播，，，这是相同行。。。设为了暂停按钮！"+position+isPlaying);

                        isPlaying = true;
                        isStop = false;
                        Log.e(TAG,"收到广播，，，这是相同行。。。设为了暂停按钮！之后的操作。。。"+position+isPlaying);
                    }
                }
            } else if (action.equals(STOP_PLAY_BY_NOTE)){
                Log.e(TAG,"-------收到广播，关闭notification，更新ControlBar中--------");
                iv_play_pause.setImageResource(R.drawable.playbar_btn_play);
                isPlaying = false;
                isStop = true;
                Log.e(TAG,"-------收到广播，关闭notification"+isPlaying+"");
            }else if (action.equals(UPDATE_PROGRESS_BAR)){
                currentPisition = intent.getIntExtra("currentPosition", 0);
                duration = intent.getLongExtra("duration",0);
                progressBar.setMax(Integer.parseInt(String.valueOf(duration)));
                progressBar.setProgress(currentPisition);
            }
            //saveDataOnDetachedFromWindow();
        }
    };


}
