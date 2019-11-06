package ca.ethanelliott.spotthefly;

import android.content.ContentValues;
import android.content.Context;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;

public class SongDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SongDB";
    private static final String TABLE_NAME = "SONGS";
    private static final String COL_ID = "id";
    private static final String COL_UUID = "uuid";
    private static final String COL_SONG_NAME = "name";
    private static final String COL_DATA = "data";


    public SongDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_SONGS_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s BLOB)", TABLE_NAME, COL_ID, COL_UUID, COL_SONG_NAME, COL_DATA);
        sqLiteDatabase.execSQL(CREATE_SONGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Do Nothing
    }

    void addSong(Song song) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_UUID, song.getUuid());
        values.put(COL_SONG_NAME, song.getName());
        values.put(COL_DATA, song.getData());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    Song getSong(String uuid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_UUID + " = ?", new String[]{uuid});
        c.moveToFirst();
        if (c.isNull(c.getColumnIndex(COL_UUID))) {
            c.close();
            db.close();
            return null;
        }
        Song s = new Song(c.getInt(c.getColumnIndex(COL_ID)), c.getString(c.getColumnIndex(COL_UUID)), c.getString(c.getColumnIndex(COL_SONG_NAME)), c.getBlob(c.getColumnIndex(COL_DATA)));
        c.close();
        db.close();
        return s;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    ArrayList<Song> getAllSongs() {
        ArrayList<Song> songArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        CursorWindow cw = new CursorWindow("bigFile", 1_000_000_000);
        AbstractWindowedCursor ac = (AbstractWindowedCursor) c;
        ac.setWindow(cw);
        ac.moveToFirst();
        while (!(ac.isAfterLast())) {
            Song s = new Song(ac.getInt(ac.getColumnIndex(COL_ID)), ac.getString(c.getColumnIndex(COL_UUID)), ac.getString(ac.getColumnIndex(COL_SONG_NAME)), ac.getBlob(ac.getColumnIndex(COL_DATA)));
            songArrayList.add(s);
            ac.moveToNext();
        }
        ac.close();
        db.close();
        return songArrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    ArrayList<String> getAllSongsNames() {
        ArrayList<String> songArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + COL_SONG_NAME + " FROM " + TABLE_NAME, null);
        CursorWindow cw = new CursorWindow("bigFile", 1_000_000);
        AbstractWindowedCursor ac = (AbstractWindowedCursor) c;
        ac.setWindow(cw);
        ac.moveToFirst();
        while (!(ac.isAfterLast())) {
            songArrayList.add(ac.getString(ac.getColumnIndex(COL_SONG_NAME)));
            ac.moveToNext();
        }
        ac.close();
        db.close();
        return songArrayList;
    }

    void deleteSong(String uuid) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL_UUID + " = ? ", new String[]{ uuid });
    }
}
