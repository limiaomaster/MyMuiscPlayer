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
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.MoreInfoFragment;
import cn.edu.cdut.lm.mymuiscplayer.module.ArtistInfo;

/**
 * Created by LimiaoMaster on 2016/8/31 21:33
 */

public class ArtistRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ArtistInfo> artistInfoList;
    private FragmentActivity fragmentActivity;

    private final static int GENERAL_LINES=1;
    private final static int LAST_LINE = 2;

    private String ARTIST_FRAGMENT = "artist_fragment";

    public ArtistRVAdapter(FragmentActivity activity, Context context, List<ArtistInfo> artistInfoList) {
        this.context = context;
        this.artistInfoList = artistInfoList;
        fragmentActivity = activity;
    }



    @Override
    public int getItemViewType(int position) {
        if (position == artistInfoList.size()) return LAST_LINE;
        else return GENERAL_LINES;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View GeneralLinesView = LayoutInflater.from(context).inflate(R.layout.item_localmusic_artist_generallines, parent , false);
        View LastLineView = LayoutInflater.from(context).inflate(R.layout.item_localmusic_lastline_empty,parent,false);
        if (viewType == GENERAL_LINES) return new ArtistRVAdapter.GeneralLinesViewHolder(GeneralLinesView);
        else if (viewType == LAST_LINE) return new ArtistRVAdapter.LastLinesViewHolder(LastLineView);
        else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //((MyViewHolder)holder).imageView.setImageResource(R.drawable.banshouren);
        if(position>= artistInfoList.size()){

        } else {
            ((GeneralLinesViewHolder)holder).artistName.setText(artistInfoList.get(position).getArtistName());
            ((GeneralLinesViewHolder)holder).numberOfTrack.setText(artistInfoList.get(position).getNumberOfTracks()+"首");
        }
    }

    @Override
    public int getItemCount() {
        return artistInfoList.size()+1;
    }

    private class GeneralLinesViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView artistName;
        TextView numberOfTrack;
        ImageView more;
        View view;
        public GeneralLinesViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.iv_artist_artistFragment);
            artistName = (TextView) itemView.findViewById(R.id.tv_artist_name_artistFragment);
            numberOfTrack = (TextView) itemView.findViewById(R.id.tv_number_of_track_artistFragment);
            more = (ImageView) itemView.findViewById(R.id.iv_moreinfo_singer);

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoreInfoFragment moreInfoArtistFragment =
                            MoreInfoFragment.newInstance(artistInfoList.get(getAdapterPosition()),ARTIST_FRAGMENT);
                    moreInfoArtistFragment.show(fragmentActivity.getSupportFragmentManager(),"music");
                }
            });
        }
    }

    private class LastLinesViewHolder extends RecyclerView.ViewHolder {
        public LastLinesViewHolder(View viewOfGeneralLines) {
            super(viewOfGeneralLines);
        }
    }
}
