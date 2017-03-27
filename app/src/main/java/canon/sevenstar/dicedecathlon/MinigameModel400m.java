package canon.sevenstar.dicedecathlon;

/**
 * Created by canon on 3/27/2017.
 */

public class MinigameModel400m extends MinigameModel100m {
    @Override
    public void initEvent() {
        diceInSet = 2;
        totalSets = 4;
        super.initEvent();
    }

    @Override
    public String getMinigameName() { return "400m Dash"; };
}
