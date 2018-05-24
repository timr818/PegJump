//COMP 4521     Name: Alfonso Miguel Pascual Santos-Tankia      Student ID: 20531732          Email: amsaa@connect.ust.hk
//COMP 4521     Name: Timothy Jacob Regan                       Student ID: 20531756          Email: tjregan@connect.ust.hk

package hk.ust.cse.comp4521.pegjump;

import android.graphics.Color;

/**
 * Created by timre on 3/30/2018.
 */

public class Constants {

    public static final int COLOR_SELECTED = Color.argb(100, 255, 255, 255);
    public static final int COLOR_NOT_SELECTED = Color.argb(0, 255, 255, 255);

    public static final int COLOR_VALUE_LABEL_TEXT = Color.argb(255, 255, 255, 255);
    public static final int COLOR_VALUE_LABEL_SHADOW = Color.argb(255, 0, 0, 0);

    public static final int OPERATION_PLUS = 0;
    public static final int OPERATION_MINUS = 1;

    public static final int MIN_PEGS = 9;
    public static final int MAX_PEGS = 14;

    public static final int WINNING_NUMBER = 734;

    public static final int PEG_YELLOW_THRESHOLD = 10;
    public static final int PEG_RED_THRESHOLD = 25;

    public static final String PREFS_FILE = "peg_app_prefs";
    public static final String PREFS_BEST_SCORE = "peg_best_score";
    public static final String PREFS_MUSIC_MUTE = "peg_music_mute";
    public static final String PREFS_SFX_MUTE = "sfx_mute";

    public static final boolean PREFS_DEFAULT_MUSIC_MUTE = false;
    public static final boolean PREFS_DEFAULT_SFX_MUTE = false;
}
