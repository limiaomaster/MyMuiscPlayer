package cn.edu.cdut.lm.mymuiscplayer.utilities;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio;

import java.util.ArrayList;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.module.AlbumInfo;

/**
 * Created by LimiaoMaster on 2016/9/1 15:55
 */

public class AlbumUtil {

    private static Uri uri  =  Audio.Albums.EXTERNAL_CONTENT_URI;
    // "content://media/external/audio/albums"
    private static Uri uri1  =  Audio.Media.EXTERNAL_CONTENT_URI;
    // "content://media/external/audio/media"
    private static String[] projectionOfAlbum = new String[]{
            Audio.Albums.ALBUM,
            Audio.Albums._ID,
            Audio.Albums.NUMBER_OF_SONGS,
            Audio.Albums.ALBUM_ART,
            Audio.Albums.ARTIST
    };

    private static String defaultSortOrder = Audio.Albums.DEFAULT_SORT_ORDER;

    public static List<AlbumInfo> getAlbumInfoList(Context context){

        List<AlbumInfo> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                uri,
                projectionOfAlbum,
                null,
                null,
                defaultSortOrder
                );
        while (cursor.moveToNext()){
            AlbumInfo albumInfo = new AlbumInfo();
            String albumName = cursor.getString(cursor.getColumnIndex(Audio.Albums.ALBUM));
            int albumId = cursor.getInt(cursor.getColumnIndex(Audio.Albums._ID));
            int numberOfTracks = cursor.getInt(cursor.getColumnIndex(Audio.Albums.NUMBER_OF_SONGS));
            String artPath = getAlbumArtUri(albumId).toString();
            String artist = cursor.getString(cursor.getColumnIndex(Audio.Albums.ARTIST));

            albumInfo.setAlbumName(albumName);
            albumInfo.setAlbumID(albumId);
            albumInfo.setNumberOfTracks(numberOfTracks);
            albumInfo.setArtPath(artPath);
            albumInfo.setArtist(artist);
            list.add(albumInfo);
        }
        cursor.close();
        return list;
    }


    public static Uri getAlbumArtUri(long albumId) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
    }

}
