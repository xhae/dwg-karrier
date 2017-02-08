package com.dwg_karrier.roys;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBaseOpenHelper extends SQLiteOpenHelper {
  public static final int DATABASE_VERSION = 2;
  public static final String DATABASE_NAME = "FeedReader.db";
  public final int readColumn = 1;
  public final int urlColumn = 2;
  public final int titleColumn = 3;
  public final int repImageUrlColumn = 4;
  public final int contentColumn = 5;
  public final int wordCountColumn = 6;

  public DataBaseOpenHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public void onCreate(SQLiteDatabase db) {
    db.execSQL(DataBases.CreateDB._CREATE);
  }

  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("drop table page;");
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
      ScriptedURL scriptedItem = new ScriptedURL(cursor.getString(urlColumn),
          cursor.getInt(readColumn), cursor.getString(titleColumn),
          cursor.getString(contentColumn), cursor.getString(repImageUrlColumn),
          cursor.getInt(wordCountColumn));
      resultList.add(scriptedItem);
    }

    return resultList;
  }

  public void setIsRead(String url, int status) {
    String query = "read = " + status;
    updateDataQuery(query, url);
  }

  public void setWordCount(String url, int wordCount) {
    /*
     * TODO(Juung): omit wordCount from input --> this should be called from crawler
     */
    String query = "wordCount = " + wordCount;
    updateDataQuery(query, url);
  }

  public void setTitle(String url, String title) {
    String query = "title = '" + title + "'";
    updateDataQuery(query, url);
  }

  public void setRepImageUrl(String url, String imageUrl) {
    String query = "repImage = '" + imageUrl + "'";
    updateDataQuery(query, url);
  }

  public void setContent(String url, String content) {
    String query = "content = '" + content + "'";
    updateDataQuery(query, url);
  }

  private void updateDataQuery(String query, String url) {
    SQLiteDatabase dataBase = getWritableDatabase();
    dataBase.execSQL("UPDATE PAGE SET " + query + " WHERE url='" + url + "';");
    dataBase.close();
  }

  //for test
  public void deleteAllPage() {
    SQLiteDatabase dataBase = getWritableDatabase();
    dataBase.execSQL("delete from page;");
    dataBase.close();
  }

  private boolean isDuplicationUrl(String url) {
    SQLiteDatabase dataBase = getReadableDatabase();
    int urlDuplicationCount;
    Cursor cursor = dataBase.rawQuery("SELECT * FROM PAGE WHERE URL = ('" + url + "') ", null);
    urlDuplicationCount = cursor.getCount();
    dataBase.close();

    if (urlDuplicationCount == 0) {
      return false;
    } else {
      return true;
    }
  }

  public void insertScriptedData(String url) {
    SQLiteDatabase dataBase = getWritableDatabase();
    dataBase.execSQL("INSERT INTO PAGE (URL) VALUES ('" + url + "');");
    dataBase.close();
  }

  //For test
  public void getTableAsString() {
    SQLiteDatabase db = getReadableDatabase();
    String tableName = "page";

    String tableString = String.format("Table %s:\n", tableName);
    Cursor allRows = db.rawQuery("SELECT * FROM " + tableName, null);
    if (allRows.moveToFirst()) {
      String[] columnNames = allRows.getColumnNames();
      do {
        for (String name : columnNames) {
          tableString += String.format("%s: %s\n", name,
              allRows.getString(allRows.getColumnIndex(name)));
        }
        tableString += "\n";

      } while (allRows.moveToNext());
    }
  }

  public void insertScriptedDataWithCheckDuplication(String url) {
    if (!isDuplicationUrl(url)) {
      insertScriptedData(url);
    }
  }

  public void insertScriptedUrl(ScriptedURL scriptedUrl) {
    if (!isDuplicationUrl(scriptedUrl.getUrl())) {
      insertScriptedUrlQuery(scriptedUrl);
    }
  }

  private void insertScriptedUrlQuery(ScriptedURL scriptedURL) {
    int readValue;
    if (scriptedURL.getIsRead()) {
      readValue = 1;
    } else {
      readValue = 0;
    }
    SQLiteDatabase dataBase = getWritableDatabase();
    dataBase.execSQL("INSERT INTO PAGE VALUES (" + readValue + ", '" + scriptedURL.getUrl() + "', '"
        + scriptedURL.getTitle() + "', '" + scriptedURL.getRepImageUrl() + "', '"
        + scriptedURL.getContent() + "', " + scriptedURL.getWordCount() + ");");
    dataBase.close();
  }
}
