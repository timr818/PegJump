package hk.ust.cse.comp4521.pegjump;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    private GameController controller;

    private Button muteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        controller = new GameController();

        configureButtons();
    }

    private void configureButtons() {
        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, PlayGame.class));
            }
        });

        muteButton = findViewById(R.id.muteButton);
        updateMuteButtonBackground();
        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.toggleMute();
                updateMuteButtonBackground();
            }
        });
    }

    private void updateMuteButtonBackground() {
        if (controller.isSoundMuted()) {
            muteButton.setBackgroundResource(R.drawable.sound);
        } else {
            muteButton.setBackgroundResource(R.drawable.sound_off);
        }
    }
}
