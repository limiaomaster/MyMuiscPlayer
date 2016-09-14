package cn.edu.cdut.lm.mymuiscplayer.utilities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Files.FileColumns;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.module.FolderInfo;

/**
 * Created by LimiaoMaster on 2016/9/3 16:47
 */

public class FolderUtil {
    public static List<FolderInfo> getFolderInfoList(Context context){

        List<FolderInfo> folderInfoList = new ArrayList<>();
        Uri uri = MediaStore.Files.getContentUri("external");
        String string = "content://media/external/file";
        Uri uri1 = Uri.parse(string);

        String[] projectionOfFolder = new String[]{FileColumns.DATA};
        String order = "bucket_display_name_pinyin";


        String where = new String(
                FileColumns.MEDIA_TYPE +" = "+ FileColumns.MEDIA_TYPE_AUDIO + " and (" +
                        FileColumns.DATA + " like '%.mp3' or " +
                        FileColumns.DATA + " like'%.wma' or " +
                        FileColumns.DATA + " like '%.flac' or " +
                        FileColumns.DATA + " like '%.wav' or " +
                        FileColumns.DATA + " like '%.m4a' )" +
                        ") group by ( " + FileColumns.PARENT
        );

        Cursor cursor = context.getContentResolver().query(
                uri,
                projectionOfFolder,
                where,
                null,
                null
                );
        while(cursor.moveToNext()){
            FolderInfo folderInfo = new FolderInfo();
            int number = 0;
            String path = cursor.getString(cursor.getColumnIndex(FileColumns.DATA));
            String pathWithFolderName = path.substring(0,path.lastIndexOf("/"));
            String pathWithNoFolderName = pathWithFolderName.substring(0,pathWithFolderName.lastIndexOf("/")+1);
            String folderName = pathWithFolderName.substring(pathWithFolderName.lastIndexOf("/")+1);

            number = getNumberOfTracksFromFolder(pathWithFolderName);

            folderInfo.setFolderName(folderName);
            folderInfo.setNumberOfTracks(number);
            folderInfo.setFolderPath(pathWithNoFolderName);

            folderInfoList.add(folderInfo);
        }
        cursor.close();
        return folderInfoList;
    }


    private static int getNumberOfTracksFromFolder(String path) {
        int i = 0;
        File file = new File(path);
        File[] files = file.listFiles();
        for (int j = 0; j < files.length; j++) {
            String name = files[j].getName();
            if (  files[j].isFile() &&
                    name.endsWith(".mp3")||
                    name.endsWith(".wma")||
                    name.endsWith(".m4a")||
                    name.endsWith(".flac")||
                    name.endsWith(".wav")||
                    name.endsWith(".ape")){
                i++;
            }
        }
        return i;
    }
}
