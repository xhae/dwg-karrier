package com.dwg_karrier.roys;

import static com.dwg_karrier.roys.ContentSwipe.saveSwipeActivity;
import static com.dwg_karrier.roys.ListActivity.saveActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

public class ContentView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
  private final String imgSizeCtrl = "<style>img{display: inline; height: auto; max-width: 100%;}</style>\n"; // fit image to the size of viewer
  String title;
  String content;
  String url;
  Date finTime;
  Date curTime;
  long startTime;
  long endTime;
  int flag;
  private String user = "xhae";
  private String user_level;
  private String user_record;
  NavigationView navigationView;
  View hView;

  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_navigation_contentview);
//    toolbarSetting();
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    Drawable translate_icon = getResources().getDrawable(R.drawable.ic_translate_white);
    toolbar.setOverflowIcon(translate_icon);
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

    final Intent getPageInfo = new Intent(this.getIntent());
    title = getPageInfo.getStringExtra("title");
    content = getPageInfo.getStringExtra("content");
    finTime = (Date) getPageInfo.getSerializableExtra("finTime");
    curTime = (Date) getPageInfo.getSerializableExtra("curTime");
    flag = getPageInfo.getCharExtra("FLAG", '2');

    setView(title, content);

    startTime = System.currentTimeMillis();

    Button finishReading = (Button) findViewById(R.id.finishReading);
    url = getPageInfo.getStringExtra("url");
    finishReading.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        endTime = System.currentTimeMillis();
        DataBaseOpenHelper dbHelper = new DataBaseOpenHelper(ContentView.this);
        dbHelper.setIsRead(url, 1);
        // Temporal check for DB and read time
        long readTime = (endTime - startTime) / 1000;
        Intent backToList = null;
        if (saveActivity != null) {
          saveActivity.finish();
          saveActivity = null;
          backToList = new Intent(ContentView.this, ListActivity.class);
        } else if (saveSwipeActivity != null) {
          saveSwipeActivity.finish();
          saveSwipeActivity = null;
          backToList = new Intent(ContentView.this, ContentSwipe.class);
        }

        if (flag == '0') {
          backToList.putExtra("FLAG", '0');
        } else if (flag == '1') {
          backToList.putExtra("FLAG", '1');
        } else {
          backToList.putExtra("finTime", finTime);
          backToList.putExtra("curTime", curTime);
        }

        backToList.putExtra("readTime", String.valueOf(readTime));
        startActivity(backToList);
        finish();
      }
    });
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.translatelanguagemenu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Translator contentTranslator = new Translator("en", content);
    Translator titleTranslator = new Translator("en", title);

    int id = item.getItemId();
    if (id == R.id.korean) {
      setView(titleTranslator.getTranslate("ko"), contentTranslator.getTranslate("ko"));
      return true;
    } else if (id == R.id.chineses) {
      setView(titleTranslator.getTranslate("zh-TW"), contentTranslator.getTranslate("zh-TW"));
      return true;
    } else if (id == R.id.german) {
      setView(titleTranslator.getTranslate("de"), contentTranslator.getTranslate("de"));
      return true;
    } else if (id == R.id.original) {
      setView(title, content);
    } 
    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.all_contents) {
      Intent openRcmdList = new Intent(ContentView.this, ListActivity.class); // open Recommend Lists
      openRcmdList.putExtra("FLAG", '0');
      startActivity(openRcmdList);
    } else if (id == R.id.recommendations) {
      Intent openRcmdList = new Intent(ContentView.this, ListActivity.class); // open Recommend Lists
      openRcmdList.putExtra("FLAG", '1');
      startActivity(openRcmdList);
    } else if (id == R.id.my_report) {
      Intent go_my_report = new Intent(this, MyReportActivity.class);
      startActivity(go_my_report);
    } else if (id == R.id.home) {
      Intent go_home = new Intent(this, MainActivity.class);
      startActivity(go_home);
      finish();
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
    DataBaseOpenHelper dbhelper = new DataBaseOpenHelper(ContentView.this);
    user_level = "Lv. " + (dbhelper.getReadPageCount() / 12) + 1;
    nav_user_level.setText(user_level);
    user_record = dbhelper.getReadPageCount() + " Pages";
    TextView nav_user_record = (TextView) hView.findViewById(R.id.nav_user_record);
    nav_user_record.setText(user_record);
    navigationView.setNavigationItemSelectedListener(this);
  }

  public void setView(String showTitle, String showContent) {
    String view = imgSizeCtrl + showContent;

    TextView tv = (TextView) findViewById(R.id.contenttitleview);
    tv.setText(showTitle);

    WebView wv = (WebView) findViewById(R.id.contentView);
    wv.setVerticalScrollBarEnabled(true);
    wv.setHorizontalScrollBarEnabled(false);

    final String mimeType = "text/html";
    final String encoding = "UTF-8";

    wv.loadDataWithBaseURL("", view, mimeType, encoding, "");

  }
}
