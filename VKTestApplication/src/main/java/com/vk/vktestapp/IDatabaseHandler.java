package com.vk.vktestapp;

import java.util.List;

/**
 * Created by Морковушка on 17.01.2016.
 */
public interface IDatabaseHandler {
    public void addAudioRec(AudioRec audio);
    public AudioRec getAudioRec(int id);
    public List<AudioRec> getAllAudioRecs();
    public int getAudioRecCount();
    public int updateAudioRec(AudioRec audio);
    public void deleteAudioRec(AudioRec audio);
    public void deleteAll();
}