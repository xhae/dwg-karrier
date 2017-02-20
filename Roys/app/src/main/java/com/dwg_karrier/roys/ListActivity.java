package com.dwg_karrier.roys;

import static com.dwg_karrier.roys.ContentSwipe.saveSwipeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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
  ArrayList<ScriptedURL> data;
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

    data = callUrl();
    lv = (ListView) findViewById(R.id.listView);
    lv.setAdapter(new ListViewAdapter(this, R.layout.item, data));

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent openSelectedPage = new Intent(ListActivity.this, ContentView.class);
        openSelectedPage.putExtra("finTime", finTime);
        openSelectedPage.putExtra("curTime", curTime);

        ScriptedURL pageInfo = data.get(position);
        String title = pageInfo.getTitle();
        String content = pageInfo.getContent();

        openSelectedPage.putExtra("title", title);
        openSelectedPage.putExtra("content", content);
        startActivity(openSelectedPage);
      }
    });

    FloatingActionButton changeMode = (FloatingActionButton) findViewById(R.id.toSwipe);
    changeMode.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        saveSwipeActivity = null;
        Intent openSwipe = new Intent(ListActivity.this, ContentSwipe.class); // open Recommend Lists
        openSwipe.putExtra("finTime", finTime);
        openSwipe.putExtra("curTime", curTime);
        startActivity(openSwipe);
        finish();
      }
    });

    // from ContentView
    Intent getReadTime = new Intent(this.getIntent());
    String readTime = getReadTime.getStringExtra("readTime");
    if (readTime != null) {
      Toast checkInfo = Toast.makeText(getApplicationContext(), "Congratulations!" + "\n" +
          "You finished reading in " + readTime + "sec", Toast.LENGTH_LONG);
      checkInfo.setGravity(Gravity.CENTER, 0, 0);
      checkInfo.show();
    }
  }

  private ArrayList<ScriptedURL> callUrl() {
    DataBaseOpenHelper dbHelper = new DataBaseOpenHelper(this);
    dbHelper.getTableAsString();
    ArrayList<ScriptedURL> unreadPageList = dbHelper.getUnreadUrlList();

    return unreadPageList;
  }
}
