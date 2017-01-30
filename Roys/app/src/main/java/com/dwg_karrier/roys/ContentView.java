package com.dwg_karrier.roys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class ContentView extends AppCompatActivity {
  private final String imgSizeCtrl = "<style>img{display: inline; height: auto; max-width: 100%;}</style>\n"; // fit image to the size of viewer
  String title;
  String content;
  String url;
  long StartTime;
  long EndTime;
  /*
   * TODO(Juung): get CurTime and finTime so that could use them to calculate rest of the time --> use in next recommendation
   * TODO: think about 'go-back' action (should go back to the first page or the second?)
   */

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.content);

    final Intent getPageInfo = new Intent(this.getIntent());
    title = getPageInfo.getStringExtra("title");
    content = getPageInfo.getStringExtra("content");
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

    StartTime = System.currentTimeMillis();

    Button finishReading = (Button) findViewById(R.id.buttonFinish);
    url = getPageInfo.getStringExtra("url");
    finishReading.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        EndTime = System.currentTimeMillis();
        DataBaseOpenHelper dbHelper = new DataBaseOpenHelper(ContentView.this);
        dbHelper.setIsRead(url, 1);

        // Temporal check for DB and read time
        long readTime = (EndTime - StartTime) / 1000;
        Log.d("readTime", String.valueOf(readTime));
        Toast checkInfo = Toast.makeText(getApplicationContext(), "Congratulations!" + "\n" +
            "You finished reading in " + String.valueOf(readTime) + "sec", Toast.LENGTH_LONG);
        checkInfo.setGravity(Gravity.BOTTOM, 0, 0);
        checkInfo.show();
      }
    });
  }
}