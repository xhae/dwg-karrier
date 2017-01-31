package com.dwg_karrier.roys;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBaseOpenHelper extends SQLiteOpenHelper {
  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "FeedReader.db";
  public final int readColumn = 1;
  public final int urlColumn = 2;
  public final int wordCountColumn = 3;

  public DataBaseOpenHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public void onCreate(SQLiteDatabase db) {
    db.execSQL(DataBases.CreateDB._CREATE);
  }

  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    onCreate(db);
  }

  public int getTotalPageCount() {
    SQLiteDatabase dataBase = getReadableDatabase();
    int result = 0;

    Cursor cursor = dataBase.rawQuery("SELECT count(*) from page ", null);
    while (cursor.moveToNext()) {
      result = cursor.getInt(0);
    }

    return result;
  }

  public int getReadPageCount() {
    SQLiteDatabase dataBase = getReadableDatabase();
    int result = 0;

    Cursor cursor = dataBase.rawQuery("SELECT count(url) FROM page WHERE read = 1;", null);
    while (cursor.moveToNext()) {
      result = cursor.getInt(0);
    }

    return result;
  }

  public ArrayList<ScriptedURL> getAllUrlList() {
    String getUrlListQuery = "SELECT * from page";
    return getUrlListFromQuery(getUrlListQuery);
  }

  public ArrayList<ScriptedURL> getUnreadUrlList() {
    String unreadQuery = "SELECT * from page where read = 0";
    return getUrlListFromQuery(unreadQuery);
  }

  private ArrayList<ScriptedURL> getUrlListFromQuery(String query) {
    SQLiteDatabase dataBase = getReadableDatabase();
    ArrayList<ScriptedURL> resultList = new ArrayList<ScriptedURL>();

    Cursor cursor = dataBase.rawQuery(query, null);
    while (cursor.moveToNext()) {
      ScriptedURL scriptedItem = new ScriptedURL(cursor.getString(urlColumn), cursor.getInt(readColumn), cursor.getInt(wordCountColumn));
      resultList.add(scriptedItem);
    }

    return resultList;
  }

  public void setIsRead(String url, int status) {
    SQLiteDatabase dataBase = getWritableDatabase();

    dataBase.execSQL("UPDATE PAGE SET read=" + status + " WHERE url='" + url + "';");
    dataBase.close();
  }

  public void setWordCount(String url, int wordCount) {
    SQLiteDatabase dataBase = getWritableDatabase();

    dataBase.execSQL("UPDATE PAGE SET wordCount=" + wordCount + " WHERE url='" + url + "';");
    dataBase.close();
  }

  public void insertScriptedData(String url) {
    SQLiteDatabase dataBase = getWritableDatabase();
    dataBase.execSQL("INSERT INTO PAGE (URL) VALUES ('" + url + "');");
    dataBase.close();
  }

}
