package com.dwg_karrier.roys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    DataBaseOpenHelper dataBaseOpenHelper = new DataBaseOpenHelper(this);

    ArrayList<ScriptedURL> scriptedURLs = dataBaseOpenHelper.getUrlList();

    TextView textView = (TextView) findViewById(R.id.testText);
    Log.d("arraylist","start");

    int i=0;
    String str="";
    while(i<scriptedURLs.size()){
      str = str+"\n"+scriptedURLs.get(i).url;
      i++;
    }

    textView.setText(str);

  }
}
