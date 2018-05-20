package hk.ust.cse.comp4521.pegjump;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;
import android.view.Gravity;
import android.widget.PopupWindow;

import java.util.Random;
import java.util.Vector;


public class GameController {

    GameBoard gameBoard;

    public int pegPoints;
    public int numMoves;

    public int operation;

    GameController() {
        gameBoard = new GameBoard();
        operation = Constants.OPERATION_PLUS;
        pegPoints = 0;
        numMoves = 0;
    }

    public void pegPressed(int id) {
        PegStatus status = gameBoard.pegs.elementAt(id).currentStatus;

        if (id == gameBoard.selectedPeg) {
            gameBoard.deselect();
        } else if (status == PegStatus.occupied) {
            gameBoard.select(id);
        } else if (status == PegStatus.available) {
            for (Pair<Integer, Integer> jumps : gameBoard.pegs.elementAt(gameBoard.selectedPeg).possibleMoves) {
                if (jumps.second == id) {
                    pegPoints += gameBoard.movePeg(gameBoard.selectedPeg, jumps.first, jumps.second, operation);
                    moveMade();
                }
            }
        }
    }


    private void moveMade() {
        //when a move has been made, there's a chance that some vacant slots will be populated
        //with a random peg
        Vector<Peg> vacantPegs = gameBoard.getVacantPegs();

        numMoves++;

        if (vacantPegs.size() < Constants.MAX_PEGS) {
            addRandomPegs(vacantPegs);

            while(noMovesLeft()) {
                addRandomPegs(vacantPegs);
            }
        }
    }

    private void addRandomPegs(Vector<Peg> vacantPegs) {
        int numOccupiedPegs = 15 - vacantPegs.size();
        int most = Constants.MAX_PEGS - numOccupiedPegs;
        int min = 0;
        if (numOccupiedPegs < Constants.MIN_PEGS) {
            min = 1;
        }

        Random rand = new Random();
        int numberToAdd = rand.nextInt(most - min) + min;
        if (rand.nextFloat() < 0.3) {
            numberToAdd = min;
        }

        int minValue = -1;
        int maxValue = -1;
        for (int i = 0; i < 15; i++) {
            int v = gameBoard.pegs.elementAt(i).value;

            if (v < minValue || minValue == -1) {
                minValue = v;
            }

            if (v > maxValue || maxValue == -1) {
                maxValue = v;
            }
        }

        for (int i = 0; i < numberToAdd; i++) {
            Peg thePeg = vacantPegs.elementAt(rand.nextInt(vacantPegs.size() - 1));
            thePeg.currentStatus = PegStatus.occupied;
            thePeg.value = rand.nextInt(maxValue - minValue) + minValue;
        }
    }

    public boolean noMovesLeft() {
        for (Peg p : gameBoard.pegs) {
            if (gameBoard.canMove(p)) {
                return false;
            }
        }

        return true;
    }

    public void gameWon(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_FILE, Context.MODE_PRIVATE);

        int prevBestScore = prefs.getInt(Constants.PREFS_BEST_SCORE, -1);
        if (prevBestScore < 0 || numMoves < prevBestScore) {
            SharedPreferences.Editor edit = prefs.edit();

            edit.putInt(Constants.PREFS_BEST_SCORE, numMoves);
            edit.commit();
        }
    }
}
