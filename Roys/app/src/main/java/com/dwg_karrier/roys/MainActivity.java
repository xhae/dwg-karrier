package com.dwg_karrier.roys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final Context mainActivity = this;

    Button btnFeedlyAccount = (Button) findViewById(R.id.FeedlyAccountBtn);
    btnFeedlyAccount.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        GetAccessToken getToken = new GetAccessToken(mainActivity);
        getToken.authenticationAndBringPages();
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


}

