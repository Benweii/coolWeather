/**
 * 
 */
package com.app.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author weibin
 *
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper{
	
	/**creates provinces table statement.*/
	private static final String CREATE_TABLE_PROVINCES = "create table provinces ("
				+ "province_id integer primary key autoincrement,"
				+ "province_code text,"
				+ "province_name text)";
	/**creates cities table statement.*/
	private static final String CREATE_TABLE_CITIES = "create table cities ("
				+ "city_id integer primary key autoincrement,"
				+ "city_code text,"
				+ "city_name text,"
				+ "province_id integer)";
	/**creates counties table statement.*/
	private static final String CREATE_TABLE_COUNTIES = "create table counties ("
				+ "county_id integer primary key autoincrement,"
				+ "county_code text,"
				+ "county_name text,"
				+ "city_id integer)";
	
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//creates provinces table.
		db.execSQL(CREATE_TABLE_PROVINCES);
		//creates cities table.
		db.execSQL(CREATE_TABLE_CITIES);
		//creates counties table.
		db.execSQL(CREATE_TABLE_COUNTIES);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
}
