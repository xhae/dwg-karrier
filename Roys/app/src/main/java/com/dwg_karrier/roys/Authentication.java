package com.dwg_karrier.roys;

import static com.dwg_karrier.roys.R.layout.auth_dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
    private ProgressDialog pDialog;
    private Context mainContext;

    public TokenGet(String tokencode, Context context) {
      code = tokencode;
      mainContext = context;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      pDialog = new ProgressDialog(mainContext);
      pDialog.setMessage("Contacting Feedly ...");
      pDialog.setIndeterminate(false);
      pDialog.setCancelable(true);
      pDialog.show();
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
          new GetPageList(new DataBaseOpenHelper(mainContext), mainContext, json.getString("access_token"), pDialog).execute(URL);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      } else {
        Toast.makeText(mainContext, "Network Error", Toast.LENGTH_SHORT).show();
        pDialog.dismiss();
      }
    }
  }

  private class GetPageList extends AsyncTask<String, Void, String> {
    String accessToken;
    private DataBaseOpenHelper dataBaseOpenHelper;
    private Context mainContext;
    private ProgressDialog pDialog;

    public GetPageList(DataBaseOpenHelper dbHelper, Context context, String token, ProgressDialog dialog) {
      dataBaseOpenHelper = dbHelper;
      mainContext = context;
      accessToken = token;
      pDialog = dialog;
    }

    private int countWords(String html) throws Exception {
      org.jsoup.nodes.Document dom = Jsoup.parse(html);
      String text = dom.text();

      return text.split(" ").length;
    }

    @Override
    protected String doInBackground(String... params) {
      try {
        URL url = new URL(params[0]);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Authorization", "OAuth " + accessToken);

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
          JSONObject feedSummary = feed.getJSONObject("summary");
          String feedContent = (String) feedSummary.get("content");
          int feedExpectedTime = countWords(feedContent) / WORDPERMIN;

          //Please Let me know if you have smart way of getting image url from html :)
          String imgUrl = feedContent.split("src=\"")[1].split("\">")[0];
          if (imgUrl == null) {
            imgUrl = DEFAULTIMGURL;
          }

          // TODO: add another check url duplication method. (Without database query.)
          if (!dataBaseOpenHelper.isDuplicatedUrl(feedUrl)) {
            dataBaseOpenHelper.insertScriptedData(feedUrl, feedTitle, feedContent, feedExpectedTime, imgUrl);
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
      pDialog.dismiss();
      Toast toast = Toast.makeText(mainContext,
          "Bring the pages from your feedly account", Toast.LENGTH_LONG);
      toast.setGravity(Gravity.CENTER, 0, 0);
      toast.show();
    }
  }
}
