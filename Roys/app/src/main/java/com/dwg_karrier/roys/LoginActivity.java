package com.dwg_karrier.roys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
  public static Activity loginActivity;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    loginActivity = LoginActivity.this;

    findViewById(R.id.login_button).setOnClickListener(loginClickListener);
  }
  
  Button.OnClickListener loginClickListener = new View.OnClickListener() {
    public void onClick(View v) {
      final Context loginActivity = LoginActivity.this;
      Authentication authentication = new Authentication(loginActivity);
      authentication.authenticationAndBringPages();
    }
  };
}
