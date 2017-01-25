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

/**
 * @author Ji hyung Moon <mjihyung@gmail.com>
 * @version 1.0
 */
public class Crawler {
  JSONObject jsonObject = null;
  String mercuryUrlTemplate = "https://mercury.postlight.com/parser?url=";

  /**
   * Initialize
   *
   * <p> Use {@link #getJsonResponse(String pageUrl)} to get page information
   */
  public Crawler(String pageUrl) {
    jsonObject = getJsonResponse(pageUrl);
  }

  /**
   * Get page information via Mercury-API
   *
   * <p> Format of page information is json.
   *
   * <p> Json contains title, content, date_published, lead_image_url, dek, url, domain, excerpt,
   * word_count, direction, total_pages, rendered_pages, next_page_url
   *
   * @param pageUrl exact url of recommended page
   * @return JsonObject contains page information
   */
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
    String title;
    try {
      title = (String) this.jsonObject.get("title");
    } catch (Exception e) {
      Log.e("getTitle_error:", e.getMessage(), e);
      e.printStackTrace();
      title = null;
    }
    return title;
  }

  /**
   * Returns only text content.
   *
   * <p> Content is consisted of image, image caption, and main story. This method shows image
   * caption and main story which is text.
   *
   * <p> Need to be upgraded to show image
   *
   * @return text format of content
   */
  public String getContent() {
    String content;
    try {
      content = (String) this.jsonObject.get("content");
      Document jsoupDoc = Jsoup.parse(content);
      content = jsoupDoc.text();
    } catch (Exception e) {
      Log.e("getContent_Error:", e.getMessage(), e);
      content = null;
    }
    return content;
  }

  public Integer getWordCount() {
    Integer wordCount = 0;
    try {
      wordCount = (Integer) this.jsonObject.get("word_count");
    } catch (Exception e) {
      Log.e("getWordCount_Error:", e.getMessage(), e);
      wordCount = null;
    }
    return wordCount;
  }

  /**
   * Makes possible to use {@link #getJsonResponse(String pageUrl)}
   *
   * <p> Sending request and receiving response via API is impossible in main Thread.
   *
   * <p> AsyncTask runs in the background of main Thread. AsyncTask = Thread + Handler
   *
   * @returns json format response from Mercury-API (detailed response is described in {@link
   *     #getJsonResponse(String strUrl)} document.
   */
  public class MyAsyncTask extends AsyncTask<String, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(String... strings) {
      StringBuilder stringBuilder = new StringBuilder();
      try {
        String pageUrl = strings[0];
        String strUrl = mercuryUrlTemplate + pageUrl;
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
      JSONObject json = null;
      try {
        String jsonStr = stringBuilder.toString();
        JSONParser jsonParser = new JSONParser();
        json = (JSONObject) jsonParser.parse(jsonStr);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return json;
    }
  }
}