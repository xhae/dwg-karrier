package com.dwg_karrier.roys;


import static com.dwg_karrier.roys.Authentication.DEFAULTIMGURL;
import static com.dwg_karrier.roys.Authentication.convertStreamToString;
import static com.dwg_karrier.roys.Authentication.countWords;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Recommender implements AsyncResponse {
  final String recommendUrl = "https://sandbox.feedly.com/v3/search/feeds?query=";
  Context mainContext;

  public Recommender(Context mainContext) {
    this.mainContext = mainContext;
  }

  public void withKeywords(String[] keywords) {
    for (String keyword : keywords) {
      String url = recommendUrl + keyword+"&fields=[keywords]";
      DataBaseOpenHelper dbhelper = new DataBaseOpenHelper(mainContext);
      GetRecommendFeedList RR = new GetRecommendFeedList(dbhelper, keyword);
      RR.delegate = this;
      RR.execute(url);
    }
  }

  @Override
  public void processFinish() {
    //Use this for after action
  }

  private class GetRecommendFeedList extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;
    final String url = "https://sandbox.feedly.com/v3/streams/contents?streamId=";
    String keyword;
    ArrayList<String> feedIdUrls = new ArrayList<String>();
    private DataBaseOpenHelper dataBaseOpenHelper;

    public GetRecommendFeedList(DataBaseOpenHelper dbHelper, String keyword) {
      this.keyword = keyword;
      this.dataBaseOpenHelper = dbHelper;
    }

    @Override
    protected String doInBackground(String... params) {
      try {
        URL url = new URL(params[0]);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
        String result = convertStreamToString(stream);

        JSONObject obj = new JSONObject(result);
        JSONArray arr = obj.getJSONArray("results");
        final int len = arr.length();

        //For Test
        //dataBaseOpenHelper.deleteAllPage();
        //dataBaseOpenHelper.getTableAsString();

        final int WORDPERMIN = 40;

        for (int i = 0; i < len; i++) {
          JSONObject feed = arr.getJSONObject(i);
          String feedId = feed.getString("feedId");
          feedIdUrls.add(this.url+feedId);
        }
      } catch (IOException | JSONException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String result) {
      for ( String url : feedIdUrls) {
        RecommendRequest RR = new RecommendRequest(dataBaseOpenHelper);
        RR.execute(url);
      }
      delegate.processFinish();
    }
  }

  private class RecommendRequest extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;
    private DataBaseOpenHelper dataBaseOpenHelper;

    public RecommendRequest(DataBaseOpenHelper dbHelper) {
      this.dataBaseOpenHelper = dbHelper;
    }

    @Override
    protected String doInBackground(String... params) {
      try {
        URL url = new URL(params[0]);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
        String result = convertStreamToString(stream);

        JSONObject obj = new JSONObject(result);
        JSONArray arr = obj.getJSONArray("items");
        final int len = arr.length();

        //For Test
        //dataBaseOpenHelper.deleteAllPage();
        //dataBaseOpenHelper.getTableAsString();

        final int WORDPERMIN = 40;

        for (int i = 0; i < len; i++) {
          JSONObject feed = arr.getJSONObject(i);
          String feedUrl = feed.getString("originId");
          String feedTitle = (String) feed.get("title");
          String keywords = feed.getString("keywords");
          JSONObject feedSummary = feed.getJSONObject("summary");
          String feedContent = (String) feedSummary.get("content");
          int feedExpectedTime = countWords(feedContent) / WORDPERMIN;
          //Please Let me know if you have smart way of getting image url from html :)
          String[] imgUrls = feedContent.split("src=\"");
          String imgUrl = null;
          if (imgUrls.length == 0) {
            imgUrl = DEFAULTIMGURL;
          } else{
            imgUrl = imgUrls[0].split("\">")[0];
          }

          // TODO: add another check url duplication method. (Without database query.)
          if (!dataBaseOpenHelper.isDuplicatedUrl(feedUrl)) {
            dataBaseOpenHelper.insertScriptedData(feedUrl, feedTitle, feedContent, feedExpectedTime, imgUrl, keywords, 1);
          }
        }
        urlConnection.disconnect();

      } catch (IOException | JSONException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String result) {
    }
  }
}
