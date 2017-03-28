package canon.sevenstar.dicedecathlon;

import java.util.Random;

/**
 * Created by canon on 3/27/2017.
 */

public class MinigameModelShotput extends MinigameModel {
    int[] diceValues;
    DiceState[] diceStates;

    int dieNumber = 0;
    int totalDice = 8;
    int attemptNumber;
    boolean attemptIsActive;
    int totalAttempts = 3;
    int[] attemptScores;

    // Called when Roll button is pressed
    public void buttonPressedRoll() {
        if (attemptIsActive) {
            Random rand = new Random(); // TODO: init properly

            // roll next die
            int nextValue = rand.nextInt(6) + 1;
            diceStates[dieNumber] = DiceState.LOCKED;
            diceValues[dieNumber] = nextValue;

            // add die to score
            attemptScores[attemptNumber] += nextValue;
            if (nextValue == 1) {
                diceStates[dieNumber] = DiceState.INVALID;
                attemptScores[attemptNumber] = 0;
            }

            dieNumber++;
            if (dieNumber == totalDice || nextValue == 1) {
                attemptIsActive = false;
            }
        }
    }

    // Called when Lock button is pressed
    public void buttonPressedLock() {
        if (dieNumber > 0 && attemptNumber < totalAttempts + 1) {
            attemptNumber++;
            initNewAttempt();
        }
    }

    public void initEvent() {
        attemptNumber = 0;
        attemptScores = new int[totalAttempts];
        initNewAttempt();
    }

    public void initNewAttempt() {
        diceValues = new int[8];
        diceStates = new DiceState[8];
        for (int i = 0; i < 8; i++) {
            diceValues[i] = 1;
            diceStates[i] = DiceState.DISABLED;
        }

        attemptIsActive = true;
        dieNumber = 0;
    }

    public int[] calculateSetScores() {
        return attemptScores;
    }

    public int calculateScore() {
        int score = 0;
        for (int i = 0; i < attemptScores.length; i++) {
            if (attemptScores[i] > score) {
                score = attemptScores[i];
            }
        }
        return score;
    }

    public boolean roundDone() {
        return attemptNumber == totalAttempts - 1 && !attemptIsActive;
    }

    public int[] getDiceValues() {
        return diceValues;
    }

    public DiceState[] getDiceStates() {
        return diceStates;
    }

    public String getMinigameName() { return "Shotput"; };

    // TODO: View stuff should technically go in a view class; separate this later
    public HeaderUiInfo getHeaderUiInfo() {
        HeaderUiInfo uiInfo = new HeaderUiInfo();

        // TODO: make this loc
        // display score
        int[] setScores = calculateSetScores();
        String scoreString = "Score: " + calculateScore() + " (";
        for (int i = 0; i < setScores.length; i++) {
            if (i != 0) {
                scoreString += " / ";
            }
            scoreString += setScores[i];
        }
        scoreString += ")";

        String rerollString = "Attempt " + (attemptNumber+1) + " of " + (totalAttempts);

        uiInfo.scoreString = scoreString;
        uiInfo.infoString = rerollString;

        uiInfo.rollButtonString = "Roll";
        uiInfo.lockButtonString = "Stop";
        if (roundDone()) {
            uiInfo.rollButtonEnabled = false;
            uiInfo.lockButtonEnabled = false;
        } else if (!attemptIsActive) {
            //TODO: FIX CRASH//Resources.getSystem().getString(R.string.button_roll);
            uiInfo.lockButtonString = "Next";
            uiInfo.rollButtonEnabled = false;
            uiInfo.lockButtonEnabled = true;
        } else {
            uiInfo.rollButtonEnabled = true;
            uiInfo.lockButtonEnabled = dieNumber > 0;
        }

        return uiInfo;
    }
}
