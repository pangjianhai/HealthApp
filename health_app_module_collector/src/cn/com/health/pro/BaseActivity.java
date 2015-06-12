package cn.com.health.pro;

import java.util.Iterator;
import java.util.Map;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;
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

	public void send_normal_request(Map<String, String> p) {
		if (!isNetWorkConnected()) {
			Toast.makeText(getApplicationContext(), "没有网络咯！",
					Toast.LENGTH_SHORT).show();
			return;
		}
		RequestParams params = new RequestParams();
		Iterator<Map.Entry<String, String>> it = p.entrySet().iterator();
		/**
		 * 添加参数
		 */
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			params.addBodyParameter(entry.getKey(), entry.getValue());
		}
		HttpUtils http = new HttpUtils();
		String url = SystemConst.server_url
				+ SystemConst.FunctionUrl.update_person_location;
		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						onStart_for_request();
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						onLoading_for_request(total, current, isUploading);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						onSuccess_for_request(responseInfo);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						onFailure_for_request(error, msg);
					}
				});
	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月12日
	 * @todo:请求普通请求初始工作
	 * @return:void
	 */
	public void onStart_for_request() {

	}

	/**
	 * 
	 * @param total
	 * @param current
	 * @param isUploading
	 * @user:pang
	 * @data:2015年6月12日
	 * @todo:请求进行中的操作
	 * @return:void
	 */
	public void onLoading_for_request(long total, long current,
			boolean isUploading) {

	}

	/**
	 * 
	 * @param responseInfo
	 * @user:pang
	 * @data:2015年6月12日
	 * @todo:请求成功
	 * @return:void
	 */
	public void onSuccess_for_request(ResponseInfo<String> responseInfo) {

	}

	/**
	 * 
	 * @param error
	 * @param msg
	 * @user:pang
	 * @data:2015年6月12日
	 * @todo:请求失败
	 * @return:void
	 */
	public void onFailure_for_request(HttpException error, String msg) {

	}

}
