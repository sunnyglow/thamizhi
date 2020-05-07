package com.vayalum.vazhvum.thamizhi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.vayalum.vazhvum.thamizhi.async.BrowserReadModeAsync;

import static com.vayalum.vazhvum.thamizhi.MainActivity.textToSpeech;

public class ExtendedReadModeActivity extends AppCompatActivity {

    public static boolean speechStopped = false;
    public static WebView readWebView = null;
    public static String webpage_text = "";
    private static int currentLine = 0;
    String lines[];
    private boolean doNothing = false;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private CountDownTimer bannerTimer;
    ImageView play;
    ImageView pause;
    ImageView prev;
    ImageView next;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_read_mode);
        setUpdAdvertisements();
        play = (ImageView) findViewById(R.id.play);
        pause = (ImageView) findViewById(R.id.pause);
        prev = (ImageView) findViewById(R.id.prev);
        next = (ImageView) findViewById(R.id.next);

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
            textToSpeech.stop();
        });

        readWebView = (WebView) findViewById(R.id.extendedReadMode);
        readWebView.setWebViewClient(new TamilTTSWebViewClient());
        WebSettings webSettings = readWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        BrowserReadModeAsync rasync = new BrowserReadModeAsync();
        try {
            String result = rasync.execute().get();
            readWebView.loadDataWithBaseURL(null, result, "text/html", "utf-8", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startSynthesizing() {
        new Thread(()->{
            if (textToSpeech != null) {
                textToSpeech.stop();
                speechStopped = false;
                String texttoSpeak = BrowserReadModeAsync.clean_result;
                lines = texttoSpeak.split("(\\.\\s|\\?|\\!)");
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

                speechStopped = true;
            }
        }).start();
    }

    class TamilTTSWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest requestl) {
            return true;
        }
    }


    public static void highlightText(String text) {
        readWebView.findAllAsync(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechStopped = true;
        if(textToSpeech != null && textToSpeech.isSpeaking()){
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

    @Override
    protected void onResume() {
        super.onResume();
        if(textToSpeech == null){
            MainActivity.initializeSpeechEngine();
        }
    }
}