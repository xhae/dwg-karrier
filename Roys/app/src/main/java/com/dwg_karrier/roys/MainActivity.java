package com.dwg_karrier.roys;

import android.content.Context;
import static com.dwg_karrier.roys.MainActivity.ACCESS_TOKEN;

import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
  static final String ACCESS_TOKEN = "A06EprS0187tNdGMJ1XPTVQa1eE8SeGLXZeK3GZy2UwZ8qzOGSqZlPmXNcYul0zueeQRLYwN1nWbFszj6PyoNOkCGSbUp9zfJ3eLROo3bJWsUQktkXPfbFruJn9TGFQQ5r16aLhP7f-VXMFNxMtlrJw21eabhWzhzO-9r0OkXBesU_0Kscpb4SaRPW4TpYpfGiusnAKhaWmeNYdu5VaCGMdFpoch:feedlydev";
  static final String ID = "3d0c7dd1-a7bb-4cdf-92f0-6c25d88c52db";

  private static String convertStreamToString(InputStream is) {

    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();

    String line = null;
    try {
      while ((line = reader.readLine()) != null) {
        sb.append(line + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return sb.toString();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setImage();

    final Context mainActivity = this;
    DataBaseOpenHelper dataBaseOpenHelper;
    dataBaseOpenHelper = new DataBaseOpenHelper(mainActivity);

    final String URL = "https://cloud.feedly.com/v3/streams/contents?streamId=user/" + ID + "/category/global.all";
    new GetPageList(dataBaseOpenHelper, mainActivity).execute(URL);

    GridLayout minuteLayout = (GridLayout)findViewById(R.id.mainLayout);
    setImage();

    int childCount = minuteLayout.getChildCount();
    final int timeUnit = 10;
    for(int i = 0 ; i < childCount ; i++) {
      final ImageView container = (ImageView) minuteLayout.getChildAt(i);
      container.setTag((i + 1) * timeUnit + "");
      container.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view){
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

  private class GetPageList extends AsyncTask<String, Void, String> {
    private DataBaseOpenHelper dataBaseOpenHelper;
    private Context mainContext;

    public GetPageList(DataBaseOpenHelper dbHelper, Context context) {
      dataBaseOpenHelper = dbHelper;
      mainContext = context;
    }

    @Override
    protected String doInBackground(String... params) {
      try {
        URL url = new URL(params[0]);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Authorization", "OAuth " + ACCESS_TOKEN);

        InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
        String result = convertStreamToString(stream);

        JSONObject obj = new JSONObject(result);
        JSONArray arr = obj.getJSONArray("items");
        int len = arr.length();

        for (int i = 0; i < len; i++) {
          dataBaseOpenHelper.insertScriptedDataWithCheckDuplication(arr.getJSONObject(i).getString("originId"));
        }

        urlConnection.disconnect();

      } catch (IOException | JSONException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String result) {
    }
  }
}

