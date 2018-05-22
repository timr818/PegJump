package hk.ust.cse.comp4521.pegjump;

import android.util.Pair;

import java.util.Random;
import java.util.Vector;

public class GameBoard {

    public int selectedPeg = -1;

    public Vector<Peg> pegs;

    GameBoard() {
        initializeBoard();
    }

    private void initializeBoard() {
        pegs = new Vector<Peg>();

        Random rand = new Random();

        int startingNumPegs = rand.nextInt(Constants.MAX_PEGS - Constants.MIN_PEGS) + Constants.MIN_PEGS;
        Vector<Integer> startingIndices = new Vector<>();
        for (int j = 0; j < 15; j++) {
            startingIndices.add(j);
        }
        for (int k = 0; k < 15 - startingNumPegs; k++) {
            startingIndices.remove(rand.nextInt(startingIndices.size()));
        }

        for (int i = 0; i < 15; i++) {
            pegs.add(new Peg(i));
            if (startingIndices.contains(i)) {
                pegs.lastElement().currentStatus = PegStatus.occupied;
            }
        }
    }

    public void deselect() {
        Peg pegObj;
        if (selectedPeg >= 0) {
            pegObj = pegs.elementAt(selectedPeg);
            pegObj.currentStatus = PegStatus.occupied;

            Vector<Peg> availMoves = getAvailableMoves(pegObj);
            for (Peg c : availMoves) {
                System.out.println("changing: " + c.id);
                c.currentStatus = PegStatus.vacant;
            }
        }

        selectedPeg = -1;
    }

    public void select(int id) {
        deselect();
        Peg pegObj =  pegs.elementAt(id);
        selectedPeg = id;
        pegObj.currentStatus = PegStatus.selected;

        Vector<Peg> availMoves = getAvailableMoves(pegObj);
        for (Peg c : availMoves) {
            c.currentStatus = PegStatus.available;
        }
    }

    public void movePeg(int from, int over, int to) {
        deselect();

        pegs.elementAt(from).currentStatus = PegStatus.vacant;
        pegs.elementAt(over).currentStatus = PegStatus.vacant;
        pegs.elementAt(to).currentStatus = PegStatus.occupied;
        pegs.elementAt(to).value = pegs.elementAt(from).value;
        //pegs.elementAt(to).applyOperation(, pegs.elementAt(over).value);
    }

    public int movePeg(int from, int over, int to, int operation) {
        deselect();

        Peg fromPeg = pegs.elementAt(from);
        Peg overPeg = pegs.elementAt(over);
        Peg toPeg = pegs.elementAt(to);

        fromPeg.currentStatus = PegStatus.vacant;
        overPeg.currentStatus = PegStatus.vacant;
        toPeg.currentStatus = PegStatus.occupied;
        toPeg.value = pegs.elementAt(from).value;
        toPeg.applyOperation(operation, pegs.elementAt(over).value);

        if (operation == Constants.OPERATION_PLUS) {
            return overPeg.value;
        } else {
            return -overPeg.value;
        }
    }

    public Vector<Peg> getAvailableMoves(Peg p) {
        Vector<Peg> result = new Vector<>();

        for (Pair<Integer, Integer> jumps : p.possibleMoves) {
            if (pegs.elementAt(jumps.first).currentStatus == PegStatus.occupied &&
                    pegs.elementAt(jumps.second).currentStatus != PegStatus.occupied) {
                result.add(pegs.elementAt(jumps.second));
            }
        }

        return result;
    }

    public boolean canMove(Peg p) {
        if (p.currentStatus != PegStatus.occupied) {
            return false;
        }

        for (Pair<Integer, Integer> jumps : p.possibleMoves) {
            if (pegs.elementAt(jumps.first).currentStatus == PegStatus.occupied &&
                    pegs.elementAt(jumps.second).currentStatus != PegStatus.occupied) {
                return true;
            }
        }

        return false;
    }

    public Vector<Peg> getVacantPegs() {
        Vector<Peg> result = new Vector<>();
        for (int i = 0; i < pegs.size(); i++) {
            if (pegs.elementAt(i).currentStatus == PegStatus.vacant) {
                result.add(pegs.elementAt(i));
            }
        }

        return result;
    }

}
