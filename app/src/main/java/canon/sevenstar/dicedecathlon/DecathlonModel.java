package canon.sevenstar.dicedecathlon;

/**
 * Created by canon on 3/27/2017.
 */

public class DecathlonModel {
    int eventIndex;
    MinigameModel[] minigameModels;

    public DecathlonModel() {
        eventIndex = 0;

        minigameModels = new MinigameModel[4];
        minigameModels[0] = new MinigameModel100m();
        minigameModels[1] = new MinigameModel400m();
        minigameModels[2] = new MinigameModel1500m();
        minigameModels[3] = new MinigameModel110mHurdle();
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
