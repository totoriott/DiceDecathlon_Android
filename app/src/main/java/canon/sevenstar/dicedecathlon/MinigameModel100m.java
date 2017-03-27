package canon.sevenstar.dicedecathlon;

import java.util.Random;

/**
 * Created by canon on 3/27/2017.
*/
public class MinigameModel100m implements MinigameModel {
    int[] diceValues;
    DiceState[] diceStates;

    int rerolls;
    int setNumber;
    int diceInSet = 4;
    int totalSets = 2;
    boolean firstRollOfSet;

    // Called when Roll button is pressed
    public void buttonPressedRoll() {
        if (setNumber < totalSets && (firstRollOfSet || rerolls > 0)) {
            if (firstRollOfSet) {
                firstRollOfSet = false;
            } else {
                rerolls--;
            }

            Random rand = new Random(); // TODO: init properly

            // roll dice
            for (int i = 0; i < 8; i++) {
                if (diceStates[i] == DiceState.UNLOCKED) {
                    diceValues[i] = rand.nextInt(6) + 1;
                }
            }
        }
    }

    // Called when Lock button is pressed
    public void buttonPressedLock() {
        if (setNumber < totalSets && !firstRollOfSet) {
            lockCurrentSet();
            setNumber++;
            if (setNumber < totalSets) {
                initForNextSet();
            }
        }
    }

    public void initEvent() {
        setNumber = 0;
        rerolls = 5;

        diceValues = new int[8];
        diceStates = new DiceState[8];
        for (int i = 0; i < 8; i++) {
            diceValues[i] = 1;
            diceStates[i] = DiceState.DISABLED;
        }

        initForNextSet();
    }

    public void lockCurrentSet() {
        for (int i = 0; i < diceInSet; i++) {
            diceStates[setNumber * diceInSet + i] = DiceState.LOCKED;
        }
    }

    public void initForNextSet() {
        firstRollOfSet = true;
        for (int i = 0; i < diceInSet; i++) {
            diceStates[setNumber * diceInSet + i] = DiceState.UNLOCKED;
        }
    }

    public int[] calculateSetScores() {
        int[] scores = new int[totalSets];

        for (int i = 0; i < totalSets; i++) {
            int setScore = 0;
            for (int die = 0; die < diceInSet; die++) {
                int dieIndex = i * diceInSet + die;
                if (diceStates[dieIndex] != DiceState.DISABLED) {
                    if (diceValues[dieIndex] == 6) {
                        setScore -= 6;
                    } else {
                        setScore += diceValues[dieIndex];
                    }
                }
            }
            scores[i] = setScore;
        }
        return scores;
    }

    public int calculateScore() {
        int[] scores = calculateSetScores();
        int score = 0;
        for (int i = 0; i < scores.length; i++) {
            score += scores[i];
        }
        return score;
    }

    public boolean roundDone() {
        return setNumber >= totalSets;
    }

    public int[] getDiceValues() {
        return diceValues;
    }
    public DiceState[] getDiceStates() {
        return diceStates;
    }
}
