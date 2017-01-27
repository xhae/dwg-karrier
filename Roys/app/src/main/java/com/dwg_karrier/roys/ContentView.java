package com.dwg_karrier.roys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

public class ContentView extends AppCompatActivity {
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.content);
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
      String imgSizeCtrl = "<style>img{display: inline; height: auto; max-width: 100%;}</style>\n"; // fit image to the size of viewer
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
