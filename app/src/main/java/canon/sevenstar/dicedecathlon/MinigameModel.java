package canon.sevenstar.dicedecathlon;

import java.util.Random;

/**
 * Created by canon on 3/27/2017.
*/
public interface MinigameModel {
    // data models
    public int[] getDiceValues();
    public DiceState[] getDiceStates();

    // Called when Roll button is pressed
    public void buttonPressedRoll();

    // Called when Lock button is pressed
    public void buttonPressedLock();

    public void initEvent();

    public int[] calculateSetScores();

    public int calculateScore();

    public boolean roundDone();

    public HeaderUiInfo getHeaderUiInfo();
}
