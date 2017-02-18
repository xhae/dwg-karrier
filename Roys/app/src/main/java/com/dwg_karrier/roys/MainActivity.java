package com.dwg_karrier.roys;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setImage();
    GridLayout minuteLayout = (GridLayout)findViewById(R.id.maingridLayout);

    int childCount = minuteLayout.getChildCount();
    final int timeUnit = 10;

    for (int i = 0 ; i < childCount ; i++) {
      final ImageView container = (ImageView) minuteLayout.getChildAt(i);
      container.setTag((i + 1) * timeUnit + "");
      container.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {

          Date curTime = new Date(System.currentTimeMillis());
          Calendar cal = Calendar.getInstance();
          cal.setTime(curTime);
          String inputTime = (String) container.getTag();
          cal.add(Calendar.MINUTE, Integer.parseInt(inputTime));
          Date d = new Date(cal.getTimeInMillis());

          Intent openRcmdList = new Intent(MainActivity.this, ContentSwipe.class); // open Recommend Lists
          openRcmdList.putExtra("finTime", d);
          openRcmdList.putExtra("curTime", curTime);
          startActivity(openRcmdList);
        }
      });
    }
  }

  private void setImage() {
    Point windowSize = new Point();
    getWindowManager().getDefaultDisplay().getSize(windowSize);
    final int screenWidth = windowSize.x;
    final int screenHeight = windowSize.y;

    ImageView min10 = (ImageView)findViewById(R.id.min10);
    ImageView min20 = (ImageView)findViewById(R.id.min20);
    ImageView min30 = (ImageView)findViewById(R.id.min30);
    ImageView min40 = (ImageView)findViewById(R.id.min40);
    ImageView min50 = (ImageView)findViewById(R.id.min50);
    ImageView min60 = (ImageView)findViewById(R.id.min60);
    ImageView more = (ImageView)findViewById(R.id.more);

    min10.getLayoutParams().width = (int)(screenWidth * 0.5);
    min20.getLayoutParams().width = (int)(screenWidth * 0.5);
    min30.getLayoutParams().width = (int)(screenWidth * 0.5);
    min40.getLayoutParams().width = (int)(screenWidth * 0.5);
    min50.getLayoutParams().width = (int)(screenWidth * 0.5);
    min60.getLayoutParams().width = (int)(screenWidth * 0.5);
    more.getLayoutParams().width = (int)(screenWidth * 0.5);

    min10.getLayoutParams().height = (int)(screenHeight * 0.5);
    min20.getLayoutParams().height = (int)(screenHeight * 0.25);
    min30.getLayoutParams().height = (int)(screenHeight * 0.25);
    min40.getLayoutParams().height = (int)(screenHeight * 0.25);
    min50.getLayoutParams().height = (int)(screenHeight * 0.25);
    min60.getLayoutParams().height = (int)(screenHeight * 0.25);
    more.getLayoutParams().height = (int)(screenHeight * 0.25);
  }
}