package com.dwg_karrier.roys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class ContentView extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.content);

    final Intent intent = new Intent(this.getIntent());
    final Date finTime = (Date) intent.getSerializableExtra("finTime");
    final Date curTime = new Date(System.currentTimeMillis());

    // get url
    try {
      // yet, write exact pageUrl
      String pageUrl = "https://mercury.postlight.com/parser?url=https://blog.google/products/google-vr/showcase-your-art-new-ways-tilt-brush-toolkit/";
      MyAsyncTask myAsyncTask = new MyAsyncTask();
      myAsyncTask.execute(pageUrl);
    } catch (Exception e) {
      e.printStackTrace();
    }

    Button b = (Button)findViewById(R.id.button4);
    b.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent1 = new Intent(ContentView.this, ListActivity.class);
        intent1.putExtra("finTime", finTime);
        intent1.putExtra("curTime", curTime);
        startActivity(intent1);
        finish();
      }
    });
  }
  public class MyAsyncTask extends AsyncTask<String, Void, String> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
      progressBar.setVisibility(View.VISIBLE);
    }

    // execute in background
    @Override
    protected String doInBackground(String... strings) {
      String contents = "Why this happens";
      StringBuilder stringBuilder = new StringBuilder();
      try {
        URL url = new URL(strings[0]);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("x-api-key", "vr2E65ffhRAZlJTvwcrmq72zPo2wcSZuuLtQ4ITc");
        // if HTML responseCode is 200, then it's okay
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
        JSONObject jsonObject = new JSONObject(jsonStr);
        contents = jsonObject.getString("content");
      } catch (Exception e) {
        e.printStackTrace();
      }
      return contents;
    }

    // after execution
    @Override
    protected void onPostExecute(String response) {
      super.onPostExecute(response);
      if (response == null) {
        response = "No contents";
      }
      ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
      progressBar.setVisibility(View.GONE);
      Log.i("INFO", response);
      TextView responseView = (TextView) findViewById(R.id.textView);
      responseView.setText(response);
    }
  }
}
