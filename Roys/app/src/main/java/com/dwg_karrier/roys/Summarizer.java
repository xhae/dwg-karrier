package com.dwg_karrier.roys;

import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author Ji hyung Moon <mjihyung@gmail.com>
 * @version 1.0
 */
public class Summarizer {
  private String analyzerUrl = "https://nehac-ml-analyzer.p.mashape.com/article";
  private String size = "?size=";
  private String text = "&text=";
  private String urlEncodeString;

  /**
   * Initialize
   *
   * <p> summary api requires url-encoded text
   *
   * @param inputText plain text that converted to url-encoded text
   */
  public Summarizer(String inputText) {
    try {
      this.urlEncodeString = URLEncoder.encode(inputText, "ASCII");
    } catch (Exception e) {
      Log.d("Summarizer Initialize", e.getMessage(), e);
    }
  }

  /**
   * Get any length of summary of text
   *
   * @param outputSize length of summarized sentences
   * @return summarized text in Array
   */
  public JSONArray getSummary(int outputSize) {
    try {
      SummaryAsyncTask summaryAsyncTask = new SummaryAsyncTask();
      JSONArray summaryArrayResult = summaryAsyncTask.execute(outputSize).get();
      return summaryArrayResult;
    } catch (Exception e) {
      Log.e("getSummary", e.getMessage(), e);
      return null;
    }
  }

  /**
   * Makes possible to use {@link #getSummary(int outputSize)}
   *
   * <p> Sending request and receiving response via API is impossible in main Thread.
   *
   * <p> AsyncTask runs in the background of main Thread. AsyncTask = Thread + Handler
   *
   * @returns json format response from Mercury-API (detailed response is described in {@link
   *     #getSummary(int outputSize)} document.
   */
  public class SummaryAsyncTask extends AsyncTask<Integer, Void, JSONArray> {

    @Override
    protected JSONArray doInBackground(Integer... integers) {
      StringBuilder stringBuilder = new StringBuilder();
      Integer outputSize = integers[0];
      try {
        URL url = new URL(analyzerUrl + size
            + String.valueOf(outputSize) + text + urlEncodeString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("X-Mashape-Key", "cEFaSsEY22mshE5HLrmxP41OZiJHp1fwKbijsn6kn8qZmVITUB");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() == conn.HTTP_OK) {
          BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
          String line;
          while ((line = br.readLine()) != null) {
            stringBuilder.append(line);
          }
          br.close();
        }
        conn.disconnect();
        JSONArray response = null;
        try {
          String jsonStr = stringBuilder.toString();
          JSONParser jsonParser = new JSONParser();
          response = (JSONArray) jsonParser.parse(jsonStr);
        } catch (Exception e) {
          e.printStackTrace();
        }
        return response;
      } catch (Exception e) {
        Log.e("Error", e.getMessage(), e);
        return null;
      }
    }
  }
}
