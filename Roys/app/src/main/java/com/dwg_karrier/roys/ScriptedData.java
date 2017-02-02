package com.dwg_karrier.roys;

public class ScriptedData {
  private String url;
  private String title;
  private double expectedTime;
  private String content;

  public ScriptedData(String url, String title, double expectedTime, String content) {
    this.url = url;
    this.title = title;
    this.expectedTime = expectedTime;
    this.content = content;
  }

  public String getUrl() {
    return url;
  }

  public String getTitle() {
    return title;
  }

  public double getExpectedTime() {
    return expectedTime;
  }

  public String getContent() {
    return content;
  }
}