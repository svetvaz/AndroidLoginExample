package com.svetlana.library;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 10;

    // Database Name
    private static final String DATABASE_NAME = "advandroid";

    // Login table name
    private static final String TABLE_LOGIN = "login";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "fname";
    private static final String KEY_LASTNAME = "lname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "uname";
    public static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

	public static final String BUSINESS_NAME = "businessname";
	public static final String TABLE_POINTS = "points";
	public static final String POINTS_ID = "pid";
	public static final String POINTS = "pointvalues";
	 private static final String UPDATED_AT = "updated_at";
		// TABLE CREATION STATEMENT

//		private static final String CREATE_BUSINESS_TABLE = "create table " + TABLE_BUSINESS
//				+ "(" + BUSINESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//				+ BUSINESS_NAME + " TEXT NOT NULL, " + "UNIQUE(" + BUSINESS_NAME + ")););";
		
		private static final String CREATE_POINTS_TABLE = "create table " + TABLE_POINTS
				+ "(" + POINTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ BUSINESS_NAME + " TEXT NOT NULL, "+ KEY_UID + " TEXT NOT NULL, "+ POINTS + " TEXT NOT NULL, "
		                + UPDATED_AT + " TEXT" + ")";


	
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FIRSTNAME + " TEXT,"
                + KEY_LASTNAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_USERNAME + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
//        db.execSQL(CREATE_BUSINESS_TABLE);
        db.execSQL(CREATE_POINTS_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUSINESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String fname, String lname, String email, String uname, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, fname); // FirstName
        values.put(KEY_LASTNAME, lname); // LastName
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_USERNAME, uname); // UserName
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
//        db.execSQL(CREATE_BUSINESS_TABLE);
        db.close(); // Closing database connection
    }


    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("fname", cursor.getString(1));
            user.put("lname", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("uname", cursor.getString(4));
            user.put("uid", cursor.getString(5));
            user.put("created_at", cursor.getString(6));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }
    
    
    public void setPoints(String uid, String businessname, String points, String updated_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_UID, uid); 
        values.put(BUSINESS_NAME, businessname); 
        values.put(POINTS, points); 
        values.put(UPDATED_AT, updated_at); 
        // Inserting Row
        db.insert(TABLE_POINTS, null, values);
        db.close(); // Closing database connection
    }   


    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }


    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows from all tables
        db.delete(TABLE_LOGIN, null, null);
//        db.delete(TABLE_BUSINESS, null, null);
        db.delete(TABLE_POINTS, null, null);
        db.close();
    }
    
    



}
