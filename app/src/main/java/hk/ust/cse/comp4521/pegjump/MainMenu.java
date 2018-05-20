package hk.ust.cse.comp4521.pegjump;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MainMenu extends AppCompatActivity {

    protected ServiceConnection mServerConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //Log.d(LOG_TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //Log.d(LOG_TAG, "onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        configureButtons();

        Intent music = new Intent(MainMenu.this, BackgroundMusicService.class);
        startService(music);
    }

    private void configureButtons() {
        ImageButton playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, PlayGame.class));
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
