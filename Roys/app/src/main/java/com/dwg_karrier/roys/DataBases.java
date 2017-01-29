package com.dwg_karrier.roys;

import android.provider.BaseColumns;

public final class DataBases {
  public static final class CreateDB implements BaseColumns {
    public static final String READ = "read";
    public static final String URL = "url";
    public static final String WORDCOUNT = "wordCount";
    public static final String _TABLENAME = "page";
    public static final String _CREATE =
        "create table " + _TABLENAME + "("
            + _ID + " integer primary key autoincrement, "
            + READ + " integer , "
            + URL + " text not null , "
            + WORDCOUNT + " integer, "
            + "unique(" + URL + ")" + ");";
  }
}