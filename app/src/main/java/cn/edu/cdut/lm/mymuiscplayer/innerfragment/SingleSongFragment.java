package cn.edu.cdut.lm.mymuiscplayer.innerfragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.adapter.LocalMusicAdapter;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;

/**
 * Created by LimiaoMaster on 2016/8/14 0014.
 */

public class SingleSongFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inner_fragment_single_music, container , false);
        ListView listView = (ListView) view.findViewById(R.id.listview_localmusic);

        List<Mp3Info> mp3InfoList = getMp3Infos();

        LocalMusicAdapter localMusicAdapter = new LocalMusicAdapter(getContext(),mp3InfoList);
        listView.setAdapter(localMusicAdapter);

        return view;
    }

    public List<Mp3Info> getMp3Infos() {

        Log.v("getMp3Infos()","开始搜索MP3文件");
        Cursor cursor = getContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
        for (int i = 0; i < cursor.getCount(); i++) {
            Mp3Info mp3Info = new Mp3Info();
            cursor.moveToNext();
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));   //音乐id
            String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));//音乐标题
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));//艺术家
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));//时长
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));  //文件大小
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));              //文件路径
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));//是否为音乐
            if (isMusic != 0) {     //只把音乐添加到集合当中
                mp3Info.setId(id);
                mp3Info.setTitle(title);
                mp3Info.setArtist(artist);
                mp3Info.setAlbum(album);
                mp3Info.setDuration(duration);
                mp3Info.setSize(size);
                mp3Info.setUrl(url);
                mp3Infos.add(mp3Info);
            }
        }
        Log.v("getMp3Infos()","总共的歌曲数目："+mp3Infos.size());
        return mp3Infos;
    }
}
