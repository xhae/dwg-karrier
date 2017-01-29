package com.dwg_karrier.roys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

public class ContentView extends AppCompatActivity {
  private final String imgSizeCtrl = "<style>img{display: inline; height: auto; max-width: 100%;}</style>\n"; // fit image to the size of viewer
  String title;
  String content;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.content);

    Intent intent1 = new Intent(this.getIntent());
    title = intent1.getStringExtra("title");
    content = intent1.getStringExtra("content");
    try {
      String view = title + "\n\n" + imgSizeCtrl + content;

      WebView wv = (WebView) findViewById(R.id.contentView);
      /*
       * viewer settings
       * v.2 might have image scaling (Let's talk about this after using v.1)
       */
      wv.setVerticalScrollBarEnabled(true);
      wv.setHorizontalScrollBarEnabled(false);

      final String mimeType = "text/html";
      final String encoding = "UTF-8";
      wv.loadDataWithBaseURL("", view, mimeType, encoding, "");
    } catch (Exception e) {
      Log.e("Error:", e.getMessage(), e);
      e.printStackTrace();
    }
  }
}