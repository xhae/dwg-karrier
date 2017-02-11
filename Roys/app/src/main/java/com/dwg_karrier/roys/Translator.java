package com.dwg_karrier.roys;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;
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
public class Translator {
  private final static String TRANSLATOR_URL = "https://translation.googleapis.com/language/translate/v2?";
  private final static String KEY = "key=AIzaSyCsQ8DSMYk10LfU_aAdRkYQSw6gRKK_9Xs";
  private final static String SOURCE = "&source=";
  private final static String TARGET = "&target=";
  private final static String QUERY = "&q=";
  public String source;
  public String urlEncodeString;

  public Translator(String source, String inputText) {
    try {
      this.source = source;
      this.urlEncodeString = URLEncoder.encode(inputText, "UTF-8");
    } catch (Exception e) {
      Log.e("Translator Initialize", e.getMessage(), e);
    }
  }

  public String getTranslate(String target) {
    try {
      Translator.TranslateAsyncTask translateAsyncTask = new Translator.TranslateAsyncTask();
      String translatedText = translateAsyncTask.execute(target).get();
      return translatedText;
    } catch (Exception e) {
      Log.e("getTranslate", e.getMessage(), e);
      return null;
    }
  }

  public class TranslateAsyncTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
      StringBuilder stringBuilder = new StringBuilder();
      String target = strings[0];
      try {
        URL url = new URL(TRANSLATOR_URL + KEY + SOURCE + source + TARGET + target + QUERY + urlEncodeString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn.getResponseCode() == conn.HTTP_OK) {
          BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
          String line;
          while ((line = br.readLine()) != null) {
            stringBuilder.append(line);
          }
          br.close();
        } else {
          String error = Integer.toString(conn.getResponseCode());
          Log.d("HTTP "+ error, conn.getResponseMessage());
        }
        conn.disconnect();
        String translatedText = "";
        try {
          String jsonStr = stringBuilder.toString();
          JSONParser jsonParser = new JSONParser();
          JSONObject response = (JSONObject) jsonParser.parse(jsonStr);
          JSONObject data = (JSONObject) response.get("data");
          JSONArray translations = (JSONArray) data.get("translations");
          JSONObject translatedResult = (JSONObject) translations.get(0);
          translatedText = translatedResult.get("translated Text").toString();
        } catch (Exception e) {
          e.printStackTrace();
        }
        return translatedText;
      } catch (Exception e) {
        Log.e("translateAPI connection", e.getMessage(), e);
        return null;
      }
    }
  }
}