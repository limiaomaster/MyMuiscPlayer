package cn.edu.cdut.lm.mymuiscplayer.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by LimiaoMaster on 2016/10/4 23:25
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_MUSIC_TABLE =
            "create table mp3list_table ("
                    + "id integer primary key autoincrement, "
                    + "music_id integer, "
                    + "music_name text, "
                    + "music_name_py text, "
                    + "artist_name text, "
                    + "artist_name_py text, "
                    + "album_name text, "
                    + "album_name_py text, "
                    + "display_name text, "
                    + "album_id integer, "
                    + "duration integer, "
                    + "size integer, "
                    + "file_path text, "
                    + "date_modified integer, "
                    + "sampling_rate integer, "
                    + "bit_rate integer, "
                    + "quality text)";

    private Context mcontext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext = context;
    }

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MUSIC_TABLE);
        Toast.makeText(mcontext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists book");
        db.execSQL("drop table if exists mp3list_table");
        onCreate(db);
    }
}
