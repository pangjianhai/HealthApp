package cn.com.health.pro.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 
 * @author pang
 * @todo http请求工具类
 *
 */
public class CommonHttpUtil {

	/**
	 * 
	 * @tags @param address
	 * @tags @param listener
	 * @date 2015年5月6日
	 * @todo 异步获取请求内容
	 * @author pang
	 */
	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("POST");
					connection.setRequestProperty("encoding", "UTF-8");
					connection.setReadTimeout(8000);
					connection.setConnectTimeout(8000);
					connection.setDoInput(true);
					connection.setDoOutput(true);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					if (listener != null) {
						listener.onFinish(sb.toString());
					}
				} catch (Exception e) {
					if (listener != null) {
						listener.onError(e);
					}
					e.printStackTrace();
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}

	/**
	 * 
	 * @tags @param address
	 * @tags @return
	 * @date 2015年5月6日
	 * @todo 同步获取请求内容
	 * @author pang
	 */
	public static String sendHttpRequest(String address) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(address);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("encoding", "UTF-8");
			connection.setReadTimeout(8000);
			connection.setConnectTimeout(8000);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			InputStream in = connection.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;

	}

	/**
	 * 
	 * @param address
	 * @param param
	 * @return
	 * @user:pang
	 * @data:2015年6月12日
	 * @todo:xUtils请求数据
	 * @return:String
	 */
	public static String sendHttpRequest(String address,
			Map<String, String> param) {

		RequestParams params = new RequestParams();
		Iterator<Map.Entry<String, String>> it = param.entrySet().iterator();
		/**
		 * 添加参数
		 */
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			params.addBodyParameter(entry.getKey(), entry.getValue());
		}
		HttpUtils http = new HttpUtils();
		try {
			ResponseStream rs = http.sendSync(HttpMethod.POST, address);
			InputStream in = rs.getBaseStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
