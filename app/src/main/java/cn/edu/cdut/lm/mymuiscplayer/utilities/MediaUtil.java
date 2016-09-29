package cn.edu.cdut.lm.mymuiscplayer.utilities;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;

import static android.provider.MediaStore.Audio.AudioColumns.IS_MUSIC;
import static android.provider.MediaStore.Audio.Media.DEFAULT_SORT_ORDER;

/**
 * Created by LimiaoMaster on 2016/8/16 8:27
 */

public class MediaUtil {
    private static Uri uri = Media.EXTERNAL_CONTENT_URI;
    private static Uri uriSearch = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

    private static String[] projectionOfMusic = new String[] {
            Media._ID,  Media.TITLE,    Media.ARTIST,
            Media.ALBUM, Media.DISPLAY_NAME, Media.DATA,
            Media.ALBUM_ID, Media.DURATION, Media.SIZE
    };

    private static String[] projectionOfMusicBySearch = new String[]{
            Media._ID,Media.TITLE, Media.ARTIST, Media.ALBUM
    };

    private static String selectionOfMusic0= "is_music=1";
    private static String selectionOfMusic1= "is_music=1 AND title != ''";
    private static String selectionOfMusic2= IS_MUSIC;
    private static String selectionOfMusic3= Media.DATA+" like '%.mp3' ";   //1234首。
    private static String selectionOfMusic4= Media.DATA+" like '%.m4a' ";   //191首，通话录音。
    private static String selectionOfMusic5= Media.DATA+" like '%.flac' ";    //34首,可播放。
    private static String selectionOfMusic6= Media.DISPLAY_NAME+" like '%.wav' ";  //40首,可播放。
    private static String selectionOfMusic7= Media.DISPLAY_NAME+" like '%[a-z]%' ";  //匹配列表，查不到结果！。

    private static String order0 = Media.TITLE+" COLLATE LOCALIZED ASC";
    private static String order1 = Media.TITLE;
    private static String order2 = DEFAULT_SORT_ORDER; // "title_key"
    private static String order3 = "title_pinyin"; // "title_pinyin"

    private static String order00 = Media.TITLE+" COLLATE LOCALIZED ASC";
    private static String order11 = Media.TITLE;
    private static String order22 = Media.ALBUM+" COLLATE LOCALIZED ASC";
    private static String order33 = Media.ARTIST+" COLLATE LOCALIZED ASC";

    //获取专辑封面的Uri
    private static final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");
    private static int position;


    /**
     * 获取所有Mp3的信息组成的列表
     * @param context
     * @return
     */
    public static List<Mp3Info> getMp3List(Context context ,int checkPosition) {
        String searchOrder = null;
        switch (checkPosition){
            case 0:
                searchOrder = order00;
                break;
            case 1:
                searchOrder = order11;
                break;
            case 2:
                searchOrder = order22;
                break;
            case 3:
                searchOrder = order33;
                break;
        }
        Cursor cursor = context.getContentResolver().query(
                uri,
                projectionOfMusic,
                null,
                null,
                searchOrder
        );

        List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
        while (cursor.moveToNext()){
            Mp3Info mp3Info = new Mp3Info();

            long id = cursor.getLong(cursor.getColumnIndex(Media._ID));	//音乐id
            String title = cursor.getString((cursor.getColumnIndex(Media.TITLE))); // 音乐标题
            String artist = cursor.getString(cursor.getColumnIndex(Media.ARTIST)); // 艺术家
            String album = cursor.getString(cursor.getColumnIndex(Media.ALBUM));	//专辑
            String displayName = cursor.getString(cursor.getColumnIndex(Media.DISPLAY_NAME));
            long albumId = cursor.getInt(cursor.getColumnIndex(Media.ALBUM_ID));
            long duration = cursor.getLong(cursor.getColumnIndex(Media.DURATION)); // 时长
            long size = cursor.getLong(cursor.getColumnIndex(Media.SIZE)); // 文件大小
            String url = cursor.getString(cursor.getColumnIndex(Media.DATA)); // 文件路径

                mp3Info.setPositionInList(position++);
                mp3Info.setId(id);
                mp3Info.setTitle(title);
                mp3Info.setArtist(artist);
                mp3Info.setAlbum(album);
                mp3Info.setDisplayName(displayName);
                mp3Info.setAlbumId(albumId);
                mp3Info.setDuration(duration);
                mp3Info.setSize(size);
                mp3Info.setUrl(url);
                mp3Infos.add(mp3Info);
        }
        cursor.close();
        position = 0;
        return mp3Infos;
    }


