package canon.sevenstar.dicedecathlon;

/**
 * Created by canon on 3/27/2017.
 */

public class MinigameModel110mHurdle extends MinigameModel100m {
    @Override
    public void initEvent() {
        diceInSet = 5;
        totalSets = 1;
        super.initEvent();
        rerolls = 4;
    }

    @Override
    public String getMinigameName() { return "110m Hurdles"; };

    @Override
    public int[] calculateSetScores() {
        int[] scores = new int[totalSets];

        for (int i = 0; i < totalSets; i++) {
            int setScore = 0;
            for (int die = 0; die < diceInSet; die++) {
                int dieIndex = i * diceInSet + die;
                setScore += diceValues[dieIndex];
            }
            scores[i] = setScore;
        }
        return scores;
    }
}

