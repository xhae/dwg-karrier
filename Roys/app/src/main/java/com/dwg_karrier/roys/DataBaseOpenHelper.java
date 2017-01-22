package com.dwg_karrier.roys;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBaseOpenHelper extends SQLiteOpenHelper {

  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "FeedReader.db";

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

  public int getReadedPageCount() {
    SQLiteDatabase dataBase = getReadableDatabase();
    int result = 0;

    Cursor cursor = dataBase.rawQuery("SELECT count(url) FROM page WHERE read = 1;", null);
    while (cursor.moveToNext()) {
      result = cursor.getInt(0);
    }

    return result;
  }

  public ArrayList<ScriptedURL> getUrlList() {
    SQLiteDatabase dataBase = getReadableDatabase();
    ArrayList<ScriptedURL> resultList = new ArrayList<ScriptedURL>();

    Cursor cursor = dataBase.rawQuery("SELECT * from page ", null);
    while (cursor.moveToNext()) {
      ScriptedURL scriptecItem = new ScriptedURL(cursor.getString(2),cursor.getInt(1));
      resultList.add(scriptecItem);
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
}