package cn.edu.cdut.lm.mymuiscplayer.utilities;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.database.MyDatabaseHelper;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;

import static android.database.sqlite.SQLiteDatabase.OPEN_READONLY;
import static android.provider.MediaStore.Audio.AudioColumns.IS_MUSIC;
import static android.provider.MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
import static cn.edu.cdut.lm.mymuiscplayer.utilities.Pinyin4jUtil.getQuanPin;

/**
 * Created by LimiaoMaster on 2016/8/16 8:27
 */

public class MediaUtil {
    private static Uri uri = Media.EXTERNAL_CONTENT_URI;
    private static Uri uriSearch = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

    private static String[] projectionOfMusic = new String[] {
            Media._ID,  Media.TITLE,    Media.ARTIST,
            Media.ALBUM, Media.DISPLAY_NAME, Media.DATA,
            Media.ALBUM_ID, Media.DURATION, Media.SIZE,
            "date_modified", "sampling_rate"
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
    private static String order01 = Media.TITLE;
    private static String order02 = Media.ALBUM+" COLLATE LOCALIZED ASC";
    private static String order03 = Media.ARTIST+" COLLATE LOCALIZED ASC";

    //获取专辑封面的Uri
    private static final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");
    private static int positionInThisList;






    /**
     * 获取所有Mp3的信息组成的列表
     * @param context 上下文
     * @param orderType 排序方式
     * @return 列表
     */
    public static List<Mp3Info> getMp3ListFromMyDatabase(Context context , int orderType){
        String order10 = "music_name_py COLLATE LOCALIZED ASC";
        String order11 = "date_modified desc";
        String order12 = "album_name_py COLLATE LOCALIZED ASC";
        String order13 = "artist_name_py COLLATE LOCALIZED ASC";

        String customOrder = null;
        switch (orderType){
            case 0:
                customOrder = order10;
                break;
            case 1:
                customOrder = order11;
                break;
            case 2:
                customOrder = order12;
                break;
            case 3:
                customOrder = order13;
                break;
        }
        List<Mp3Info> mp3InfoList = new ArrayList<>();
        SQLiteDatabase database = SQLiteDatabase.openDatabase(context.getDatabasePath("MusicDataBase.db").toString(),null,OPEN_READONLY);
        Cursor cursor = database.query("mp3list_table",null,null,null,null,null,customOrder);
        while (cursor.moveToNext()){
            Mp3Info mp3Info = new Mp3Info();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int music_id = cursor.getInt(cursor.getColumnIndex("music_id"));
            String music_name = cursor.getString(cursor.getColumnIndex("music_name"));
            String music_name_py = cursor.getString(cursor.getColumnIndex("music_name_py"));

            String artist_name = cursor.getString(cursor.getColumnIndex("artist_name"));
            String artist_name_py = cursor.getString(cursor.getColumnIndex("artist_name_py"));

            String album_name = cursor.getString(cursor.getColumnIndex("album_name"));
            String album_name_py = cursor.getString(cursor.getColumnIndex("album_name_py"));
            String display_name = cursor.getString(cursor.getColumnIndex("display_name"));
            int album_id = cursor.getInt(cursor.getColumnIndex("album_id"));
            int duration = cursor.getInt(cursor.getColumnIndex("duration"));
            int size = cursor.getInt(cursor.getColumnIndex("size"));
            String file_path = cursor.getString(cursor.getColumnIndex("file_path"));
            int date_modified = cursor.getInt(cursor.getColumnIndex("date_modified"));

            int sampling_rate = cursor.getInt(cursor.getColumnIndex("sampling_rate"));
            int bit_rate = cursor.getInt(cursor.getColumnIndex("bit_rate"));
            String quality = cursor.getString(cursor.getColumnIndex("quality"));

            mp3Info.setPositionInThisList(positionInThisList++);
            mp3Info.setId(id);
            mp3Info.setMusicId(music_id);
            mp3Info.setTitle(music_name);
            mp3Info.setTitle_pinyin(music_name_py);
            mp3Info.setArtist(artist_name);
            mp3Info.setArtist_pinyin(artist_name_py);
            mp3Info.setAlbum(album_name);
            mp3Info.setAlbum_pinyin(album_name_py);
            mp3Info.setDisplayName(display_name);
            mp3Info.setAlbumId(album_id);
            mp3Info.setDuration(duration);
            mp3Info.setSize(size);
            mp3Info.setUrl(file_path);
            mp3Info.setDateModified(date_modified);
            mp3Info.setSampling_rate(sampling_rate);
            mp3Info.setBit_rate(bit_rate);
            mp3Info.setQuality(quality);

            mp3InfoList.add(mp3Info);
        }
        cursor.close();
        database.close();
        positionInThisList = 0;
        return mp3InfoList;
    }



    public static void createMyDatabase(Context context) {
        File databaseFile = context.getDatabasePath("MusicDataBase.db");
        deleteFile(databaseFile);
        MyDatabaseHelper databaseHelper = new MyDatabaseHelper(context, "MusicDataBase.db", null, 1);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor = context.getContentResolver().query(
                uri,
                projectionOfMusic,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(Media._ID));    //音乐id
            String title = cursor.getString((cursor.getColumnIndex(Media.TITLE))); // 音乐标题
            String title_py = getQuanPin(title);
            String artist = cursor.getString(cursor.getColumnIndex(Media.ARTIST)); // 艺术家
            String artist_py = getQuanPin(artist);
            String album = cursor.getString(cursor.getColumnIndex(Media.ALBUM));    //专辑
            String album_py = getQuanPin(album);
            String displayName = cursor.getString(cursor.getColumnIndex(Media.DISPLAY_NAME));
            int albumId = cursor.getInt(cursor.getColumnIndex(Media.ALBUM_ID));
            int duration = cursor.getInt(cursor.getColumnIndex(Media.DURATION)); // 时长
            int size = cursor.getInt(cursor.getColumnIndex(Media.SIZE)); // 文件大小
            String url = cursor.getString(cursor.getColumnIndex(Media.DATA)); // 文件路径
            int date_modified = cursor.getInt(cursor.getColumnIndex("date_modified"));    //修改日期
            int sampling_rate = cursor.getInt(cursor.getColumnIndex("sampling_rate"));    //采样率
            int bit_rate = 0;
            if(duration != 0){
                bit_rate =  (size*8)/duration;
            }

            String quality = "low";
            if(sampling_rate<44100){
                quality = "low";
            }else if (sampling_rate >= 44100 && sampling_rate <= 48000){
                if (bit_rate >= 320){
                    quality = "high";
                }else quality = "low";
            }else if( sampling_rate > 48000 ){
                if (bit_rate >= 640){
                    quality = "super";
                }else quality = "high";
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put("music_id", id);
            contentValues.put("music_name", title);
            contentValues.put("music_name_py", title_py);

            contentValues.put("artist_name", artist);
            contentValues.put("artist_name_py", artist_py);

            contentValues.put("album_name", album);
            contentValues.put("album_name_py", album_py);

            contentValues.put("display_name", displayName);
            contentValues.put("album_id", albumId);
            contentValues.put("duration", duration);
            contentValues.put("size", size);
            contentValues.put("file_path", url);
            contentValues.put("date_modified",date_modified);
            contentValues.put("sampling_rate",sampling_rate);
            contentValues.put("bit_rate",bit_rate);
            contentValues.put("quality",quality);

            database.insert("mp3list_table", null, contentValues);
        }
        cursor.close();
        //database.close();
    }

    private static void deleteFile(File file) {
        if (file.exists()) {        // 判断文件是否存在
            if (file.isFile()) {    // 判断是否是文件
                file.delete();      // delete()方法 你应该知道 是删除的意思;
            }
        }
    }

    /**
     * 获取MP3信息的列表，通过搜索关键字。
     * @param context 上下文
     * @param keyword 关键字
     * @return 列表
     */
    public static List<Mp3Info> getSearchedMp3ListFromMyDatabase(Context context, String keyword){
        List<Mp3Info> list = new ArrayList<>();
        String selection =
                "music_name like '%"+keyword+"%' or "+
                        "artist_name like '%"+keyword+"%' or "+
                        "album_name like '%"+keyword+"%' ";

        SQLiteDatabase database = SQLiteDatabase.openDatabase(context.getDatabasePath("MusicDataBase.db").toString(),null,OPEN_READONLY);
        Cursor cursor = database.query("mp3list_table",null,selection,null,null,null,null);

        while (cursor.moveToNext()){
            Mp3Info mp3Info = new Mp3Info();

            int music_id = cursor.getInt(cursor.getColumnIndex("music_id"));
            String music_name = cursor.getString(cursor.getColumnIndex("music_name"));
            String music_name_py = cursor.getString(cursor.getColumnIndex("music_name_py"));

            String artist_name = cursor.getString(cursor.getColumnIndex("artist_name"));
            String artist_name_py = cursor.getString(cursor.getColumnIndex("artist_name_py"));

            String album_name = cursor.getString(cursor.getColumnIndex("album_name"));
            String album_name_py = cursor.getString(cursor.getColumnIndex("album_name_py"));
            String display_name = cursor.getString(cursor.getColumnIndex("display_name"));
            int album_id = cursor.getInt(cursor.getColumnIndex("album_id"));
            int duration = cursor.getInt(cursor.getColumnIndex("duration"));
            int size = cursor.getInt(cursor.getColumnIndex("size"));
            String file_path = cursor.getString(cursor.getColumnIndex("file_path"));
            int date_modified = cursor.getInt(cursor.getColumnIndex("date_modified"));

            int sampling_rate = cursor.getInt(cursor.getColumnIndex("sampling_rate"));
            int bit_rate = cursor.getInt(cursor.getColumnIndex("bit_rate"));
            String quality = cursor.getString(cursor.getColumnIndex("quality"));

            mp3Info.setMusicId(music_id);
            mp3Info.setTitle(music_name);
            mp3Info.setTitle_pinyin(music_name_py);
            mp3Info.setArtist(artist_name);
            mp3Info.setArtist_pinyin(artist_name_py);
            mp3Info.setAlbum(album_name);
            mp3Info.setAlbum_pinyin(album_name_py);
            mp3Info.setDisplayName(display_name);
            mp3Info.setAlbumId(album_id);
            mp3Info.setDuration(duration);
            mp3Info.setSize(size);
            mp3Info.setUrl(file_path);
            mp3Info.setDateModified(date_modified);
            mp3Info.setSampling_rate(sampling_rate);
            mp3Info.setBit_rate(bit_rate);
            mp3Info.setQuality(quality);

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