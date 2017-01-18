package com.dwg_karrier.roys;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  final String accessToken = "A06EprS0187tNdGMJ1XPTVQa1eE8SeGLXZeK3GZy2UwZ8qzOGSqZlPmXNcYul0zueeQRLYwN1nWbFszj6PyoNOkCGSbUp9zfJ3eLROo3bJWsUQktkXPfbFruJn9TGFQQ5r16aLhP7f-VXMFNxMtlrJw21eabhWzhzO-9r0OkXBesU_0Kscpb4SaRPW4TpYpfGiusnAKhaWmeNYdu5VaCGMdFpoch:feedlydev";
  final String id = "3d0c7dd1-a7bb-4cdf-92f0-6c25d88c52db";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button btnFeedlyAccount=(Button)findViewById(R.id.FeedlyAccountBtn);
    btnFeedlyAccount.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        if(android.os.Build.VERSION.SDK_INT > 9) {
          StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
          StrictMode.setThreadPolicy(policy);
        }

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("https://cloud.feedly.com/v3/streams/contents?streamId=user/"+id+"/category/global.all");
        // replace with your url
        request.setHeader("Authorization","OAuth "+accessToken);
        HttpResponse response;
        try {
          response = client.execute(request);
          TextView TextView = (TextView)findViewById(R.id.textView2);
          HttpEntity entity = response.getEntity();

          if (entity != null) {

            // A Simple JSON Response Read
            InputStream instream = entity.getContent();
            String result = convertStreamToString(instream);
            // now you have the string representation of the HTML request
            TextView.setText("RESPONSE: " + result);
            instream.close();
            try {
              JSONObject obj = new JSONObject(result);
              JSONArray arr = obj.getJSONArray("items");
              int len = arr.length();
              TextView.setText("");
              for(int i = 0; i<len; i++){
                TextView.append(arr.getJSONObject(i).getString("originId"));
              };

            }catch(org.json.JSONException e)
            {
              e.printStackTrace();
            }


          }

          //TextView.setText(response.toString());

        } catch (ClientProtocolException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });
  }
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
}

