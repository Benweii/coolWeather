/**
 * 
 */
package com.app.coolweather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.app.coolweather.receiver.HttpCallbackListener;

import android.app.Activity;
import android.net.ConnectivityManager;

/**
 * @author weibin
 *
 */
public class HttpUtil {
	
	private static final String TAG = "HttpUtil";
	
	/**Send http request to server.*/
	public static void sendHttpRequest(final String address, final HttpCallbackListener httpCallbackListener){
		//Start a subThread process cost time logic
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				HttpURLConnection connection = null;
				
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8*1000);
					connection.setReadTimeout(8*1000);
					BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					StringBuilder response = new StringBuilder();
					String strLine = null;
					while ((strLine = br.readLine()) != null) {
						response.append(strLine);
					}
					
					if (httpCallbackListener != null) {
						LogUtil.d(TAG, "response content:" + response.toString());
						//Callback onFinish().
						httpCallbackListener.onFinish(response.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.e(TAG, e.getMessage() + " in sendHttpRequest().");
					if (httpCallbackListener != null) {
						//Callback onError().
						httpCallbackListener.onError(e);
					}
				} finally{
					if (connection != null) {
						connection.disconnect();
					}
				}
				
			}
		}).start();
		
	}

}
