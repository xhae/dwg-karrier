package com.dwg_karrier.roys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by userpc on 2017-01-16.
 */

public class ListViewAdapter extends BaseAdapter {

  private LayoutInflater inflater;
  private ArrayList<item> data;
  private int layout;

  public ListViewAdapter(Context contest, int layout, ArrayList<item> data) {
    this.inflater = (LayoutInflater)contest.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.data  = data;
    this.layout = layout;
  }
  @Override
  public int getCount()
  {
    return data.size();
  }

  @Override
  public String getItem(int position) {return data.get(position).getTitle();}

  @Override
  public long getItemId(int position) {return position;}

  @Override
  public View getView (int position, View convertView, ViewGroup parent){

    if (convertView == null) {
      convertView=inflater.inflate(layout,parent, false);
    }
    item item = data.get(position);
    TextView title = (TextView)convertView.findViewById(R.id.TitleView);
    title.setText("Title: "+item.getTitle());
    TextView spend_time = (TextView)convertView.findViewById(R.id.TimeView);
    spend_time.setText("Time: "+item.getSpend_time()+"min");

    return convertView;
  }
}
