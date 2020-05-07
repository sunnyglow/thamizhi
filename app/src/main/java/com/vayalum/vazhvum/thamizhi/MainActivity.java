package com.vayalum.vazhvum.thamizhi;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    static TextToSpeech textToSpeech;
    static boolean voiceFound = false;
    public static boolean tts_package_found = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<ApplicationInfo> appsInfo = this.getPackageManager().getInstalledApplications(0);
        for (final ApplicationInfo appInfo : appsInfo) {
            //System.out.println("Packages: " + appInfo.packageName);
            if (appInfo.packageName.contains("com.google.android.tts")) {
                tts_package_found = true;
            }
        }

        if (tts_package_found) {
            textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        //textToSpeech.setEngineByPackageName("com.google.android.tts");
                        Set<Voice> voiceList = textToSpeech.getVoices();
                        for (Voice voice : voiceList) {
                            //Log.v("", "Voices available: " + voice.getName());
                            if (voice.getName().startsWith("ta")) {
                                //Log.v("", "Voice available: " + voice.getName());
                                textToSpeech.setVoice(voice);
                                voiceFound = true;
                                break;
                            }
                        }
                    }
                }
            }, "com.google.android.tts");

            new Thread(()->{
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getBaseContext(), DashboardActivity.class);
                startActivity(intent);
            }).start();

        } else {
            installTTSPackage();
        }
    }

    public void installTTSPackage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("தமிழ் ஒலி Install செய்யபடவில்லை.  Install செய்யலாமா..?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.tts")));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.tts")));
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onBackPressed();
                        onBackPressed();
                    }
                }).create().show();
    }

    public static void initializeSpeechEngine() {
        if (textToSpeech != null) {
            Set<Voice> voiceList = textToSpeech.getVoices();
            for (Voice voice : voiceList) {
                //Log.v("", "Voices available: " + voice.getName());
                if (voice.getName().startsWith("ta")) {
                    //Log.v("", "Voice available: " + voice.getName());
                    textToSpeech.setVoice(voice);
                    voiceFound = true;
                    break;
                }
            }
        }
    }
}