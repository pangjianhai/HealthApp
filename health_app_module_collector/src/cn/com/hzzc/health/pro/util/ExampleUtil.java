package cn.com.hzzc.health.pro.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author pang
 *
 */
public class ExampleUtil {
	public static final String PREFS_NAME = "JPUSH_EXAMPLE";
	public static final String PREFS_DAYS = "JPUSH_EXAMPLE_DAYS";
	public static final String PREFS_START_TIME = "PREFS_START_TIME";
	public static final String PREFS_END_TIME = "PREFS_END_TIME";
	public static final String KEY_APP_KEY = "JPUSH_APPKEY";

	public static boolean isEmpty(String s) {
		if (null == s)
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}

	/**
	 * 
	 * @param s
	 * @return
	 * @user:pang
	 * @data:2015年6月4日
	 * @todo:校验Tag Alias 只能是数字,英文字母和中文
	 * @return:boolean
	 */
	public static boolean isValidTagAndAlias(String s) {
		Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
		Matcher m = p.matcher(s);
		return m.matches();
	}

	/**
	 * 
	 * @param context
	 * @return
	 * @user:pang
	 * @data:2015年6月4日
	 * @todo:取得AppKey
	 * @return:String
	 */
	public static String getAppKey(Context context) {
		Bundle metaData = null;
		String appKey = null;
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai)
				metaData = ai.metaData;
			if (null != metaData) {
				appKey = metaData.getString(KEY_APP_KEY);
				if ((null == appKey) || appKey.length() != 24) {
					appKey = null;
				}
			}
		} catch (NameNotFoundException e) {

		}
		return appKey;
	}

	// 取得版本号
	public static String GetVersion(Context context) {
		try {
			PackageInfo manager = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return manager.versionName;
		} catch (NameNotFoundException e) {
			return "Unknown";
		}
	}

	public static void showToast(final String toast, final Context context) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}).start();
	}

	/**
	 * 
	 * @param context
	 * @return
	 * @user:pang
	 * @data:2015年6月4日
	 * @todo:是否联网
	 * @return:boolean
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	public static String getImei(Context context, String imei) {
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			imei = telephonyManager.getDeviceId();
		} catch (Exception e) {
			Log.e(ExampleUtil.class.getSimpleName(), e.getMessage());
		}
		return imei;
	}
}
