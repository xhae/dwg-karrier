package com.dwg_karrier.roys;

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
