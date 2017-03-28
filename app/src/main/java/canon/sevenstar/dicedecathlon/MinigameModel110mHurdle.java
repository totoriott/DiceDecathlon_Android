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
    protected boolean sixesAreBad() { return false; }
}

