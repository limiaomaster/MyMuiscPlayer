package cn.edu.cdut.lm.mymuiscplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.MoreInformationFragment;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.service.PlayerService;

/**
 * Created by LimiaoMaster on 2016/8/24 18:37
 */
public class SingleSongRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final String UPDATE_UI_ON_LIST_CLICK = "cn.edu.cdut.lm.mymusicplayer.UPDATE_UI_ON_LIST_CLICK";

    private  FragmentActivity fragmentActivity;
    private  Context context;
    private  List<Mp3Info> list;
    final static int FIRST_ITEM = 0;
    final static int ITEM = 1;

    public SingleSongRVAdapter(Context context, List<Mp3Info> list) {
        this.context = context;
        this.list = list;
    }

    public SingleSongRVAdapter(FragmentActivity activity, Context context, List<Mp3Info> list) {
        this.context = context;
        this.list = list;
        fragmentActivity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("SingleSongRVAdapter()","getItemViewType()方法得到执行！ " +
                "position为"+position+"  返回值为："+(position == FIRST_ITEM ? FIRST_ITEM : ITEM));
        return position == FIRST_ITEM ? FIRST_ITEM : ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("SingleSongRVAdapter()","onCreateViewHolder()方法得到执行!  ");
        View viewOfFirst = LayoutInflater.from(context).inflate(R.layout.item_first_line_local_music,parent,false);
        View viewOfLast = LayoutInflater.from(context).inflate(R.layout.item_local_music,parent,false);
        if(viewType == FIRST_ITEM) return new FirstLineViewHolder(viewOfFirst);
        else return new LastLinesViewHolder(viewOfLast);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Mp3Info mp3Info = null;
        if (holder instanceof FirstLineViewHolder) {
            ((FirstLineViewHolder) holder).textView.setText("(共" + list.size() + "首)");
        }
        if (position >= 1 && position <= list.size()) {
            mp3Info = list.get(position - 1);
            ((LastLinesViewHolder) holder).title.setText(mp3Info.getTitle());
            ((LastLinesViewHolder) holder).artist.setText(mp3Info.getArtist());
            ((LastLinesViewHolder) holder).album.setText(mp3Info.getAlbum());

            ((LastLinesViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText( context,"您点击了第："+(position-1)+"行",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("position", position-1);
                    intent.setClass(context, PlayerService.class);
                    context.startService(intent);
                    Log.e("onItemClick","启动了PlayerService播放服务！");

                    Intent broadCastIntent = new Intent();
                    broadCastIntent.setAction(UPDATE_UI_ON_LIST_CLICK);
                    broadCastIntent.putExtra("position",position-1);
                    context.sendBroadcast(broadCastIntent);
                    Log.e("onItemClick","发送了UPDATE_UI的广播！");

                    /*((LastLinesViewHolder) holder).speaker.setVisibility(View.VISIBLE);*/
                }
            });

        } else if (position > list.size()){
            ((LastLinesViewHolder) holder).title.setText("");
            ((LastLinesViewHolder) holder).artist.setText("");
            ((LastLinesViewHolder) holder).album.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return list.size()+2;
    }

    private class FirstLineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView ;
        ImageView imageView;
        public FirstLineViewHolder(View viewOfFist) {
            super(viewOfFist);
            textView = (TextView) viewOfFist.findViewById(R.id.number_of_music);
            imageView = (ImageView) viewOfFist.findViewById(R.id.multi_pick_to_do_someting);
            viewOfFist.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class LastLinesViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView artist;
        TextView album;
        ImageView more;
        ImageView speaker;
        View view;
        public LastLinesViewHolder(View viewOfLast) {
            super(viewOfLast);
            view = viewOfLast;
            title = (TextView) viewOfLast.findViewById(R.id.title_localmusic);
            artist = (TextView) viewOfLast.findViewById(R.id.artist_localmusic);
            album = (TextView) viewOfLast.findViewById(R.id.album_localmusic);
            more = (ImageView) viewOfLast.findViewById(R.id.iv_more_localmusic);
            speaker = (ImageView) viewOfLast.findViewById(R.id.speaker);

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("LastLinesViewHolder()",list.get(getAdapterPosition()-1)+"");
                    MoreInformationFragment moreInformationFragment = MoreInformationFragment.newInstance(list.get(getAdapterPosition()-1),0);

                    moreInformationFragment.show(fragmentActivity.getSupportFragmentManager(),"music");
                }
            });
        }
    }
}
