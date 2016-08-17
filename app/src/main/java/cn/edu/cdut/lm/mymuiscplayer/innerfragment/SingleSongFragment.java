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
 * Created by LimiaoMaster on 2016/8/14 0014.
 */

public class SingleSongFragment extends Fragment implements AdapterView.OnItemClickListener{

    private List<Mp3Info> mp3InfoList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inner_fragment_single_music, container , false);
        ListView listView = (ListView) view.findViewById(R.id.listview_localmusic);

        mp3InfoList = MediaUtil.getMp3Infos(getContext());  //调用工具包中的getMp3Infos()方法，获取Mp3Info对象的列表。

        LocalMusicAdapter localMusicAdapter = new LocalMusicAdapter(getContext(), mp3InfoList);
        listView.setAdapter(localMusicAdapter);
        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Mp3Info mp3Info = mp3InfoList.get(position);
        Log.e("onItemClick","您点击了第："+position+"行！");
        Log.e("onItemClick",""+mp3Info);
        Intent intent = new Intent();
        intent.putExtra("url",mp3Info.getUrl());
        intent.putExtra("position", position);
        intent.putExtra("title",mp3Info.getTitle());
        intent.putExtra("artist",mp3Info.getArtist());

        intent.setClass(getContext(), PlayerService.class);
        getActivity().startService(intent);
        Log.e("onItemClick","启动了播放服务！");

    }
}
