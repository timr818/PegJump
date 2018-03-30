package hk.ust.cse.comp4521.pegjump;

/**
 * Created by timre on 3/29/2018.
 */

public class GameController {

    private static boolean soundMuted = false;

    public void toggleMute() {
        soundMuted = !soundMuted;
    }

    public boolean isSoundMuted() {
        return soundMuted;
    }

    public void pegPressed(int id) {
        System.out.println("peg " + id + " was pressed.");
    }
}
