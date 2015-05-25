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

/**
 * @author weibin
 *
 */
public class CoolWeatherDB {
	
	private static final String TAG = "CoolWeatherDB";
	/**database name.*/
	private static final String DB_NAME = "cool_weather";
	/**database version.*/
	private static final int DB_VERSION = 2;
	
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
		try {
			if (province != null ) {
				ContentValues values = new ContentValues();
				values.put("province_code", province.getProvinceCode());
				values.put("province_name", province.getProvinceName());
				db.insert("provinces", null, values);
			}else {
				LogUtil.i(TAG, "province is null in saveProvince().");
			}
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage() + " in saveCounty().");
		}
	}
	
	/**load all provinces from DB.*/
	public List<Province> loadProvinces() {
		List<Province> provinces = new ArrayList<Province>();
		Cursor cursor = db.query("provinces", null, null, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			try {
				do{
					Province province = new Province();
					province.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
					province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
					province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
					provinces.add(province);
				}while(cursor.moveToNext());
			} catch (Exception e) {
				LogUtil.e(TAG, e.getMessage() + " in loadProvinces().");
			}finally{
				cursor.close();
			}
		}else {
			LogUtil.i(TAG, "cursor is null or no data in loadProvinces().");
		}
		
		return provinces;
	}
	
	/**save city entity into DB.*/
	public void saveCity(City city) {
		try {
			if (city != null) {
				ContentValues values = new ContentValues();
				values.put("city_code", city.getCityCode());
				values.put("city_name", city.getCityName());
				values.put("province_code", city.getProvinceCode());
				db.insert("cities", null, values);
			} else {
				LogUtil.i(TAG, "city is null in saveCity().");
			}
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage() + " in saveCity().");
		}
	}
	
	/**load all cities from DB.*/
	public List<City> loadCities(String provinceCode) {
		List<City> cities = new ArrayList<City>();
		Cursor cursor = db.query("cities", null, "province_code = ?", new String[]{provinceCode}, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			try {
				do {
					City city = new City();
					city.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
					city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
					city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
					city.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
					cities.add(city);
				} while (cursor.moveToNext());
			} catch (Exception e) {
				LogUtil.e(TAG, e.getMessage() + " in loadCities().");
			}finally{
				cursor.close();
			}
		} else {
			LogUtil.i(TAG, "cursor is null or no data in loadCities()");
		}
		return cities;
	}
	
	/**save county entity into DB.*/
	public void saveCounty(County county) {
		try {
			if (county != null) {
				ContentValues values = new ContentValues();
				values.put("county_code", county.getCountyCode());
				values.put("county_name", county.getCountyName());
				values.put("city_code", county.getCityCode());
				db.insert("counties", null, values);
			} else {
				LogUtil.i(TAG, "county is null or no data in saveCounty().");
			}
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage() + " in saveCounty().");
			
		}
	}
	
	/**load all counties from DB.*/
	public List<County> loadCounties(String cityCode) {
		List<County> counties = new ArrayList<County>();
		Cursor cursor = db.query("counties", null, "city_code = ?", new String[]{cityCode}, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			try {
				do {
					County county = new County();
					county.setCountyId(cursor.getInt(cursor.getColumnIndex("county_id")));
					county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
					county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
					county.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
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
