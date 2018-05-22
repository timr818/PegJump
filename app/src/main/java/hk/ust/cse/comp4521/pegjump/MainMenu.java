package hk.ust.cse.comp4521.pegjump;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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

        //final Context thisContext = this;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this/*thisContext*/, gso);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 0);

            }
        });

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut();
                Log.e("Logout Success", "User has logged out");

                /*
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            //@Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                            }
                        });
                */
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 0) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            //Log.e("GOT IN", "GOT INSIDE FUNCTION TO SET TASK!");
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.e("Login Success", account.getId());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("Error", "signInResult:failed code=" + e.getStatusCode());
        }
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
