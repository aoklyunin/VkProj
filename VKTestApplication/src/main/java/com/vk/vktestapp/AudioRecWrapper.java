package com.vk.vktestapp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Морковушка on 17.01.2016.
 */
public class AudioRecWrapper implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<AudioRec> lst;

    public AudioRecWrapper(ArrayList<AudioRec> items) {
        this.lst = items;
    }
    public ArrayList<AudioRec> getItemDetails() {
        return lst;
    }
}

