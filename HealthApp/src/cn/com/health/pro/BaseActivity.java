package cn.com.health.pro;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import cn.com.health.pro.config.HealthApplication;
import cn.com.health.pro.persist.SharedPreInto;
import cn.com.health.pro.util.ActivityCollector;

/**
 * 
 * @author pang
 * @todo 所有activity的父类
 *
 */
public class BaseActivity extends Activity {

	/**
	 * 用户ID
	 */
	public String userId;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		/**
		 * 获取用户ID
		 */
		userId = HealthApplication.getUserId();
		/**
		 * activity放入收集器
		 */
		ActivityCollector.addActivity(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	/**
	 * 
	 * @tags @param contenxt
	 * @tags @return
	 * @date 2015年5月28日
	 * @todo 判断是否可以联网
	 * @author pang
	 */
	public boolean isNetWorkConnected() {
		ConnectivityManager cm = (ConnectivityManager) BaseActivity.this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isAvailable()) {
			return true;
		}
		return false;
	}

}
