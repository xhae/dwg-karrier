package com.dwg_karrier.roys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
  boolean isAccountConnected; // check if user connect to Account.
  Button.OnClickListener loginClickListener = new View.OnClickListener() {
    public void onClick(View v) {
      final Context loginActivity = LoginActivity.this;
      Authentication authentication = new Authentication(loginActivity);
      authentication.authenticationAndBringPages();
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    loginActivity = LoginActivity.this;

    findViewById(R.id.login_button).setOnClickListener(loginClickListener);
  }
}
