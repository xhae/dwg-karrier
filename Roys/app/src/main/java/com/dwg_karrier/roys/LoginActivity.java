package com.dwg_karrier.roys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
  public static Activity loginActivity;

  private TextToSpeech myTTS;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    loginActivity = LoginActivity.this;
    myTTS = new TextToSpeech(this, this);

    findViewById(R.id.login_button).setOnClickListener(loginClickListener);
  }
  
  Button.OnClickListener loginClickListener = new View.OnClickListener() {
    public void onClick(View v) {
      final Context loginActivity = LoginActivity.this;
      Authentication authentication = new Authentication(loginActivity);
      authentication.authenticationAndBringPages();
    }
  };
  @Override
  public void onInit(int status) {
    String myText1 = "안녕하세요 안드로이드 블로그 녹두장군 입니다.";
    String myText2 = "말하는 스피치 입니다.";
    myTTS.speak(myText1, TextToSpeech.QUEUE_FLUSH, null);
    myTTS.speak(myText2, TextToSpeech.QUEUE_ADD, null);
  }
}
