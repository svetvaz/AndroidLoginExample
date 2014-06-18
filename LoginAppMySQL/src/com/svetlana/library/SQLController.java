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


	
	
    public String getPreferences(String key)
    {
        SharedPreferences prefs = ourcontext.getSharedPreferences(ourcontext.getPackageName(), 0);
        return prefs.getString(key, "");
    }


	
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
