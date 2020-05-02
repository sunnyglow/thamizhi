package com.vayalum.vazhvum.thamizhi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;
import java.util.Set;

import static com.vayalum.vazhvum.thamizhi.MainActivity.textToSpeech;

public class DashboardActivity extends AppCompatActivity {

    Boolean voiceFound = false;
    ImageView browse_web;
    ImageView input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        browse_web = findViewById(R.id.browse_web);
        browse_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVoiceStatus();
                Intent browserIntent = new Intent(getBaseContext(), BrowseModeActivity.class);
                startActivity(browserIntent);
            }
        });

        input = (ImageView) findViewById(R.id.input);

        input.setOnClickListener(v ->{
            Intent browserIntent = new Intent(getBaseContext(), InputReadActivity.class);
            startActivity(browserIntent);
        });

        ImageView share = (ImageView) findViewById(R.id.share);
        ImageView like  = (ImageView) findViewById(R.id.like);

        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Text", Toast.LENGTH_SHORT).show();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Thamizhi is an highquality Tamil Text to Speech Application. \n Download from google play store. \n http://play.google.com/store/apps/details?id=com.vayalum.vazhvum.thamizhi");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //v.getId() will give you the image id
                Toast.makeText(v.getContext(), "Like Us..!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=com.vayalum.vazhvum.thamizhi"));
                startActivity(intent);

            }
        });


    }




    public void checkVoiceStatus() {
        Set<Voice> voiceList = textToSpeech.getVoices();
        for (Voice voice : voiceList) {
            if (voice.getName().startsWith("ta")) {
                voiceFound = true;
                break;
            }
        }
        if (!voiceFound) {
            installVoice();
        }
    }

    public void installVoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("தமிழ் ஒலி Install செய்யபடவில்லை.  Install செய்யலாமா..?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Intent intent = new Intent();
                        //intent.setAction("com.android.settings.TTS_SETTINGS");
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //getBaseContext().startActivity(intent);
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

}