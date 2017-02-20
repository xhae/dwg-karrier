package com.dwg_karrier.roys;

import static com.dwg_karrier.roys.ListActivity.saveActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.simple.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Date;

public class ContentSwipe extends AppCompatActivity {
  String title;
  String content;
  String imageUrl;
  double duration; // time duration between current_time and finish time
  public int totalPageNum;
  private SectionsPagerAdapter pageSwipeAdapter;
  private ViewPager pageSwipeView;
  private static ArrayList<ScriptedURL> unreadPageList; // TODO (Csoyee, Jungshik): static prob...
  static Date finTime;
  static Date curTime;
  public static Activity saveSwipeActivity;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.swipecontent);

    DataBaseOpenHelper dbHelper = new DataBaseOpenHelper(this);
    // change to time
    unreadPageList = dbHelper.getUnreadUrlList();
    totalPageNum = unreadPageList.size();
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

    public static PlaceholderFragment newInstance(int sectionNumber, int totalPageNum, String title, String content, String imageUrl) {
      PlaceholderFragment fragment = new PlaceholderFragment();
      Bundle args = new Bundle();

      args.putInt(ARG_SECTION_NUMBER, sectionNumber);
      args.putInt("TOTALPAGENUM", totalPageNum);
      if (totalPageNum == 0) {
        args.putString("TITLE", "No Content");
        args.putString("CONTENT", "There is no content saved");
      } else {
        ScriptedURL pageInfo = unreadPageList.get(sectionNumber);
        args.putString("TITLE", pageInfo.getTitle());
        args.putString("CONTENT", pageInfo.getContent());
        args.putString("REPIMAGE", pageInfo.getRepImageUrl());
      }
      fragment.setArguments(args);
      return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
        savedInstanceState) {
      View rootView = inflater.inflate(R.layout.contentfragment, container, false);
      final TextView contentTitle = (TextView) rootView.findViewById(R.id.title);
      TextView contentSum = (TextView) rootView.findViewById(R.id.summary);
      ImageView backgroundImage = (ImageView) rootView.findViewById(R.id.backImage);

      if (getArguments().getInt("TOTALPAGENUM") == 0) {
        contentTitle.setText(getArguments().getString("TITLE"));
        contentSum.setText(getArguments().getString("CONTENT"));
        backgroundImage.setImageResource(R.drawable.empty);
      } else {
        final String getTitle = getArguments().getString("TITLE");
        final String getContent = getArguments().getString("CONTENT");

        contentTitle.setText(getTitle);
        try {
          Picasso.with(getActivity().getApplicationContext()).load(getArguments().getString("REPIMAGE")).into(backgroundImage);
        } catch (Exception e) {
          e.printStackTrace();
        }
        /*
         * TODO(juung): get wordCount and determine different summarized line
         * if (wordCount < ) {
         *  summaryNum = 3;
         *  return summaryNum;
         * } elif (wordCount < ) {
         *  summaryNum = 6;
         *  return summaryNum;
         * }
         */
        Document jsoupdoc = Jsoup.parse(getContent);
        final String contentText = jsoupdoc.text();

        Summarizer summarizer = new Summarizer(contentText);
        int summaryNum = 3;
        JSONArray summaryArrayResult = summarizer.getSummary(summaryNum);
        String summaryResult = "";
        for (int i = 0; i < summaryArrayResult.size(); i++) {
          try {
            summaryResult += summaryArrayResult.get(i) + "\n";
          } catch (Exception e) {
            Log.e("Summarize", "No Summarized result");
          }
        }
        final TextView contentSummary = (TextView) rootView.findViewById(R.id.summary);
        contentSummary.setText(summaryResult);
        backgroundImage.setOnClickListener(new ImageView.OnClickListener() {
          public void onClick(View v) {
            Intent openSelectedPage = new Intent(getActivity(), ContentView.class);
            saveSwipeActivity = getActivity();
            saveActivity = null;
            openSelectedPage.putExtra("finTime", finTime);
            openSelectedPage.putExtra("curTime", curTime);
            openSelectedPage.putExtra("title", getTitle);
            openSelectedPage.putExtra("content", getContent);
            startActivity(openSelectedPage);
          }
        });
      }

      return rootView;
    }
  }

  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      return PlaceholderFragment.newInstance(position, totalPageNum, title, content, imageUrl);
    }

    @Override
    public int getCount() {
      if (totalPageNum != 0) {
        return totalPageNum;
      } else {
        return 1;
      }
    }
  }
}
