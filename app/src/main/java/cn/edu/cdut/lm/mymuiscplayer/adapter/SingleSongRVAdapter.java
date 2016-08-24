package cn.edu.cdut.lm.mymuiscplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;

/**
 * Created by LimiaoMaster on 2016/8/24 18:37
 */
public class SingleSongRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final Context context;
    private final List<Mp3Info> list;
    final static int FIRST_ITEM = 0;
    final static int ITEM = 1;

    public SingleSongRVAdapter(Context context, List<Mp3Info> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("SingleSongRVAdapter()","getItemViewType()方法得到执行！ " +
                "position为"+position+"  返回值为："+(position == FIRST_ITEM ? FIRST_ITEM : ITEM));
        return position == FIRST_ITEM ? FIRST_ITEM : ITEM;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("SingleSongRVAdapter()","onCreateViewHolder()方法得到执行!  ");
        View viewOfFist = LayoutInflater.from(context).inflate(R.layout.item_first_line_local_music,parent,false);
        View viewOfLast = LayoutInflater.from(context).inflate(R.layout.item_local_music,parent,false);
        if(viewType == FIRST_ITEM) return new FirstLineViewHolder(viewOfFist);
        else return new LastLinesViewHolder(viewOfLast);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Mp3Info mp3Info = null;
        if(position>=1){
            mp3Info = list.get(position - 1);
        }
        if(holder instanceof FirstLineViewHolder){
            ((FirstLineViewHolder) holder).textView.setText("(共"+list.size()+"首)");
        } else if (holder instanceof  LastLinesViewHolder){
            ((LastLinesViewHolder) holder).title.setText(mp3Info.getTitle());
            ((LastLinesViewHolder) holder).artist.setText(mp3Info.getArtist());
            ((LastLinesViewHolder) holder).album.setText(mp3Info.getAlbum());
        }
    }

    /*@Override
    public void onBindViewHolder(SSRVHolder holder, int position) {
        Mp3Info mp3Info = list.get(position);
        holder.title.setText(mp3Info.getTitle());
        holder.artist.setText(mp3Info.getArtist());
        holder.album.setText(mp3Info.getAlbum());
    }*/

    @Override
    public int getItemCount() {
        return list.size();
    }


    private class FirstLineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView ;
        ImageView imageView;
        public FirstLineViewHolder(View viewOfFist) {
            super(viewOfFist);
            textView = (TextView) viewOfFist.findViewById(R.id.number_of_music);
            imageView = (ImageView) viewOfFist.findViewById(R.id.multi_pick_to_do_someting);
            viewOfFist.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class LastLinesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView artist;
        TextView album;
        public LastLinesViewHolder(View viewOfLast) {
            super(viewOfLast);
            title = (TextView) viewOfLast.findViewById(R.id.title_localmusic);
            artist = (TextView) viewOfLast.findViewById(R.id.artist_localmusic);
            album = (TextView) viewOfLast.findViewById(R.id.album_localmusic);
            viewOfLast.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
