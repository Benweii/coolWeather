/**
 * 
 */
package com.app.coolweather.activity;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author weibin
 *
 */
public class BaseActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
}
