package cn.edu.cdut.lm.mymuiscplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.module.MusicAbout;

/**
 * Created by Administrator on 2016/8/12 0012.
 */

public class MusicAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<MusicAbout> list;
    private Context context;

    public MusicAdapter(Context context, List<MusicAbout> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView == null) {
            convertView =  LayoutInflater.from(context).inflate(R.layout.item_music,null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.number = (TextView) convertView.findViewById(R.id.number);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MusicAbout musicAbout = list.get(position);

        int imageId = musicAbout.getImageId();
        String title = musicAbout.getTitle();
        String number = musicAbout.getNumber();

        viewHolder.image.setImageResource(imageId);
        viewHolder.title.setText(title);
        viewHolder.number.setText(number);

        return convertView;
    }

    private static class ViewHolder {
        ImageView image;
        TextView title;
        TextView number;
    }
}
