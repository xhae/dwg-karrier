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
    Intent intent = new Intent(this.getIntent());
    finTime = (Date) intent.getSerializableExtra("finTime");
    curTime = (Date) intent.getSerializableExtra("curTime");

    duration = (finTime.getTime() - curTime.getTime()) / minute;
    data = callUrl();
    ListViewAdapter adapter = new ListViewAdapter(this, R.layout.item, data);
    lv = (ListView) findViewById(R.id.listView);
    lv.setAdapter(adapter);

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent1 = new Intent(ListActivity.this, ContentView.class);
        intent1.putExtra("finTime", finTime);
        intent1.putExtra("curTime", curTime);

        ScriptedData pageInfo = data.get(position);
        String title = pageInfo.getTitle();
        String content = pageInfo.getContent();

        intent1.putExtra("title", title);
        intent1.putExtra("content", content);
        startActivity(intent1);
        finish();
      }
    });
  }

  private ArrayList<ScriptedData> callUrl() {
    DataBaseOpenHelper test = new DataBaseOpenHelper(this);
    ArrayList<ScriptedURL> wholeList = new ArrayList<ScriptedURL>();
    // intend: wholeList = test.getURLList();
    ArrayList ret = new ArrayList<ScriptedData>();
    double tempTime;
    final int wordsperMin = 180;

    // temp test code
    String tempUrl1 = "http://www.bloter.net/archives/265787";
    String tempUrl2 = "http://www.bloter.net/archives/256595";
    String tempUrl3 = "http://www.bloter.net/archives/265786";
    String tempUrl4 = "http://www.bloter.net/archives/267575";
    String tempUrl5 = "http://www.bloter.net/archives/254316";
    wholeList.add(new ScriptedURL(tempUrl1, 1));
    wholeList.add(new ScriptedURL(tempUrl2, 1));
    wholeList.add(new ScriptedURL(tempUrl3, 1));
    wholeList.add(new ScriptedURL(tempUrl4, 1));
    wholeList.add(new ScriptedURL(tempUrl5, 1));

    for (ScriptedURL temp : wholeList) {
      tempTime = (double) temp.getWordCount() / wordsperMin;
      Log.d("test tempTime", "" + tempTime + "wordCount" + temp.getWordCount());
      String title = temp.getTitle();
      String content = temp.getContent();
      ret.add(new ScriptedData(title, tempTime, content));
    }
    return ret;
  }
}