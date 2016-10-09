package cn.edu.cdut.lm.mymuiscplayer.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.activity.LocalMusicActivity;
import cn.edu.cdut.lm.mymuiscplayer.activity.MainActivity;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;


/**
 * Created by Administrator on 2016/8/12 0012.
 */

public class MusicFragment extends Fragment implements View.OnClickListener {

    private List<Mp3Info> mp3InfoList = new ArrayList<>();
    private TextView textView1;
    private LinearLayout linearLayout1;
    private int second;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        linearLayout1 = (LinearLayout) view.findViewById(R.id.ll_localmusic_musicfragment);
        LinearLayout linearLayout2 = (LinearLayout) view.findViewById(R.id.ll_recentplay_musicfragment);
        LinearLayout linearLayout3 = (LinearLayout) view.findViewById(R.id.ll_download_musicfragment);
        LinearLayout linearLayout4 = (LinearLayout) view.findViewById(R.id.ll_myartists_musicfragment);
        LinearLayout linearLayout5 = (LinearLayout) view.findViewById(R.id.ll_myMV_musicfragment);
        linearLayout1.setOnClickListener(this);
        textView1 = (TextView) view.findViewById(R.id.tv_numberoftrack_musicfragment);

        File databaseFile = getContext().getDatabasePath("MusicDataBase.db");
        Log.e("MusicFragment",databaseFile+"");
        if(databaseFile.exists()){
            Log.e("MusicFragment","文件存在！");
            mp3InfoList = MediaUtil.getMp3ListFromMyDatabase(getContext(),0);
            textView1.setText(mp3InfoList.size()+"");
            if(getActivity() instanceof MainActivity ){
                ((MainActivity)getActivity()).showQuickControl(true);
            }
        }else {
            Toast.makeText(getContext(),"正在生成本地音乐资源数据库",Toast.LENGTH_LONG).show();
            linearLayout1.setClickable(false);
            new Thread(){
                @Override
                public void run() {
                    handler_timer.sendEmptyMessage(1);
                    Looper.prepare();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        MediaUtil.createMyDatabase(getContext());
                    }else MediaUtil.createMyDatabaseLowSystem(getContext());

                    mp3InfoList = MediaUtil.getMp3ListFromMyDatabase(getContext(),0);
                    int size = mp3InfoList.size();
                    Message message = new Message();
                    message.arg1 = size;
                    handler_getMusicNUM.sendMessage(message);
                    handler_timer.removeMessages(1);
                    Looper.loop();
                }
            }.start();

        }
        return view;
    }

    Handler handler_timer = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                textView1.setText("生成本地音乐资源数据库中，请稍后..."+ second++ +" ");
                handler_timer.sendEmptyMessageDelayed(1,1000);
            }
        }
    };

    Handler handler_getMusicNUM = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int number = msg.arg1;
            textView1.setText(number+"");
            linearLayout1.setClickable(true);
            if(getActivity() instanceof MainActivity ){
                ((MainActivity)getActivity()).showQuickControl(true);
            }
            Toast.makeText(getContext(),"生成数据库用时 "+second+" 秒！",Toast.LENGTH_LONG).show();
            if(second <= 5){
                Toast.makeText(getContext(),"您手机中的歌曲比较少啦，记得多听音乐呦~",Toast.LENGTH_LONG).show();
            }else if (second >5 && second <= 10){
                Toast.makeText(getContext(),"您手机中的歌曲比较多啦，多听音乐心情好~",Toast.LENGTH_LONG).show();
            }else if(second > 10){
                Toast.makeText(getContext(),"您手机中的歌曲太多啦，看来是个音乐发烧友啊~",Toast.LENGTH_LONG).show();
            }
        }
    };

 /*   public boolean exist( String dbName ) {
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            database = SQLiteDatabase.openDatabase(dbName, null,SQLiteDatabase.OPEN_READONLY);
            flag = true;
        }  catch (FileNotFoundException e) {
            flag = false;
        } finally {
            if (database != null)
                database.close();
            database = null;
        }  return flag;
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_localmusic_musicfragment:
                Intent intent = new Intent(getActivity(), LocalMusicActivity.class);
                startActivity(intent);
        }
    }
}

