package com.dwg_karrier.roys;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {
  boolean isAccountConnected; // check if user connect to Account.

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    findViewById(R.id.login_button).setOnClickListener(loginClickListener);
  }

  Button.OnClickListener loginClickListener = new View.OnClickListener() {
    public void onClick(View v) {
      final Context loginActivity = LoginActivity.this;
      ImageView mainImage = (ImageView) findViewById(R.id.login_image);
      Authentication authentication = new Authentication(loginActivity);
      authentication.authenticationAndBringPages();
      isAccountConnected = true;
    }
  };
}
