package com.nextradioapp.androidSDK.data;

import android.database.Cursor;

public class CursorHelper {
    Cursor mCursor;

    public CursorHelper(Cursor cursor) {
        this.mCursor = cursor;
    }

    public String getString(String columnName) {
        return this.mCursor.getString(this.mCursor.getColumnIndex(columnName));
    }

    public long getLong(String columnName) {
        return this.mCursor.getLong(this.mCursor.getColumnIndex(columnName));
    }

    public int getInt(String columnName) {
        return this.mCursor.getInt(this.mCursor.getColumnIndex(columnName));
    }

    public boolean getBoolean(String columnName) {
        return this.mCursor.getInt(this.mCursor.getColumnIndex(columnName)) == 1;
    }

    public CursorHelper moveToPosition(int position) {
        this.mCursor.moveToPosition(position);
        return this;
    }
}
