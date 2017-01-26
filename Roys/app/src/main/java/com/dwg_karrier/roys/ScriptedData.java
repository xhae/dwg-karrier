package com.dwg_karrier.roys;

/**
 * Created by userpc on 2017-01-16.
 */

public class ScriptedData {
  private String title;
  private String spend_time;

  public String getTitle(){
    return title;
  }
  public String getSpend_time(){
    return spend_time;
  }

  public ScriptedData(String title, String spend_time){
    this.title = title;
    this.spend_time = spend_time;
  }
}
