package com.example.paolo.pm_scheduling;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "Scheduling";
    private static final String TABLE_Attendance = "attendance";

    private static final String COL_1 = "ID";
    private static final String COL_2 = "studNo";
    private static final String COL_3 = "studName";
    private static final String COL_4 = "Date";
    private static final String COL_5 = "Remarks";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //ito ay para sa attendace na table
        String createTable = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "studNo TEXT, studName TEXT, Date TEXT, Remarks TEXT)", TABLE_Attendance);
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP IF TABLE EXISTS %s", TABLE_Attendance));
        onCreate(db);
    }

    public boolean insertData(String studno, String studname, String date, String remarks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, studno);
        contentValues.put(COL_3, studname);
        contentValues.put(COL_4, date);
        contentValues.put(COL_5, remarks);

        Log.d(TAG, "insertData: Adding " + studno + " to " + TABLE_Attendance);
        long result = db.insert(TABLE_Attendance, null, contentValues);

        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_Attendance;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}
