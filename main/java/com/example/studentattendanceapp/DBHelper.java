package com.example.studentattendanceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Attendance.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE Subject (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL)"
        );
        db.execSQL(
                "CREATE TABLE Attendance (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "subjectId INTEGER NOT NULL, " +
                        "date TEXT NOT NULL, " +
                        "present INTEGER NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS Subject");
        db.execSQL("DROP TABLE IF EXISTS Attendance");
        onCreate(db);
    }


    public boolean addSubject(String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        long result = db.insert("Subject", null, cv);
        return result != -1;
    }


    public Cursor getSubjects() {
        return getReadableDatabase()
                .rawQuery("SELECT id, name FROM Subject", null);
    }


    public boolean addAttendance(int subjectId, String date, boolean present) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("subjectId", subjectId);
        cv.put("date", date);
        cv.put("present", present ? 1 : 0);
        long result = db.insert("Attendance", null, cv);
        return result != -1;
    }


    public Cursor getAttendance(int subjectId) {
        return getReadableDatabase().rawQuery(
                "SELECT id, date, present FROM Attendance WHERE subjectId = ? ORDER BY date",
                new String[]{ String.valueOf(subjectId) }
        );
    }


    public int getPresentCount(int subjectId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM Attendance WHERE subjectId = ? AND present = 1",
                new String[]{ String.valueOf(subjectId) }
        );
        int count = 0;
        if (c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();
        return count;
    }


    public int getTotalCount(int subjectId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM Attendance WHERE subjectId = ?",
                new String[]{ String.valueOf(subjectId) }
        );
        int count = 0;
        if (c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();
        return count;
    }
}
