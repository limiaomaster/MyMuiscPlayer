package cn.edu.cdut.lm.mymuiscplayer.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cdut.lm.mymuiscplayer.R;

/**
 * Created by LimiaoMaster on 2016/8/26 12:29
 */

public class MusicMoreInformationUtil {

    static int[] image = new int[]{
            R.drawable.lay_icn_next, R.drawable.lay_icn_fav, R.drawable.lay_icn_cmt,
            R.drawable.lay_icn_share, R.drawable.lay_icn_upload, R.drawable.lay_icn_delete,
            R.drawable.lay_icn_artist, R.drawable.lay_icn_alb, R.drawable.lay_icn_quality,
            R.drawable.lay_icn_ring, R.drawable.lay_icn_document, R.drawable.lay_icn_restore,
            R.drawable.lay_icn_color_ring
    };

    static String[] text = new String[]{
            "下一首播放", "收藏到歌单", "评论",
            "分享", "上传到云盘", "删除",
            "歌手：", "专辑：", "音质升级",
            "设为铃声", "查看歌曲信息", "还原歌曲信息",
            "彩铃"
    };

    public static List<Map<String, Object>> getMoreInformation() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i = 0; i<13 ;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image",image[i]);
            map.put("text",text[i]);
            list.add(map);
        }
        return list;
    }
}
