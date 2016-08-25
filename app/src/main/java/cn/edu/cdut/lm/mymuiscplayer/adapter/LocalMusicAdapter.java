package cn.edu.cdut.lm.mymuiscplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;

/**
 * Created by LimiaoMaster on 2016/8/15 0015 下午 12:14
 */

public class LocalMusicAdapter extends BaseAdapter {

    private Context context;
    private List<Mp3Info> mp3InfoList;


    public LocalMusicAdapter(Context context , List<Mp3Info> mp3InfoList) {
        this.context = context;
        this.mp3InfoList = mp3InfoList;
    }

    @Override
    public int getCount() {
        return mp3InfoList.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return mp3InfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView == null) {
            convertView =  LayoutInflater.from(context).inflate(R.layout.item_local_music,null);
            viewHolder = new ViewHolder();

            viewHolder.title = (TextView) convertView.findViewById(R.id.title_localmusic);
            viewHolder.artist = (TextView) convertView.findViewById(R.id.artist_localmusic);
            viewHolder.album = (TextView) convertView.findViewById(R.id.album_localmusic);
            viewHolder.more = (ImageView) convertView.findViewById(R.id.iv_more_localmusic);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Mp3Info mp3Info = mp3InfoList.get(position);
        if (position < mp3InfoList.size()){
            String title = mp3Info.getTitle();
            String artist = mp3Info.getArtist();
            String album = mp3Info.getAlbum();

            viewHolder.title.setText(title);
            viewHolder.artist.setText(artist);
            viewHolder.album.setText(album);
        } else {
            viewHolder.title.setText("");
            viewHolder.artist.setText("");
            viewHolder.album.setText("");
        }
        addButtomClickAction(viewHolder.more,mp3Info,position);
        return convertView;
    }


    private void addButtomClickAction(View view, final Mp3Info mp3Info, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mp3Info.getTitle();

                Toast.makeText(context,"您点击的歌曲名叫 "+title,Toast.LENGTH_SHORT).show();
            }
        });
    }


    private static class ViewHolder {
        TextView title;
        TextView artist;
        TextView album;
        ImageView more;
    }
}
