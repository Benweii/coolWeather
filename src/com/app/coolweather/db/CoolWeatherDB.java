/**
 * 
 */
package com.app.coolweather.db;

import java.util.ArrayList;
import java.util.List;

import com.app.coolweather.model.City;
import com.app.coolweather.model.County;
import com.app.coolweather.model.Province;
import com.app.coolweather.util.LogUtil;
import com.app.coolweather.util.MyAppUtil;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author weibin
 *
 */
public class CoolWeatherDB {
	
	private static final String TAG = "CoolWeatherDB";
	/**database name.*/
	private static final String DB_NAME = "cool_weather";
	/**database version.*/
	private static final int DB_VERSION = 1;
	
	private static CoolWeatherDB  coolWeatherDB;
	private SQLiteDatabase db;
	
	/**private constructor.*/
	private CoolWeatherDB(){
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(MyAppUtil.getContext(),
				DB_NAME, null, DB_VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	/**get CoolWeather class instance.*/
	public static synchronized CoolWeatherDB getInstance() {
		
		if (coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB();
		}
		
		return coolWeatherDB;
	}
	
	/**save province entity into DB.*/
	public void saveProvince(Province province) {
		if (province != null ) {
			ContentValues values = new ContentValues();
			values.put("province_code", province.getProvinceCode());
			values.put("province_name", province.getProvinceName());
			db.insert("provinces", null, values);
		}else {
			LogUtil.i(TAG, "province is null.");
		}
	}
	
	/**load all provinces from DB.*/
	public List<Province> loadProvinces() {
		List<Province> provinces = new ArrayList<Province>();
		Cursor cursor = db.query("provinces", null, null, null, null, null, null);
		if (cursor != null) {
			try {
				cursor.moveToFirst();
				do{
					Province province = new Province();
					province.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
					province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
					province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
					provinces.add(province);
				}while(cursor.moveToNext());
			} catch (Exception e) {
				LogUtil.e(TAG, e.getMessage() + " in loadProvinces.");
			}finally{
				cursor.close();
			}
		}else {
			LogUtil.i(TAG, "cursor is null in loadProvinces().");
		}
		
		return provinces;
	}
	
	/**save city entity into DB.*/
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_code", city.getCityCode());
			values.put("city_name", city.getCityName());
			values.put("province_id", city.getProvinceId());
			db.insert("cities", null, values);
		} else {
			LogUtil.i(TAG, "city is null.");
		}
	}
	
	/**load all cities from DB.*/
	public List<City> loadCities() {
		List<City> cities = new ArrayList<City>();
		Cursor cursor = db.query("cities", null, null, null, null, null, null);
		if (cursor != null) {
			try {
				cursor.moveToFirst();
				do {
					City city = new City();
					city.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
					city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
					city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
					city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
					cities.add(city);
				} while (cursor.moveToNext());
			} catch (Exception e) {
				LogUtil.e(TAG, e.getMessage() + " in loadCities().");
			}finally{
				cursor.close();
			}
		} else {
			LogUtil.i(TAG, "cursor is null in loadCities()");
		}
		return cities;
	}
	
	/**save county entity into DB.*/
	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_code", county.getCountyCode());
			values.put("county_name", county.getCountyName());
			values.put("city_id", county.getCityId());
			db.insert("counties", null, values);
		} else {
			LogUtil.i(TAG, "county is null.");
		}
	}
	
	/**load all counties from DB.*/
	public List<County> loadCounties() {
		List<County> counties = new ArrayList<County>();
		Cursor cursor = db.query("counties", null, null, null, null, null, null);
		if (cursor != null) {
			try {
				cursor.moveToFirst();
				do {
					County county = new County();
					county.setCountyId(cursor.getInt(cursor.getColumnIndex("county_id")));
					county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
					county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
					county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
					counties.add(county);
				} while (cursor.moveToNext());
			} catch (Exception e) {
				LogUtil.e(TAG, e.getMessage() + " in loadCounties().");
			}finally{
				cursor.close();
			}
		} else {
			LogUtil.i(TAG, "cursor is null in loadCounties().");
		}
		
		return counties;
	}
}
