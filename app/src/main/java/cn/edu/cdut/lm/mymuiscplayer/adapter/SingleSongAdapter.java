package cn.edu.cdut.lm.mymuiscplayer.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
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
import cn.edu.cdut.lm.mymuiscplayer.activity.LocalMusicActivity;
import cn.edu.cdut.lm.mymuiscplayer.dialogfragment.MoreInfoSingleSongFragment;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.service.PlayerService;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by LimiaoMaster on 2016/8/24 18:37
 */
public class SingleSongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String UPDATE_SPEAKER_LIST_POSITION = "cn.edu.cdut.lm.mymusicplayer.UPDATE_SPEAKER_LIST_POSITION";
    private static final String UPDATE_SORT_ORDER = "cn.edu.cdut.lm.mymusicplayer.UPDATE_SORT_ORDER";
    public  int sortOrder = 0;

    private AppCompatActivity activity;
    private Context context;
    private List<Mp3Info> mp3List;
    private final static int FIRST_LINE = 0;
    private final static int GENERAL_LINES=1;
    private final static int LAST_LINE = 2;

    long lastClickTime = 0;
    final int MIN_CLICK_DELAY_TIME = 700;
    private int listPosition = -1;


    public SingleSongAdapter(LocalMusicActivity localMusicActivity, Context applicationContext) {
        activity = localMusicActivity;
        context = applicationContext;
        getUpdatedMp3InfoList();
    }
    private void getUpdatedMp3InfoList() {
        SharedPreferences pref = context.getSharedPreferences("data", MODE_PRIVATE);
        sortOrder = pref.getInt("sort_order_check_position" , 0);
        mp3List = MediaUtil.getMp3ListFromMyDatabase(context , sortOrder);
        notifyDataSetChanged();
    }

    /**
     * 根据要渲染行的position 产生类型。
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if(position == 0 ) return FIRST_LINE;
        else if (position == mp3List.size()+1) return LAST_LINE;
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
            ((FirstLineViewHolder) holder).textView.setText("(共" + mp3List.size() + "首)");
        }
        if (position >= 1 && position <= mp3List.size()) {
            mp3Info = mp3List.get(position - 1);
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
                            /*Log.e("Adapter",converterToFirstSpell(mp3Info.getTitle()));
                            Log.e("Adapter",converterToFirstSpell(mp3Info.getArtist()));
                            Log.e("Adapter",converterToFirstSpell(mp3Info.getAlbum()));*/
                           /* Log.e("Adapter",mp3Info.getTitle_pinyin());
                            Log.e("Adapter",mp3Info.getArtist_pinyin());
                            Log.e("Adapter",mp3Info.getAlbum_pinyin());*/

                            listPosition = position-1;
                        }
                    }
                    lastClickTime = currentTime;
                }
            });
        }
    }

    private void selectThisMusic(int position) {
        for(int i = 0; i<mp3List.size(); i++){
            if (i == position){
                mp3List.get(i).setSelected(true);
            }else mp3List.get(i).setSelected(false);
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
        if (mp3List.size() == 0 ) return 1;
        else return mp3List.size()+2;
    }

    private class FirstLineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView ;
        ImageView imageView;
        FirstLineViewHolder(View viewOfFirst) {
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

            /*Intent intent1 = new Intent();
            intent1.setClass(context, PlayingActivity.class);
            context.startActivity(intent1);*/

            /*Intent broadCastIntent = new Intent();
            broadCastIntent.setAction(UPDATE_UI_ON_LIST_CLICK);
            broadCastIntent.putExtra("position",0);
            context.sendBroadcast(broadCastIntent);*/
        }
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
                    Log.i("LastLinesViewHolder()",mp3List.get(getAdapterPosition()-1)+"");
                    MoreInfoSingleSongFragment moreInformationFragment = MoreInfoSingleSongFragment.newInstance(mp3List.get(getAdapterPosition()-1),0);
                    moreInformationFragment.show(activity.getSupportFragmentManager(),"music");
                }
            });
        }
    }

    private class LastLinesViewHolder extends RecyclerView.ViewHolder {
        LastLinesViewHolder(View viewOfGeneralLines) {
            super(viewOfGeneralLines);
        }
    }


    public class UpdateSpeakerReceiver extends BroadcastReceiver{
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

    public class UpdateSortOrderReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(UPDATE_SORT_ORDER)){
                getUpdatedMp3InfoList();
            }
        }
    }
}
