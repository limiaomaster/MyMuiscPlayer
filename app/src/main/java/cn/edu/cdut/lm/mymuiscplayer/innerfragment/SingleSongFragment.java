package cn.edu.cdut.lm.mymuiscplayer.innerfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.adapter.LocalMusicAdapter;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.service.PlayerService;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;

/**
 * Created by LimiaoMaster on 2016/8/14 0014 21:43
 */

public class SingleSongFragment extends Fragment implements AdapterView.OnItemClickListener{

    public static final String UPDATE_UI_ON_LIST_CLICK = "cn.edu.cdut.lm.mymusicplayer.UPDATE_UI_ON_LIST_CLICK";

    private List<Mp3Info> mp3InfoList;


    private int listPosition ;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inner_fragment_single_music, container , false);
        ListView listView = (ListView) view.findViewById(R.id.listview_localmusic);

        mp3InfoList = MediaUtil.getMp3List(getContext());  //调用工具包中的getMp3Infos()方法，获取Mp3Info对象的列表。

        LocalMusicAdapter localMusicAdapter = new LocalMusicAdapter(getContext(), mp3InfoList);
        listView.setAdapter(localMusicAdapter);
        listView.setOnItemClickListener(this);



        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Mp3Info mp3Info = mp3InfoList.get(position);
        Log.e("onItemClick","您点击了第："+position+"行！");

        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.setClass(getContext(), PlayerService.class);
        getActivity().startService(intent);
        Log.e("onItemClick","启动了PlayerService播放服务！");

        Intent broadCastIntent = new Intent();
        broadCastIntent.setAction(UPDATE_UI_ON_LIST_CLICK);
        broadCastIntent.putExtra("position",position);
        getActivity().sendBroadcast(broadCastIntent);
        Log.e("onItemClick","发送了UPDATE_UI的广播！");



        /*ImageView thisSpeaker = (ImageView) listView.getChildAt(position).findViewById(speaker);
        ImageView lastSpeaker = (ImageView) listView.getChildAt(listPosition).findViewById(speaker);

        if(position != listPosition ){
            thisSpeaker.setVisibility(View.VISIBLE);
            lastSpeaker.setVisibility(View.GONE);
            listPosition = position;
        }else thisSpeaker.setVisibility(View.VISIBLE);*/



        /*View v=parent.getChildAt(position);
        v.setBackgroundColor(Color.RED);*/






    }
}
