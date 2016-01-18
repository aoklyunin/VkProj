package com.vk.vktestapp;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

/**
 * Класс для загрузки файлов, если в конструкторе передать url="", то скачиваются все аудиозаписи текущего пользователя
 * */
class DownloadFileFromURL extends AsyncTask<Void, Integer, Integer> {

    private ProgressBar firstBar;
    private TextView barText;
    private String pathToSave;
    private Set<String> audioSet;
    private int audioCnt;
    private String sourceUrl;

    // конструктор класса для скачивания файлов
    DownloadFileFromURL(String path,ProgressBar bar,TextView text, String url){
        pathToSave = path;
        firstBar = bar;
        barText = text;
        sourceUrl = url;
    }
    // сам фоновый процесс
    @Override
    protected Integer doInBackground(Void... params) {
        if (sourceUrl=="") {
            // номер скачиваемой аудиозаписи
            int pos = 0;
            // проходим по всем элементам мн-ва названий аудиозаписей
            for (AudioRec audio : CurVkClient.getAudioMap()) {
                // отображаем изменения в progressBar'e
                publishProgress(pos);
                String name = audio.getArtist()+" "+audio.getTitle();
                // выводим в лог
                Log.d("DOWNLOAD FROM VK", name + "    " + audio.getUrl());
                // загружаем файл
                loadFile(pathToSave + name + ".mp3", audio.getUrl());
                // увеличиваем номер скачиваемой аудиозаписи
                pos++;
            }
        }else{
            loadFile(pathToSave, sourceUrl);
        }
        return null;
    }

    // инициализация процесса
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // задаём текст полю
        barText.setText("Идёт загрузка..");
        // делаем поле видимым
        barText.setVisibility(View.VISIBLE);
        // делаем ProcessBar видимым
        firstBar.setVisibility(View.VISIBLE);
        // задаём максимум ProcessBar'у
        if (sourceUrl.equals(""))
            firstBar.setMax(CurVkClient.getAudioMap().size());
        else
            firstBar.setMax(100);
    }
    // метод для загрузки файлов
    public void loadFile(String path,String urlPath){
        try {
            // создаём url-адрес
            URL url = new URL(urlPath);
            // создаём url-соединение
            URLConnection conection = url.openConnection();
            // подключаемся
            conection.connect();
            // размер файла
            int len=0;
            // если передан адрес, откуда качать
            if (!sourceUrl.equals(""))
                // узнаём размер файла
                len = conection.getContentLength();
            // создаём поток чтения
            InputStream input = new BufferedInputStream(url.openStream(),8192);
            // создаём поток вывода
            OutputStream output = new FileOutputStream(path);
            // создаём буфер для чтения
            byte data[] = new byte[1024];
            // переменная для чтения из потока
            int count;
            // переменная для подсчёта процента скаченности файла
            int total = 0;
            // пока есть, что читать, читаем
            while ((count = input.read(data)) != -1) {
                if (!sourceUrl.equals("")) {
                    total+=count;
                    if (len!=0) {
                        publishProgress((int) (total * 100 / len));
                    }else{
                        Log.e("DOWNLOAD","размер файла 0");
                    }
                }
                output.write(data, 0, count);
            }
            // заполняем поток
            output.flush();
            // закрываем потоки
            output.close();
            input.close();
            // ловим ошибки
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
    }
    // здесь надо обрабатывать все команды для UI
    protected void onProgressUpdate(Integer... progress) {
        // задаём прогресс ProgressBar'a
        firstBar.setProgress(progress[0]);
        // задаём соответствующий текст полю
        // если скачиваем не 1 файл
        if (sourceUrl.equals(""))
            barText.setText("Идёт загрузка: "+progress[0]+" из "+audioCnt);
        else
            barText.setText("Идёт загрузка: "+progress[0]+"%");
    }
    @Override
    // что нужно сделать в конце процесса
    protected void onPostExecute(Integer param) {
        // скрываем progressBar
        firstBar.setVisibility(View.INVISIBLE);
        // скрываем текст
        barText.setVisibility(View.INVISIBLE);
        // выводим сообщение, что всё ок
        Log.d("DOWNLOAD","Downloading ended");
    }
}