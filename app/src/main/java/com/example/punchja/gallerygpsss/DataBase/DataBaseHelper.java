package com.example.punchja.gallerygpss.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "phone.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "photo";
    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_LOCATION = "loaction";
    public static final String COL_IMAGE = "image";

    private static final String SQL_CREATE_TABLE_PHONE
            = "CREATE TABLE " + TABLE_NAME + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_TITLE + " TEXT,"
            + COL_LOCATION + " TEXT,"
            + COL_IMAGE + " TEXT"
            + ")";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_PHONE);

        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, "Alp");
        cv.put(COL_LOCATION, "-456,+476");
        cv.put(COL_IMAGE, "alp.jpg");
        db.insert(TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(COL_TITLE, "Everrest");
        cv.put(COL_LOCATION, "+164,-456");
        cv.put(COL_IMAGE, "everest.jpg");
        db.insert(TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
