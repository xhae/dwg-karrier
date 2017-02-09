package com.dwg_karrier.roys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageListActivity extends AppCompatActivity {
  ImageView img;
  String imgUrl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.image_view);
    img = (ImageView) findViewById(R.id.image);
    // TODO: change imgUrl into crawler.getLeadImgUrl()
    imgUrl = "http://www.bloter.net/wp-content/uploads/2015/09/portal_logo_150923.png"; // sample
    Picasso.with(this).load(imgUrl).into(img);
  }
}
