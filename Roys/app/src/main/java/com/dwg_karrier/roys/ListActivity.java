package com.dwg_karrier.roys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class ListActivity extends AppCompatActivity {
  public static Activity saveActivity;
  ListView lv;
  ArrayList<ScriptedData> data;
  Date finTime; // expected finish time
  Date curTime; // current time
  double duration; // time duration between current_time and finish time

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.list);
    saveActivity = ListActivity.this;

    final int minute = 60000;
    Intent getTimeInfo = new Intent(this.getIntent());
    finTime = (Date) getTimeInfo.getSerializableExtra("finTime");
    curTime = (Date) getTimeInfo.getSerializableExtra("curTime");

    duration = (finTime.getTime() - curTime.getTime()) / minute;
    /*
     * TODO: get Url from DB
     */
    data = callUrl(); // get Url from test DB
    lv = (ListView) findViewById(R.id.listView);
    lv.setAdapter(new ListViewAdapter(this, R.layout.item, data));

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent openSelectedPage = new Intent(ListActivity.this, ContentView.class);
        openSelectedPage.putExtra("finTime", finTime);
        openSelectedPage.putExtra("curTime", curTime);

        ScriptedData pageInfo = data.get(position);
        String title = pageInfo.getTitle();
        String content = pageInfo.getContent();
        String url = pageInfo.getUrl();

        openSelectedPage.putExtra("title", title);
        openSelectedPage.putExtra("content", content);
        openSelectedPage.putExtra("url", url);
        startActivity(openSelectedPage);
        finish();
      }
    });

    // from ContentView
    Intent getReadTime = new Intent(this.getIntent());
    String readTime =  getReadTime.getStringExtra("readTime");
    if (readTime != null) {
      Toast checkInfo = Toast.makeText(getApplicationContext(), "Congratulations!" + "\n" +
          "You finished reading in " + readTime + "sec", Toast.LENGTH_LONG);
      checkInfo.setGravity(Gravity.CENTER, 0, 0);
      checkInfo.show();
    }
  }

  private ArrayList<ScriptedData> callUrl() {
    DataBaseOpenHelper dbHelper = new DataBaseOpenHelper(this);
    ArrayList ret = new ArrayList<ScriptedData>();
    double tempTime;
    final int wordsperMin = 180;
    ArrayList<ScriptedURL> wholeList = dbHelper.getUnreadUrlList();

    for (ScriptedURL temp : wholeList) {
      tempTime = (double) temp.getWordCount() / wordsperMin;
      Log.d("test tempTime", "" + tempTime + "wordCount" + temp.getWordCount());
      String title = temp.getTitle();
      String content = temp.getContent();
      String url = temp.getUrl();
      ret.add(new ScriptedData(url, title, tempTime, content));
    }
    return ret;
  }
}
