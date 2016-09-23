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

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by LimiaoMaster on 2016/9/23 15:30
 */

public class BottomControlBarFragment extends Fragment {
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


    public BottomControlBarFragment newInstance(){

        return new BottomControlBarFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
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
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        editor.commit();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(UPDATE_UI_ON_LIST_CLICK) ||
                    action.equals(UPDATE_UI_ON_COMPLETION) ||
                    action.equals(UPDATE_CONTROL_BAR)) {
                listPosition = intent.getIntExtra("position",0);
                if (listPosition != lastPosition){
                    Log.e("BottomControlBar","-------收到广播，listPosition != lastPosition，更新ControlBar中--------");
                    //更新专辑封面
                    albumId = mp3InfoList.get(listPosition).getAlbumId();
                    Bitmap bitmapp = MediaUtil.getAlbumArtByPath(albumId,context);
                    iv_art_work.setImageBitmap(bitmapp);
                    Log.e("BottomControlBar","-------收到广播，专辑封面已更新--------");
                    //更新  播放暂停  按钮。
                    iv_play_pause.setImageResource(R.drawable.playbar_btn_pause);
                    Log.e("BottomControlBar","-------收到广播，播放暂停按钮已更新--------");
                    //更新歌名和艺术家
                    title = mp3InfoList.get(listPosition).getTitle();
                    artist = mp3InfoList.get(listPosition).getArtist();
                    tv_title_of_music.setText(title);
                    tv_artist_of_music.setText(artist);
                    Log.e("BottomControlBar","-------收到广播，歌名和艺术家已更新--------");
                    //设置跑马灯，滚动显示歌名。
                    tv_title_of_music.setSingleLine(true);
                    tv_title_of_music.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    tv_title_of_music.setSelected(true);
                    tv_title_of_music.setMarqueeRepeatLimit(-1);

                    isPlaying = true;
                    isStop = false;
                    Log.e("BottomControlBar","-------收到广播，isPlaying的状态为："+isPlaying+"");

                    lastPosition = listPosition;
                    nextPosition = (listPosition+1)%listSize;
                }else {
                    Log.e("BottomControlBar","-------收到广播，listPosition == lastPosition，更新ControlBar中--------");

                    if (isPlaying){
                        iv_play_pause.setImageResource(R.drawable.playbar_btn_play);
                        Log.e("BottomControlBar","收到广播，，，这是相同行。。。设为了播放按钮！"+listPosition+isPlaying);

                        isPlaying = false;
                        isStop = false;
                        Log.e("BottomControlBar","收到广播，，，这是相同行。。。设为了播放按钮！之后的操作。。。"+listPosition+isPlaying);

                    }else {
                        iv_play_pause.setImageResource(R.drawable.playbar_btn_pause);
                        Log.e("BottomControlBar","收到广播，，，这是相同行。。。设为了暂停按钮！"+listPosition+isPlaying);

                        isPlaying = true;
                        isStop = false;
                        Log.e("BottomControlBar","收到广播，，，这是相同行。。。设为了暂停按钮！之后的操作。。。"+listPosition+isPlaying);
                    }
                }
            } else if (action.equals(STOP_PLAY_BY_NOTE)){
                Log.e("BottomControlBar","-------收到广播，关闭notification，更新ControlBar中--------");
                iv_play_pause.setImageResource(R.drawable.playbar_btn_play);
                isPlaying = false;
                isStop = true;
                Log.e("BottomControlBar","-------收到广播，关闭notification"+isPlaying+"");
            }else if (action.equals(UPDATE_PROGRESS_BAR)){
                currentPisition = intent.getIntExtra("currentPosition", 0);
                duration = intent.getLongExtra("duration",0);
                progressBar.setMax(Integer.parseInt(String.valueOf(duration)));
                progressBar.setProgress(currentPisition);
            }
            saveDataOnDetachedFromWindow();
        }
    };
}
