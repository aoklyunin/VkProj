package com.vk.vktestapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper implements IDatabaseHandler {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VK";
    public static final String TABLE_CONTACTS = "audioRecTable";
    public static final String KEY_ID = "id";
    public static final String KEY_AUDIO_ID = "audioId";
    public static final String KEY_OWNER_ID = "ownerId";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_URL = "url";
    public static final String KEY_GENRE_ID = "genreId";
    public static final String KEY_IS_LOADED = "isLoaded";
    public static final String KEY_TYPE = "type";
    public static final String KEY_PATH_TO_SAVE = "pathToSave";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_AUDIO_ID + " INTEGER,"
                + KEY_OWNER_ID + " INTEGER,"
                + KEY_ARTIST + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_DURATION + " INTEGER,"
                + KEY_URL + " TEXT,"
                + KEY_GENRE_ID + " INTEGER,"
                + KEY_IS_LOADED + " INTEGER,"
                + KEY_TYPE + " TEXT,"
                + KEY_PATH_TO_SAVE + " TEXT"+")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    @Override
    public void addAudioRec(AudioRec audio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = audio.getDBValues();
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    @Override
    public AudioRec getAudioRec(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {
                        KEY_ID, KEY_AUDIO_ID, KEY_OWNER_ID,
                        KEY_ARTIST, KEY_TITLE, KEY_DURATION,
                        KEY_URL, KEY_GENRE_ID, KEY_IS_LOADED,
                        KEY_TYPE,KEY_PATH_TO_SAVE}, KEY_ID + "=?",
                        new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        AudioRec audio = new AudioRec(cursor.getInt(0),
                cursor.getInt(1) ,
                cursor.getInt(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getInt(5),
                cursor.getString(6),
                cursor.getInt(7),
                cursor.getInt(8),
                cursor.getString(9),
                cursor.getString(10));
        return audio;
    }

    @Override
    public List<AudioRec> getAllAudioRecs() {
        List<AudioRec> audioList = new ArrayList<AudioRec>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                for( String s : cursor.getColumnNames())
                    Log.d("COLUMN CNT",s);
                AudioRec audio =  new AudioRec(cursor.getInt(0),
                                               cursor.getInt(1) ,
                                               cursor.getInt(2),
                                               cursor.getString(3),
                                               cursor.getString(4),
                                               cursor.getInt(5),
                                               cursor.getString(6),
                                               cursor.getInt(7),
                                               cursor.getInt(8),
                                               cursor.getString(9),
                                               cursor.getString(10) );
                audioList.add(audio);
            } while (cursor.moveToNext());
        }
        return audioList;
    }

    @Override
    public int updateAudioRec(AudioRec audio) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ARTIST, audio.getArtist());
        values.put(KEY_TITLE, audio.getTitle());

        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(audio.getID()) });
    }

    @Override
    public void deleteAudioRec(AudioRec audio) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?", new String[] { String.valueOf(audio.getID()) });
        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, null, null);
        db.close();
    }

    @Override
    public int getAudioRecCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }
}