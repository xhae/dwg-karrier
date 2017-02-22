package com.dwg_karrier.roys;

import static com.dwg_karrier.roys.R.layout.auth_dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.apache.commons.collections4.map.DefaultedMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class Authentication {
  static final String DEFAULTIMGURL = "https://blogdotstartlinkdotio.files.wordpress.com/2016/01/12620892_1077588858927687_1133266313_o.jpg?w=490&h=772";
  static final String CLIENTID = "sandbox";
  static final String CLIENTSECRET = "OE12J47X2W5PEF7CKPGZ";
  static final String REDIRECTURI = "http://localhost";
  static final String OAUTHURL = "https://sandbox.feedly.com/v3/auth/auth";
  static final String OAUTHSCOPE = "https://cloud.feedly.com/subscriptions";
  static final String TOKENURL = "https://sandbox.feedly.com/v3/auth/token";
  static final String GRANTTYPE = "authorization_code";
  Context mainContext;

  public Authentication(Context context) {
    mainContext = context;
  }

  static String convertStreamToString(InputStream is) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();

    String line = null;
    try {
      while ((line = reader.readLine()) != null) {
        sb.append(line + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return sb.toString();
  }

  void authenticationAndBringPages() {
    final Dialog authDialog = new Dialog(mainContext);
    authDialog.setContentView(auth_dialog);
    WebView web;
    web = (WebView) authDialog.findViewById(R.id.webv);
    web.getSettings().setJavaScriptEnabled(true);
    web.loadUrl(OAUTHURL + "?redirect_uri=" + REDIRECTURI + "&response_type=code&client_id=" + CLIENTID + "&scope=" + OAUTHSCOPE);
    web.setWebViewClient(new WebViewClient() {
      String authCode;

      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (url.contains("?code=")) {
          Uri uri = Uri.parse(url);
          authCode = uri.getQueryParameter("code");
          authDialog.dismiss();
          new TokenGet(authCode, mainContext).execute();
        } else if (url.contains("error=access_denied")) {
          Log.i("", "ACCESS_DENIED_HERE");
          authDialog.dismiss();
          Toast.makeText(mainContext, "Bringing feedly count failed!", Toast.LENGTH_SHORT).show();
        }
      }
    });
    authDialog.show();
  }

  public JSONObject requestToken(String address, String token, String clientID, String clientSecret, String redirectUri, String grantType) {
    JSONObject jObj = null;
    try {
      String params = "?code=" + token + "&client_id=" + clientID + "&client_secret=" + clientSecret + "&redirect_uri=" + redirectUri + "&grant_type=" + grantType;
      URL url = new URL(address + params);
      HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setRequestMethod("POST");
      InputStream inputStream;

      int status = urlConnection.getResponseCode();
      if (status != HttpURLConnection.HTTP_OK)
        inputStream = urlConnection.getErrorStream();
      else
        inputStream = new BufferedInputStream(urlConnection.getInputStream());

      String result = convertStreamToString(inputStream);
      try {
        jObj = new JSONObject(result);
      } catch (JSONException e) {
        Log.e("JSON Parser", "Error parsing data " + e.toString());
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return jObj;
  }

  private class TokenGet extends AsyncTask<String, String, JSONObject> {
    private String code;
    private ProgressDialog pDialog1;
    private Context mainContext;

    public TokenGet(String tokencode, Context context) {
      code = tokencode;
      mainContext = context;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      pDialog1 = new ProgressDialog(mainContext);
      pDialog1.setMessage("Contacting Feedly ...");
      pDialog1.setIndeterminate(false);
      pDialog1.setCancelable(true);
      pDialog1.show();
    }

    @Override
    protected JSONObject doInBackground(String... args) {
      JSONObject json = requestToken(TOKENURL, code, CLIENTID, CLIENTSECRET, REDIRECTURI, GRANTTYPE);
      return json;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
      if (json != null) {
        try {
          final String URL = "https://sandbox.feedly.com/v3/streams/contents?streamId=user/" + json.getString("id") + "/category/global.all";
          pDialog1.dismiss();
          new GetPageList(new DataBaseOpenHelper(mainContext), mainContext, json.getString("access_token"), pDialog1).execute(URL);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      } else {
        Toast.makeText(mainContext, "Network Error", Toast.LENGTH_SHORT).show();
        pDialog1.dismiss();
      }
    }
  }

  private class GetPageList extends AsyncTask<String, Void, JSONArray> {
    String accessToken;
    private DataBaseOpenHelper dataBaseOpenHelper;
    private Context mainContext;
    private ProgressDialog pDialog2;

    public GetPageList(DataBaseOpenHelper dbHelper, Context context, String token, ProgressDialog dialog) {
      dataBaseOpenHelper = dbHelper;
      mainContext = context;
      accessToken = token;
      pDialog2 = dialog;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      /*
      pDialog2 = new ProgressDialog(mainContext);
      pDialog2.setMessage("Bring Pages ...");
      pDialog2.setIndeterminate(false);
      pDialog2.setCancelable(true);
      pDialog2.show();
      */
    }

    @Override
    protected JSONArray doInBackground(String... params) {
      try {
        URL url = new URL(params[0]);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Authorization", "OAuth " + accessToken);

        InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
        String result = convertStreamToString(stream);

        JSONObject obj = new JSONObject(result);
        JSONArray arr = obj.getJSONArray("items");
        urlConnection.disconnect();
        return arr;
      } catch (IOException | JSONException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(JSONArray arr) {
      final int len = arr.length();
      final int WORDPERMIN = 90;
      try {
        //For Test
        dataBaseOpenHelper.deleteAllPage();
        //dataBaseOpenHelper.getTableAsString();


        final Map<String, Integer> keywordCount = new DefaultedMap<>(0);
        for (int i = 0; i < len; i++) {
          Log.d("iteration", Integer.toString(i));
          final JSONObject feed = arr.getJSONObject(i);
          final String feedUrl = feed.getString("originId");
          if (len < 7) {
            Crawler crawler = new Crawler(feedUrl);
            String feedTitle = crawler.getTitle();
            String feedContent = crawler.getContent();
            int wordCount = crawler.getWordCount();
            int feedExpectedTime = wordCount / WORDPERMIN;
            String imgUrl = crawler.getLeadImgUrl();
            String keywords = null;
            try {
              keywords = feed.getString("keywords");
            } catch (JSONException e) {
              e.printStackTrace();
            }
            for (String keyword : keywords.replace("[\"", "").replace("\"]", "").split("\",\"")) {
              keywordCount.put(keyword, keywordCount.get(keyword) + 1);
            }
            if (!dataBaseOpenHelper.isDuplicatedUrl(feedUrl))
              dataBaseOpenHelper.insertScriptedData(feedUrl, feedTitle, feedContent, feedExpectedTime, imgUrl, keywords, 0);

          } else {
          new Thread(new Runnable() {
            public void run() {
              Crawler crawler = new Crawler(feedUrl);
              String feedTitle = crawler.getTitle();
              String feedContent = crawler.getContent();
              int wordCount = crawler.getWordCount();
              int feedExpectedTime = wordCount / WORDPERMIN;
              String imgUrl = crawler.getLeadImgUrl();
              String keywords = null;
              try {
                keywords = feed.getString("keywords");
              } catch (JSONException e) {
                e.printStackTrace();
              }
              for (String keyword : keywords.replace("[\"", "").replace("\"]", "").split("\",\"")) {
                keywordCount.put(keyword, keywordCount.get(keyword) + 1);
              }
              if (!dataBaseOpenHelper.isDuplicatedUrl(feedUrl))
                dataBaseOpenHelper.insertScriptedData(feedUrl, feedTitle, feedContent, feedExpectedTime, imgUrl, keywords, 0);

            }
          }).start();
          }
        }
        pDialog2.dismiss();
        //for max value
        Set<String> keys = keywordCount.keySet();
        Integer max = -1;
        String maxKey = "";
        for (String key : keys) {
          if (keywordCount.get(key) > max) {
            max = keywordCount.get(key);
            maxKey = key;
          }
        }
        Recommender recommender = new Recommender(mainContext, dataBaseOpenHelper);
        recommender.withKeywords(new String[]{maxKey});
      } catch (Exception e) {
        Log.e("crawler error", e.getMessage(), e);
      }


      Intent startRoys = new Intent(mainContext, MainActivity.class);
      mainContext.startActivity(startRoys);
    }
  }
}
