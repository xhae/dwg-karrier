package com.dwg_karrier.roys;

public class ScriptedURL {
  private String url;
  private boolean isRead;
  private int wordCount;
  private Crawler crawler;

  ScriptedURL(String url, boolean isRead) {
    this.url = url;
    this.isRead = isRead;
    this.crawler = new Crawler(url);
  }

  ScriptedURL(String url, int isRead) {
    this.url = url;
    if (isRead == 1) {
      this.isRead = true;
    } else {
      this.isRead = false;
    }
    this.crawler = new Crawler(url);
  }

  ScriptedURL(String url) {
    this.url = url;
    this.isRead = false;
    this.crawler = new Crawler(url);
  }

  ScriptedURL(String url, int isRead, int wordCount) {
    this.url = url;
    if (isRead == 1) {
      this.isRead = true;
    } else {
      this.isRead = false;
    }
    this.wordCount = wordCount;
    this.crawler = new Crawler(url);
  }

  public String getUrl() {
    return url;
  }

  public boolean getIsRead() {
    return isRead;
  }

  public String getTitle() {
    return crawler.getTitle();
  }

  public String getContent() {
    return crawler.getContent();
  }

  public int getWordCount() {
    wordCount = crawler.getWordCount();
    return wordCount;
  }
}
