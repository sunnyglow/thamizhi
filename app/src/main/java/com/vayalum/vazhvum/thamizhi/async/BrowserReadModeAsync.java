package com.vayalum.vazhvum.thamizhi.async;

import android.os.AsyncTask;

import com.vayalum.vazhvum.thamizhi.BrowseModeActivity;

import org.jetwick.snacktory.HtmlFetcher;
import org.jetwick.snacktory.JResult;
import org.jetwick.snacktory.ReFormatOutput;

import java.net.URL;

public class BrowserReadModeAsync extends AsyncTask<URL, String, String> {

    public static String clean_result = "";

    protected String doInBackground(URL... urls) {
        String result = "";
        try {
            HtmlFetcher fetcher = new HtmlFetcher();
            String articleUrl = BrowseModeActivity.url;
            JResult res = fetcher.fetchAndExtract(articleUrl, 10 * 1000, true);
            String text = res.getText();
            String title = res.getTitle();
            String imageUrl = res.getImageUrl();
            result = ReFormatOutput.formateData(res.getTitle(), imageUrl, res.getActualContent());
            clean_result = "";
            clean_result = res.getActualContent();
            //System.out.println(result);
        } catch (Exception exp) {
            exp.printStackTrace();
        }

        return result;
    }
    protected void onProgressUpdate(String... progress) {

    }
    protected void onPostExecute(String result) {

    }
}
