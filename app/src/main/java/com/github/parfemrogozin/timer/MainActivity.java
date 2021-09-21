package com.github.parfemrogozin.timer;

import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MyCountDownTimer myCountDownTimer = null;
    private TextView clockDisplay;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clockDisplay = findViewById(R.id.clock_display);
        Button pushUp = findViewById(R.id.button_kliky);
        Button sitUp = findViewById(R.id.button_lehsedy);
        pushUp.setOnClickListener(this);
        sitUp.setOnClickListener(this);
        tts = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.US);
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        if (myCountDownTimer != null)
            myCountDownTimer.cancel();
        tts.stop();
        tts.shutdown();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        long milliseconds = 0;
        switch (view.getId()) {
            case R.id.button_kliky:
                milliseconds = 30000;
                break;
            case R.id.button_lehsedy:
                milliseconds = 60000;
                break;

        }
        if (myCountDownTimer != null)
            myCountDownTimer.cancel();
        myCountDownTimer = new MyCountDownTimer(milliseconds, 1000);
        myCountDownTimer.start();
    }

    private void updateClock(long time_left) {
        int minutes = (int) (time_left / 1000) / 60;
        int seconds = (int) (time_left / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        clockDisplay.setText(timeLeftFormatted);
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long time_left) {
            updateClock(time_left);
        }

        @Override
        public void onFinish() {
            clockDisplay.setText(R.string.stop_text);
            CharSequence say_stop = getString(R.string.stop_text);
            String uniqueID = UUID.randomUUID().toString();
            tts.speak(say_stop, QUEUE_FLUSH, null, uniqueID);
            this.cancel();
        }
    }


}