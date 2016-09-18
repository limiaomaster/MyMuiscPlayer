package cn.edu.cdut.lm.mymuiscplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.module.AlbumInfo;
import cn.edu.cdut.lm.mymuiscplayer.module.ArtistInfo;
import cn.edu.cdut.lm.mymuiscplayer.module.FolderInfo;

/**
 * Created by LimiaoMaster on 2016/9/1 10:24
 */
public class MoreInfoFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArtistInfo artistInfo;
    private AlbumInfo albumInfo;
    private FolderInfo folderInfo;
    private List<Map<String,Object>> list;

    private String ARTIST_FRAGMENT = "artist_fragment";
    private String ALBUM_FRAGMENT = "album_fragment";
    private String FOLDER_FRAGMENT = "folder_fragment";

    private String fragmentType;

    public MoreInfoFragmentAdapter(ArtistInfo artistInfo, List<Map<String, Object>> list, String type) {
        this.artistInfo = artistInfo;
        this.list = list;
        fragmentType = type;
    }

    public MoreInfoFragmentAdapter(AlbumInfo albumInfo, List<Map<String, Object>> list, String type) {
        this.albumInfo = albumInfo;
        this.list = list;
        fragmentType = type;
    }

    public MoreInfoFragmentAdapter(FolderInfo folderInfo, List<Map<String, Object>> list, String type) {
        this.folderInfo = folderInfo;
        this.list = list;
        fragmentType = type;
    }


    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View firstView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_info_first_line_artist,parent,false);
        View generalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_info_local_music,parent,false);
        if(viewType == 0) return new MoreInfoFragmentAdapter.FirstViewHolder(firstView);
        return new MoreInfoFragmentAdapter.GeneralViewHolder(generalView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FirstViewHolder) {
            if( fragmentType.equals(ARTIST_FRAGMENT)){
                ((FirstViewHolder) holder).textView.setText("歌手："+artistInfo.getArtistName());
            }else if (fragmentType.equals(ALBUM_FRAGMENT)){
                ((FirstViewHolder) holder).textView.setText("专辑："+albumInfo.getAlbumName());
            }else if (fragmentType.equals(FOLDER_FRAGMENT)){
                ((FirstViewHolder) holder).textView.setText("文件夹："+folderInfo.getFolderName());
            }
        } else {
            ((GeneralViewHolder)holder).imageView.setImageResource((int) list.get(position-1).get("image"));
            ((GeneralViewHolder) holder).textView.setText((String)list.get(position-1).get("text"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    private class FirstViewHolder extends RecyclerView.ViewHolder {
        TextView textView ;
        public FirstViewHolder(View firstView) {
            super(firstView);
            textView = (TextView) firstView.findViewById(R.id.tv_first_line_more_info_artist);
        }
    }

    private class GeneralViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView ;
        TextView textView;
        GeneralViewHolder(View generalView) {
            super(generalView);
            imageView = (ImageView) generalView.findViewById(R.id.iv_moreInfo);
            textView = (TextView) generalView.findViewById(R.id.tv_moreInfo);
        }
    }
}
