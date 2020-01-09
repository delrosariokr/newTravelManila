package com.example.manila;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelperForUsers extends SQLiteOpenHelper {

    //User
    public static final String Database_Name = "Accounts.db";
    public static final String Database_Table = "Accounts_List";
    public static final String Column_ID = "User_ID";
    public static final String Column_FName = "FirstName";
    public static final String Column_LName = "LastName";
    public static final String Column_Email = "Email";
    public static final String Column_Pass = "Password";
    public static final String Column_Level = "Level";
    public static final String Column_Exp = "Experience";

    //UserVisit
    public static final String Database_Table_UserVisit = "User_Visit";
    public static final String Column_Date_UserVisit = "Date";

    //Landmarks
    public static final String Database_Table_Landmarks = "LandmarksList";
    public static final String Column_ID_Landmarks = "Landmarks_ID";
    public static final String Column_Description_Landmarks = "Description";
    public static final String Column_Title_Landmarks = "Title";

    //User_Visited
    public static final String Database_Table_Visited = "VisitedList";
    public static final String Column_Date_Visited = "Date";

    //Rewards
    public static final String Database_Table_Rewards = "RewardsList";
    public static final String Column_ID_Rewards = "Rewards_ID";
    public static final String Column_Description_Rewards = "Description";
    public static final String Column_RewardsCode_Rewards = "RewardsCode";


    public DatabaseHelperForUsers(@Nullable Context context) {
        super(context, Database_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Table for Users
        db.execSQL("CREATE TABLE " + Database_Table + " (" + Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ","
                + Column_FName + " TEXT NOT NULL " + ","
                + Column_LName + " TEXT NOT NULL " + ","
                + Column_Email + " TEXT UNIQUE NOT NULL " + ","
                + Column_Pass + " TEXT NOT NULL " + ","
                + Column_Level + " INTEGER NOT NULL " + ","
                + Column_Exp + " INTEGER NOT NULL) ");

        //Table for Landmarks
        db.execSQL("CREATE TABLE " + Database_Table_Landmarks + " (" + Column_ID_Landmarks + " INTEGER PRIMARY KEY AUTOINCREMENT" + ","
                + Column_Title_Landmarks + " TEXT " + ","
                + Column_Description_Landmarks + " TEXT) ");

        //Table for User_Visit
        db.execSQL("CREATE TABLE " + Database_Table_UserVisit + " (" + Column_ID + " INTEGER " + ","
                + Column_ID_Landmarks + " INTEGER " + ","
                + Column_Date_UserVisit + " TEXT " + ","
                + "FOREIGN KEY " + "(" + Column_ID_Landmarks + ")" + " REFERENCES " + Database_Table_Landmarks + "(" + Column_ID + ")"
                + ", FOREIGN KEY " + "(" + Column_ID + ")" + " REFERENCES " + Database_Table + "(" + Column_ID + ")" + ")");

        //Table for User_Visited
        db.execSQL("CREATE TABLE " + Database_Table_Visited + " (" + Column_ID + " INTEGER " + ","
                + Column_ID_Landmarks + " INTEGER " + ","
                + Column_Date_Visited + " TEXT " + ","
                + "FOREIGN KEY " + "(" + Column_ID_Landmarks + ")" + " REFERENCES " + Database_Table_Landmarks + "(" + Column_ID + ")"
                + ", FOREIGN KEY " + "(" + Column_ID + ")" + " REFERENCES " + Database_Table + "(" + Column_ID + ")" + ")");

        //Table for Rewards
        db.execSQL("CREATE TABLE " + Database_Table_Rewards + " (" + Column_ID_Rewards + " INTEGER PRIMARY KEY AUTOINCREMENT" + ","
                + Column_Description_Rewards + " TEXT " + ","
                + Column_RewardsCode_Rewards + " TEXT) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Database_Table);
        db.execSQL("DROP TABLE IF EXISTS " + Database_Table_Visited);
        db.execSQL("DROP TABLE IF EXISTS " + Database_Table_Landmarks);
        db.execSQL("DROP TABLE IF EXISTS " + Database_Table_Rewards);
        db.execSQL("DROP TABLE IF EXISTS " + Database_Table_UserVisit);
        onCreate(db);
    }

    public boolean insertDataInRewards(String Description, String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column_Description_Rewards, Description);
        contentValues.put(Column_RewardsCode_Rewards, code);
        long result = db.insert(Database_Table_Rewards, null, contentValues);
        if (result == -1) {
            return false;
        }
        return true;
    }

    public boolean insertDataInLandmarks(String Title, String Description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column_Title_Landmarks, Title);
        contentValues.put(Column_Description_Landmarks, Description);
        long result = db.insert(Database_Table_Landmarks, null, contentValues);
        if (result == -1) {
            return false;
        }
        return true;
    }

    public boolean insertDataInVisited(String UserID, String LandmarksID, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column_ID, UserID);
        contentValues.put(Column_ID_Landmarks, LandmarksID);
        contentValues.put(Column_Date_Visited, date);
        long result = db.insert(Database_Table_Visited, null, contentValues);
        if (result == -1) {
            return false;
        }
        return true;
    }

    public boolean insertDataInUserVisit(int UserID, int LandmarksID, String Date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column_ID, UserID);
        contentValues.put(Column_ID_Landmarks, LandmarksID);
        contentValues.put(Column_Date_UserVisit, Date);
        long result = db.insert(Database_Table_UserVisit, null, contentValues);
        if (result == -1) {
            return false;
        }
        return true;
    }

    public boolean insertData(String Fname, String Lname, String Email, String Pass, int Level, int Exp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column_FName, Fname);
        contentValues.put(Column_LName, Lname);
        contentValues.put(Column_Email, Email);
        contentValues.put(Column_Pass, Pass);
        contentValues.put(Column_Level, Level);
        contentValues.put(Column_Exp, Exp);
        long result = db.insert(Database_Table, null, contentValues);
        if (result == -1) {
            return false;
        }
        return true;
    }

    public Boolean CheckLogin(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Database_Table, null);
        while (cursor.moveToNext()) {
            if (cursor.getString(3).equals(username) && cursor.getString(4).equals(password)) {
                return true;
            }
        }
        return false;
    }

    public Boolean CheckIfUser_ListExist(int userId, int landmarksId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Database_Table_UserVisit, null);
        while (cursor.moveToNext()) {
            if (cursor.getString(0).equals(String.valueOf(userId)) && cursor.getString(1).equals(String.valueOf(landmarksId))) {
                return true;
            }
        }
        return false;
    }

    public int getIDFromTableLandmarks(String Title) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Database_Table_Landmarks, null);
        while (cursor.moveToNext()) {
            if (cursor.getString(1).equals(Title)) {
                return cursor.getInt(0);
            }
        }
        return 0;
    }

    public int getIDFromTableUser(String User) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Database_Table, null);
        while (cursor.moveToNext()) {
            if (cursor.getString(3).equals(User)) {
                return cursor.getInt(0);
            }
        }
        return 0;
    }


    public String Description(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Database_Table_Landmarks, null);
        System.out.println("HereFirst");
        while (cursor.moveToNext()) {
            if (cursor.getString(1).equals(title)) {
                return cursor.getString(2);
            }
        }
        return "Failed, Doesn't exist";
    }

    public Cursor getAllDataForLandmarks() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Database_Table_Landmarks, null);
        return cursor;
    }

    public Cursor getAllDataForList(int userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Database_Table_UserVisit + " WHERE " + Column_ID + " = " + userID, null);
        return cursor;
    }

    public String getLandmarksBaseOnID(int LandmarkID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Database_Table_Landmarks, null);
        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == (LandmarkID)) {
                return cursor.getString(1);
            }
        }

        return "NULL on GetLandmarksBaseOnID";
    }

    public String getDate(int userID, int LandmarkID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Database_Table_UserVisit, null);
        while (cursor.moveToNext()) {
            if (cursor.getString(0).equals(String.valueOf(userID)) && cursor.getString(1).equals(String.valueOf(LandmarkID))) {
                return cursor.getString(2);
            }
        }
        return null;
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

    public Integer DeleteDataUserList(int userID,int landMarkID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Database_Table_UserVisit, Column_ID + " = ? AND "+ Column_ID_Landmarks+ " = ? ", new String[]{ String.valueOf(userID) , String.valueOf(landMarkID) });
    }

}

