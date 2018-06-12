package com.example.kaskine.fileaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kyle Askine
 * 11/28/17
 */

public class FileAccessDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "file_access.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_VALUES = "SOMETHING_ELSE";
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_VALUE = "VALUE";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_VALUES +
                    " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_VALUE + " TEXT " +
                    ")";

    FileAccessDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_VALUES);
        onCreate(sqLiteDatabase);
    }

    /**
     * Clears the table by dropping and recreating it.
     */
    public void delete() {
        SQLiteDatabase database = getWritableDatabase();

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_VALUES);
        database.execSQL(TABLE_CREATE);
    }
}
