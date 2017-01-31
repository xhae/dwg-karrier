package com.dwg_karrier.roys;

public class ScriptedURL {
  private String url;
  private boolean isRead;
  private Crawler crawler;
  private int wordCount;
  private String title;
  private String content;


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
    this.wordCount = crawler.getWordCount();
    this.title = crawler.getTitle();
    this.content = crawler.getContent();
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
    return title;
  }

  public String getContent() {
    return content;
  }

  public int getWordCount() {
    return wordCount;
  }
}
