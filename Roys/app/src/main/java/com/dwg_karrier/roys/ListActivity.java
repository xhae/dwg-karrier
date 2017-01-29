package com.dwg_karrier.roys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class ListActivity extends AppCompatActivity {
  ListView lv;
  ArrayList<ScriptedData> data;
  Date finTime; // expected finish time
  Date curTime; // current time
  double duration; // time duration between current_time and finish time

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.list);

    final int minute = 60000;
    final Intent intent = new Intent(this.getIntent());
    finTime = (Date) intent.getSerializableExtra("finTime");
    curTime = (Date) intent.getSerializableExtra("curTime");

    duration = (finTime.getTime() - curTime.getTime()) / minute;
    data = callUrl();

    lv = (ListView) findViewById(R.id.listView);
    lv.setAdapter(new ListViewAdapter(this, R.layout.item, data));

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent1 = new Intent(ListActivity.this, ContentView.class);
        intent1.putExtra("finTime", finTime);
        intent1.putExtra("curTime", curTime);
        // TODO: put selected ScriptedData to intent. data[position]
        startActivity(intent1);
        finish();
      }
    });
  }

  private ArrayList<ScriptedData> callUrl() {
    DataBaseOpenHelper dbHelper = new DataBaseOpenHelper(this);
    ArrayList<ScriptedURL> wholeList = new ArrayList<ScriptedURL>();
    // intend: wholeList = test.getURLList();
    ArrayList ret = new ArrayList<ScriptedData>();
    double tempTime;
    final int wordsperMin = 180;
    ArrayList<ScriptedURL> scriptedUrls = dbHelper.getUrlList();

    for (int i = 0; i < scriptedUrls.size(); i++) {
      wholeList.add(scriptedUrls.get(i));
    }

    for (ScriptedURL temp : wholeList) {
      tempTime = (double) temp.getWordCount() / wordsperMin;
      Log.d("test tempTime", "" + tempTime + "wordCount" + temp.getWordCount());
      ret.add(new ScriptedData("", tempTime, ""));
    }

    return ret;
  }
}