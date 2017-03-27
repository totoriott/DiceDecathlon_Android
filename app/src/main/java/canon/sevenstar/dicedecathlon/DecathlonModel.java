package canon.sevenstar.dicedecathlon;

/**
 * Created by canon on 3/27/2017.
 */

public class DecathlonModel {
    int eventIndex;
    MinigameModel[] minigameModels;

    public DecathlonModel() {
        eventIndex = 0;

        minigameModels = new MinigameModel[1];
        minigameModels[0] = new MinigameModelHighJump();
    }

    public int getMinigameIndex() {
        return eventIndex;
    }

    public boolean onFinalEvent() {
        return eventIndex == minigameModels.length - 1;
    }

    public void startNextMinigame() {
        if (eventIndex < minigameModels.length) {
            eventIndex++;
        }
    }

    public MinigameModel getCurrentMinigame() {
        if (eventIndex >= minigameModels.length) {
            return null;
        }

        return minigameModels[eventIndex];
    }
}
