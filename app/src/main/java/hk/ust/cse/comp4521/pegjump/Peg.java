package hk.ust.cse.comp4521.pegjump;

import android.util.Pair;

import java.util.Random;
import java.util.Vector;

public class Peg {

    public int id;
    public int value;
    public PegStatus currentStatus;
    public Vector<Pair<Integer, Integer>> possibleMoves;

    Peg(int id) {
        possibleMoves = new Vector<>();
        value = generateInitialValue();
        this.id = id;

        switch (id) {
            case 0:
                possibleMoves.add(new Pair<>(1, 2));
                possibleMoves.add(new Pair<>(5, 9));
                break;
            case 1:
                possibleMoves.add(new Pair<>(2, 3));
                possibleMoves.add(new Pair<>(6, 10));
                break;
            case 2:
                possibleMoves.add(new Pair<>(1, 0));
                possibleMoves.add(new Pair<>(3, 4));
                possibleMoves.add(new Pair<>(6, 9));
                possibleMoves.add(new Pair<>(7, 11));
                break;
            case 3:
                possibleMoves.add(new Pair<>(2, 1));
                possibleMoves.add(new Pair<>(7, 10));
                break;
            case 4:
                possibleMoves.add(new Pair<>(3, 2));
                possibleMoves.add(new Pair<>(8, 11));
                break;
            case 5:
                possibleMoves.add(new Pair<>(6, 7));
                possibleMoves.add(new Pair<>(9, 12));
                break;
            case 6:
                possibleMoves.add(new Pair<>(7, 8));
                possibleMoves.add(new Pair<>(10, 13));
                break;
            case 7:
                possibleMoves.add(new Pair<>(6, 5));
                possibleMoves.add(new Pair<>(10, 12));
                break;
            case 8:
                possibleMoves.add(new Pair<>(7, 6));
                possibleMoves.add(new Pair<>(11, 13));
                break;
            case 9:
                possibleMoves.add(new Pair<>(5, 0));
                possibleMoves.add(new Pair<>(6, 2));
                possibleMoves.add(new Pair<>(10, 11));
                possibleMoves.add(new Pair<>(12, 14));
                break;
            case 10:
                possibleMoves.add(new Pair<>(6, 1));
                possibleMoves.add(new Pair<>(7, 3));
                break;
            case 11:
                possibleMoves.add(new Pair<>(7, 2));
                possibleMoves.add(new Pair<>(8, 4));
                possibleMoves.add(new Pair<>(10, 9));
                possibleMoves.add(new Pair<>(13, 14));
                break;
            case 12:
                possibleMoves.add(new Pair<>(9, 5));
                possibleMoves.add(new Pair<>(10, 7));
                break;
            case 13:
                possibleMoves.add(new Pair<>(10, 6));
                possibleMoves.add(new Pair<>(11, 8));
                break;
            case 14:
                possibleMoves.add(new Pair<>(12, 9));
                possibleMoves.add(new Pair<>(13, 11));
                break;
            default:
        }

        currentStatus = PegStatus.vacant;
    }

    private int generateInitialValue() {
        Random rand = new Random();
        return rand.nextInt(10);
    }

    public void applyOperation(int op, int rhs) {
        if (op == Constants.OPERATION_PLUS) {
            value += rhs;
        } else {
            value = Math.abs(value - rhs);
        }
    }
}
