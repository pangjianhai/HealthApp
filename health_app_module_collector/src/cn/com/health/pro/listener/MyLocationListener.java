package cn.com.health.pro.listener;

import java.util.Iterator;
import java.util.Map;

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
		System.out.println("location--------------");
		if (location != null) {
			String time = location.getTime();
			double lat = location.getLatitude();
			double lon = location.getLongitude();
			String addr = location.getAddrStr();
			System.out.println(time + "--" + lat + "--" + lon + "--" + addr);
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
		http.send(HttpRequest.HttpMethod.POST, "uploadUrl....", params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						// testTextView.setText("conn...");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						// if (isUploading) {
						// testTextView.setText("upload: " + current + "/" +
						// total);
						// } else {
						// testTextView.setText("reply: " + current + "/" +
						// total);
						// }
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// testTextView.setText("reply: " +
						// responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// testTextView.setText(error.getExceptionCode() + ":" +
						// msg);
					}
				});
	}
}
