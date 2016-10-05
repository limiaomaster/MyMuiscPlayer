package cn.edu.cdut.lm.mymuiscplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.activity.SearchActivity;
import cn.edu.cdut.lm.mymuiscplayer.dialogfragment.MoreInfoSingleSongFragment;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.service.PlayerService;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;

/**
 * Created by LimiaoMaster on 2016/9/27 20:15
 */

public class SearchAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>{
    private AppCompatActivity activity;
    private Context context;
    private int listPosition = -1;
    long lastClickTime = 0;
    final int MIN_CLICK_DELAY_TIME = 700;

    private List<Mp3Info> serachedList = new ArrayList<>();
    private List<Mp3Info> fullList = new ArrayList<>();


    public SearchAdapter( SearchActivity searchActivity, Context context) {
        activity = searchActivity;
        this.context = context;
        fullList = MediaUtil.getMp3ListFromMyDatabase(context,0);
    }

    public void getListByKeyword(List<Mp3Info> list){
        this.serachedList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewOfGeneralLines = LayoutInflater.from(context).inflate(R.layout.item_localmusic_singlesong,parent,false);
        return new GeneralLinesViewHolder(viewOfGeneralLines);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Mp3Info mp3Info = serachedList.get(position);
        ((GeneralLinesViewHolder)holder).title.setText(mp3Info.getTitle());
        ((GeneralLinesViewHolder) holder).artist.setText(mp3Info.getArtist());
        ((GeneralLinesViewHolder) holder).album.setText(mp3Info.getAlbum());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = Calendar.getInstance().getTimeInMillis();

                    if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                        if ((position) != listPosition) {
                            Log.e("SearchAdapter", "点击了不同的行 " + position);
                            int positionToPlay = findPositionInFullList(position);
                            Log.e("SearchAdapter", "在fullList中的位置是： " + positionToPlay);
                            playTheMusicOnClick(positionToPlay);
                            listPosition = position;
                        }
                    }
                lastClickTime = currentTime;
                }
        });
    }
    private int findPositionInFullList(int position){
        Log.e("SearchAdapter",serachedList.get(position).toString());
        int positionOfMatched = 0;
        for (Mp3Info mp3Info : fullList) {
            if (mp3Info.getId() == serachedList.get(position).getId()) {
                Log.e("SearchAdapter",serachedList.get(position).getId()+"");
                positionOfMatched = mp3Info.getPositionInList();
                break;
            }
        }
        return positionOfMatched;
    }

    private void playTheMusicOnClick(int position) {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.setClass(context, PlayerService.class);
        context.startService(intent);
        Log.e("Adaptor","点击了不同的行 "+position+"发送了请求播放的广播--------");
    }

    @Override
    public int getItemCount() {
        return serachedList.size();
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

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positionToPlay = findPositionInFullList(getAdapterPosition());
                    MoreInfoSingleSongFragment moreInformationFragment = MoreInfoSingleSongFragment.newInstance(fullList.get(positionToPlay),0);
                    moreInformationFragment.show(activity.getSupportFragmentManager(),"music");
                }
            });
        }
    }
}
