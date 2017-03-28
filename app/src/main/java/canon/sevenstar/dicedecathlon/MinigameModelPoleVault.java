package canon.sevenstar.dicedecathlon;

import java.util.Random;

/**
 * Created by canon on 3/27/2017.
 */

public class MinigameModelPoleVault extends MinigameModel {

    int[] diceValues;
    DiceState[] diceStates;

    int totalDice;
    int startDice = 2;
    int maxDice = 8;
    int score;
    boolean stillAlive;
    boolean selectingHeight;
    boolean selectingDice;
    int barHeight;
    int attemptNumber;
    int totalAttempts = 3;
    int lastRoll;

    int startBarHeight = 10;
    int maxBarHeight = 48;
    int barIncrement = 2;

    // Called when Roll button is pressed
    public void buttonPressedRoll() {
        if (!stillAlive) {
            return;
        }

        if (selectingHeight) {
            selectingHeight = false;
            selectingDice = true;
            attemptNumber = 0;
            totalDice = (int)Math.ceil(barHeight / 6.0); // give you the minimum number of dice you could succeed with
            initDice();
        } else if (selectingDice) {
            selectingDice = false;
        }
        else {
            Random rand = new Random(); // TODO: init properly

            // roll next die
            int sumOfRoll = 0;
            boolean faulted = false;
            for (int i = 0; i < totalDice; i++) {
                int nextValue = rand.nextInt(6) + 1;
                diceStates[i] = DiceState.UNLOCKED;
                diceValues[i] = nextValue;
                if (nextValue == 1) {
                    diceStates[i] = DiceState.INVALID;
                    faulted = true;
                }

                sumOfRoll += nextValue;
            }
            if (faulted) {
                sumOfRoll = 0;
            }
            lastRoll = sumOfRoll;

            // TODO: if you beat roll, then yay
            if (sumOfRoll >= barHeight) {
                score = barHeight;
                selectingHeight = true;
                if (score == maxBarHeight) {
                    stillAlive = false;
                } else {
                    buttonPressedLock(); // increment height lazily
                }
            } else {
                attemptNumber++;
                if (attemptNumber == totalAttempts) {
                    stillAlive = false;
                }
            }
            // TODO: if you're out of attempts, then boo
        }
    }

    public void initDice() {
        diceValues = new int[8];
        diceStates = new DiceState[8];
        for (int i = 0; i < 8; i++) {
            diceValues[i] = 1;
            diceStates[i] = DiceState.DISABLED;
        }
    }

    // Called when Lock button is pressed
    public void buttonPressedLock() {
        if (!stillAlive) {
            return;
        }

        if (selectingHeight && barHeight + barIncrement <= maxBarHeight) {
            barHeight += barIncrement;
        } else if (selectingDice && totalDice < maxDice) {
            totalDice++;
        }
    }

    public void initEvent() {
        barHeight = startBarHeight;
        stillAlive = true;
        selectingHeight = true;
        selectingDice = false;
        initDice();
    }

    public int calculateScore() {
        return score;
    }

    public boolean roundDone() {
        return !stillAlive;
    }

    public int[] getDiceValues() {
        return diceValues;
    }

    public DiceState[] getDiceStates() {
        return diceStates;
    }

    public String getMinigameName() { return "Pole Vault"; };

    // TODO: View stuff should technically go in a view class; separate this later
    public HeaderUiInfo getHeaderUiInfo() {
        HeaderUiInfo uiInfo = new HeaderUiInfo();

        // TODO: make this loc
        // display score
        String scoreString = "Score: " + calculateScore();

        String rerollString = "";
        if (!selectingHeight && !selectingDice) {
            rerollString = (totalAttempts - attemptNumber) + " attempt(s) left to roll " + barHeight;
            if (attemptNumber != 0) {
                if (lastRoll == 0) {
                    rerollString = "Faulted; " + rerollString;
                } else {
                    rerollString = "Rolled " + lastRoll + "; " + rerollString;
                }

            }
        } else if (selectingHeight && score > 0) {
            rerollString = "Rolled " + lastRoll + " and cleared bar " + score;
        } else if (selectingDice) {
            rerollString = "Select number of dice to roll for bar " + barHeight + " (max " + maxDice + ")";
        }

        uiInfo.scoreString = scoreString;
        uiInfo.infoString = rerollString;

        if (selectingHeight) {
            uiInfo.rollButtonString = "Try " + barHeight;
            uiInfo.lockButtonString = "Skip " + barHeight;
            uiInfo.rollButtonEnabled = true;
            uiInfo.lockButtonEnabled = barHeight + barIncrement <= maxBarHeight;
        } else if (selectingDice) {
            uiInfo.rollButtonString = "Roll " + totalDice;
            uiInfo.lockButtonString = "Add Die";
            uiInfo.rollButtonEnabled = true;
            uiInfo.lockButtonEnabled = totalDice < maxDice;
        } else {
            uiInfo.rollButtonString = "Roll";
            uiInfo.rollButtonEnabled = stillAlive;
            uiInfo.lockButtonEnabled = false;
        }

        return uiInfo;
    }
}
