package com.dwg_karrier.roys;

public class ScriptedURL {
  private String url;
  private boolean isRead;
  private int wordCount;

  ScriptedURL(String url, boolean isRead) {
    this.url = url;
    this.isRead = isRead;
  }

  ScriptedURL(String url, int isRead) {
    this.url = url;

    if (isRead == 1) {
      this.isRead = true;
    } else {
      this.isRead = false;
    }
  }

  ScriptedURL(String url) {
    this.url = url;
    this.isRead = false;
  }

  ScriptedURL(String url, int isRead, int wordCount) {
    this.url = url;
    if (isRead == 1) {
      this.isRead = true;
    } else {
      this.isRead = false;
    }
    this.wordCount = wordCount;
  }

  public String getUrl() {
    return url;
  }

  public boolean getIsRead() {
    return isRead;
  }

  public int getWordCount() {
    return wordCount;
  }
}