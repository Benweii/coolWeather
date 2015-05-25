/**
 * 
 */
package com.app.coolweather.util;

import android.text.TextUtils;

import com.app.coolweather.db.CoolWeatherDB;
import com.app.coolweather.model.City;
import com.app.coolweather.model.County;
import com.app.coolweather.model.Province;

/**
 * @author weibin
 *
 */
public class Utility {
	private static final String TAG = "Utility";

	/**Parse and process provinces data from server. Then save them to DB.*/
	public static synchronized boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			LogUtil.d(TAG, "Provinces response:" + response);
			String[] provinces = response.split(",");
			if (provinces != null && provinces.length > 0) {
				for (String p : provinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//Save parse province data into table by call saveProvince().
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, String provinceCode) {
		if (!TextUtils.isEmpty(response)) {
			LogUtil.d(TAG, "Cities response:" + response);
			String[] cities = response.split(",");
			if (cities != null && cities.length > 0) {
				for (String c : cities) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceCode(provinceCode);
					//Save parse city data into table by call saveCities().
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String response, String cityCode) {
		if (!TextUtils.isEmpty(response)) {
			LogUtil.d(TAG, "Counties response:" + response);
			String[] counties = response.split(",");
			if (counties != null && counties.length > 0) {
				for (String c : counties) {
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityCode(cityCode);
					//Save parse county data into table by call saveCounty().
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
}
