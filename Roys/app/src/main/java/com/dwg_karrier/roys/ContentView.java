package com.dwg_karrier.roys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
      String content = crawler.getContent();
      TextView responseView = (TextView) findViewById(R.id.textView);
      responseView.setText(content);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
