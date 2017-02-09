package com.dwg_karrier.roys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

public class ContentSwipe extends AppCompatActivity {
  String title;
  String content;
  Date finTime;
  Date curTime;
  double duration; // time duration between current_time and finish time
  private SectionsPagerAdapter pageSwipeAdapter;
  private ViewPager pageSwipeView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.swipecontent);
    pageSwipeAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    pageSwipeView = (ViewPager) findViewById(R.id.container);
    pageSwipeView.setAdapter(pageSwipeAdapter);

    Intent getTimeInfo = new Intent(this.getIntent());
    finTime = (Date) getTimeInfo.getSerializableExtra("finTime");
    curTime = (Date) getTimeInfo.getSerializableExtra("curTime");

    final int minute = 60000;
    duration = (finTime.getTime() - curTime.getTime()) / minute;

    FloatingActionButton changeMode = (FloatingActionButton) findViewById(R.id.toList);
    changeMode.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent openRcmdList = new Intent(ContentSwipe.this, ListActivity.class); // open Recommend Lists
        openRcmdList.putExtra("finTime", finTime);
        openRcmdList.putExtra("curTime", curTime);
        startActivity(openRcmdList);
        finish();
      }
    });
  }

  public static class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
      super();
    }

    public static PlaceholderFragment newInstance(int sectionNumber) {
      PlaceholderFragment fragment = new PlaceholderFragment();
      Bundle args = new Bundle();
      args.putInt(ARG_SECTION_NUMBER, sectionNumber);
      /*
       * TODO(csoyee) put DB data into args like e.g. putType(key, value) in each fragment.
       * args.putString("tempURL", "args test");
       */
      fragment.setArguments(args);
      return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.contentfragment, container, false);
      TextView contentTitle = (TextView) rootView.findViewById(R.id.title);
      contentTitle.setText("title: " + getArguments().getInt(ARG_SECTION_NUMBER));

      /*
       * TODO(csoyee): set representative image
       * ImageView backgroundImage = (ImageView) rootView.findViewById(R.id.backImage);
       * backgroundImage.setImageURI();
       */

      /*
       * TODO(leesera): connect to contentView
       * Maybe Same as we did at ListActivity.
       * Before we should put ars for each fragment
       */

      return rootView;
    }
  }

  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      return PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
      // TODO(csoyee): change to the number of contents.
      return 10;
    }

  }
}
