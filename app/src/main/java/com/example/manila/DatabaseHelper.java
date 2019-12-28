package com.example.manila;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String Database_Name = "Accounts.db";
    public static final String Database_Table = "Accounts_List";
    public static final String Column_ID = "ID";
    public static final String Column_FName = "FirstName";
    public static final String Column_LName = "LastName";
    public static final String Column_Email = "Email";
    public static final String Column_Pass = "Password";


    public DatabaseHelper(@Nullable Context context) {
        super(context, Database_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Database_Table + " (" + Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ","
                + Column_FName + " TEXT NOT NULL " + ","
                + Column_LName + " TEXT NOT NULL " + ","
                + Column_Email + " TEXT UNIQUE NOT NULL " + ","
                + Column_Pass + " TEXT NOT NULL) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Database_Table);
        onCreate(db);
    }

    public boolean insertData(String Fname, String Lname, String Email, String Pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column_FName, Fname);
        contentValues.put(Column_LName, Lname);
        contentValues.put(Column_Email, Email);
        contentValues.put(Column_Pass, Pass);
        long result = db.insert(Database_Table, null, contentValues);
        if (result == -1) {
            return false;
        }
        return true;
    }

    public Boolean CheckLogin(String username,String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Database_Table, null);
        while (cursor.moveToNext()){
            if(cursor.getString(3).equals(username)&& cursor.getString(4).equals(password)){
                return true;
            }
        }
    return false;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Database_Table, null);
        return cursor;
    }

    public boolean UpdateData(String ID, String Fname, String Lname, String Email, String Pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column_ID, ID);
        contentValues.put(Column_FName, Fname);
        contentValues.put(Column_LName, Lname);
        contentValues.put(Column_Email, Email);
        contentValues.put(Column_Pass, Pass);
        db.update(Database_Table, contentValues, "ID = ?", new String[]{ID});
        return true;
    }

    public Integer DeleteData(String ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Database_Table, "ID = ?", new String[]{ID});
    }
}

