package com.vayalum.vazhvum.thamizhi;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import static com.vayalum.vazhvum.thamizhi.MainActivity.textToSpeech;

public class InputReadActivity extends AppCompatActivity {

    public static boolean speechStopped = false;;
    private static int currentLine = 0;
    EditText inputText;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private CountDownTimer bannerTimer;
    ImageView play;
    ImageView pause;
    ImageView prev;
    ImageView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_read);
        setUpdAdvertisements();
        play = (ImageView) findViewById(R.id.play);
        pause = (ImageView) findViewById(R.id.pause);
        prev = (ImageView) findViewById(R.id.prev);
        next = (ImageView) findViewById(R.id.next);
        inputText = (EditText) findViewById(R.id.textArea);

        play.setOnClickListener(v -> {
            speechStopped = false;
            startSynthesizing();
        });
        pause.setOnClickListener(v -> {
            textToSpeech.stop();
            speechStopped = true;
        });
        prev.setOnClickListener(v -> {
            if (textToSpeech != null && textToSpeech.isSpeaking()) {
                if (currentLine > 1) {
                    currentLine -= 2;
                }
                textToSpeech.stop();
            }
        });

        next.setOnClickListener(v -> {
            if (currentLine > 0) {
                currentLine++;
            }
            textToSpeech.stop();
        });
    }

    public void startSynthesizing() {
        new Thread(() -> {
            if (textToSpeech != null) {
                textToSpeech.stop();
                speechStopped = false;
                String texttoSpeak = inputText.getText().toString();
                String lines[] = texttoSpeak.split("(\\.|\\?|\\!)");
                currentLine = 0;
                for (; currentLine < lines.length; ) {
                    if (speechStopped) {
                        break;
                    }
                    if (!textToSpeech.isSpeaking()) {
                        //highlightText(lines[currentLine]);
                        CharSequence charSequence = lines[currentLine];
                        textToSpeech.setSpeechRate(1.0f);
                        textToSpeech.speak(charSequence, TextToSpeech.QUEUE_ADD, null, "thamizhi");
                        currentLine++;
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechStopped = true;
        if (textToSpeech != null && textToSpeech.isSpeaking()) {
            textToSpeech.stop();
        }
    }

    private void setUpdAdvertisements() {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        bannerTimer = new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
                start();
            }
        }.start();
    }
}