package canon.sevenstar.dicedecathlon;

/**
 * Created by canon on 3/27/2017.
 */

public class MinigameModel1500m extends MinigameModel100m {
    @Override
    public void initEvent() {
        diceInSet = 1;
        totalSets = 8;
        super.initEvent();
    }
}
