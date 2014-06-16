package com.svetlana.library;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SQLController {

	private DatabaseHandler dbhelper;
	private Context ourcontext;
	private SQLiteDatabase database;

	public SQLController(Context c) {
		ourcontext = c;
	}

	public SQLController open() throws SQLException {
		dbhelper = new DatabaseHandler(ourcontext);
		database = dbhelper.getWritableDatabase();
		return this;

	}

	public void close() {
		dbhelper.close();
	}

//	public long insertBusiness(String name) {
//		ContentValues cv = new ContentValues();
//		cv.put(DatabaseHandler.BUSINESS_NAME, name);
//		long error = database.insert(DatabaseHandler.TABLE_BUSINESS, null, cv);
//		//also insert into points
//		cv = new ContentValues();
//		cv.put(DatabaseHandler.BUSINESS_NAME, name);
//		cv.put(DatabaseHandler.KEY_UID,getPreferences("USER_ID"));
//		cv.put(DatabaseHandler.POINTS,Integer.valueOf(0));
//		//cv.put(DatabaseHandler.KEY_UID, uid);
//		if(error!=-1){
//		database.insert(DatabaseHandler.TABLE_POINTS, null, cv);
//		}
//		return error;
//
//	}
	
	
    public String getPreferences(String key)
    {
        SharedPreferences prefs = ourcontext.getSharedPreferences(ourcontext.getPackageName(), 0);
        return prefs.getString(key, "");
    }

//	public Cursor readBusinessEntry() {
//		String[] allColumns = new String[] { DatabaseHandler.BUSINESS_ID, DatabaseHandler.BUSINESS_NAME};
//
//		Cursor c = database.query(DatabaseHandler.TABLE_BUSINESS, allColumns, null, null, null,
//				null, null);
//
//		if (c != null) {
//			c.moveToFirst();
//		}
//		return c;
//
//	}
	
	public void updatePoints(String businessname, Integer uid, Integer pointvalues) {
		ContentValues cv = new ContentValues();
		cv.put(DatabaseHandler.BUSINESS_NAME, businessname);
		cv.put(DatabaseHandler.KEY_UID, uid);
		cv.put(DatabaseHandler.POINTS, pointvalues);
		String[] whereArgs = new String[] { businessname, String.valueOf(uid)};
		database.update(DatabaseHandler.TABLE_POINTS, cv, "businessname = ? and uid=? ", whereArgs);

	}

	public Cursor readPoints() {
		String[] allColumns = new String[] { DatabaseHandler.BUSINESS_NAME, DatabaseHandler.POINTS};
		Cursor c = database.query(DatabaseHandler.TABLE_POINTS, allColumns, null, null, null,
				null, null);

		if (c != null) {
			c.moveToFirst();
		}
		return c;

	}

}
