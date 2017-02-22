package com.dwg_karrier.roys;

import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
  private final static String KEY = "key=AIzaSyDaveRm4x7EzZcBw_Ulxzxf7Q8RMR14_aE"; // v2: AIzaSyAUeWE4PYyB4JOtEsUZYwMit1OvAiAd5_E
  private final static String SOURCE = "&source=";
  private final static String TARGET = "&target=";
  private final static String QUERY = "&q=";
  public String source;
  public String urlEncodeString;

  /**
   * Initialize
   *
   * <p> Change simple text format to utf-8 encoded text
   *
   * @param source    original language (e.g., en)
   * @param inputText simple text
   */
  public Translator(String source, String inputText) {
    try {
      this.source = source;
      this.urlEncodeString = URLEncoder.encode(inputText, "UTF-8");
    } catch (Exception e) {
      Log.e("Translator Initialize", e.getMessage(), e);
    }
  }

  /**
   * get translation result
   *
   * @param target target language (e.g., ru)
   * @return translated text
   */
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
      String target = strings[0];
      StringBuilder stringBuilder = new StringBuilder();
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
        }
        conn.disconnect();
      } catch (Exception e) {
        Log.e("Error", e.getMessage(), e);
        return null;
      }
      JSONObject json;
      String translatedText = "";
      try {
        String jsonStr = stringBuilder.toString();
        JSONParser jsonParser = new JSONParser();
        json = (JSONObject) jsonParser.parse(jsonStr);
        JSONObject data = (JSONObject) json.get("data");
        JSONArray translations = (JSONArray) data.get("translations");
        JSONObject getTranslation = (JSONObject) translations.get(0);
        translatedText = (String) getTranslation.get("translatedText");
      } catch (Exception e) {
        e.printStackTrace();
      }
      return translatedText;
    }
  }
}