package cn.edu.cdut.lm.mymuiscplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;

/**
 * Created by LimiaoMaster on 2016/8/26 11:03
 */
public class MoreInformationRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Mp3Info mp3Info;
    private List<Map<String, Object>> list;

    public MoreInformationRVAdapter(Mp3Info mp3Info , List<Map<String, Object>> list) {
        this.mp3Info = mp3Info;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View firstView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_info_first_line_local_music,parent,false);
        View generalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_info_local_music,parent,false);
        if(viewType == 0) return new FirstViewHolder(firstView);
        return new GeneralViewHolder(generalView);
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof FirstViewHolder){
            ((FirstViewHolder) holder).textView.setText(mp3Info.getTitle());
        }else {
            ((GeneralViewHolder)holder).imageView.setImageResource((int)list.get(position-1).get("image"));
            ((GeneralViewHolder)holder).textView.setText((String)list.get(position-1).get("text"));
            switch (position){
                case 7:
                    Log.i("onBindViewHolder()","符合case7，，，，");
                     String artist = mp3Info.getArtist();
                    Log.i("onBindViewHolder()","歌手是："+artist);
                    ((GeneralViewHolder)holder).textView.setText("歌手："+artist);
                    break;
                case 8:
                    String album = mp3Info.getAlbum();
                    ((GeneralViewHolder) holder).textView.setText("专辑："+album);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return 14;
    }



    private class FirstViewHolder extends RecyclerView.ViewHolder {
        TextView textView ;
        public FirstViewHolder(View firstView) {
            super(firstView);
            textView = (TextView) firstView.findViewById(R.id.tv_first_line_more_infor);
        }
    }

    private class GeneralViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView ;
        TextView textView;
        public GeneralViewHolder(View generalView) {
            super(generalView);
            imageView = (ImageView) generalView.findViewById(R.id.iv_moreInfo);
            textView = (TextView) generalView.findViewById(R.id.tv_moreInfo);
        }
    }


}
