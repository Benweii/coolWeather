/**
 * 
 */
package com.app.coolweather.receiver;

/**
 * @author weibin
 *
 */
public interface HttpCallbackListener {

	public void onFinish(String response);
	public void onError(Exception e);
	
}
