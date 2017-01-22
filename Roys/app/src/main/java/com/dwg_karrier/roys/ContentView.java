package com.dwg_karrier.roys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Date;

public class ContentView extends AppCompatActivity {
  Date finTime;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.content);

    final Intent intent = new Intent(this.getIntent());
    finTime = (Date)intent.getSerializableExtra("finTime");
    final Date cur_time = new Date(System.currentTimeMillis());

    Button b = (Button)findViewById(R.id.button4);
    b.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent1 = new Intent(ContentView.this, ListActivity.class);
        intent1.putExtra("finTime", finTime);
        intent1.putExtra("curTime", cur_time);
        startActivity(intent1);
        finish();
      }
    });
  }
}
