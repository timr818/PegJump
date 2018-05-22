package hk.ust.cse.comp4521.pegjump;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;

public class MainMenu extends AppCompatActivity {

    ToggleButton muteButton; //for music
    ToggleButton sfxButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //update information from preferences
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_FILE, Context.MODE_PRIVATE);
        GlobalSpace.musicMute = prefs.getBoolean(Constants.PREFS_MUSIC_MUTE, Constants.PREFS_DEFAULT_MUSIC_MUTE);
        GlobalSpace.sfxMute = prefs.getBoolean(Constants.PREFS_SFX_MUTE, Constants.PREFS_DEFAULT_SFX_MUTE);

        Intent music = new Intent(MainMenu.this, BackgroundMusicService.class);
        startService(music);

        configureButtons();
    }

    private void configureButtons() {
        ImageButton playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, PlayGame.class));
            }
        });

        muteButton = findViewById(R.id.musicMuteButton);
        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalSpace.musicMute) {
                    if (BackgroundMusicService.mediaPlayer != null && !BackgroundMusicService.mediaPlayer.isPlaying()) {
                        BackgroundMusicService.mediaPlayer.start();
                    }
                } else {
                    if (BackgroundMusicService.mediaPlayer != null && BackgroundMusicService.mediaPlayer.isPlaying()) {
                        BackgroundMusicService.mediaPlayer.pause();
                    }
                }

                GlobalSpace.musicMute = !GlobalSpace.musicMute;

                SharedPreferences prefs = getSharedPreferences(Constants.PREFS_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();

                edit.putBoolean(Constants.PREFS_MUSIC_MUTE, GlobalSpace.musicMute);
                edit.apply();
            }
        });

        sfxButton = findViewById(R.id.soundMuteButton);
        if (GlobalSpace.sfxMute) {
            sfxButton.setChecked(false);
        }
        sfxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalSpace.sfxMute = !GlobalSpace.sfxMute;

                SharedPreferences prefs = getSharedPreferences(Constants.PREFS_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(Constants.PREFS_SFX_MUTE, GlobalSpace.sfxMute);
                edit.apply();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (BackgroundMusicService.mediaPlayer != null && !BackgroundMusicService.mediaPlayer.isPlaying() && !GlobalSpace.musicMute)
            BackgroundMusicService.mediaPlayer.start();

        if (GlobalSpace.musicMute) {
            muteButton.setChecked(false);
        } else {
            muteButton.setChecked(true);
        }

        if (GlobalSpace.sfxMute) {
            sfxButton.setChecked(false);
        } else {
            sfxButton.setChecked(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (BackgroundMusicService.mediaPlayer != null && BackgroundMusicService.mediaPlayer.isPlaying())
            BackgroundMusicService.mediaPlayer.pause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent music = new Intent(MainMenu.this, BackgroundMusicService.class);
        stopService(music);
    }

}
