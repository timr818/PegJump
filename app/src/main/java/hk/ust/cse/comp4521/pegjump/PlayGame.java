package hk.ust.cse.comp4521.pegjump;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

public class PlayGame extends AppCompatActivity implements View.OnClickListener {

    GameController controller;

    Button pauseButton;

    PopupWindow pauseWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        controller = new GameController();

        configurePopups();
        configureButtons();
    }

    private void configurePopups() {
        View pauseView = getLayoutInflater().inflate(R.layout.fragment_pause_menu, null);
        pauseWindow = new PopupWindow(pauseView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pauseWindow.setFocusable(true);
        pauseWindow.setBackgroundDrawable(new ColorDrawable());

        //Initialize buttons
        Button resumeButton = pauseView.findViewById(R.id.resumeButton);
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pauseWindow.isShowing()) {
                    resumeGame();
                }
            }
        });

        Button exitButton = pauseView.findViewById(R.id.exitToMenuButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void configureButtons() {
        pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseGame();
            }
        });

        Button peg = findViewById(R.id.peg0);
        peg.setOnClickListener(this);
        peg = findViewById(R.id.peg1);
        peg.setOnClickListener(this);
        peg = findViewById(R.id.peg2);
        peg.setOnClickListener(this);
        peg = findViewById(R.id.peg3);
        peg.setOnClickListener(this);
        peg = findViewById(R.id.peg4);
        peg.setOnClickListener(this);
        peg = findViewById(R.id.peg5);
        peg.setOnClickListener(this);
        peg = findViewById(R.id.peg6);
        peg.setOnClickListener(this);
        peg = findViewById(R.id.peg7);
        peg.setOnClickListener(this);
        peg = findViewById(R.id.peg8);
        peg.setOnClickListener(this);
        peg = findViewById(R.id.peg9);
        peg.setOnClickListener(this);
        peg = findViewById(R.id.peg10);
        peg.setOnClickListener(this);
        peg = findViewById(R.id.peg11);
        peg.setOnClickListener(this);
        peg = findViewById(R.id.peg12);
        peg.setOnClickListener(this);
        peg = findViewById(R.id.peg13);
        peg.setOnClickListener(this);
        peg = findViewById(R.id.peg14);
        peg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.peg0:
                controller.pegPressed(0);
                break;
            case R.id.peg1:
                controller.pegPressed(1);
                break;
            case R.id.peg2:
                controller.pegPressed(2);
                break;
            case R.id.peg3:
                controller.pegPressed(3);
                break;
            case R.id.peg4:
                controller.pegPressed(4);
                break;
            case R.id.peg5:
                controller.pegPressed(5);
                break;
            case R.id.peg6:
                controller.pegPressed(6);
                break;
            case R.id.peg7:
                controller.pegPressed(7);
                break;
            case R.id.peg8:
                controller.pegPressed(8);
                break;
            case R.id.peg9:
                controller.pegPressed(9);
                break;
            case R.id.peg10:
                controller.pegPressed(10);
                break;
            case R.id.peg11:
                controller.pegPressed(11);
                break;
            case R.id.peg12:
                controller.pegPressed(12);
                break;
            case R.id.peg13:
                controller.pegPressed(13);
                break;
            case R.id.peg14:
                controller.pegPressed(14);
                break;
            default:
                controller.pegPressed(-1);
        }
    }

    private void pauseGame() {
        pauseWindow.showAtLocation(pauseButton, Gravity.CENTER, 0, 0);
    }

    private void resumeGame() {
        pauseWindow.dismiss();
    }

}
