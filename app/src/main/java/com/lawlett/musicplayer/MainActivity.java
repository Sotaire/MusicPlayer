package com.lawlett.musicplayer;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.lawlett.musicplayer.interfaces.OnSongClickListener;
import com.lawlett.musicplayer.models.SongModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL_FOR_SONG = "http://45.88.104.12/hc/preview/temp_067TG/";
    private static final String MP3 = ".mp3";
    RecyclerView recyclerView;
    MusicAdapter musicAdapter;
    String mp3Url = "";
    ArrayList<String> songsURL = new ArrayList<>();
    ArrayList<SongModel> songModels = new ArrayList<>();
    Button download;
    MediaPlayer mp;

    private final String urlHome = "http://hotcharts.ru/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        musicAdapter = new MusicAdapter();
        recyclerView.setAdapter(musicAdapter);
        download = findViewById(R.id.button);
        ParsMusic parsMusic = new ParsMusic();
        parsMusic.execute();
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicAdapter.setSongs(songModels);
            }
        });
        musicAdapter.setListener(new OnSongClickListener() {
            @Override
            public void onSongClick(int position) {
                if (mp != null) {
                    try {
                        mp.stop();
                        mp.setDataSource("");
                        mp.release();
                        mp = null;
                    } catch (Exception e) {

                    }
                }
                char[] chars = songsURL.get(position).toString().toCharArray();
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
                final String url = BASE_URL_FOR_SONG + mp3Url + MP3;
                mp = new MediaPlayer();
                try {
                    mp.setDataSource(url);
                    mp.prepare();
                    mp.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public class ParsMusic extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(urlHome)
                        .referrer("http://hotcharts.ru/")
                        .get();
                final Elements els = doc.select("td[class=song]");
                for (int i = 0; i < els.size(); i++) {
                    final Element as = els.get(i);
                    String url1 = as.getElementsByTag("a").attr("href");
                    songsURL.add(url1);
                    String artist = as.getElementsByTag("a").attr("data-artist-name");
                    String song = as.getElementsByTag("a").attr("data-song-name");
                    songModels.add(new SongModel(song, artist));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}