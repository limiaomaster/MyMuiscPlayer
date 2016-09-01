package cn.edu.cdut.lm.mymuiscplayer.utilities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio;

import java.util.ArrayList;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.module.ArtistInfo;

/**
 * Created by LimiaoMaster on 2016/8/31 20:15
 */

public class ArtistUtil {
    private static Uri uri = Audio.Artists.EXTERNAL_CONTENT_URI;
    private static String[] projectionOfSinger = new String[]{
            Audio.Artists.ARTIST,
            Audio.Artists.NUMBER_OF_TRACKS,
            Audio.Artists._ID
    };
    private static String defaultSortOrder = Audio.Artists.DEFAULT_SORT_ORDER;

    public static List<ArtistInfo> getArtistList(Context context) {
        List<ArtistInfo> artistInfoList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(uri, projectionOfSinger, null, null, defaultSortOrder);

        while (cursor.moveToNext()) {
            ArtistInfo artistInfo = new ArtistInfo();       //此行一定要写在while循环里面！！！
            String artistName = cursor.getString(cursor.getColumnIndex(Audio.Artists.ARTIST));
            int numberOfTracks = cursor.getInt(cursor.getColumnIndex(Audio.Artists.NUMBER_OF_TRACKS));
            Long artistId = cursor.getLong(cursor.getColumnIndex(Audio.Artists._ID));

            artistInfo.setArtistName(artistName);
            artistInfo.setNumberOfTracks(numberOfTracks);
            artistInfo.setArtistId(artistId);
            artistInfoList.add(artistInfo);
        }
        cursor.close();
        return artistInfoList;
    }
}
