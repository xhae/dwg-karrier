package com.dwg_karrier.roys;

import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/* Add on to listActivity
* Use getTitle(), getContent(), getWordCount() */

public class Crawler {
  JSONObject jsonObject = null;

  public Crawler(String strUrl) {
    jsonObject = getJsonResponse(strUrl);
  }

  private JSONObject getJsonResponse(String pageUrl) {
    try {
      MyAsyncTask myAsyncTask = new MyAsyncTask();
      jsonObject = myAsyncTask.execute(pageUrl).get();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return jsonObject;
  }

  public String getTitle() {
    if (this.jsonObject == null) {
      return null;
    }
    String title;
    try {
      title = (String) this.jsonObject.get("title");
    } catch (Exception e) {
      Log.e("getTitle_error:", e.getMessage(), e);
      e.printStackTrace();
      return null;
    }
    return title;
  }

  public String getContent() {
    if (this.jsonObject == null) {
      return null;
    }
    String content;
    try {
      content = (String) this.jsonObject.get("content");
      Document jsoupDoc = Jsoup.parse(content);
      content = jsoupDoc.text();
    } catch (Exception e) {
      Log.e("getContent_Error:", e.getMessage(), e);
      return null;
    }
    return content;
  }

  public Integer getWordCount() {
    if (this.jsonObject == null) {
      return null;
    }
    Integer wordCount = 0;
    try {
      wordCount = (Integer) this.jsonObject.get("word_count");
    } catch (Exception e) {
      Log.e("getWordCount_Error:", e.getMessage(), e);
      return null;
    }
    return wordCount;
  }

  public class MyAsyncTask extends AsyncTask<String, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(String... strings) {
      JSONObject json = null;
      StringBuilder stringBuilder = new StringBuilder();
      try {
        String strUrl = strings[0];
        URL url = new URL(strUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("x-api-key", "vr2E65ffhRAZlJTvwcrmq72zPo2wcSZuuLtQ4ITc");
        if (conn.getResponseCode() == conn.HTTP_OK) {
          BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
          String line;
          while ((line = br.readLine()) != null) {
            stringBuilder.append(line);
          }
          br.close();
        }
        conn.disconnect();
      } catch (Exception e) {
        Log.e("Error", e.getMessage(), e);
        return null;
      }
      try {
        String jsonStr = stringBuilder.toString();
        JSONParser jsonParser = new JSONParser();
        try {
          json = (JSONObject) jsonParser.parse(jsonStr);
        } catch (Exception e) {
          e.printStackTrace();
        }
        return json;
      } catch (Exception e) {
        e.printStackTrace();
      }
      return json;
    }
  }
}
