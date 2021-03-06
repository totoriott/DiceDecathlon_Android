package canon.sevenstar.dicedecathlon;

import java.util.Random;

/**
 * Created by canon on 3/27/2017.
 */

public class MinigameModelDiscus extends MinigameModel {
    int[] diceValues;
    DiceState[] diceStates;

    int totalDice = 5;
    int attemptNumber;
    boolean attemptIsActive;
    boolean firstRoll;
    int totalAttempts = 3;
    int[] attemptScores;
    int diceLockedThisTime;

    public boolean valueIsLockable(int value) {
        return value % 2 == 0;
    }

    // Called when Roll button is pressed
    public void buttonPressedRoll() {
        if (attemptIsActive && (diceLockedThisTime > 0 || firstRoll)) {
            Random rand = new Random(); // TODO: init properly

            int attemptScore = 0;
            boolean lockableDieFound = false;
            for (int i = 0; i < totalDice; i++) {
                if (diceStates[i] == DiceState.LOCKED) {
                    diceStates[i] = DiceState.FROZEN;
                } else if (diceStates[i] == DiceState.UNLOCKED || diceStates[i] == DiceState.INVALID) {
                    diceValues[i] = rand.nextInt(6) + 1;
                    if (valueIsLockable(diceValues[i])) {
                        lockableDieFound = true;
                        diceStates[i] = DiceState.UNLOCKED;
                    } else {
                        diceStates[i] = DiceState.INVALID;
                    }
                }
            }

            if (!lockableDieFound) {
                attemptScores[attemptNumber] = 0;
                attemptIsActive = false;
            }

            diceLockedThisTime = 0;
            firstRoll = false;
        }
    }

    // Called when Lock button is pressed
    public void buttonPressedLock() {
        if ((attemptNumber < totalAttempts && diceLockedThisTime > 0) || (!attemptIsActive && !roundDone())) {
            attemptIsActive = false;
            if (attemptNumber < totalAttempts - 1) {
                attemptNumber++;
                initNewAttempt();
            }
        }
    }

    public void buttonPressedDie(int dieIndex) {
        if (attemptIsActive && !firstRoll) {
            if (!valueIsLockable(diceValues[dieIndex])) {
                return;
            }

            if (diceStates[dieIndex] == DiceState.UNLOCKED) {
                diceStates[dieIndex] = DiceState.LOCKED;
                diceLockedThisTime++;
                attemptScores[attemptNumber] += diceValues[dieIndex];
            } else if (diceStates[dieIndex] == DiceState.LOCKED) {
                diceStates[dieIndex] = DiceState.UNLOCKED;
                diceLockedThisTime--;
                attemptScores[attemptNumber] -= diceValues[dieIndex];
            }
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

        for (int i = 0; i < totalDice; i++) {
            diceStates[i] = DiceState.UNLOCKED;
            diceValues[i] = 1;
        }

        attemptIsActive = true;
        firstRoll = true;
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
        return attemptNumber >= totalAttempts - 1 && !attemptIsActive;
    }

    public int[] getDiceValues() {
        return diceValues;
    }

    public DiceState[] getDiceStates() {
        return diceStates;
    }

    public String getMinigameName() { return "Discus"; };

    public boolean areAllDiceLocked() {
        for (int i = 0; i < totalDice; i++) {
            if (diceStates[i] == DiceState.UNLOCKED || diceStates[i] == DiceState.INVALID) {
                return false;
            }
        }

        return true;
    }

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
        if (attemptIsActive) {
            if (firstRoll) {
                rerollString += "; roll to begin";
            } else if (diceLockedThisTime == 0) {
                rerollString += "; select at least one die to lock";
            } else {
                rerollString += "; current score " + attemptScores[attemptNumber];
            }
        } else if (attemptScores[attemptNumber] == 0) {
            rerollString += "; fouled";
        }

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
            uiInfo.rollButtonEnabled = (firstRoll || diceLockedThisTime > 0) && !areAllDiceLocked();
            uiInfo.lockButtonEnabled = diceLockedThisTime > 0;
        }

        return uiInfo;
    }
}
