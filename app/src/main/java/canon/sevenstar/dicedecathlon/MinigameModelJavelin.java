package canon.sevenstar.dicedecathlon;

/**
 * Created by canon on 3/27/2017.
 */

public class MinigameModelJavelin extends MinigameModelDiscus {
    @Override
    public void initEvent() {
        totalDice = 6;
        super.initEvent();
    }

    @Override
    public boolean valueIsLockable(int value) {
        return value % 2 == 1;
    }

    @Override
    public String getMinigameName() {
        return "Javelin";
    }
}