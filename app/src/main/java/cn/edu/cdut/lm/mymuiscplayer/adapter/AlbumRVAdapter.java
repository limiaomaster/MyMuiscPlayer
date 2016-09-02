package cn.edu.cdut.lm.mymuiscplayer.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.module.AlbumInfo;

/**
 * Created by LimiaoMaster on 2016/9/1 15:35
 */
public class AlbumRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private FragmentActivity fragmentActivity;
    private List<AlbumInfo> albumInfoList;

    private final static int GENERAL_LINES=1;
    private final static int LAST_LINE = 2;

    public AlbumRVAdapter(FragmentActivity activity, Context context, List<AlbumInfo> albumInfoList) {
        fragmentActivity = activity;
        this.context = context;
        this.albumInfoList = albumInfoList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == albumInfoList.size()) return LAST_LINE;
        else return GENERAL_LINES;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View GeneralLinesView = LayoutInflater.from(context).inflate(R.layout.item_album_local_music, parent ,false);
        View LastLineView = LayoutInflater.from(context).inflate(R.layout.item_localmusic_lastline_empty,parent,false);
        if (viewType == GENERAL_LINES) return new GeneralLinesViewHolder(GeneralLinesView);
        else if (viewType == LAST_LINE) return new LastLinesViewHolder(LastLineView);
        else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //((MyViewHolder)holder).imageView.setImageResource(R.drawable.banshouren);
        if(position>=albumInfoList.size()){

        }else {
            ((GeneralLinesViewHolder) holder).albumName.setText(albumInfoList.get(position).getAlbumName());
            ((GeneralLinesViewHolder) holder).numberOfTracks.setText(albumInfoList.get(position).getNumberOfTracks()+"首");
            ((GeneralLinesViewHolder) holder).artistName.setText(albumInfoList.get(position).getArtist());
        }

    }

    @Override
    public int getItemCount() {
        return albumInfoList.size()+1;
    }


    private class GeneralLinesViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView albumName;
        TextView numberOfTracks;
        TextView artistName;
        View view;
        public GeneralLinesViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.iv_artist_albumFragment);
            albumName = (TextView) itemView.findViewById(R.id.tv_albumName_albumFragment);
            numberOfTracks = (TextView) itemView.findViewById(R.id.tv_number_of_track_albumFragment);
            artistName = (TextView) itemView.findViewById(R.id.tv_artistName_albumFragment);
        }
    }

    private class LastLinesViewHolder extends RecyclerView.ViewHolder {
        public LastLinesViewHolder(View viewOfGeneralLines) {
            super(viewOfGeneralLines);
        }
    }

}
