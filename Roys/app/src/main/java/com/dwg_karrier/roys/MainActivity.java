package com.dwg_karrier.roys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    Button b = (Button)findViewById(R.id.button);
    final EditText editText = (EditText)findViewById(R.id.editText);

    b.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (editText.getText().toString().isEmpty()) {
          Toast toast = Toast.makeText(getApplicationContext(),"Input time!", Toast.LENGTH_LONG);
          toast.setGravity(Gravity.BOTTOM, 0, 0);
          toast.show();
          return;
        }

        Date cur_time = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(cur_time);
        String take_time = String.valueOf(editText.getText());
        cal.add(Calendar.MINUTE, Integer.parseInt(take_time));
        Date d = new Date(cal.getTimeInMillis());

        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        intent.putExtra("fin_time", d);
        intent.putExtra("cur_time", cur_time);
        startActivity(intent);
      }
    });
  }
}
