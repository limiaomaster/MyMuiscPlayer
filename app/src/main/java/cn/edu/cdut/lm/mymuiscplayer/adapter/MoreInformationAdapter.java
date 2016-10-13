package cn.edu.cdut.lm.mymuiscplayer.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.database.MyDatabaseHelper;
import cn.edu.cdut.lm.mymuiscplayer.dialogfragment.MoreInfoSingleSongFragment;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;

/**
 * Created by LimiaoMaster on 2016/8/26 11:03
 */
public class MoreInformationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final String UPDATE_SORT_ORDER = "cn.edu.cdut.lm.mymusicplayer.UPDATE_SORT_ORDER";

    private String TAG = "MoreInformationAdapter";
    private Context mContext;
    private Mp3Info mp3Info;
    private List<Map<String, Object>> list;
    private MoreInfoSingleSongFragment fragment;
    private boolean deleteFile ;

    public MoreInformationAdapter(Mp3Info mp3Info , List<Map<String, Object>> list) {
        this.mp3Info = mp3Info;
        this.list = list;
    }

    public MoreInformationAdapter(Context context, Mp3Info mp3Info, List<Map<String, Object>> list) {
        mContext = context;
        this.mp3Info = mp3Info;
        this.list = list;
    }

    public MoreInformationAdapter(Context context, Mp3Info mp3Info, List<Map<String, Object>> list, MoreInfoSingleSongFragment moreInfoSingleSongFragment) {
        mContext = context;
        this.mp3Info = mp3Info;
        this.list = list;
        fragment = moreInfoSingleSongFragment;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View firstView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_info_first_line_single_music,parent,false);
        View generalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_info_local_music,parent,false);
        if(viewType == 0) return new FirstViewHolder(firstView);
        return new GeneralViewHolder(generalView);
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position){
                        case 6:
                            new AlertDialog.Builder(mContext)
                                    .setTitle("确定将所选音乐从本地列表中移除吗？")
                                    .setMultiChoiceItems(new String[]{"同时删除本地文件"}, new boolean[]{false}, new DialogInterface.OnMultiChoiceClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                            if(which == 0 && isChecked ){
                                                deleteFile = true;
                                            }
                                        }
                                    })
                                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.e(TAG, "点击了删除键！！！");
                                            //从手机数据库中删除该项
                                            Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mp3Info.getMusicId());
                                            mContext.getContentResolver().delete(uri, null, null);
                                            //从自己的数据库中删除该项
                                            MyDatabaseHelper databaseHelper = new MyDatabaseHelper(mContext, "MusicDataBase.db",null,1);
                                            SQLiteDatabase database = databaseHelper.getWritableDatabase();
                                            database.delete("mp3list_table","music_id = ? ",new String[]{mp3Info.getMusicId()+""});
                                            //删除该文件
                                            if (deleteFile) {
                                                File file = new File(mp3Info.getUrl());
                                                Log.e(TAG, "要删除的文件是：" + file);
                                                MediaUtil.deleteFile(file);
                                            }
                                            Intent intent_change_order = new Intent();
                                            intent_change_order.setAction(UPDATE_SORT_ORDER);
                                            mContext.sendBroadcast(intent_change_order);

                                            Toast.makeText(mContext,"删除成功",Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(mContext,"点击了取消键",Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();
                            break;
                    }
                    fragment.dismiss();
                }
            });
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
            textView = (TextView) firstView.findViewById(R.id.tv_first_line_more_info_single);
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
