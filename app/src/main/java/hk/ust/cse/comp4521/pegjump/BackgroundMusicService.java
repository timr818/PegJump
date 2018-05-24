//COMP 4521     Name: Alfonso Miguel Pascual Santos-Tankia      Student ID: 20531732          Email: amsaa@connect.ust.hk
//COMP 4521     Name: Timothy Jacob Regan                       Student ID: 20531756          Email: tjregan@connect.ust.hk

package hk.ust.cse.comp4521.pegjump;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class BackgroundMusicService extends Service {

    public static MediaPlayer mediaPlayer;

    public static boolean isPlaying = false;

    public BackgroundMusicService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mediaPlayer = MediaPlayer.create(this, R.raw.bensoundsummer);
        mediaPlayer.setLooping(true);
        if (!GlobalSpace.musicMute)
            mediaPlayer.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isPlaying = false;
        mediaPlayer.stop();
        mediaPlayer.release();
    }


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
