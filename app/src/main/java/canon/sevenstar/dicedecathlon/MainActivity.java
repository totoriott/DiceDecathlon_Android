package canon.sevenstar.dicedecathlon;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

enum DiceState {
    DISABLED, FROZEN, LOCKED, UNLOCKED
}

public class MainActivity extends AppCompatActivity {
    int[] imageButtonIds = {R.id.imageButton0, R.id.imageButton1, R.id.imageButton2, R.id.imageButton3,
            R.id.imageButton4, R.id.imageButton5, R.id.imageButton6, R.id.imageButton7};
    ImageButton[] imageButtons;

    int[] diceImageIds = {R.drawable.die_0, R.drawable.die_1, R.drawable.die_2, R.drawable.die_3,
            R.drawable.die_4, R.drawable.die_5,R.drawable.die_6};

    // data models
    int[] diceValues;
    DiceState[] diceStates;

    // TODO: move game specific stuff around
    int rerolls;
    int setNumber;
    int diceInSet = 4;
    int totalSets = 2;
    boolean firstRollOfSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        imageButtons = new ImageButton[8];
        for (int i = 0; i < 8; i++) {
            imageButtons[i] = (ImageButton) findViewById(imageButtonIds[i]);
        }

        initEvent();
        renderUI();
    }

    // Called when Roll button is pressed
    public void buttonPressedRoll(View view) {
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
                    diceValues[i] = rand.nextInt(6)+1;
                }
            }

            renderUI();
        }
    }

    // Called when Lock button is pressed
    public void buttonPressedLock(View view) {
        if (setNumber < totalSets && !firstRollOfSet) {
            lockCurrentSet();
            setNumber++;
            if (setNumber < totalSets) {
                initForNextSet();
            }
        }

        renderUI();
    }

    private void initEvent() {
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

    private void lockCurrentSet() {
        for (int i = 0; i < diceInSet; i++) {
            diceStates[setNumber*diceInSet+i] = DiceState.LOCKED;
        }
    }

    private void initForNextSet() {
        firstRollOfSet = true;
        for (int i = 0; i < diceInSet; i++) {
            diceStates[setNumber*diceInSet+i] = DiceState.UNLOCKED;
        }
    }

    private int[] calculateSetScores() {
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

    private int calculateScore() {
        int[] scores = calculateSetScores();
        int score = 0;
        for (int i = 0; i < scores.length; i++) {
            score += scores[i];
        }
        return score;
    }

    private boolean roundDone() {
        return setNumber >= totalSets;
    }

    private void renderUI() {
        TextView scoreView = (TextView)findViewById(R.id.scoreView);
        TextView infoView = (TextView)findViewById(R.id.infoView);

        // TODO: make this loc
        // display score
        int[] setScores = calculateSetScores();
        String scoreString = "Score: ";
        for (int i = 0; i < setScores.length; i++) {
            if (i != 0) {
                scoreString += " + ";
            }
            scoreString += setScores[i];
        }
        scoreString += " = " + calculateScore();
        scoreView.setText(scoreString);

        // display reroll count
        infoView.setText("Rerolls left: " + rerolls);

        // render buttons
        Button rollButton = (Button)findViewById(R.id.button);
        Button lockButton = (Button)findViewById(R.id.button2);
        if (roundDone()) {
            rollButton.setEnabled(false);
            lockButton.setEnabled(false);
        } else if (firstRollOfSet) {
            rollButton.setText(R.string.button_roll);
            rollButton.setEnabled(true);
            lockButton.setEnabled(false);
        } else {
            rollButton.setText(R.string.button_reroll);
            rollButton.setEnabled(rerolls > 0);
            lockButton.setEnabled(true);
        }

        renderDice();
    }

    private void renderDice() {
        // TODO: fix dice Ypos
        // render dice
        for (int i = 0; i < 8; i++) {
            imageButtons[i].setImageResource(diceImageIds[0]);

            // change background color depending on state
            switch (diceStates[i]) {
                case DISABLED:
                    imageButtons[i].setBackgroundColor(Color.DKGRAY);
                    break;

                case FROZEN:
                    imageButtons[i].setBackgroundColor(Color.DKGRAY);
                    break;

                case LOCKED:
                    imageButtons[i].setBackgroundColor(Color.YELLOW);
                    break;

                case UNLOCKED:
                    imageButtons[i].setBackgroundColor(Color.WHITE);
                    break;
            }

            // render dice value as appropriate
            if (diceStates[i] != DiceState.DISABLED) {
                if (diceValues[i] >= 1 && diceValues[i] <= 6) {
                    imageButtons[i].setImageResource(diceImageIds[diceValues[i]]);
                }
            }
        }
    }
}
