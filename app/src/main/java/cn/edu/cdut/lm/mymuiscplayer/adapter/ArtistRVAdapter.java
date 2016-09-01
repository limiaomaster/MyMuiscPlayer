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
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.MoreInfoArtistFragment;
import cn.edu.cdut.lm.mymuiscplayer.module.ArtistInfo;

/**
 * Created by LimiaoMaster on 2016/8/31 21:33
 */

public class ArtistRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ArtistInfo> artistInfoList;
    private FragmentActivity fragmentActivity;

    public ArtistRVAdapter(FragmentActivity activity, Context context, List<ArtistInfo> artistInfoList) {
        this.context = context;
        this.artistInfoList = artistInfoList;
        fragmentActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_artist_local_music, parent , false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //((MyViewHolder)holder).imageView.setImageResource(R.drawable.banshouren);
        ((MyViewHolder)holder).artistName.setText(artistInfoList.get(position).getArtistName());
        ((MyViewHolder)holder).numberOfTrack.setText(artistInfoList.get(position).getNumberOfTracks()+"首");
    }

    @Override
    public int getItemCount() {
        return artistInfoList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView artistName;
        TextView numberOfTrack;
        ImageView more;
        View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.iv_artist_artistFragment);
            artistName = (TextView) itemView.findViewById(R.id.tv_artist_name_artistFragment);
            numberOfTrack = (TextView) itemView.findViewById(R.id.tv_number_of_track_artistFragment);
            more = (ImageView) itemView.findViewById(R.id.iv_moreinfo_singer);

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoreInfoArtistFragment moreInfoArtistFragment = MoreInfoArtistFragment.newInstance(artistInfoList.get(getAdapterPosition()));
                    moreInfoArtistFragment.show(fragmentActivity.getSupportFragmentManager(),"music");
                }
            });
        }

    }
}
