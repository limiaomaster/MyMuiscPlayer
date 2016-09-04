package cn.edu.cdut.lm.mymuiscplayer.utilities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Files.FileColumns;

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

        String where = new String(
                FileColumns.MEDIA_TYPE +" = "+ FileColumns.MEDIA_TYPE_AUDIO + " and (" +
                        FileColumns.DATA + " like '%.mp3' or " +
                        FileColumns.DATA + " like'%.wma' or " +
                        FileColumns.DATA + " like '%.flac' or " +
                        FileColumns.DATA + " like '%.wav' )" +
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
            String data = cursor.getString(cursor.getColumnIndex(FileColumns.DATA));
            String path = data.substring(0,data.lastIndexOf("/"));
            String pathWithNoFolderName = path.substring(0,path.lastIndexOf("/")+1);
            String folderName = path.substring(path.lastIndexOf("/")+1);

            folderInfo.setFolderName(folderName);
            folderInfo.setFolderPath(pathWithNoFolderName);

            folderInfoList.add(folderInfo);
        }
        cursor.close();
        return folderInfoList;
    }
}
