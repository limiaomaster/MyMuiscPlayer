package cn.edu.cdut.lm.mymuiscplayer.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.MoreInfoFragment;
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

    private String ALBUM_FRAGMENT = "album_fragment";


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
        View GeneralLinesView = LayoutInflater.from(context).inflate(R.layout.item_localmusic_album_generallines, parent ,false);
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

            Uri uri = Uri.parse(albumInfoList.get(position).getArtPath()+"");
            Log.i("onBindViewHolder()",albumInfoList.get(position).getArtPath());
            Log.i("onBindViewHolder()",uri+"");

            ((GeneralLinesViewHolder) holder).draweeView.setImageURI(uri);
        }

    }

    @Override
    public int getItemCount() {
        return albumInfoList.size()+1;
    }


    private class GeneralLinesViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        SimpleDraweeView draweeView;
        TextView albumName;
        TextView numberOfTracks;
        TextView artistName;
        ImageView more;
        View view;
        public GeneralLinesViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.iv_albumArt_albumFragment);
            albumName = (TextView) itemView.findViewById(R.id.tv_albumName_albumFragment);
            numberOfTracks = (TextView) itemView.findViewById(R.id.tv_number_of_track_albumFragment);
            artistName = (TextView) itemView.findViewById(R.id.tv_artistName_albumFragment);
            more = (ImageView) itemView.findViewById(R.id.iv_more_albumFragment);
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoreInfoFragment moreInfoArtistFragment =
                            MoreInfoFragment.newInstance(albumInfoList.get(getAdapterPosition()),ALBUM_FRAGMENT);
                    moreInfoArtistFragment.show(fragmentActivity.getSupportFragmentManager(),"album");
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
