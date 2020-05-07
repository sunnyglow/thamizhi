package com.vayalum.vazhvum.thamizhi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class BrowseModeActivity extends AppCompatActivity {

    EditText urlBar;
    ProgressBar pbWebpage = null;
    WebView mWebView = null;
    public static String url = "";
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private CountDownTimer bannerTimer;


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_mode);

        setUpdAdvertisements();
        ImageView readMode = (ImageView) findViewById(R.id.webpage_listennow_iv);
        readMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                buildNextIntersitialAd();
                Intent intent = new Intent(view.getContext(), ExtendedReadModeActivity.class);
                url = mWebView.getOriginalUrl();
                startActivity(intent);
            }
        });


        urlBar = findViewById(R.id.url_edit_text);
        urlBar.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    goToURLString(urlBar.getText().toString(), getBaseContext());
                    return true;
                }
                return false;
            }
        });

        ImageView clear = (ImageView) findViewById(R.id.clear_edit_text);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                urlBar.setText("");
                urlBar.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(urlBar, 0);
            }
        });

        pbWebpage = (ProgressBar) findViewById(R.id.webpage_progressBar);


        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("https://www.google.in/");

        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView paramAnonymousWebView, int paramAnonymousInt) {
                if (paramAnonymousInt == 100) {
                    pbWebpage.setVisibility(View.GONE);
                    return;
                }
                urlBar.setText(paramAnonymousWebView.getUrl());
                pbWebpage.setVisibility(View.VISIBLE);
                pbWebpage.setProgress(paramAnonymousInt);
            }
        });

        ImageView prev = (ImageView) findViewById(R.id.webpage_prev_iv);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.goBack();
            }
        });

        ImageView next = (ImageView) findViewById(R.id.webpage_next_iv);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.goForward();
            }
        });

        ImageView back = (ImageView) findViewById(R.id.webpage_back_iv);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void goToURLString(String paramString, Context c) {
        if (paramString == null || paramString.trim().isEmpty()) {
            return;
        }
        if (paramString.indexOf(".") < 0) {
            String gooleURL = "https://www.google.com/search?q=" + paramString;
            Toast.makeText(c, gooleURL, Toast.LENGTH_SHORT).show();
            this.mWebView.loadUrl(gooleURL);
            return;
        }
        String str1 = paramString;
        if (paramString.length() < 7) {
            str1 = "https://" + paramString;
        } else {
            String str2 = paramString.substring(0, 7);
            if (!str2.equals("http://")) {
                str1 = paramString;
                if (!str2.equals("https:/")) {
                    str1 = "https://" + paramString;
                }
            }

        }
        Toast.makeText(c, str1, Toast.LENGTH_SHORT).show();
        this.mWebView.loadUrl(str1);
        return;
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

        mInterstitialAd = new InterstitialAd(this);
        String inter_id = getApplicationContext().getResources().getString(R.string.interstitial_id);
        mInterstitialAd.setAdUnitId(inter_id);
        AdRequest intersitialAdRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(intersitialAdRequest);
    }

    private void buildNextIntersitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        String inter_id = getApplicationContext().getResources().getString(R.string.interstitial_id);
        mInterstitialAd.setAdUnitId(inter_id);
        AdRequest intersitialAdRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(intersitialAdRequest);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}