package com.dwg_karrier.roys;

import static com.dwg_karrier.roys.LoginActivity.loginActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
  private String user = "xhae";
  private String user_level;
  private String user_record;
  NavigationView navigationView ;
  View hView ;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_navigation);
    if (loginActivity != null) {
      loginActivity.finish();
      loginActivity = null;
    }

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    actionBar.setDisplayHomeAsUpEnabled(true);
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();
    navigationView = (NavigationView) findViewById(R.id.nav_view);
    hView = navigationView.getHeaderView(0);
    setUserInfo();

    setImage();
    GridLayout minuteLayout = (GridLayout) findViewById(R.id.maingridLayout);
    int childCount = minuteLayout.getChildCount();
    final int timeUnit = 10;
    for (int i = 0; i < childCount; i++) {
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

  @Override
  public void onStart() {
    super.onStart();
    setUserInfo();
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.all_contents) {
      Intent openRcmdList = new Intent(MainActivity.this, ListActivity.class); // open Recommend Lists
      openRcmdList.putExtra("FLAG", '0');
      startActivity(openRcmdList);
    } else if (id == R.id.recommendations) {
      Intent openRcmdList = new Intent(MainActivity.this, ListActivity.class); // open Recommend Lists
      openRcmdList.putExtra("FLAG", '1');
      startActivity(openRcmdList);
    } else if (id == R.id.my_report) {
      Intent go_my_report = new Intent(this, MyReportActivity.class);
      startActivity(go_my_report);
    }
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  private void setUserInfo() {
    ImageView nav_user_image = (ImageView) hView.findViewById(R.id.nav_user_image);
    // TODO(juung): bring user_image from preference
    nav_user_image.setImageResource(R.mipmap.ic_user_xhae);
    TextView nav_user = (TextView) hView.findViewById(R.id.nav_user_id);
    nav_user.setText(user);
    TextView nav_user_level = (TextView) hView.findViewById(R.id.nav_user_level);
    DataBaseOpenHelper dbhelper = new DataBaseOpenHelper(MainActivity.this);
    user_level = "Lv. " + dbhelper.getReadPageCount() / 12;
    nav_user_level.setText(user_level);
    user_record = dbhelper.getReadPageCount() + " Pages";
    TextView nav_user_record = (TextView) hView.findViewById(R.id.nav_user_record);
    nav_user_record.setText(user_record);
    navigationView.setNavigationItemSelectedListener(this);
  }

  private void setImage() {
    Point windowSize = new Point();
    getWindowManager().getDefaultDisplay().getSize(windowSize);
    final int screenWidth = windowSize.x;
    final int screenHeight = windowSize.y;

    ImageView min10 = (ImageView) findViewById(R.id.min10);
    ImageView min20 = (ImageView) findViewById(R.id.min20);
    ImageView min30 = (ImageView) findViewById(R.id.min30);
    ImageView min40 = (ImageView) findViewById(R.id.min40);
    ImageView min50 = (ImageView) findViewById(R.id.min50);
    ImageView min60 = (ImageView) findViewById(R.id.min60);
    ImageView more = (ImageView) findViewById(R.id.more);

    min10.getLayoutParams().width = (int) (screenWidth * 0.5);
    min20.getLayoutParams().width = (int) (screenWidth * 0.5);
    min30.getLayoutParams().width = (int) (screenWidth * 0.5);
    min40.getLayoutParams().width = (int) (screenWidth * 0.5);
    min50.getLayoutParams().width = (int) (screenWidth * 0.5);
    min60.getLayoutParams().width = (int) (screenWidth * 0.5);
    more.getLayoutParams().width = (int) (screenWidth * 0.5);

    min10.getLayoutParams().height = (int) (screenHeight * 0.5);
    min20.getLayoutParams().height = (int) (screenHeight * 0.25);
    min30.getLayoutParams().height = (int) (screenHeight * 0.25);
    min40.getLayoutParams().height = (int) (screenHeight * 0.25);
    min50.getLayoutParams().height = (int) (screenHeight * 0.25);
    min60.getLayoutParams().height = (int) (screenHeight * 0.25);
    more.getLayoutParams().height = (int) (screenHeight * 0.25);
  }
}

