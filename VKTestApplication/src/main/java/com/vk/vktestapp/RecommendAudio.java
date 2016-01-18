package com.vk.vktestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class RecommendAudio extends AppCompatActivity {
    ArrayList<AudioRec> audioArr;

    String getUrlByTitle(String title){
        for (AudioRec audio :audioArr)
             if( audio.getTitle()+" - "+audio.getArtist()==title)
                 return audio.getUrl();
        return "не найдено";
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_audio);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        DBHelper db = new DBHelper(this);
        List<AudioRec> audios = db.getAllAudioRecs();
        int i = 0;
        for (AudioRec audio : audios) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                                               TableRow.LayoutParams.MATCH_PARENT));
            int j = 0;
            int [] widthArr  = {70,200,200,400,400,140,400,70,70,300,240};
            for (String val : audio.getValues()) {
                TextView text = new TextView(this);
                text.setText(val);
                TableRow.LayoutParams lp = new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT,
                                                                      TableRow.LayoutParams.MATCH_PARENT);
                lp.setMargins(2,2,2,2);
                text.setMaxWidth(widthArr[j]);
                text.setPadding(5, 5, 5, 5);
                text.setLayoutParams(lp);
                tableRow.addView(text, j);
                j++;
            }
            tableLayout.addView(tableRow, i++);
        }
        db.close();
    }
}
