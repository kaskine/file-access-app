package com.example.kaskine.fileaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Kyle Askine
 * 11/28/17
 */

public class FileData {

    private static final String DEBUG_TAG = "FileData";

    private SQLiteDatabase database;
    private FileAccessDBHelper helper;

    private static final String[] ALL_COLUMNS = {
            FileAccessDBHelper.COLUMN_ID,
            FileAccessDBHelper.COLUMN_VALUE
    };

    /**
     * Constructor
     * @param context - The base Context of the Application
     */
    FileData(Context context) {

        helper = new FileAccessDBHelper(context);
    }

    /**
     * Opens a writable database connection
     */
    public void open() {

        database = helper.getWritableDatabase();
        Log.d(DEBUG_TAG, "Database opened.");
    }

    /**
     * Pushes the String input into the database
     * @param input - The input value to add to the database.
     */
    public void push(String input) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(FileAccessDBHelper.COLUMN_VALUE, input);
        database.insert(FileAccessDBHelper.TABLE_VALUES, null, contentValues);
        Log.d(DEBUG_TAG, "Item added to the database.");
    }

    /**
     * Pulls all values from the database.
     * @return A String containing all values in the database.
     */
    public String pull() {

        StringBuilder output = new StringBuilder();
        Cursor cursor = database.query(FileAccessDBHelper.TABLE_VALUES,
                                       ALL_COLUMNS,
                                       null,
                                       null,
                                       null,
                                       null,
                                       null);

        int row = cursor.getColumnIndex(FileAccessDBHelper.COLUMN_ID);
        int name = cursor.getColumnIndex(FileAccessDBHelper.COLUMN_VALUE);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String line = cursor.getString(row) + " " + cursor.getString(name) + "\n";
            output.append(line);
        }

        cursor.close();
        return output.toString();
    }

    /**
     * Deletes everything in the current database table.
     */
    public void delete() {
        helper.delete();
    }

    /**
     * Closes the database connection
     */
    public void close() {

        if (helper != null) {
            helper.close();
        }
    }
}
