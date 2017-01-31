package com.dwg_karrier.roys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    Button btnFeedlyAccount = (Button) findViewById(R.id.FeedlyAccountBtn);
    btnFeedlyAccount.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        final String URL = "https://cloud.feedly.com/v3/streams/contents?streamId=user/" + ID + "/category/global.all";
        new GetPageList().execute(URL);
      }
    });

    Button b = (Button) findViewById(R.id.button);
    final EditText editText = (EditText) findViewById(R.id.editText);

    b.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (editText.getText().toString().isEmpty()) {
          Toast toast = Toast.makeText(getApplicationContext(), "Input time!", Toast.LENGTH_LONG);
          toast.setGravity(Gravity.BOTTOM, 0, 0);
          toast.show();
          return;
        }

        Date curTime = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(curTime);
        String inputTime = String.valueOf(editText.getText());
        cal.add(Calendar.MINUTE, Integer.parseInt(inputTime));
        Date d = new Date(cal.getTimeInMillis());

        Intent openRcmdList = new Intent(MainActivity.this, ListActivity.class); // open Recommend Lists
        openRcmdList.putExtra("finTime", d);
        openRcmdList.putExtra("curTime", curTime);
        startActivity(openRcmdList);
      }
    });
  }

  private class GetPageList extends AsyncTask<String, Void, String> {
    /*
     * TODO(leesera): the constructor gets the DB as a parameter
     */
    public GetPageList() {
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
          /*
           * TODO(leesera): the url of script page should be saved at DB
           * like arr.getJSONObject(i).getString("originId");
           */
          //
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
