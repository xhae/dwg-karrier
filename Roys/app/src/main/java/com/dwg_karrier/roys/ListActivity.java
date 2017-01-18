package com.dwg_karrier.roys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by userpc on 2017-01-16.
 */

public class ListActivity extends AppCompatActivity{
  ListView lv;
  ArrayList<item> data;
  Date fin_time; /* expected finish time */
  Date cur_time; /* current time*/
  long duration; /* time duration between current_time and finish time */

  protected void onCreate (Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.list);

    final Intent intent = new Intent(this.getIntent());
    fin_time = (Date)intent.getSerializableExtra("fin_time");
    cur_time = (Date)intent.getSerializableExtra("cur_time");

    duration = (fin_time.getTime() - cur_time.getTime()) / 60000;
    data = getData();

    lv = (ListView)findViewById(R.id.listView);
    lv.setAdapter(new ListViewAdapter(this, R.layout.item, data));

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent1 = new Intent(ListActivity.this, ContentView.class);
        intent1.putExtra("fin_time", fin_time);
        intent1.putExtra("cur_time", cur_time);
        startActivity(intent1);
        finish();
      }
    });
  }
  private ArrayList<item> getData() {
    ArrayList ret = new ArrayList<item>();

    /*url list 불러오는 함수*/
    //ret = callurl();

    // imsi test code //
    ret.add(new item("fin_time: " +fin_time, ""+ duration));
    ret.add(new item(cur_time +"", "test time...2"));
    ret.add(new item("test title...3", "test time...3"));
    ret.add(new item("test title...4", "test time...4"));
    ret.add(new item("test title...5", "test time...5"));
    ret.add(new item("test title...6", "test time...6"));
    ret.add(new item("test title...7", "test time...7"));
    ret.add(new item("test title...8", "test time...8"));
    ret.add(new item("test title...9", "test time...9"));
    ret.add(new item("test title...10", "test time...10"));
    ret.add(new item("test title...11", "test time...11"));
    ret.add(new item("test title...12", "test time...12"));
    return ret;
  }

  private ArrayList<item> callurl(){
    ArrayList ret = new ArrayList<item>();

    /*word_count 기반으로 시간 계산하는 함수*/
    // cal_time();



    return ret;
  }

  private String cal_time(){
    String ret = "";

    return ret;
  }
}
