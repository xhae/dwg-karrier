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
    data = getData();

    lv = (ListView) findViewById(R.id.listView);
    lv.setAdapter(new ListViewAdapter(this, R.layout.item, data));

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent1 = new Intent(ListActivity.this, ContentView.class);
        intent1.putExtra("finTime", finTime);
        intent1.putExtra("curTime", curTime);
        startActivity(intent1);
        finish();
      }
    });
  }

  private ArrayList<ScriptedData> getData() {
    ArrayList ret = new ArrayList<ScriptedData>();

    // url list 불러오는 함수
    ret = callURL();

    return ret;
  }

  private ArrayList<ScriptedData> callURL() {
    DataBaseOpenHelper test = new DataBaseOpenHelper(null);
    ArrayList ret = new ArrayList<ScriptedData>();
    ArrayList<ScriptedURL> wholeList = new ArrayList<ScriptedURL>(); // readable DB call method error...
    double tempTime;
    int wordsperMin = 180;

    // temp test code
    wholeList.add(new ScriptedURL("temp url1", 1, 130));
    wholeList.add(new ScriptedURL("temp url2", 1, 13));
    wholeList.add(new ScriptedURL("temp url3", 1, 1123));
    wholeList.add(new ScriptedURL("temp url4", 1, 13921));
    wholeList.add(new ScriptedURL("temp url5", 1, 23213));

    for (ScriptedURL temp : wholeList) {
      tempTime = (double)temp.getWordCount() / wordsperMin;
      Log.d("test tempTime", ""+tempTime+ "wordCount"+temp.getWordCount());
      ret.add(new ScriptedData("", tempTime, ""));
    }

    return ret;
  }
}