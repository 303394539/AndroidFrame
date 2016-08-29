package com.baic.cache.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by baic on 16/5/13.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "baic.cache.db";
    public static final String TABLE_NAME = "cache";
    public static final String TMP_TABLE_NAME = "tmpCache";

    public static final String KEY = "key";
    public static final String CONTENT_ID = "content_id";
    public static final String CONTENT = "content";
    public static final String TIME = "time";

    private static DBHelper instance = null;

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    public static DBHelper getInstance(Context context, String name) {
        if (instance == null || !instance.getDatabaseName().equals(name)) {
            instance = new DBHelper(context, name);
        }
        return instance;
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DBHelper(Context context, String name) {
        super(context, name, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();

        //cache
        sb.append("CREATE TABLE  IF NOT EXISTS ");
        sb.append(TABLE_NAME);
        sb.append(" (");
        sb.append("_id INTEGER PRIMARY KEY,");
        sb.append(KEY).append(" TEXT UNIQUE,");
        sb.append(CONTENT_ID).append(" TEXT,");
        sb.append(CONTENT).append(" TEXT,");
        sb.append(TIME).append(" TEXT");
        sb.append(");");
        db.execSQL(sb.toString());

        sb.delete(0, sb.length());
        sb.append("CREATE TABLE  IF NOT EXISTS ");
        sb.append(TMP_TABLE_NAME);
        sb.append(" (");
        sb.append("_id INTEGER PRIMARY KEY,");
        sb.append(KEY).append(" TEXT UNIQUE,");
        sb.append(CONTENT_ID).append(" TEXT,");
        sb.append(CONTENT).append(" TEXT,");
        sb.append(TIME).append(" TEXT");
        sb.append(");");
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            oldVersion++;
        }
    }
}
