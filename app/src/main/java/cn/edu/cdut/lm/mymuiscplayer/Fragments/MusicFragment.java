package cn.edu.cdut.lm.mymuiscplayer.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.adapter.MusicAdapter;
import cn.edu.cdut.lm.mymuiscplayer.module.MusicAbout;

/**
 * Created by Administrator on 2016/8/12 0012.
 */

public class MusicFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<MusicAbout> musicAboutList = new ArrayList<>(4);

    private int [] imageIDs = {R.mipmap.topmenu_icn_free,R.mipmap.topmenu_icn_member,
                                            R.mipmap.topmenu_icn_msg,R.mipmap.topmenu_icn_store};

    private String [] titles = {"本地音乐", "最近播放","下载管理","我的歌手"};

    private String [] numbers = {"(1369)","(100)","(5)","(3)"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        ListView musicAboutListView = (ListView) view.findViewById(R.id.listview);

        for(int i = 0; i < 80; i++){
            MusicAbout musicAbout = new MusicAbout();
            musicAbout.setImageId(imageIDs[i%4]);
            musicAbout.setTitle(titles[i%4]);
            musicAbout.setNumber(numbers[i%4]);
            musicAboutList.add(musicAbout);
        }

        MusicAdapter musicAdapter = new MusicAdapter(getContext(),musicAboutList);
        musicAboutListView.setAdapter(musicAdapter);

        musicAboutListView.setOnItemClickListener(this);
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v("onItemClick","您点击了第 "+position+" 行！");
        switch (position){
            case 0:
                Log.v("onItemClick","开始处理第0行数据！");
                LocalMusicFragment localMusicFragment = new LocalMusicFragment();
                FragmentManager fragmentManager = getFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(this);
                //  注意，必须加载localMusicFragment到一个布局文件的根节点的id上。
                fragmentTransaction.add(R.id.fullscreen,localMusicFragment, "localmusic");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }
    }
}

