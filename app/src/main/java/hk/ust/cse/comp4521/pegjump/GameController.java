package hk.ust.cse.comp4521.pegjump;

import android.util.Pair;

import java.util.Random;
import java.util.Vector;


public class GameController {

    GameBoard gameBoard;

    private int pegPoints;

    public int operation;

    private static boolean soundMuted = false;

    GameController() {
        gameBoard = new GameBoard();
        operation = Constants.OPERATION_PLUS;
        pegPoints = 0;
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

        System.out.println(vacantPegs.size() + " vacnat pegs");

        if (vacantPegs.size() < Constants.MAX_PEGS) {
            int numOccupiedPegs = 15 - vacantPegs.size();
            int most = Constants.MAX_PEGS - numOccupiedPegs;
            int min = 0;
            if (numOccupiedPegs < Constants.MIN_PEGS) {
                min = 1;
            }

            Random rand = new Random();
            int numberToAdd = rand.nextInt(most) + min;
            for (int i = 0; i < numberToAdd; i++) {
                vacantPegs.elementAt(rand.nextInt(vacantPegs.size() - 1)).revive();
            }
        }
    }

    public int getPegValue(int id) {
        return gameBoard.pegs.elementAt(id).value;
    }

    public void toggleMute() {
        soundMuted = !soundMuted;
    }

    public boolean isSoundMuted() {
        return soundMuted;
    }

    public int getPegPoints() {
        return pegPoints;
    }
}
