package cn.edu.cdut.lm.mymuiscplayer.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cdut.lm.mymuiscplayer.R;

/**
 * Created by LimiaoMaster on 2016/8/26 12:29
 */

public class MoreInfoUtil {

    private static int[] imageInSingleMusic = new int[]{
            R.drawable.lay_icn_next, R.drawable.lay_icn_fav, R.drawable.lay_icn_cmt,
            R.drawable.lay_icn_share, R.drawable.list_play_icn_cloud, R.drawable.lay_icn_delete,
            R.drawable.lay_icn_artist, R.drawable.lay_icn_alb, R.drawable.lay_icn_quality,
            R.drawable.lay_icn_ring, R.drawable.lay_icn_document, R.drawable.lay_icn_restore,
            R.drawable.lay_icn_color_ring
    };

    private static String[] textInSingleMusic = new String[]{
            "下一首播放", "收藏到歌单", "评论",
            "分享", "上传到云盘", "删除",
            "歌手：", "专辑：", "音质升级",
            "设为铃声", "查看歌曲信息", "还原歌曲信息",
            "彩铃"
    };

    private static int[] imageInArtist = new int[]{
            R.drawable.lay_icn_next, R.drawable.lay_icn_fav, R.drawable.list_play_icn_cloud,
            R.drawable.lay_icn_delete
    };
    private static String[] textInArtist = new String[]{
            "播放", "收藏到歌单", "上传到云盘", "删除"
    };

    private static int[] imageInNavigation = new int[]{
            R.drawable.topmenu_icn_msg, R.drawable.topmenu_icn_store, R.drawable.topmenu_icn_vip,
            R.drawable.topmenu_icn_free, R.drawable.topmenu_icn_identify, R.drawable.topmenu_icn_skin,
            R.drawable.topmenu_icn_night, R.drawable.topmenu_icn_time, R.drawable.topmenu_icn_clock,
            R.drawable.topmenu_icn_vehicle, R.drawable.topmenu_icn_cloud
    };
    private static String[] textInNavigation = new String[]{
            "我的消息", "积分商城", "会员中心",
            "在线听歌免流量", "听歌识曲", "主题换肤",
            "夜间模式", "停时停止播放", "音乐闹钟",
            "驾驶模式", "我的音乐云盘"
    };

    public static List<Map<String, Object>> getMoreInfoOnSingleMusic() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i = 0; i<13 ;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image",imageInSingleMusic[i]);
            map.put("text",textInSingleMusic[i]);
            list.add(map);
        }
        return list;
    }

    public static List<Map<String,Object>> getMoreInfoOnArtist(){
        List<Map<String,Object>> list = new ArrayList<>();
        for (int i = 0 ; i<4 ; i++){
            Map<String , Object> map = new HashMap<>();
            map.put("image",imageInArtist[i]);
            map.put("text",textInArtist[i]);
            list.add(map);
        }
        return list;
    }

    public static List<Map<String,Object>> getNaviInfo(){
        List<Map<String,Object>> list = new ArrayList<>();
        for (int i = 0 ; i< 11 ; i++){
            Map<String,Object> map = new HashMap<>();
            map.put("image",imageInNavigation[i]);
            map.put("text",textInNavigation[i]);
            list.add(map);
        }
        return list;
    }
}
