package canon.sevenstar.dicedecathlon;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int[] imageButtonIds = {R.id.imageButton0, R.id.imageButton1, R.id.imageButton2, R.id.imageButton3,
            R.id.imageButton4, R.id.imageButton5, R.id.imageButton6, R.id.imageButton7};
    ImageButton[] imageButtons;

    int[] diceImageIds = {R.drawable.die_0, R.drawable.die_1, R.drawable.die_2, R.drawable.die_3,
            R.drawable.die_4, R.drawable.die_5,R.drawable.die_6};

    // data models
    DecathlonModel decathlonModel;
    MinigameModel gameModel;

    // used for shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onVerticalShake() {
                buttonPressedRoll(null);
            }

            public void onHorizontalShake() {
                buttonPressedLock(null);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        imageButtons = new ImageButton[8];
        for (int i = 0; i < 8; i++) {
            imageButtons[i] = (ImageButton) findViewById(imageButtonIds[i]);
        }

        decathlonModel = new DecathlonModel();
        initMinigame();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    private void initMinigame() {
        gameModel = decathlonModel.getCurrentMinigame();
        gameModel.initEvent();
        renderUI();
    }

    // Called when Roll button is pressed
    public void buttonPressedRoll(View view) {
        if (gameModel.roundDone() && !decathlonModel.onFinalEvent()) {
            decathlonModel.startNextMinigame();
            initMinigame();
        } else {
            gameModel.buttonPressedRoll();
        }

        renderUI();
    }

    // Called when Lock button is pressed
    public void buttonPressedLock(View view) {
        gameModel.buttonPressedLock();
        renderUI();
    }

    private void renderUI() {
        TextView eventHeaderView = (TextView)findViewById(R.id.eventHeaderView);
        TextView scoreView = (TextView)findViewById(R.id.scoreView);
        TextView infoView = (TextView)findViewById(R.id.infoView);

        // render buttons
        Button rollButton = (Button)findViewById(R.id.button);
        Button lockButton = (Button)findViewById(R.id.button2);

        HeaderUiInfo uiInfo = gameModel.getHeaderUiInfo();
        String eventString = "Round " + (decathlonModel.getMinigameIndex()+1) + ": " + gameModel.getMinigameName();

        eventHeaderView.setText(eventString);
        scoreView.setText(uiInfo.scoreString);
        infoView.setText(uiInfo.infoString);
        rollButton.setEnabled(uiInfo.rollButtonEnabled);
        if (uiInfo.rollButtonString.length() > 0) {
            rollButton.setText(uiInfo.rollButtonString);
        }
        lockButton.setEnabled(uiInfo.lockButtonEnabled);
        if (uiInfo.lockButtonString.length() > 0) {
            lockButton.setText(uiInfo.lockButtonString);
        }

        // TODO: this is kinda a hack
        if (gameModel.roundDone()) {
            rollButton.setText(R.string.button_nextEvent);
            rollButton.setEnabled(true);
        }

        renderDice();
    }

    private void renderDice() {
        DiceState[] diceStates = gameModel.getDiceStates();
        int[] diceValues = gameModel.getDiceValues();

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
