package com.dwg_karrier.roys;

import static com.dwg_karrier.roys.ContentSwipe.saveSwipeActivity;
import static com.dwg_karrier.roys.ListActivity.saveActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Date;

public class ContentView extends AppCompatActivity {
  private final String imgSizeCtrl = "<style>img{display: inline; height: auto; max-width: 100%;}</style>\n"; // fit image to the size of viewer
  String title;
  String content;
  String escapedContent;
  String url;
  Date finTime;
  Date curTime;
  long startTime;
  long endTime;

  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.content);
    toolbarSetting();

    final Intent getPageInfo = new Intent(this.getIntent());
    title = getPageInfo.getStringExtra("title");
    content = getPageInfo.getStringExtra("content");
    finTime = (Date) getPageInfo.getSerializableExtra("finTime");
    curTime = (Date) getPageInfo.getSerializableExtra("curTime");

    setView(title, content);

    startTime = System.currentTimeMillis();

    Button finishReading = (Button) findViewById(R.id.finishReading);
    url = getPageInfo.getStringExtra("url");
    finishReading.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        endTime = System.currentTimeMillis();
        DataBaseOpenHelper dbHelper = new DataBaseOpenHelper(ContentView.this);
        dbHelper.setIsRead(url, 1);
        // Temporal check for DB and read time
        long readTime = (endTime - startTime) / 1000;

        Intent backToList = null;
        if (saveActivity != null) {
          saveActivity.finish();
          saveActivity = null;
          backToList = new Intent(ContentView.this, ListActivity.class);
        } else if (saveSwipeActivity != null) {
          saveSwipeActivity.finish();
          saveSwipeActivity = null;
          backToList = new Intent(ContentView.this, ContentSwipe.class);
        }

        backToList.putExtra("finTime", finTime);
        backToList.putExtra("curTime", curTime);
        backToList.putExtra("readTime", String.valueOf(readTime));
        startActivity(backToList);
        finish();
      }
    });
  }

  public void setView(String showTitle, String showContent) {
    String view = showTitle + "\n\n" + imgSizeCtrl + showContent;

    WebView wv = (WebView) findViewById(R.id.contentView);
    wv.setVerticalScrollBarEnabled(true);
    wv.setHorizontalScrollBarEnabled(false);

    final String mimeType = "text/html";
    final String encoding = "UTF-8";
    wv.loadDataWithBaseURL("", view, mimeType, encoding, "");
  }

  private void toolbarSetting() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.contentToolbar);
    toolbar.inflateMenu(R.menu.translatelanguagemenu);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        Translator contentTranslator = new Translator("en", content);
        Translator titleTranslator = new Translator("en", title);
        switch (item.getItemId()) {
          case R.id.chineses:
            setView(titleTranslator.getTranslate("zh-TW"), contentTranslator.getTranslate("zh-TW"));
            return true;
          case R.id.korean:
            setView(titleTranslator.getTranslate("ko"), contentTranslator.getTranslate("ko"));
            return true;
          case R.id.german:
            setView(titleTranslator.getTranslate("de"), contentTranslator.getTranslate("de"));
            return true;
        }
        return true;
      }
    });
  }
}
