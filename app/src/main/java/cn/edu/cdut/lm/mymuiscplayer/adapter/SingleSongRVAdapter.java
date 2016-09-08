package cn.edu.cdut.lm.mymuiscplayer.adapter;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import cn.edu.cdut.lm.mymuiscplayer.utilities.NotificationUtil;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * Created by LimiaoMaster on 2016/8/24 18:37
 */
public class SingleSongRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final String UPDATE_UI_ON_LIST_CLICK = "cn.edu.cdut.lm.mymusicplayer.UPDATE_UI_ON_LIST_CLICK";
    //获取专辑封面的Uri
    private static final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");
    private static final String CLOSE_NOTIFICATION = "close_notification";

    private static final int CODE_CLOSE = 111;
    private static final int CODE_PAUSE = 222;
    private static final int NOTIFICATION_ID = 5709;
    private  FragmentActivity fragmentActivity;
    private  Context context;
    private  List<Mp3Info> list;
    private final static int FIRST_LINE = 0;
    private final static int GENERAL_LINES=1;
    private final static int LAST_LINE = 2;

    private NotificationManager manger;
    public static NotificationUtil notificationUtil;
    long lastClickTime = 0;
    final int MIN_CLICK_DELAY_TIME = 1000;


    public SingleSongRVAdapter(FragmentActivity activity, Context context, List<Mp3Info> list) {
        this.context = context;
        this.list = list;
        fragmentActivity = activity;
        manger = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        notificationUtil = new NotificationUtil(context);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Mp3Info mp3Info = null;
        if (holder instanceof FirstLineViewHolder) {
            ((FirstLineViewHolder) holder).textView.setText("(共" + list.size() + "首)");
        }
        if (position >= 1 && position <= list.size()) {
            mp3Info = list.get(position - 1);
            ((GeneralLinesViewHolder) holder).title.setText(mp3Info.getTitle());
            ((GeneralLinesViewHolder) holder).artist.setText(mp3Info.getArtist());
            ((GeneralLinesViewHolder) holder).album.setText(mp3Info.getAlbum());


        }
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

            viewOfGeneralLines.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long currentTime = Calendar.getInstance().getTimeInMillis();
                    if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                        //播放你点击的歌曲
                        playTheMusicOnClick();
                        //更新控制条
                        updateBottomControlBar();
                        //更新Notification
                        new Thread(){
                            @Override
                            public void run() {
                                notificationUtil.updateNotificationUI(getAdapterPosition()-1);
                            }
                        }.start();
                        lastClickTime = currentTime;
                    }
                }
            });

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("LastLinesViewHolder()",list.get(getAdapterPosition()-1)+"");
                    MoreInfoSingleSongFragment moreInformationFragment = MoreInfoSingleSongFragment.newInstance(list.get(getAdapterPosition()-1),0);
                    moreInformationFragment.show(fragmentActivity.getSupportFragmentManager(),"music");
                }
            });
        }

        private void playTheMusicOnClick() {
            Intent intent = new Intent();
            intent.putExtra("position", getAdapterPosition()-1);
            intent.setClass(context, PlayerService.class);
            context.startService(intent);
        }
        private void updateBottomControlBar() {
            Intent broadCastIntent = new Intent();
            broadCastIntent.setAction(UPDATE_UI_ON_LIST_CLICK);
            broadCastIntent.putExtra("position",getAdapterPosition()-1);
            context.sendBroadcast(broadCastIntent);
        }

    }

    private class LastLinesViewHolder extends RecyclerView.ViewHolder {
        public LastLinesViewHolder(View viewOfGeneralLines) {
            super(viewOfGeneralLines);
        }
    }


}
