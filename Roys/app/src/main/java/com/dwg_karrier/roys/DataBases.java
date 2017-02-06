package com.dwg_karrier.roys;

import android.provider.BaseColumns;

public final class DataBases {
  public static final class CreateDB implements BaseColumns {
    public static final String READ = "read";
    public static final String URL = "url";
    public static final String EXPECTEDTIME = "expectedtime";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String _TABLENAME = "page";
    public static final String IMGURL = "imgurl";
    public static final String _CREATE =
        "create table " + _TABLENAME + "("
            + _ID + " integer primary key autoincrement, "
            + READ + " integer default 0 , "
            + URL + " text not null , "
            + TITLE + " text, "
            + CONTENT + " text, "
            + EXPECTEDTIME + " integer, "
            + IMGURL + " text, "
            + "unique(" + URL + ")" + ");";
  }
}