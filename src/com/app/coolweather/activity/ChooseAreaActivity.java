/**
 * 
 */
package com.app.coolweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.app.coolweather.R;
import com.app.coolweather.db.CoolWeatherDB;
import com.app.coolweather.model.City;
import com.app.coolweather.model.County;
import com.app.coolweather.model.Province;
import com.app.coolweather.receiver.HttpCallbackListener;
import com.app.coolweather.util.HttpUtil;
import com.app.coolweather.util.LogUtil;
import com.app.coolweather.util.Utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author weibin
 *
 */
public class ChooseAreaActivity extends BaseActivity {

	private static final String TAG = "ChooseAreaActivity";
	private static final int LEVEL_PROVINCE = 1;
	private static final int LEVEL_CITY = 2;
	private static final int LEVEL_COUNTY = 3;
	/**Current list level.*/
	private int currentLevel;

	private TextView tv_area_title;
	private ListView lv_area_data;
	private ArrayAdapter<String> lv_area_adapter;

	/**Province list.*/
	private List<Province> provinceList;
	/**City list.*/
	private List<City> cityList;
	/**County list.*/
	private List<County> countyList;
	/**Use for display info data list.*/
	private List<String> dataList = new ArrayList<String>();

	/**Selected province.*/
	private Province selectedProvince;
	/**Selected city.*/
	private City selectedCity;
	/**Selected county.*/
	private County selectedCounty;

	private CoolWeatherDB coolWeatherDB;
	private ProgressDialog progressDialog;

	public static void actionStart(Context context) {
		Intent intent = new Intent(context, ChooseAreaActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_area_list);
		tv_area_title = (TextView) findViewById(R.id.tv_area_title);
		lv_area_data = (ListView) findViewById(R.id.lv_area_data);
		lv_area_adapter = new ArrayAdapter<String>(ChooseAreaActivity.this,
				android.R.layout.simple_list_item_1, dataList);
		lv_area_data.setAdapter(lv_area_adapter);
		lv_area_data.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int index, long id) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(index);
					queryCitiesFromDB();
				}else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(index);
					queryCountiesFromDB();
				}else {
					//display weather info.
				}

			}
		});
		
		coolWeatherDB = CoolWeatherDB.getInstance();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		//judge network is or not useful.
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null || (!networkInfo.isAvailable())) {
			Toast.makeText(ChooseAreaActivity.this, "Network is unavailable in onResume().", Toast.LENGTH_SHORT).show();
		}
		
		queryProvincesFromDB();
	}
	
	/** query provinces information */
	private void queryProvincesFromDB() {
		provinceList = coolWeatherDB.loadProvinces();
		
		if (provinceList.size() > 0) {
			LogUtil.d(TAG, "query provinces from DB.");
			//clear old data set first.
			dataList.clear();
			//update data set.
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
			}
			//notify data set changed.
			lv_area_adapter.notifyDataSetChanged();
			//set per component priority.
			lv_area_data.setSelection(0);
			tv_area_title.setText(R.string.tv_area_title_origin);
			//update currentLevel value.
			currentLevel = LEVEL_PROVINCE;
		} else {
			LogUtil.d(TAG, "query provinces from server.");
			queryFormServer("provinces", null);
		}
	}

	/** query cities information */
	private void queryCitiesFromDB() {
		cityList = coolWeatherDB.loadCities(selectedProvince.getProvinceCode());
		
		if (cityList.size() > 0) {
			LogUtil.d(TAG, "query cities from DB.");
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			lv_area_adapter.notifyDataSetChanged();
			lv_area_data.setSelection(0);
			tv_area_title.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			LogUtil.d(TAG, "query cities from server, selected province code = " + selectedProvince.getProvinceCode());
			queryFormServer("cities", selectedProvince.getProvinceCode());
		}
	}
	
	/** query cities information */
	private void queryCountiesFromDB() {
		countyList = coolWeatherDB.loadCounties(selectedCity.getCityCode());
		
		if (countyList.size() > 0) {
			LogUtil.d(TAG, "query counties from DB.");
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}
			lv_area_adapter.notifyDataSetChanged();
			lv_area_data.setSelection(0);
			tv_area_title.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		} else {
			LogUtil.d(TAG, "query counties from server.");
			queryFormServer("counties", selectedCity.getCityCode());
		}
	}
	
	/** query areas information from server*/
	private void queryFormServer(final String type, String code) {
		String address;
		if (TextUtils.isEmpty(code)) {
			//set request provinces info interface address.
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}else {
			//set request cities or counties info interface address.
			address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		}
		
		//display progress dialog when user wait time, that loading areas info from server.
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener (){

			@Override
			public void onFinish(String response) {
				
				boolean result = false;
				if ("provinces".equals(type)) {
					result = Utility.handleProvincesResponse(coolWeatherDB, response);
				}else if ("cities".equals(type)) {
					result = Utility.handleCitiesResponse(coolWeatherDB, response, selectedProvince.getProvinceCode());
				}else if("counties".equals(type)){
					result = Utility.handleCountiesResponse(coolWeatherDB, response, selectedCity.getCityCode());
				}else {
					LogUtil.d(TAG, "Not match with type: " + type + " when handleHttpServerResponse.");
				}
				
				if (result) {
					//Through call Activity runOnUiThread() back to mainThread from subThred.Then process logic.
					runOnUiThread(new Runnable() {
						public void run() {
							//Close process dialog.
							closeProcessDialog();
							if ("provinces".equals(type)) {
								//Call queryProvinces() again.
								queryProvincesFromDB();
							}else if ("cities".equals(type)) {
								//Call queryCities() again.
								queryCitiesFromDB();
							}else if ("counties".equals(type)) {
								//Call queryCounties again.
								queryCountiesFromDB();
							}else {
								LogUtil.d(TAG, "Not match type:" + type + " when prepare to callback.");
							}
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				//Through call Activity runOnUiThread() back to mainThread from subThred.Then process logic.
				runOnUiThread(new Runnable() {
					public void run() {
						LogUtil.d(TAG, "Current thread id:" + Thread.currentThread().getId());
						//Close process dialog.
						closeProcessDialog();
						//Popup a toast notification.
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_LONG).show();
					}
				});
			}
			
		});
		
	}
	
	/**Display process dialog.*/
	private void showProgressDialog(){
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(ChooseAreaActivity.this);
			progressDialog.setMessage("正在加载……");
			//set progressDialog cann't canceled.
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**Close process dialog.*/
	private void closeProcessDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	
	/**
	 * Catch back key pressed event. 
	 * Then decided back to city's ListView or province's ListView or finish by currentLevel value.
	 * */
	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		if (currentLevel == LEVEL_COUNTY) {
			//Update title and ListView by call queryCities().
			queryCitiesFromDB();
		}else if(currentLevel == LEVEL_CITY){
			//Update title and ListView by call queryProvinces().
			queryProvincesFromDB();
		}else {
			finish();
		}
	}
}
