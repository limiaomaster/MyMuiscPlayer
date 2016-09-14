package cn.edu.cdut.lm.mymuiscplayer.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.MoreInfoSingleSongFragment;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.service.PlayerService;


/**
 * Created by LimiaoMaster on 2016/8/24 18:37
 */
public class SingleSongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final String UPDATE_UI_ON_LIST_CLICK = "cn.edu.cdut.lm.mymusicplayer.UPDATE_UI_ON_LIST_CLICK";
    public static final String UPDATE_SPEAKER_LIST_POSITION = "cn.edu.cdut.lm.mymusicplayer.UPDATE_SPEAKER_LIST_POSITION";

    private  FragmentActivity fragmentActivity;
    private  Context context;
    private  List<Mp3Info> list;
    private final static int FIRST_LINE = 0;
    private final static int GENERAL_LINES=1;
    private final static int LAST_LINE = 2;

    long lastClickTime = 0;
    final int MIN_CLICK_DELAY_TIME = 700;
    private int listPosition = -1;

    public SingleSongAdapter(FragmentActivity activity, Context context, List<Mp3Info> list) {
        this.context = context;
        this.list = list;
        fragmentActivity = activity;
        UpdateSpeakerReceiver updateSpeakerReceiver = new UpdateSpeakerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATE_SPEAKER_LIST_POSITION);
        context.registerReceiver(updateSpeakerReceiver,intentFilter);
    }

    /**
     * 根据要渲染行的position 产生类型。
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if(position == 0 ) return FIRST_LINE;
        else if (position == list.size()+1) return LAST_LINE;
        else return GENERAL_LINES;
    }

    /**
     * 根据行的类型，产生ViewHolder。
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewOfFirstLine = LayoutInflater.from(context).inflate(R.layout.item_localmusic_singlesong_firstline,parent,false);
        View viewOfGeneralLines = LayoutInflater.from(context).inflate(R.layout.item_localmusic_singlesong,parent,false);
        View viewOfLastLine = LayoutInflater.from(context).inflate(R.layout.item_localmusic_lastline_empty,parent,false);
        if(viewType == FIRST_LINE) return new FirstLineViewHolder(viewOfFirstLine);
        else if (viewType == GENERAL_LINES) return new GeneralLinesViewHolder(viewOfGeneralLines);
        else if (viewType == LAST_LINE) return new LastLinesViewHolder(viewOfLastLine);
        else return null;
    }

    /**
     * 用ViewHolder配置要显示的内容。
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Mp3Info mp3Info ;
        if (holder instanceof FirstLineViewHolder) {
            ((FirstLineViewHolder) holder).textView.setText("(共" + list.size() + "首)");
        }
        if (position >= 1 && position <= list.size()) {
            mp3Info = list.get(position - 1);
            ((GeneralLinesViewHolder) holder).title.setText(mp3Info.getTitle());
            ((GeneralLinesViewHolder) holder).artist.setText(mp3Info.getArtist());
            ((GeneralLinesViewHolder) holder).album.setText(mp3Info.getAlbum());

            ((GeneralLinesViewHolder) holder).speaker.setVisibility(mp3Info.isSelected()?View.VISIBLE:View.GONE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long currentTime = Calendar.getInstance().getTimeInMillis();

                    if ((position-1) != listPosition) {
                        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                            Log.e("Adaptor","点击了不同的行 "+(position-1));
                            playTheMusicOnClick(position-1);
                            listPosition = position-1;
                        }
                    }
                    lastClickTime = currentTime;
                }
            });
        }
    }

    public void selectThisMusic(int position) {
        for(int i = 0; i<list.size(); i++){
            if (i == position){
                list.get(i).setSelected(true);
            }else list.get(i).setSelected(false);
        }
        notifyDataSetChanged();
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
        if (list.size() == 0 ) return 1;
        else return list.size()+2;
    }

    private class FirstLineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView ;
        ImageView imageView;
        public FirstLineViewHolder(View viewOfFirst) {
            super(viewOfFirst);
            textView = (TextView) viewOfFirst.findViewById(R.id.number_of_music);
            imageView = (ImageView) viewOfFirst.findViewById(R.id.multi_pick_to_do_someting);
            viewOfFirst.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("position", 0);
            intent.setClass(context, PlayerService.class);
            context.startService(intent);

            Intent broadCastIntent = new Intent();
            broadCastIntent.setAction(UPDATE_UI_ON_LIST_CLICK);
            broadCastIntent.putExtra("position",0);
            context.sendBroadcast(broadCastIntent);
        }
    }

    private class GeneralLinesViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView artist;
        TextView album;
        ImageView more;
        ImageView speaker;
        View view;
        public GeneralLinesViewHolder(View viewOfGeneralLines) {
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
                    Log.i("LastLinesViewHolder()",list.get(getAdapterPosition()-1)+"");
                    MoreInfoSingleSongFragment moreInformationFragment = MoreInfoSingleSongFragment.newInstance(list.get(getAdapterPosition()-1),0);
                    moreInformationFragment.show(fragmentActivity.getSupportFragmentManager(),"music");
                }
            });
        }
    }

    private class LastLinesViewHolder extends RecyclerView.ViewHolder {
        public LastLinesViewHolder(View viewOfGeneralLines) {
            super(viewOfGeneralLines);
        }
    }


    class UpdateSpeakerReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(UPDATE_SPEAKER_LIST_POSITION)){
                Log.e("Adapter","收到UPDATE_SPEAKER的广播");
                int position = intent.getIntExtra("position",0);
                selectThisMusic(position);
                listPosition = position;
            }
        }
    }
}
