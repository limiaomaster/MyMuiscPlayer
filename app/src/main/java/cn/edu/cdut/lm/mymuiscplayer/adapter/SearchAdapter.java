package cn.edu.cdut.lm.mymuiscplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;

/**
 * Created by LimiaoMaster on 2016/9/27 20:15
 */

public class SearchAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>{
    Context context;
    private List<Mp3Info> list = new ArrayList<>();

    public SearchAdapter(Context context) {
        this.context = context;
    }

    public void getListByKeyword(List<Mp3Info> list){
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewOfGeneralLines = LayoutInflater.from(context).inflate(R.layout.item_localmusic_singlesong,parent,false);
        return new GeneralLinesViewHolder(viewOfGeneralLines);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Mp3Info mp3Info = list.get(position);
        ((GeneralLinesViewHolder)holder).title.setText(mp3Info.getTitle());
        ((GeneralLinesViewHolder) holder).artist.setText(mp3Info.getArtist());
        ((GeneralLinesViewHolder) holder).album.setText(mp3Info.getAlbum());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class GeneralLinesViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView artist;
        TextView album;
        ImageView more;
        ImageView speaker;
        View view;
        GeneralLinesViewHolder(View viewOfGeneralLines) {
            super(viewOfGeneralLines);
            view = viewOfGeneralLines;
            title = (TextView) viewOfGeneralLines.findViewById(R.id.title_localmusic);
            artist = (TextView) viewOfGeneralLines.findViewById(R.id.artist_localmusic);
            album = (TextView) viewOfGeneralLines.findViewById(R.id.album_localmusic);
            more = (ImageView) viewOfGeneralLines.findViewById(R.id.iv_more_localmusic);
            speaker = (ImageView) viewOfGeneralLines.findViewById(R.id.speaker);

            /*more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("LastLinesViewHolder()",list.get(getAdapterPosition()-1)+"");
                    MoreInfoSingleSongFragment moreInformationFragment = MoreInfoSingleSongFragment.newInstance(list.get(getAdapterPosition()-1),0);
                    moreInformationFragment.show(activity.getSupportFragmentManager(),"music");
                }
            });*/
        }
    }
}
