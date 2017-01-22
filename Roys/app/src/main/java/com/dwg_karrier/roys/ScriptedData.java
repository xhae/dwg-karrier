package com.dwg_karrier.roys;

/**
 * Created by userpc on 2017-01-16.
 */

public class ScriptedData {
  private String title;
  private String expectedTime;

  public String getTitle() {
    return title;
  }
  public String getExpectedTime() {
    return expectedTime;
  }

  public ScriptedData(String title, String expectedTime) {
    this.title = title;
    this.expectedTime = expectedTime;
  }
}
