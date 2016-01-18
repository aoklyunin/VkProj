// в конструторе я передаю Активность как параметр -
// не знаю, стоит так делать или нет, но пока что работает

package com.vk.vktestapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by student on 23.11.15.
 */
public class CurVkClient {
    private static final String[] sMyScope = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
            VKScope.MESSAGES,
            VKScope.DOCS,
            VKScope.AUDIO,
            VKScope.ADS
    };
    private static ArrayList<AudioRec> audioArr;

    private boolean flgGetRecommend = false;

    private boolean flgFirstLogin;
    private boolean isResumed = false;
    private Activity mainActivity;
    private ProgressBar firstBar;

    public static ArrayList<AudioRec>getAudioMap(){ return audioArr;}

    public static void clearAudioMap(){ audioArr.clear();}
    public boolean isFirstLogin(){ return flgFirstLogin;}
    public void setFirstLogin(boolean flg){flgFirstLogin=flg;}
    public void setFlgGetRecommend(boolean flg){flgGetRecommend=flg;}
    public boolean getFlgGetRecommend(){ return flgGetRecommend;}
    public void setIsResumed(boolean flg){isResumed=flg;}
    public boolean getIsResumed(){ return isResumed;}
    private String [] audioHandles;

    // загрузка аудио
    public void loadMyAudio(int cnt,int offset){
        VKParameters params = new VKParameters();
        params.put(VKApiConst.OFFSET, offset);
        params.put(VKApiConst.COUNT, cnt);
        VKRequest requestAudio = VKApi.audio().get(params);
        requestAudio.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                processAudioResponse(response,false);
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }

            @Override
            public void onError(VKError error) {
                getVkError(error);
                super.onError(error);
            }

            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }
        });

    }
    // получить рекомендованные запис
    public void getRecommendAudio(int cnt){
        flgGetRecommend = false;
        VKParameters params = new VKParameters();
        params.put(VKApiConst.COUNT, cnt);
        VKRequest requestAudio = VKApi.audio().getRecommendations(params);
        requestAudio.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                processAudioResponse(response, true);
                flgGetRecommend = true;
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }

            @Override
            public void onError(VKError error) {
                getVkError(error);
                super.onError(error);
            }

            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }
        });
    }
    // обработка запроса на получение аудиозаписей
    public void processAudioResponse(VKResponse response,boolean flgRecommend){
        // получаем экземпляр элемента ListView
        int size = ((VKList<VKApiAudio>) response.parsedModel).size();
        Log.d("AUDIO_RESPONSE", "кол-во записей: " + size);
        for (int i = 0; i < size; i++) {
            VKApiAudio vkApiAudio = ((VKList<VKApiAudio>) response.parsedModel).get(i);
            AudioRec audio = new AudioRec(0,vkApiAudio.id,vkApiAudio.owner_id,
                                          vkApiAudio.title,vkApiAudio.artist,vkApiAudio.duration,
                                          vkApiAudio.url,vkApiAudio.genre,0,"","");
            if (flgRecommend)
                audio.setType("Recommend");
            else
                audio.setType("Audio");
            audioArr.add(audio);
        }
    }
    // конструктор
    CurVkClient(Context c,ProgressBar bar){
        mainActivity = (Activity)c;
        audioArr = new ArrayList<AudioRec>();

        flgFirstLogin = true;
        firstBar = bar;
        // запускаем API Вконтакте
        VKSdk.wakeUpSession(c, new VKCallback<VKSdk.LoginState>() {
            @Override
            // функция-обработчик ответов логина ВК
            public void onResult(VKSdk.LoginState res) {
                if (isResumed) {
                    signInVk();
                }
            }

            // функция-обработчик ошибок
            @Override
            public void onError(VKError error) {
                getVkError(error);
            }
        });
    }
    // функция выхода из ВК
    public void logout(){VKSdk.logout();}
    // обработка ошибок VK
    public void getVkError(VKError error) {
        switch (error.errorCode) {
            case -102:
                Toast.makeText(mainActivity, "Ошибка авторизации: " + error.toString(), Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(mainActivity, error.errorMessage, Toast.LENGTH_SHORT).show();
                break;
        }
    }
    // функция входа в ВК
    public void signInVk() {
        if(flgFirstLogin) {
            if (VKSdk.isLoggedIn()) {
                Toast.makeText(mainActivity, "вы уже авторизированы", Toast.LENGTH_SHORT).show();
            } else {
                VKSdk.login(mainActivity, sMyScope);
            }
            flgFirstLogin = false;
        }
    }

    public void displayAudio(){
      /*  // используем адаптер данных
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity,android.R.layout.simple_list_item_1, audioHandles);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                String text = (String) ((TextView) itemClicked).getText();
                String url = audioMap.get(text);
                String path = "/storage/emulated/0/VkMusic/SingleLoad/" + text + ".mp3";
                new DownloadFileFromURL(path,firstBar,barText,url).execute();
            }
        });*/
    }

    public void fillDB(DBHelper db,int recCnt,int audioCnt){
        loadMyAudio(audioCnt, 0);
        getRecommendAudio(recCnt);
        for (AudioRec audio: audioArr){
            db.addAudioRec(audio);
        }
    }
}
