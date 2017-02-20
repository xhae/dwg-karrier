package com.dwg_karrier.roys;

import android.provider.BaseColumns;

public final class DataBases {
  public static final class CreateDB implements BaseColumns {
    public static final String READ = "read";
    public static final String URL = "url";
    public static final String CONTENT = "content";
    public static final String TITLE = "title";
    public static final String REPIMAGEURL = "repImage";
    public static final String EXPECTEDTIME = "expectedtime";
    public static final String _TABLENAME = "page";
    public static final String KEYWORDS = "keywords";
    public static final String ISRECOMMENDED = "isrecommeded";
    public static final String _CREATE =
        "create table " + _TABLENAME + "("
            + _ID + " integer primary key autoincrement, "
            + READ + " integer default 0 , "
            + URL + " text not null , "
            + TITLE + " text default '' , "
            + REPIMAGEURL + " text default '' , "
            + CONTENT + " text default '' , "
            + EXPECTEDTIME + " integer, "
            + KEYWORDS + " text default '' , "
            + ISRECOMMENDED + " integer default 0 '' , "
            // TODO: Data Type Change
            // if wordCount will change to expectedTime and data type change to double -> USE REAL TYPE instead INTEGER
            + "unique(" + URL + ")" + ");";
  }
}
