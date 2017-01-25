package com.dwg_karrier.roys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
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

    try {
      /*
       * TODO
       * bring user-clicked URL
       * merge with soyee's code
       */
      // yet, write exact pageUrl
      String pageUrl = "http://www.bloter.net/archives/265787";
      Crawler crawler = new Crawler(pageUrl);
      // show title and content in one page
      // It's better to scroll down in concatenated version
      String title = crawler.getTitle();
      String content = crawler.getContent();
      TextView contentView = (TextView) findViewById(R.id.contentView);
      contentView.setText(title + "\n\n" + content);
      contentView.setMovementMethod(new ScrollingMovementMethod());
    } catch (Exception e) {
      Log.e("Error:", e.getMessage(), e);
      e.printStackTrace();
    }

    Button b = (Button) findViewById(R.id.button4);
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
}