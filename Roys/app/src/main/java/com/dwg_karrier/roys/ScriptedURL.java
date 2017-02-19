package com.dwg_karrier.roys;

import org.apache.commons.lang3.StringEscapeUtils;

public class ScriptedURL {
  private String url;
  private boolean isRead;
  private Crawler crawler;
  private double expectedTime;
  private String title;
  private String content;
  private String repImageUrl;

  ScriptedURL(String url, boolean isRead) {
    this.url = url;
    this.isRead = isRead;
    this.crawler = new Crawler(url);
  }

  ScriptedURL(String url, int isRead) {
    this.url = url;
    this.isRead = isRead == 1;
    this.crawler = new Crawler(url);
    this.title = crawler.getTitle();
    this.content = crawler.getContent();
  }

  ScriptedURL(String url) {
    this.url = url;
    this.isRead = false;
    this.crawler = new Crawler(url);
  }

  ScriptedURL(int isRead, String title, String content, double expectedTime) {
	  
    this.isRead = isRead == 1;
    this.title = title;
    this.content = content;
    this.expectedTime = expectedTime;
  }

  ScriptedURL(String url, int isRead, String title, String content, String repImageUrl, double expectedTime) {
    this.url = url;
    this.isRead = isRead == 1;
    this.title = StringEscapeUtils.unescapeHtml4(title);
    this.content = StringEscapeUtils.unescapeHtml4(content);
    this.repImageUrl = repImageUrl;
    this.expectedTime = expectedTime;
  }

  public String getUrl() {
    return url;
  }

  public boolean getIsRead() {
    return isRead;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public double getExpectedTime() {
    return expectedTime;
  }

  public String getRepImageUrl() {
    return repImageUrl;
  }
}
