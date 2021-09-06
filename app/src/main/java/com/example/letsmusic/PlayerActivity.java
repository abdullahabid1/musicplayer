package com.example.letsmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    Button btn_pause, btn_next, btn_previous;
    TextView songText;
    SeekBar songSeekbar;

    static MediaPlayer  myMediaPlayer;
    int position;
    String sname;
    ArrayList<File> mySongs;
    Thread updateseekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btn_next = (Button) findViewById(R.id.next);
        btn_pause = (Button) findViewById(R.id.paus);
        btn_previous = (Button) findViewById(R.id.pre);

        songText = (TextView) findViewById(R.id.viewText);

        songSeekbar = (SeekBar)findViewById(R.id.seekBar);

        updateseekBar = new Thread(){
            @Override
            public void run() {

                int totalDuration = myMediaPlayer.getDuration();
                int currentPosition = 0;

                while(currentPosition<totalDuration){
                    try {
                        sleep(500);
                        currentPosition = myMediaPlayer.getCurrentPosition();
                        songSeekbar.setProgress(currentPosition);

                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        if (myMediaPlayer!=null){
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs=(ArrayList) bundle.getParcelableArrayList("songs");
        sname = mySongs.get(position).getName().toString();
        String songName = i.getStringExtra("songname");

        songText.setText(songName);
        songText.setSelected(true);
        position = bundle.getInt("pos",0);

        Uri u = Uri.parse(mySongs.get(position).toString());
        myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);
        myMediaPlayer.start();
        songSeekbar.setMax(myMediaPlayer.getDuration());

        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                myMediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songSeekbar.setMax(myMediaPlayer.getDuration());
                if (myMediaPlayer.isPlaying()){
                    btn_pause.setBackgroundResource(R.drawable.play);
                    myMediaPlayer.pause();
                }
                else {
                    btn_pause.setBackgroundResource(R.drawable.pause);
                    myMediaPlayer.start();
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer.stop();
                myMediaPlayer.release();
                position = ((position+1)%mySongs.size());

                Uri u = Uri.parse(mySongs.get(position).toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = mySongs.get(position).getName().toString();
                songText.setText(sname);
                myMediaPlayer.start();
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myMediaPlayer.stop();
                myMediaPlayer.release();
                position = ((position-1)%mySongs.size());

                Uri u = Uri.parse(mySongs.get(position).toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = mySongs.get(position).getName().toString();
                songText.setText(sname);
                myMediaPlayer.start();
            }
        });
    }
}