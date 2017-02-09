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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ji hyung Moon <mjihyung@gmail.com>
 * @version 2.0
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
      Log.e("getTitle_error", e.getMessage(), e);
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
      content = jsoupDoc.toString();
    } catch (Exception e) {
      Log.e("getContent_Error", e.getMessage(), e);
      content = null;
    }
    return content;
  }

  public int getWordCount() {
    int wordCount;
    try {
      wordCount = ((Long) this.jsonObject.get("word_count")).intValue();
    } catch (Exception e) {
      Log.e("getWordCount_Error", e.getMessage(), e);
      wordCount = 0;
    }
    return wordCount;
  }

  public String getLeadImgUrl() {
    String imgUrl;
    try {
      imgUrl = (String) this.jsonObject.get("lead_image_url");
    } catch (Exception e) {
      Log.e("getLeadImgUrl_Error", e.getMessage(), e);
      imgUrl = null;
    }
    return imgUrl;
  }

  /**
   * TODO(juung): Bring date of contents
   *
   * <p> Need to be fixed. (return null)
   *
   * <p> In my estimation, it might be API prob or dateformat prob
   *
   * @return string of published date with 'yyyy-MM-dd' format
   */
  public String getDate() {
    String date;
    try {
      SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss"); //2016-09-30T07:00:12.000Z yyyy-MM-dd'T'KK:mm:ss
      Date datePublished = parserSDF.parse((String) this.jsonObject.get("date_published"));
      SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd");
      date = newDate.format(datePublished);
    } catch (Exception e) {
      Log.e("getData_Error:", e.getMessage(), e);
      date = null;
    }
    return date;
  }

  /**
   * Makes possible to use {@link #getJsonResponse(String pageUrl)}
   *
   * <p> Sending request and receiving response via API is impossible in main Thread.
   *
   * <p> AsyncTask runs in the background of main Thread. AsyncTask = Thread + Handler
   *
   * @returns json format response from Mercury-API (detailed response is described in {@link
   * #getJsonResponse(String strUrl)} document.
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
