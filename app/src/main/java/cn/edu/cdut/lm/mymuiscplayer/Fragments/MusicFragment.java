package cn.edu.cdut.lm.mymuiscplayer.fragments;

import android.content.Intent;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.activity.LocalMusicActivity;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;


/**
 * Created by Administrator on 2016/8/12 0012.
 */

public class MusicFragment extends Fragment implements View.OnClickListener {

    private List<Mp3Info> mp3InfoList = new ArrayList<>();
    private TextView textView1;
    private LinearLayout linearLayout1;

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
        }else {
            linearLayout1.setClickable(false);
            textView1.setText("生成本地音乐资源数据库中，请稍后...");

            new Thread(){
                @Override
                public void run() {
                    Looper.prepare();
                    MediaUtil.createMyDatabase(getContext());
                    mp3InfoList = MediaUtil.getMp3ListFromMyDatabase(getContext(),0);
                    int size = mp3InfoList.size();
                    Message message = new Message();
                    message.arg1 = size;
                    handler.sendMessage(message);
                    Looper.loop();
                }
            }.start();

        }
        return view;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int number = msg.arg1;
            textView1.setText(number+"");
            linearLayout1.setClickable(true);
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

