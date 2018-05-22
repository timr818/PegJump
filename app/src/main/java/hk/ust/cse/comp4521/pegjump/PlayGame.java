package hk.ust.cse.comp4521.pegjump;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import static hk.ust.cse.comp4521.pegjump.BackgroundMusicService.mediaPlayer;

public class PlayGame extends AppCompatActivity implements View.OnClickListener {

    GameController controller;

    ConstraintLayout layout;

    TextView pegPointsDisplay;
    TextView numMovesDisplay;
    TextView bestScoreDisplay;
    TextView winningScoreDisplay;
    TextView globalWinningScoreDisplay;

    ImageButton[] pegButtons = new ImageButton[15];
    TextView[] valueLabels = new TextView[15];

    Button pauseButton;
    RadioButton plusButton;
    RadioButton minusButton;

    PopupWindow pauseWindow;
    PopupWindow winWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        layout = findViewById(R.id.playGameLayout);

        controller = new GameController(this);

        configureTextDisplays();
        configurePopups();
        configureButtons();
        updateBoardVisuals();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mediaPlayer != null && !mediaPlayer.isPlaying() && !GlobalSpace.musicMute) {
            mediaPlayer.start();
        }

        //REMEMBER TO CHECK IF THE GOOGLE PLAY API IS UP TO DATE https://developers.google.com/android/guides/setup TODO
    }

    private void configureTextDisplays() {
        pegPointsDisplay = findViewById(R.id.pegPointsDisplay);
        numMovesDisplay = findViewById(R.id.numMovesDisplay);
        bestScoreDisplay = findViewById(R.id.bestScoreDisplay);

        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_FILE, Context.MODE_PRIVATE);
        int prevBest = prefs.getInt(Constants.PREFS_BEST_SCORE, -1);

        if (prevBest >= 0) {
            String message = getResources().getString(R.string.prev_best_label, prevBest);
            bestScoreDisplay.setText(message);
        } else {
            bestScoreDisplay.setText(getResources().getString(R.string.prev_best_missing_label));
        }
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

        Button restartButton1 = pauseView.findViewById(R.id.restartButton);
        restartButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartGame();
                pauseWindow.dismiss();
            }
        });

        Switch musicMuteSwitch = pauseView.findViewById(R.id.pauseMusicMuteSwitch);
        if (GlobalSpace.musicMute) {
            musicMuteSwitch.setChecked(false);
        } else {
            musicMuteSwitch.setChecked(true);
        }
        musicMuteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GlobalSpace.musicMute = !isChecked;

                if (!GlobalSpace.musicMute) {
                    if (BackgroundMusicService.mediaPlayer != null && !BackgroundMusicService.mediaPlayer.isPlaying()) {
                        BackgroundMusicService.mediaPlayer.start();
                    }
                } else {
                    if (BackgroundMusicService.mediaPlayer != null && BackgroundMusicService.mediaPlayer.isPlaying()) {
                        BackgroundMusicService.mediaPlayer.pause();
                    }
                }

                SharedPreferences prefs = getSharedPreferences(Constants.PREFS_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(Constants.PREFS_MUSIC_MUTE, GlobalSpace.musicMute);
                edit.apply();
            }
        });

        Switch sfxSwitch = pauseView.findViewById(R.id.pauseSfxMuteSwitch);
        if (GlobalSpace.sfxMute) {
            sfxSwitch.setChecked(false);
        } else {
            sfxSwitch.setChecked(true);
        }
        sfxSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GlobalSpace.sfxMute = !isChecked;

                SharedPreferences prefs = getSharedPreferences(Constants.PREFS_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(Constants.PREFS_SFX_MUTE, GlobalSpace.sfxMute);
                edit.apply();
            }
        });


        View winView = getLayoutInflater().inflate(R.layout.fragment_winning_screen, null);
        winningScoreDisplay = winView.findViewById(R.id.winningMovesLabel);
        winWindow = new PopupWindow(winView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        winWindow.setBackgroundDrawable(new ColorDrawable());

        //View globalWinView = getLayoutInflater().inflate(R.layout.fragment_winning_screen, null);
        globalWinningScoreDisplay = winView.findViewById(R.id.globalHighScore);

        Button restartButton = winView.findViewById(R.id.restartButton);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartGame();
                winWindow.dismiss();
            }
        });

        Button exitButton2 = winView.findViewById(R.id.exitButton);
        exitButton2.setOnClickListener(new View.OnClickListener() {
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

        plusButton = findViewById(R.id.plusButton);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.operation = Constants.OPERATION_PLUS;
            }
        });

        minusButton = findViewById(R.id.minusButton);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.operation = Constants.OPERATION_MINUS;
            }
        });

        for (int i = 0; i < 15; i++) {
            ImageButton current = getPegView(i);
            current.setOnClickListener(this);
            pegButtons[i] = current;

            //instantiate the label for the value of the peg.
            TextView text = new TextView(this);
            text.setId(100 + i);
            text.setTextColor(Constants.COLOR_VALUE_LABEL_TEXT);
            text.setShadowLayer(6, 0, 0, Constants.COLOR_VALUE_LABEL_SHADOW);
            text.setText(getResources().getString(R.string.peg_label));
            ConstraintLayout.LayoutParams textLayoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            layout.addView(text, textLayoutParams);
            valueLabels[i] = text;

            //add the constraints
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(layout);
            constraintSet.connect(text.getId(), ConstraintSet.TOP, current.getId(), ConstraintSet.TOP, 0);
            constraintSet.connect(text.getId(), ConstraintSet.LEFT, current.getId(), ConstraintSet.LEFT, 0);
            constraintSet.connect(text.getId(), ConstraintSet.BOTTOM, current.getId(), ConstraintSet.BOTTOM, 62);
            constraintSet.connect(text.getId(), ConstraintSet.RIGHT, current.getId(), ConstraintSet.RIGHT, 0);
            constraintSet.applyTo(layout);
        }
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

        updateBoardVisuals();
    }

    private void updateBoardVisuals() {
        pegPointsDisplay.setText(getResources().getString(R.string.peg_points_label, controller.pegPoints));
        numMovesDisplay.setText(getResources().getString(R.string.num_moves_label, controller.numMoves));

        for (int i = 0; i < 15; i++) {
            ImageButton pegView = getPegView(i);
            TextView pegLabel = getPegLabel(i);
            Peg currentPeg = controller.gameBoard.pegs.elementAt(i);
            PegStatus status = currentPeg.currentStatus;
            pegLabel.setText(getResources().getString(R.string.peg_label,  currentPeg.value));
            if (i == controller.gameBoard.selectedPeg) {
                pegView.setColorFilter(Constants.COLOR_SELECTED);
                pegView.setImageResource(getProperPegImage(currentPeg.value));
                pegLabel.setVisibility(View.VISIBLE);
            } else if (status == PegStatus.occupied) {
                pegView.setColorFilter(Constants.COLOR_NOT_SELECTED);
                pegView.setImageResource(getProperPegImage(currentPeg.value));
                pegLabel.setVisibility(View.VISIBLE);
            } else if (status == PegStatus.available) {
                pegView.setColorFilter(Constants.COLOR_NOT_SELECTED);
                pegView.setImageResource(R.drawable.available_peg);
                pegLabel.setVisibility(View.INVISIBLE);
            } else {
                pegView.setColorFilter(Constants.COLOR_NOT_SELECTED);
                pegView.setImageResource(R.drawable.vacant_peg);
                pegLabel.setVisibility(View.INVISIBLE);
            }
        }

        if (controller.pegPoints == Constants.WINNING_NUMBER) {
            showWinScreen();
            controller.gameWon(this.getApplicationContext());
        }
    }

    private ImageButton getPegView(int id) {
        switch (id) {
            case 0:
                return findViewById(R.id.peg0);
            case 1:
                return findViewById(R.id.peg1);
            case 2:
                return findViewById(R.id.peg2);
            case 3:
                return findViewById(R.id.peg3);
            case 4:
                return findViewById(R.id.peg4);
            case 5:
                return findViewById(R.id.peg5);
            case 6:
                return findViewById(R.id.peg6);
            case 7:
                return findViewById(R.id.peg7);
            case 8:
                return findViewById(R.id.peg8);
            case 9:
                return findViewById(R.id.peg9);
            case 10:
                return findViewById(R.id.peg10);
            case 11:
                return findViewById(R.id.peg11);
            case 12:
                return findViewById(R.id.peg12);
            case 13:
                return findViewById(R.id.peg13);
            case 14:
                return findViewById(R.id.peg14);
            default:
                System.out.println("Default case activated. Something went wrong.");
                return findViewById(R.id.peg0);
        }
    }

    private TextView getPegLabel(int id) {
        return findViewById(100 + id);
    }

    public void showWinScreen() {

        if (winningScoreDisplay != null) {
            winningScoreDisplay.setText(getResources().getString(R.string.peg_label, controller.numMoves));
            globalWinningScoreDisplay.setText(getResources().getString(R.string.peg_label, controller.numMoves));

        }
        winWindow.showAtLocation(pauseButton, Gravity.CENTER, 0, 0);
    }

    private void pauseGame() {
        pauseWindow.showAtLocation(pauseButton, Gravity.CENTER, 0, 0);
    }

    private void resumeGame() {
        pauseWindow.dismiss();
    }

    private void restartGame() {
        controller = new GameController(this);
        configureTextDisplays();
        plusButton.callOnClick();
        plusButton.setChecked(true);
        minusButton.setChecked(false);
        updateBoardVisuals();
    }

    private int getProperPegImage(int value) {
        if (value < Constants.PEG_YELLOW_THRESHOLD) {
            return R.drawable.occupied_blue_peg;
        } else if (value < Constants.PEG_RED_THRESHOLD) {
            return R.drawable.occupied_yellow_peg;
        } else {
            return R.drawable.occupied_red_peg;
        }
    }

}
