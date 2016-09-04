package cn.edu.cdut.lm.mymuiscplayer.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.MoreInfoFragment;
import cn.edu.cdut.lm.mymuiscplayer.module.FolderInfo;

/**
 * Created by LimiaoMaster on 2016/9/3 16:07
 */
public class FolderRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private FragmentActivity fragmentActivity;
    private Context context;
    private List<FolderInfo> folderInfoList;
    private final static int GENERAL_LINES=1;
    private final static int LAST_LINE = 2;
    private final String NETEASE_EXTERNAL_FOLDER = "/storage/extSdCard/netease/cloudmusic/";
    private final String NETEASE_INTERNAL_FOLDER = "/storage/emulated/0/netease/cloudmusic/";
    private String FOLDER_FRAGMENT = "folder_fragment";


    public FolderRVAdapter(FragmentActivity activity, Context context, List<FolderInfo> folderInfoList) {
        fragmentActivity = activity;
        this.context = context;
        this.folderInfoList = folderInfoList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= folderInfoList.size()) return LAST_LINE;
        else return GENERAL_LINES;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewOfGeneralLines = LayoutInflater.from(context).inflate(R.layout.item_localmusic_folder,parent,false);
        View viewOfLastLine = LayoutInflater.from(context).inflate(R.layout.item_localmusic_lastline_empty,parent,false);
        if(viewType == LAST_LINE) return new LastLineViewHolder(viewOfLastLine);
        else if (viewType == GENERAL_LINES) return new GeneralLinesViewHolder(viewOfGeneralLines);
        else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position >= folderInfoList.size()){

        }else {
            String folderName = folderInfoList.get(position).getFolderName();
            String folderPath = folderInfoList.get(position).getFolderPath();
            ((GeneralLinesViewHolder)holder).folderName.setText(folderName);
            ((GeneralLinesViewHolder) holder).path.setText(folderPath);

             if (folderPath.equals(NETEASE_EXTERNAL_FOLDER)||folderPath.equals(NETEASE_INTERNAL_FOLDER)){
                ((GeneralLinesViewHolder)holder).folderName.append(" (网易云音乐)");
            }

            Log.i("onBindViewHolder()",folderInfoList.get(position).getFolderName());
            Log.i("onBindViewHolder()",folderInfoList.get(position).getFolderPath());
        }
    }

    @Override
    public int getItemCount() {
        return folderInfoList.size()+1;
    }

    private class GeneralLinesViewHolder extends RecyclerView.ViewHolder {
        TextView folderName;
        TextView path;
        ImageView more;
        View view;
        public GeneralLinesViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            folderName = (TextView) itemView.findViewById(R.id.tv_folder_name);
            //要在此设置这些属性，在xml布局中设置的不管用。
            folderName.setSingleLine(true);
            folderName.setEllipsize(TextUtils.TruncateAt.END);
            //folderName.setSelected(true);
            path = (TextView) itemView.findViewById(R.id.tv_path_folderFragment);
            more = (ImageView) itemView.findViewById(R.id.iv_more_folderFragment);
            more.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    MoreInfoFragment moreInfoArtistFragment = MoreInfoFragment.newInstance(folderInfoList.get(getAdapterPosition()),FOLDER_FRAGMENT);
                    moreInfoArtistFragment.show(fragmentActivity.getSupportFragmentManager(),"album");
                }
            });
        }
    }

    private class LastLineViewHolder extends RecyclerView.ViewHolder{

        public LastLineViewHolder(View itemView) {
            super(itemView);
        }
    }
}
