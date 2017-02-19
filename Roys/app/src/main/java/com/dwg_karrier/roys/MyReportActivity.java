package com.dwg_karrier.roys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.handstudio.android.hzgrapherlib.animation.GraphAnimation;
import com.handstudio.android.hzgrapherlib.graphview.LineGraphView;
import com.handstudio.android.hzgrapherlib.vo.GraphNameBox;
import com.handstudio.android.hzgrapherlib.vo.linegraph.LineGraph;
import com.handstudio.android.hzgrapherlib.vo.linegraph.LineGraphVO;

import java.util.ArrayList;
import java.util.List;

public class MyReportActivity extends AppCompatActivity {
  private ViewGroup layoutGraphView;
  private String user = "xhae";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_report);
    // show user_image
    ImageView imageView = (ImageView) findViewById(R.id.my_image);
    // TODO(juung): show user image from preference
    imageView.setImageResource(R.mipmap.ic_user_xhae);
    // show user id
    TextView textView = (TextView) findViewById(R.id.my_name);
    // TODO(juung): show user id from preference
    textView.setText(user);
    // show my time zone
    layoutGraphView = (ViewGroup) findViewById(R.id.layout_graph_view);
    setLineGraph();
    // show my keyword


  }
  private void setLineGraph() {
    //all setting
    LineGraphVO vo = makeLineGraphAllSetting();

    //default setting
//  LineGraphVO vo = makeLineGraphDefaultSetting();

    layoutGraphView.addView(new LineGraphView(this, vo));
  }

  private LineGraphVO makeLineGraphDefaultSetting() {

    String[] legendArr = {"1", "2", "3", "4", "5"};
    float[] graph1 = {500, 100, 300, 200, 100};

    List<LineGraph> arrGraph = new ArrayList<>();
    arrGraph.add(new LineGraph(user, 0xaac3d69b, graph1));

    LineGraphVO vo = new LineGraphVO(legendArr, arrGraph);
    return vo;
  }

  /**
   * make line graph using options
   */
  private LineGraphVO makeLineGraphAllSetting() {
    //BASIC LAYOUT SETTING
    //padding
    int paddingBottom = LineGraphVO.DEFAULT_PADDING;
    int paddingTop = LineGraphVO.DEFAULT_PADDING;
    int paddingLeft = LineGraphVO.DEFAULT_PADDING;
    int paddingRight = LineGraphVO.DEFAULT_PADDING;

    //graph margin
    int marginTop = LineGraphVO.DEFAULT_MARGIN_TOP;
    int marginRight = LineGraphVO.DEFAULT_MARGIN_RIGHT;

    //max value
    int maxValue = LineGraphVO.DEFAULT_MAX_VALUE;

    //increment
    int increment = LineGraphVO.DEFAULT_INCREMENT;

    //GRAPH SETTING
    /*
     * TODO(Juung): change graph1 to user's personal record from preference info (average)
     */
    String[] legendArr = {"5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
        "18", "19", "20", "21", "22", "23", "24", "1", "2", "3", "4"};
    float[] graph1 = {500, 100, 300, 200, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    List<LineGraph> arrGraph = new ArrayList();
    arrGraph.add(new LineGraph(user, 0xaac3d69b, graph1));

    LineGraphVO vo = new LineGraphVO(
        paddingBottom, paddingTop, paddingLeft, paddingRight,
        marginTop, marginRight, maxValue, increment, legendArr, arrGraph);

    //set animation
    vo.setAnimation(new GraphAnimation(GraphAnimation.LINEAR_ANIMATION, GraphAnimation.DEFAULT_DURATION));
    //set graph name box
    vo.setGraphNameBox(new GraphNameBox());
    //set draw graph region
    vo.setDrawRegion(true);

    return vo;
  }
}