    /**
     * 获取MP3信息的列表，通过搜索关键字。
     * @param context
     * @param keyword
     * @return
     */
    public static List<Mp3Info> searchMp3InfoByKeyword(Context context, String keyword){
        List<Mp3Info> list = new ArrayList<>();
        String[] selectoinArgs = new String[]{"%"+keyword+"%"};
        String selection =
                Media.TITLE+" like '%"+keyword+"%' or "+
                Media.ARTIST+" like '%"+keyword+"%' or "+
                Media.ALBUM+" like '%"+keyword+"%' ";

                Cursor cursor = context.getContentResolver().query(
                uri,
                projectionOfMusicBySearch,
                selection,
                null,
                null
        );
        while (cursor.moveToNext()){
            Mp3Info mp3Info = new Mp3Info();

            long id = cursor.getLong(cursor.getColumnIndex(Media._ID));	//音乐id
            Log.e("MediaUtil","id是："+id);
            String title = cursor.getString(cursor.getColumnIndex(Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndex(Media.ALBUM));	//专辑
            mp3Info.setId(id);
            mp3Info.setTitle(title);
            mp3Info.setArtist(artist);
            mp3Info.setAlbum(album);
            list.add(mp3Info);
        }
        cursor.close();
        return list;

    }




    private static String getCoverArtPath(long albumId, Context context) {
        Cursor albumCursor = context.getContentResolver().query(
                uriSearch,
                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + " = ?",
                new String[]{Long.toString(albumId)},
                null
        );
        boolean queryResult = albumCursor.moveToFirst();
        String result = null;
        if (queryResult) {
            result = albumCursor.getString(0);
        }
        albumCursor.close();
        return result;
    }

    public static Bitmap getAlbumArtByPath(long album_id,Context context){
        String path = getCoverArtPath(album_id,context);
        if (path!=null){
            return BitmapFactory.decodeFile(path);
        }else return BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.placeholder_disk), null, null);
    }










    /**
     * 获取默认专辑图片
     * @param context
     * @return
     */
    public static Bitmap getDefaultArtwork(Context context, boolean small) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        if(small){	//返回小图片
            return BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.placeholder_disk), null, opts);
        }
        return BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.placeholder_disk_380), null, opts);
    }

    /**
     * 从文件当中获取专辑封面位图
     * @param context
     * @param songid
     * @param albumid
     * @return
     */
    private static Bitmap getArtworkFromFile(Context context, long songid, long albumid){
        Bitmap bm = null;
        if(albumid < 0 && songid < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            FileDescriptor fd = null;
            if(albumid < 0){
                Uri uri = Uri.parse("content://media/external/audio/media/" + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if(pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            } else {
                Uri uri = ContentUris.withAppendedId(albumArtUri, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if(pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            }
            options.inSampleSize = 1;
            // 只进行大小判断
            options.inJustDecodeBounds = true;
            // 调用此方法得到options得到图片大小
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            // 我们的目标是在800pixel的画面上显示
            // 所以需要调用computeSampleSize得到图片缩放的比例
            options.inSampleSize = 100;
            // 我们得到了缩放的比例，现在开始正式读入Bitmap数据
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            //根据options参数，减少所需要的内存
            bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bm;
    }

    /**
     * 获取专辑封面位图对象
     * @param context
     * @param song_id
     * @param album_id
     * @param allowdefalut
     * @return
     */
    public static Bitmap getArtwork(Context context, long song_id, long album_id, boolean allowdefalut, boolean small){
        if(album_id < 0) {
            if(song_id < 0) {
                Bitmap bm = getArtworkFromFile(context, song_id, -1);
                if(bm != null) {
                    return bm;
                }
            }
            if(allowdefalut) {
                return getDefaultArtwork(context, small);
            }
            return null;
        }
        ContentResolver res = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(albumArtUri, album_id);
        if(uri != null) {
            InputStream in = null;
            try {
                in = res.openInputStream(uri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                //先制定原始大小
                options.inSampleSize = 1;
                //只进行大小判断
                options.inJustDecodeBounds = true;
                //调用此方法得到options得到图片的大小
                BitmapFactory.decodeStream(in, null, options);
                // 我们的目标是在你N pixel的画面上显示。 所以需要调用computeSampleSize得到图片缩放的比例 *
                // 这里的target为800是根据默认专辑图片大小决定的，800只是测试数字但是试验后发现完美的结合 *
                if(small){
                    options.inSampleSize = computeSampleSize(options, 40);
                } else{
                    options.inSampleSize = computeSampleSize(options, 600);
                }
                // 我们得到了缩放比例，现在开始正式读入Bitmap数据
                options.inJustDecodeBounds = false;
                options.inDither = false;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                in = res.openInputStream(uri);
                return BitmapFactory.decodeStream(in, null, options);
            } catch (FileNotFoundException e) {
                Bitmap bm = getArtworkFromFile(context, song_id, album_id);
                if(bm != null) {
                    if(bm.getConfig() == null) {
                        bm = bm.copy(Bitmap.Config.RGB_565, false);
                        if(bm == null && allowdefalut) {
                            return getDefaultArtwork(context, small);
                        }
                    }
                } else if(allowdefalut) {
                    bm = getDefaultArtwork(context, small);
                }
                return bm;
            } finally {
                try {
                    if(in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 对图片进行合适的缩放
     * @param options
     * @param target
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options, int target) {
        int w = options.outWidth;
        int h = options.outHeight;
        int candidateW = w / target;
        int candidateH = h / target;
        int candidate = Math.max(candidateW, candidateH);
        if(candidate == 0) {
            return 1;
        }
        if(candidate > 1) {
            if((w > target) && (w / candidate) < target) {
                candidate -= 1;
            }
        }
        if(candidate > 1) {
            if((h > target) && (h / candidate) < target) {
                candidate -= 1;
            }
        }
        return candidate;
    }

    /**
     * 往List集合中添加Map对象数据，每一个Map对象存放一首音乐的所有属性
     * @param mp3Infos
     * @return
     */
    public static List<HashMap<String, String>> getMusicMaps(List<Mp3Info> mp3Infos) {
        List<HashMap<String, String>> mp3list = new ArrayList<HashMap<String, String>>();
        for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext();) {
            Mp3Info mp3Info = (Mp3Info) iterator.next();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("title", mp3Info.getTitle());
            map.put("Artist", mp3Info.getArtist());
            map.put("album", mp3Info.getAlbum());
            map.put("displayName", mp3Info.getDisplayName());
            map.put("albumId", String.valueOf(mp3Info.getAlbumId()));
            map.put("duration", formatTime(mp3Info.getDuration()));
            map.put("size", String.valueOf(mp3Info.getSize()));
            map.put("url", mp3Info.getUrl());
            mp3list.add(map);
        }
        return mp3list;
    }

    /**
     * 格式化时间，将毫秒转换为分:秒格式
     * @param time
     * @return
     */
    public static String formatTime(long time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }
}