package cn.edu.cdut.lm.mymuiscplayer.fragments;

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

import java.util.ArrayList;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.activity.LocalMusicActivity;
import cn.edu.cdut.lm.mymuiscplayer.adapter.MusicAdapter;
import cn.edu.cdut.lm.mymuiscplayer.module.ItemOfMusicFragment;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;

/**
 * Created by Administrator on 2016/8/12 0012.
 */

public class MusicFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<ItemOfMusicFragment> ItemOfMusicFragmentList = new ArrayList<>(5);

    private int [] imageIDs = {
            R.drawable.music_icn_local,R.drawable.music_icn_recent,
            R.drawable.music_icn_dld,R.drawable.music_icn_artist,
            R.drawable.music_icn_mv
    };

    private String [] titles = {"本地音乐", "最近播放","下载管理","我的歌手","我的MV"};

    private String [] numbers = {"(1369)","(100)","(5)","(3)","(20)"};
/*
    private  LocalMusicFragment localMusicFragment ;
*/


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v("MusicFrag_CreateView","我正在创建视图，，，");
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        ListView musicAboutListView = (ListView) view.findViewById(R.id.listview);

        createItemList();

        MusicAdapter musicAdapter = new MusicAdapter(getContext(),ItemOfMusicFragmentList);
        musicAboutListView.setAdapter(musicAdapter);

        musicAboutListView.setOnItemClickListener(this);

        return view;
    }

    private void createItemList(){
        //获取手机Mp3歌曲总数。
        int size = MediaUtil.getMp3List(getContext()).size();
        numbers[0] = "("+size+")";
        for(int i = 0; i < 5; i++){
            ItemOfMusicFragment itemOfMusicFragment = new ItemOfMusicFragment();
            itemOfMusicFragment.setImageId(imageIDs[i%5]);
            itemOfMusicFragment.setTitle(titles[i%5]);
            itemOfMusicFragment.setNumber(numbers[i%5]);
            ItemOfMusicFragmentList.add(itemOfMusicFragment);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v("onItemClick","您点击了第 "+position+" 行！");
        switch (position){
            case 0:
                Log.i("onItemClick","准备进入“本地音乐”，，，");
                Intent intent = new Intent(getActivity(), LocalMusicActivity.class);
                startActivity(intent);
                //getActivity().finish();
        }
    }
}

