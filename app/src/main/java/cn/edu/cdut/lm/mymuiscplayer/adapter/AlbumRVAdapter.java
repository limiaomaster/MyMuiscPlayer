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

    public AlbumRVAdapter(FragmentActivity activity, Context context, List<AlbumInfo> albumInfoList) {
        fragmentActivity = activity;
        this.context = context;
        this.albumInfoList = albumInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_album_local_music, parent ,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder)holder).imageView.setImageResource(R.drawable.banshouren);
        ((MyViewHolder) holder).albumName.setText(albumInfoList.get(position).getAlbumName());
        ((MyViewHolder) holder).numberOfTracks.setText(albumInfoList.get(position).getNumberOfTracks()+"");
        ((MyViewHolder) holder).artistName.setText(albumInfoList.get(position).getArtist());
    }

    @Override
    public int getItemCount() {
        return albumInfoList.size();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView albumName;
        TextView numberOfTracks;
        TextView artistName;
        View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.iv_artist_albumFragment);
            albumName = (TextView) itemView.findViewById(R.id.tv_albumName_albumFragment);
            numberOfTracks = (TextView) itemView.findViewById(R.id.tv_number_of_track_albumFragment);
            artistName = (TextView) itemView.findViewById(R.id.tv_artistName_albumFragment);
        }
    }

}
