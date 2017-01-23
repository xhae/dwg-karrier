package com.dwg_karrier.roys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
  long duration; // time duration between current_time and finish time

  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.list);

    final int minute = 60000;
    final Intent intent = new Intent(this.getIntent());
    finTime = (Date)intent.getSerializableExtra("finTime");
    curTime = (Date)intent.getSerializableExtra("curTime");

    duration = (finTime.getTime() - curTime.getTime()) / minute;
    data = getData();

    lv = (ListView)findViewById(R.id.listView);
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

    /*
     * url list 불러오는 함수
     * ret = callURL();
     */

    // temp test code
    ret.add(new ScriptedData("finTime: " + finTime, ""+ duration));
    ret.add(new ScriptedData(curTime +"", "test time...2"));
    ret.add(new ScriptedData("test title...3", "test time...3"));
    ret.add(new ScriptedData("test title...4", "test time...4"));
    ret.add(new ScriptedData("test title...5", "test time...5"));
    ret.add(new ScriptedData("test title...6", "test time...6"));
    ret.add(new ScriptedData("test title...7", "test time...7"));
    ret.add(new ScriptedData("test title...8", "test time...8"));
    ret.add(new ScriptedData("test title...9", "test time...9"));
    ret.add(new ScriptedData("test title...10", "test time...10"));
    ret.add(new ScriptedData("test title...11", "test time...11"));
    ret.add(new ScriptedData("test title...12", "test time...12"));
    return ret;
  }

  private ArrayList<ScriptedData> callURL() {
//  DataBaseOpenHelper test = new DataBaseOpenHelper(/*what????*/);
    ArrayList ret = new ArrayList<ScriptedData>();
    ArrayList<ScriptedURL> wholeList = new ArrayList<ScriptedURL>();

    /*
     * word_count 기반으로 시간 계산하는 함수
     * calTime();
     */

    return ret;
  }

  private String calTime() {
    return "";
  }
}
