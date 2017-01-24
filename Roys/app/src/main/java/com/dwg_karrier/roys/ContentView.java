package com.dwg_karrier.roys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

public class ContentView extends AppCompatActivity {
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.content);
    try {
      /* TODO
      * bring user-clicked URL
      * merge with soyee's code
      */
      // yet, write exact pageUrl
      String pageUrl = "https://mercury.postlight.com/parser?url=http://www.bloter.net/archives/265787";
      Crawler crawler = new Crawler(pageUrl);
      // show title and content in one page
      // It's better to scroll down in concatenated version
      String title = crawler.getTitle();
      String content = crawler.getContent();
      TextView contentView = (TextView) findViewById(R.id.contentView);
      contentView.setText(title+"\n\n"+content);
      contentView.setMovementMethod(new ScrollingMovementMethod());
    } catch (Exception e) {
      Log.e("Error:", e.getMessage(), e);
      e.printStackTrace();
    }
  }
}

