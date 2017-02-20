package com.dwg_karrier.roys;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handstudio.android.hzgrapherlib.animation.GraphAnimation;
import com.handstudio.android.hzgrapherlib.graphview.LineGraphView;
import com.handstudio.android.hzgrapherlib.vo.linegraph.LineGraph;
import com.handstudio.android.hzgrapherlib.vo.linegraph.LineGraphVO;

import java.util.ArrayList;
import java.util.List;

public class MyReportActivity extends AppCompatActivity {
  private ViewGroup layoutGraphView;
  // TODO(juung): bring user_id from preference
  private String user = "xhae";
  // TODO(juung): bring keywords from isRead == 1
  String[] wordCloud = new String[]{"Google", "Software Engineer", "App", "Google Korea", "Woman",
      "Develop with Google", "Karrier", "Roys", "Soyee", "Jihyung", "Sera", "Sunju", "xhae", "Jungshik"};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_report);
    Point windowSize = new Point();
    getWindowManager().getDefaultDisplay().getSize(windowSize);
    final int screenHeight = windowSize.y;

    WebView wordCloudView = (WebView) findViewById(R.id.my_keyword);
    wordCloudView.getLayoutParams().height = (int) (screenHeight * 0.6);
    ScrollView reportScroll = (ScrollView) findViewById(R.id.myScrollView);
    reportScroll.smoothScrollTo(0,0);
    TextView pageNum = (TextView) findViewById(R.id.pagecount);
    DataBaseOpenHelper dbhelper = new DataBaseOpenHelper(MyReportActivity.this);
    pageNum.setText(dbhelper.getReadPageCount() +" pages");

    // show user_image
    ImageView imageView = (ImageView) findViewById(R.id.my_image);
    // TODO(juung): show user image from preference
    imageView.setImageResource(R.mipmap.ic_user_xhae);
    // show user id
    TextView textView = (TextView) findViewById(R.id.my_name);
    textView.setText(user);
    // show my time zone
    layoutGraphView = (ViewGroup) findViewById(R.id.layout_graph_view);
    setLineGraph();
    // show my keyword
    final WebView d3 = (WebView) findViewById(R.id.my_keyword);
//    d3.setVerticalScrollBarEnabled(false);
//    d3.setHorizontalScrollBarEnabled(false);
    WebSettings ws = d3.getSettings();
    ws.setJavaScriptEnabled(true);
//    ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//    d3.setOnTouchListener(new View.OnTouchListener() {
//      public boolean onTouch(View v, MotionEvent event) {
//        return (event.getAction() == MotionEvent.ACTION_MOVE);
//      }
//    });
    d3.loadUrl("file:///android_asset/d3.html");
    d3.setWebViewClient(new WebViewClient() {
      @Override
      public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        StringBuffer sb = new StringBuffer();
        sb.append("wordCloud([");
        for (int i = 0; i < wordCloud.length; i++) {
          sb.append("'").append(wordCloud[i]).append("'");
          if (i < wordCloud.length - 1) {
            sb.append(",");
          }
        }
        sb.append("])");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
          d3.evaluateJavascript(sb.toString(), null);
        } else {
          d3.loadUrl("javascript:" + sb.toString());
        }
      }
    });
  }

  private void setLineGraph() {
    //all setting
    LineGraphVO vo = makeLineGraphAllSetting();
    layoutGraphView.addView(new LineGraphView(this, vo));
  }

  private LineGraphVO makeLineGraphAllSetting() {
    //GRAPH SETTING
    /*
     * TODO(Juung): change graph1 to user's personal record from preference info (average)
     */
    String[] legendArr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
        "18", "19", "20", "21", "22", "23", "24"};
    float[] graph1 = {9, 5, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 1, 3, 0, 0, 0, 0, 17, 20, 5, 3, 10};
    //BASIC LAYOUT SETTING
    //padding
    int paddingBottom = 45;
    int paddingTop = 5;
    int paddingLeft = 65;
    int paddingRight = 25;
    //graph margin
    int marginTop = LineGraphVO.DEFAULT_MARGIN_TOP;
    int marginRight = 3;
    //max value
    int maxValue = (int) getMaxValue(graph1) + 5;
    //increment
    int increment = 5;

    List<LineGraph> arrGraph = new ArrayList();
    arrGraph.add(new LineGraph("Read page #", 0xaac3d69b, graph1));

    LineGraphVO vo = new LineGraphVO(
        paddingBottom, paddingTop, paddingLeft, paddingRight,
        marginTop, marginRight, maxValue, increment, legendArr, arrGraph);
    //set animation
    vo.setAnimation(new GraphAnimation(GraphAnimation.LINEAR_ANIMATION, GraphAnimation.DEFAULT_DURATION));
    //set draw graph region
    vo.setDrawRegion(true);
    return vo;
  }

  public static float getMaxValue(float[] numbers) {
    float maxValue = numbers[0];
    for (int i = 1; i < numbers.length; i++) {
      if (numbers[i] > maxValue) {
        maxValue = numbers[i];
      }
    }
    return maxValue;
  }
}
