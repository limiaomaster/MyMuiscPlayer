package cn.edu.cdut.lm.mymuiscplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MoreInfoUtil;

/**
 * Created by LimiaoMaster on 2016/9/18 11:09
 */
public class NavigationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int HEADER_POSITION = 0;
    private static final int TAIL_POSITION = 1;
    private static final int CONTENT_POSITION = 2;
    private List<Map<String,Object>> list;
    private Context context;
    public NavigationAdapter(Context context) {
        list = MoreInfoUtil.getNaviInfo();
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return HEADER_POSITION;
        }else if (position >= list.size()+1){
            return TAIL_POSITION;
        }else return CONTENT_POSITION;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view_header = LayoutInflater.from(context).inflate(R.layout.item_navi_header,parent,false);
        View view_content = LayoutInflater.from(context).inflate(R.layout.item_navi_content,parent,false);
        if (viewType == HEADER_POSITION) return new HeaderViewHolder(view_header);
        else if (viewType ==CONTENT_POSITION)return  new ContentViewHolder(view_content);
        else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ContentViewHolder) {
            ((ContentViewHolder)holder).imageView.setImageResource((Integer) list.get(position-1).get("image"));
            ((ContentViewHolder) holder).textView.setText((CharSequence) list.get(position-1).get("text"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder{
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
    private class ContentViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public ContentViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_navi_item);
            textView = (TextView) itemView.findViewById(R.id.tv_navi_item);
        }
    }
}
