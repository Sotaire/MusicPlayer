package com.lawlett.musicplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView imgView;
    List<Recipe> mListRecipe = new ArrayList<>();
    String lastPage;
    String image;
    String mp3Url = "";
    String http = "http://45.88.104.12/hc/preview/temp_067TG/2020.07/Bastard%20%20-%20F..k%20that.mp3";

    private final String urlHome = "http://hotcharts.ru/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = findViewById(R.id.title_img);
//        new ParsImage().execute();
        new ParsMusic().execute();

    }


    public class ParsImage extends AsyncTask<Void, Void, Void> {
        @Override
        public Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(urlHome)
                        .get();
                Elements els = doc.select("div[class=b_nav_stations  clearfix]");
                image = "http://hotcharts.ru/" + els.select("li[class=hidden-phone]>a >img").attr("src");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MainActivity.this).load(image).into(imgView);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;


        }
    }

    public class ParsMusic extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(urlHome)
//                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36")
                        .referrer("http://hotcharts.ru/")
                        .get();
//
//                Elements els = doc.select("div[class=jp-jplayer]");
//               // final String uri =els.select("audio").attr("");
//                String s = els.attr("src");
//                String el = doc.select("div[class=jp-jplayer] > audio").attr("src");
//                Log.e("music", "doInBackground: ");
//                Log.d("fdh","dgf");
                final Elements els = doc.select("td[class=song]");
                final Element as = els.get(0);
                String url1 = as.getElementsByTag("a").attr("href");
                char[] chars = url1.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    if (chars[i] != '#') {
                        if (chars[i] == ' ') {
                            mp3Url = mp3Url + "%20";
                        } else {
                            if (chars[i] == '\'') {
                                mp3Url = mp3Url + "%27";
                            } else {
                                mp3Url = mp3Url + chars[i];
                            }
                        }
                    }
                }
                final String url = "http://45.88.104.12/hc/preview/temp_067TG/" + mp3Url + ".mp3";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MediaPlayer mp = new MediaPlayer();
                        try {
                            mp.setDataSource(url);
                            mp.prepare();
                            mp.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    private void itemRecipes(String url) {
        try {
            String imgRecipe, nameRecipe, lingPageRecipe;
            Document doc = Jsoup.connect(url).get();
            Elements els = doc.select("div[class=items] >div[class=row] >div[class=col span_6 tourItem]");
            for (Element el : els) {
                imgRecipe = "http://wowbody.com.ua" + el.select("<img").attr("src");
                nameRecipe = el.select("div[class=title] > a").text();
                lingPageRecipe = el.select("div[class=title] > a").attr("href");

                Log.e("recipe", "itemRecipes: " + imgRecipe + " " + nameRecipe + " " + lingPageRecipe);

                mListRecipe.add(new Recipe(imgRecipe, nameRecipe, lingPageRecipe));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}