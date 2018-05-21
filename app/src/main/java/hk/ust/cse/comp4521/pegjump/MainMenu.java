package hk.ust.cse.comp4521.pegjump;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        configureButtons();

        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_FILE, Context.MODE_PRIVATE);
        GlobalSpace.musicMute = prefs.getBoolean(Constants.PREFS_MUSIC_MUTE, Constants.PREFS_DEFAULT_MUSIC_MUTE);
        if (!GlobalSpace.musicMute) {
            Intent music = new Intent(MainMenu.this, BackgroundMusicService.class);
            startService(music);
        }
    }

    private void configureButtons() {
        ImageButton playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, PlayGame.class));
            }
        });

        ToggleButton muteButton = findViewById(R.id.musicMuteButton);
        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //bla
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (BackgroundMusicService.mediaPlayer != null && !BackgroundMusicService.mediaPlayer.isPlaying())
            BackgroundMusicService.mediaPlayer.start();

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
