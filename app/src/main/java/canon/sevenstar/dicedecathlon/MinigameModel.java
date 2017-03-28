package canon.sevenstar.dicedecathlon;

import java.util.Random;

/**
 * Created by canon on 3/27/2017.
*/
public abstract class MinigameModel {
    // data models
    public abstract int[] getDiceValues();
    public abstract DiceState[] getDiceStates();

    // Called when Roll button is pressed
    public abstract void buttonPressedRoll();

    // Called when Lock button is pressed
    public abstract void buttonPressedLock();

    public abstract void initEvent();

    public abstract boolean roundDone();

    public abstract HeaderUiInfo getHeaderUiInfo();

    public abstract String getMinigameName();

    public void buttonPressedDie(int dieIndex) {};
}
