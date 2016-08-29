package com.baic.cache.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baic.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baic on 16/5/13.
 */
public class DBManager {

    private static DBHelper dbHelper = null;
    private static final int maxNum = 10000;

    public static synchronized void init(Context context) {
        dbHelper = DBHelper.getInstance(context);
    }

    public static synchronized void init(Context context, String dbName) {
        dbHelper = DBHelper.getInstance(context, dbName);
    }

    private static void deleteExtra(String table, int maxNum) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.beginTransaction();
            db.execSQL(String.format("delete from %s where (select count(_id) from %s )> %d and _id in (select _id from %s order by _id desc limit (select count(_id) from %s) offset %d )",
                    table, table, maxNum, table, table, maxNum));

            db.setTransactionSuccessful();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static synchronized void insert(String table, String key, String content, String contentId, long time) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY, key);
            contentValues.put(DBHelper.CONTENT_ID, contentId);
            contentValues.put(DBHelper.CONTENT, content);
            contentValues.put(DBHelper.TIME, time);
            db.insertWithOnConflict(table, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static synchronized void delete(String table, String key) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            StringBuilder sb = new StringBuilder(DBHelper.KEY);
            sb.append(" = ?");
            db.delete(table, sb.toString(), new String[]{key});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static synchronized void update(String table, String key, String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.CONTENT, content);
            StringBuilder sb = new StringBuilder(DBHelper.KEY);
            sb.append(" = ?");
            db.updateWithOnConflict(table, contentValues, sb.toString(), new String[]{key}, SQLiteDatabase.CONFLICT_REPLACE);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static synchronized Data select(String table, String key) {
        List<Data> list = selectList(table, key);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public static synchronized List<Data> selectList(String table, String key) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Data> dataList = null;
        try {
            cursor = db.query(table, null, String.format("%s = '%s'", DBHelper.KEY, key), null, null, null, null);
            if (cursor.getCount() > 0) {
                dataList = new ArrayList<Data>();
                Data data = null;
                while (cursor.moveToNext()) {
                    data = new Data();
                    data.setKey(cursor.getString(cursor.getColumnIndex(DBHelper.KEY)));
                    data.setContentId(cursor.getString(cursor.getColumnIndex(DBHelper.CONTENT_ID)));
                    data.setContent(cursor.getString(cursor.getColumnIndex(DBHelper.CONTENT)));
                    data.setTime(cursor.getLong(cursor.getColumnIndex(DBHelper.TIME)));
                    dataList.add(data);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return dataList;
    }

    public static synchronized void clearTable(String table) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(table, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static synchronized void save(String table, String key, String value, long time) {
        insert(table, key, value, null, time);
    }

    public static synchronized void save(String key, String value, long time) {
        save(DBHelper.TABLE_NAME, key, value, time);
    }

    public static synchronized void save(String key, String value) {
        save(key, value, System.currentTimeMillis());
    }

    public static synchronized void saveTmp(String key, String value, long time) {
        save(DBHelper.TMP_TABLE_NAME, key, value, time);
        deleteExtra(DBHelper.TMP_TABLE_NAME, maxNum);
    }

    public static synchronized void saveTmp(String key, String value) {
        saveTmp(key, value, System.currentTimeMillis());
    }

    public static Data get(String table, String key) {
        return select(table, key);
    }

    public static List<Data> getList(String table, String key) {
        return selectList(table, key);
    }

    public static Data get(String key) {
        return get(DBHelper.TABLE_NAME, key);
    }

    public static Data getTmp(String key) {
        return get(DBHelper.TMP_TABLE_NAME, key);
    }

    public static List<Data> getList(String key) {
        return getList(DBHelper.TABLE_NAME, key);
    }

    public static List<Data> getTmpList(String key) {
        return getList(DBHelper.TMP_TABLE_NAME, key);
    }

    public static void remove(String key) {
        delete(DBHelper.TABLE_NAME, key);
    }

    public static void removeTmp(String key) {
        delete(DBHelper.TMP_TABLE_NAME, key);
    }

    public static void clear(){
        DBManager.clearTable(DBHelper.TABLE_NAME);
    }

    public static void clearTmp(){
        DBManager.clearTable(DBHelper.TMP_TABLE_NAME);
    }
}