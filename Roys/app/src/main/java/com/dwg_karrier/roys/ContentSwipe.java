package com.dwg_karrier.roys;

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
import android.widget.TextView;

import org.json.simple.JSONArray;

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
       * TODO(juung): get wordCount and determine different summarized line
       * if (wordCount < ) {
       *  summaryNum = 3;
       *  return summaryNum;
       * } elif (wordCount < ) {
       *  summaryNum = 6;
       *  return summaryNum;
       * }
       */
      int summaryNum = 3;

      /*
       * TODO(juung): html text(from DB) to simple text(contentText)
       */
      // example of simple text
      String contentText = "Once, not so long ago, movies were Great. Leading men could sing, " +
          "dance and caddishly charm. Leading women were knockouts. Moviemakers borrowed from the " +
          "tried and true conventions of theater. Earnest, affecting emotion shone through in each " +
          "dazzling scene. And today? Today, leading men can sing, dance and caddishly charm. " +
          "Leading women are knockouts. Moviemakers borrow from the tried and true conventions of theater. " +
          "Sometimes, in the case of the rarest gems, earnest, affecting emotion shines through in each dazzling scene." +
          " Why, then, do we uphold movies about nostalgia for simpler, better times as among the best and " +
          "most important films of the year? Surely, in 2016 especially, there are more urgent and powerful " +
          "stories being told. And yet, “La La Land,” an ode to the Hollywood of yore, racked up seven Golden Globe nominations ― more than any other film, some of them more deserving based on cinematic and storytelling merits alone, relevancy aside. “Jackie” is a postmodern sendup of historical narratives, a patchwork portrait of a woman that’s as brainy as it is enveloping. “Moonlight” is a quiet drama that never treads into saccharine territory, in spite of its heartbreaking subject matter. “Arrival” is a visually stunning, much-needed meditation on empathy. “La La Land” is a movie about how great movies used to be, back in the day. Its fellow might-be Oscar contenders disprove its very premise. And still, “La La Land,” which opened in limited release last weekend after months on festival slates, is the front-runner for Best Picture at the Academy Awards. Of its prospects, Vulture writes, “a warm, embraceable contender [...] feels exactly like what the Academy will respond to right now.” Indeed, the warmth of “La La Land” is its strength, and its charm. Leads Emma Stone and Ryan Gosling dance around true connection, before they finally come together for a cheery summer of art-making and bar-going. Stone plays Mia, a downtrodden, aspiring actress who keeps narrowly missing parts while moonlighting at a coffee shop. Gosling plays Sebastian, a man whose consuming passion for old school jazz gets in the way of his social skills. They pull each other up by their bootstraps, encouraging one another to pursue their dreams ― to follow in the footsteps of Charlie Parker and Ingrid Bergman, respectively.   Yes, it’s highly stylized; that’s very much the point. A scene in the Griffith Observatory recalls “Funny Face”-like fantasies, complete with dress-swirling and literal dancing among the stars. Even if you find such romance cloying, it’s hard to take issue with the aesthetic choices made in “La La Land,” because they’re all so deliberate and the film is staunchly self-aware. (Stone laments that a one-woman play she’s written and stars in might be “too nostalgic,” for example.) It embraces its genre (musical, love story), and successfully subverts it (there’s more at stake here than boy-gets-girl). For the story it aims to tell, it makes use of the perfect cinematic tools, tricks and throwbacks. The question, then, is: why are we still telling this story, and cherishing it as the zenith of filmmaking? Film, in particular, is fixated on its past, viewing its own history through a rose-tinted lens. In 2011, a silent film set in Hollywood took home Best Picture; that same year, “Hugo,” which racked up four wins in its own right, centered on a similar, nostalgic theme. In literature, riffs on classics are treated as fun fan fodder, not hallmarks of contemporary success. Stories that fit modern romances into the template laid out by Jane Austen, for example, aren’t dashed off as unserious, but they aren’t racking up National Book Award nominations, either. That’s because the gatekeepers of other art forms seem to acknowledge the fallacy of traditional appeal. Brave new modes of storytelling are considered valid and important, not somehow inferior to the established greats. But in the world of “La La Land,” newness alone is the butt of industry jokes. On her way to starlettdom, Mia tries out for a barrage of parts, never succeeding in landing one. But the roles, Sebastian suggests and she believes, are unworthy of her. Of a part she’s going out for, Mia quips, “it’s Goldilocks from the perspective of the bears,” a joke about ― what? That retelling the same, reliably popular stories has become a hackneyed money grab? That’d be an odd criticism when “La La Land” itself brims with pastiche. That films today are too focused on recasting stories from the perspective of the disenfranchised? That’d be a callous line to put in, but it wouldn’t be an outlier. Early in the film, Sebastian laments that the beloved jazz bar where he worked has been converted into a “samba-tapas” spot, a recurring joke throughout the film. Samba is a Brazilian dance; tapas are a Spanish cuisine. To combine the two is a misguided mashup, an attempt to tick off multiple diversity boxes, or else the result of ignorance. For Sebastian, it’s evidence that Los Angelenos “worship everything and value nothing,” a complaint that would be interesting if it were unpacked a little before the characters broke into song. Other socioeconomic issues are raised and then glibly danced over. John Legend plays the frontman of a jazz group that threads synthy loops into its classical foundation, and blows up on YouTube and on tour as a result. He’s cast as an artless foil to Sebastian’s wistful ― and, frankly, privileged ― outlook. Questions of class, race and privilege are superficially present, but largely absent from a film that wrestles with the idea of the American dream. This might’ve flown in 2006, but in 2016, it’s clear that at last these factors press on a story that wants to pretend they don’t exist.  Maybe instead of celebrating the gaiety of “La La Land,” and of classic Hollywood, it’s a good time to recognize the other things movies do well, and have done well for decades: find hope amid tragedy, quiet triumph amid oppression, and beauty amid the fracturing feeling of loss.  11 Films With Black Stars To Look Forward To In 2016 1 of 11 \"Ghostbusters,\" Leslie Jones Who you gonna call when it’s time to reboot Ghostbusters? How about an all-female cast! The revamped 1980s film will star Melissa McCarthy, Kristen Wiig, and SNL’s Leslie Jones and Kate McKinnon, and feature cameos from original cast members Bill Murray, Ernie Hudson, and Dan Aykroyd. Ghostbusters hits theaters July 15. Share this slide: Columbia Pictures\n";
      Summarizer summarizer = new Summarizer(contentText);
      Log.d("initialize Summarizer", "good");
      JSONArray summaryArrayResult = summarizer.getSummary(summaryNum);
      String summaryResult = "";
      for (int i = 0; i < summaryArrayResult.size(); i++) {
        try {
          summaryResult += summaryArrayResult.get(i) + "\n";
        } catch (Exception e) {
          Log.e("Summarize", "No Summarized result");
        }
      }
      TextView contentSummary = (TextView) rootView.findViewById(R.id.summary);
      contentSummary.setText(summaryResult);

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
