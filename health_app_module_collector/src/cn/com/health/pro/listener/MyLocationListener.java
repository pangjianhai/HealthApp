package cn.com.health.pro.listener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import cn.com.health.pro.SystemConst;
import cn.com.health.pro.config.HealthApplication;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * @todo 地理位置变化监听器
 * @author pang
 *
 */
public class MyLocationListener implements BDLocationListener {

	@Override
	public void onReceiveLocation(BDLocation location) {
		System.out.println("location--------------" + location);
		if (location != null) {
			String time = location.getTime();
			double lat = location.getLatitude();
			double lon = location.getLongitude();
			String addr = location.getAddrStr();
			try {
				JSONObject obj = new JSONObject();
				obj.put("userId", HealthApplication.getUserId());
				obj.put("latitude", lat + "");
				obj.put("longitude", lon + "");
				obj.put("curraddress", addr);

				Map<String, String> m = new HashMap<String, String>();
				m.put("para", obj.toString());
				send(m);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void send(Map<String, String> p) {
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
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println("==================>");
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("==================<");
						;
					}
				});
	}
}
