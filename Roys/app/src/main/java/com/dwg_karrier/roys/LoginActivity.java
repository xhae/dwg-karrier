package com.dwg_karrier.roys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {
  static boolean isAccountConnected; // check if user connect to Account.

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    isAccountConnected = false;
    findViewById(R.id.login_button).setOnClickListener(loginClickListener);

    Thread thread = new Thread() {
      @Override
      public void run() {
        while (!isAccountConnected) {
        }
        Intent showMain = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(showMain);
        finish();
      }
    };

    thread.start();
  }

  Button.OnClickListener loginClickListener = new View.OnClickListener() {
    public void onClick(View v) {
      final Context loginActivity = LoginActivity.this;
      Authentication authentication = new Authentication(loginActivity);
      authentication.authenticationAndBringPages();
    }
  };
}
