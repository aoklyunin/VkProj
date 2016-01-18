package com.vk.vktestapp;

import android.content.ContentValues;

import java.io.Serializable;

/**
 * Created by Морковушка on 17.01.2016.
 */
public class AudioRec implements Serializable{

    public static final int columnSize = 11;

    private static final long serialVersionUID = 1L;

    private int id;
    private int audio_id;       // id аудиозаписи
    private int owner_id; // id владельца
    private String artist; //  артист
    private String title; // заголовок
    private int duration; // длительность аудиозаписи
    private String url;   // путь
    private int genre_id; // номер жанра
    private int isLoaded; // загружена аудиозапись на устройство или нет
    private String type; // рекомендованные, мои аудиозаписи или пустая аудиозапись
    private String savePath;

    AudioRec(int id, int audio_id,int owner_id, String artist,String title,int duration,String url,int genre_id,int isLoaded,String type,String savePath){
        this.id = id;
        this.audio_id = audio_id;
        this.owner_id = owner_id;
        this.artist = artist;
        this.title = title;
        this.duration = duration;
        this.url = url;
        this.genre_id = genre_id;
        this.isLoaded = isLoaded;
        this.type = type;
        this.savePath = savePath;
    }
    AudioRec(){
        new AudioRec(0,0,0,"","",0,"",0,0,"emppty","");
    }

    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public int getID(){return id;}
    public void setType(String type){ this.type = type;}
    public String getUrl(){ return url;}
    public ContentValues getDBValues(){
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_AUDIO_ID, audio_id);
        values.put(DBHelper.KEY_OWNER_ID, owner_id);
        values.put(DBHelper.KEY_ARTIST, artist);
        values.put(DBHelper.KEY_TITLE, title);
        values.put(DBHelper.KEY_DURATION, duration);
        values.put(DBHelper.KEY_URL, url);
        values.put(DBHelper.KEY_GENRE_ID, genre_id);
        values.put(DBHelper.KEY_IS_LOADED, isLoaded);
        values.put(DBHelper.KEY_TYPE, type);
        values.put(DBHelper.KEY_PATH_TO_SAVE, savePath);
        return values;
    }
    public String [] getValues(){
        String [] arr = new String[columnSize];
        arr[0] = String.valueOf(id);
        arr[1] = String.valueOf(audio_id);
        arr[2] = String.valueOf(owner_id);
        arr[3] = artist;
        arr[4] = title;
        arr[5] = String.valueOf(duration);
        arr[6] = url;
        arr[7] = String.valueOf(genre_id);
        arr[8] = String.valueOf(isLoaded);
        arr[9] = type;
        arr[10] = savePath;
        return arr;
    }

}
