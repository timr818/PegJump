package hk.ust.cse.comp4521.pegjump;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PlayGame extends AppCompatActivity implements View.OnClickListener {

    GameController controller;

    ConstraintLayout layout;

    TextView pegPointsDisplay;

    ImageButton[] pegButtons = new ImageButton[15];
    TextView[] valueLabels = new TextView[15];

    Button pauseButton;
    ImageButton plusButton;
    ImageButton minusButton;

    PopupWindow pauseWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        layout = findViewById(R.id.playGameLayout);

        controller = new GameController();

        pegPointsDisplay = findViewById(R.id.pegPointsDisplay);

        configurePopups();
        configureButtons();
        updateBoardVisuals();
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

        plusButton = findViewById(R.id.plusButton);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.operation = Constants.OPERATION_PLUS;
                plusButton.setColorFilter(Constants.COLOR_SELECTED);
                minusButton.setColorFilter(Constants.COLOR_VACANT);
            }
        });

        minusButton = findViewById(R.id.minusButton);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.operation = Constants.OPERATION_MINUS;
                minusButton.setColorFilter(Constants.COLOR_SELECTED);
                plusButton.setColorFilter(Constants.COLOR_VACANT);
            }
        });

        for (int i = 0; i < 15; i++) {
            ImageButton current = getPegView(i);
            current.setOnClickListener(this);
            pegButtons[i] = current;

            //instantiate the label for the value of the peg.
            TextView text = new TextView(this);
            text.setId(100 + i);
            text.setText(Integer.toString(controller.getPegValue(i)));
            ConstraintLayout.LayoutParams textLayoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            layout.addView(text, textLayoutParams);
            valueLabels[i] = text;

            //add the constraints
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(layout);
            constraintSet.connect(text.getId(), ConstraintSet.TOP, current.getId(), ConstraintSet.TOP, 0);
            constraintSet.connect(text.getId(), ConstraintSet.LEFT, current.getId(), ConstraintSet.LEFT, 0);
            constraintSet.connect(text.getId(), ConstraintSet.BOTTOM, current.getId(), ConstraintSet.BOTTOM, 0);
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
        pegPointsDisplay.setText(Integer.toString(controller.getPegPoints()));

        ImageButton a = findViewById(R.id.peg0);
        a.setColorFilter(Constants.COLOR_OCCUPIED);

        for (int i = 0; i < 15; i++) {
            ImageButton pegView = getPegView(i);
            TextView pegLabel = getPegLabel(i);
            Peg currentPeg = controller.gameBoard.pegs.elementAt(i);
            PegStatus status = currentPeg.currentStatus;
            pegLabel.setText(Integer.toString(controller.getPegValue(i)));
            if (i == controller.gameBoard.selectedPeg) {
                pegView.setColorFilter(Constants.COLOR_SELECTED);
                pegLabel.setVisibility(View.VISIBLE);
            } else if (status == PegStatus.occupied) {
                pegView.setColorFilter(Constants.COLOR_OCCUPIED);
                pegLabel.setVisibility(View.VISIBLE);
            } else if (status == PegStatus.available) {
                pegView.setColorFilter(Constants.COLOR_AVAILABLE);
                pegLabel.setVisibility(View.INVISIBLE);
            } else {
                pegView.setColorFilter(Constants.COLOR_VACANT);
                pegLabel.setVisibility(View.INVISIBLE);
            }
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

    private void pauseGame() {
        pauseWindow.showAtLocation(pauseButton, Gravity.CENTER, 0, 0);
    }

    private void resumeGame() {
        pauseWindow.dismiss();
    }

}
