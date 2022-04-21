package com.ru.sursangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaplayer.stop();
        mediaplayer.release();
        updateSeek.interrupt();
    }

    TextView textview;
    ImageView play, previous, next;
    ArrayList<File> songs;
    MediaPlayer mediaplayer;
    String textContent;
    int position;
    SeekBar seekBar;
    Thread updateSeek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textview = findViewById(R.id.textview);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songlist");
        textContent = intent.getStringExtra("currentSong");
        textview.setText(textContent);
        textview.setSelected(true);
        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaplayer = MediaPlayer.create(this, uri);
        mediaplayer.start();
        seekBar.setMax(mediaplayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaplayer.seekTo(seekBar.getProgress());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

        });
        updateSeek = new Thread() {
            @Override
            public void run() {
                int currentposition = 0;
                try {
                    while (currentposition < mediaplayer.getDuration()) {
                        currentposition = mediaplayer.getCurrentPosition();
                        seekBar.setProgress(currentposition);
                        sleep(800);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaplayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaplayer.pause();
                }
                else {
                    play.setImageResource(R.drawable.pause);
                    mediaplayer.start();
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaplayer.stop();
                mediaplayer.release();
              if(position!=0)
              {
                  position=position-1;
              }
              else{
                  position= songs.size()-1;
              }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaplayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaplayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaplayer.getDuration());
                textContent= songs.get(position).getName().toString();
                textview.setText(textContent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaplayer.stop();
                mediaplayer.release();
                if(position!= songs.size()-1)
                {
                    position=position+1;
                }
                else{
                    position= 0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaplayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaplayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaplayer.getDuration());
                textContent= songs.get(position).getName().toString();
                textview.setText(textContent);
            }
        });

    }
}