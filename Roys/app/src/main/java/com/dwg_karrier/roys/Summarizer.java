package com.dwg_karrier.roys;

import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import java.net.URLEncoder;

/**
 * @author Ji hyung Moon <mjihyung@gmail.com>
 * @version 1.0
 */
public class Summarizer {
  private String analyzerUrl = "https://nehac-ml-analyzer.p.mashape.com/article";
  private String size = "?size=";
  private String text = "&text=";
  private String urlEncodeString;

  /**
   * Initialize
   *
   * <p> summary api requires url-encoded text
   *
   * @param inputText plain text that converted to url-encoded text
   */
  public Summarizer(String inputText) {
    try {
      this.urlEncodeString = URLEncoder.encode(inputText, "ASCII");
    } catch (Exception e) {
      Log.d("Summarizer Initialize", e.getMessage(), e);
    }
  }

  /**
   * Get any length of summary of text
   *
   * @param outputSize length of summarized sentences
   * @return summary text
   */
  public String getSummary(int outputSize) {
    try {
      HttpResponse<JsonNode> response = Unirest.get(analyzerUrl + size
          + String.valueOf(outputSize) + text + urlEncodeString)
          .header("X-Mashape-Key", "HJUpHedt5ImshhJMjvFOhZt8ciSWp1PKGMKjsnngAnsZhfF3f4") // get Key from hyeonjong
          .header("Accept", "application/json")
          .asJson();
      return response.getBody().toString();
      // TODO(Juung): check the result form of result
    } catch (Exception e) {
      Log.e("No summary response", e.getMessage(), e);
      return null;
    }
  }
}
