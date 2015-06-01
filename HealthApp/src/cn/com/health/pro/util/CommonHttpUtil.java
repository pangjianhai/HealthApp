package cn.com.health.pro.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;

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

	public static String sendHttpRequest(String address,
			Map<String, String> param) {

		AjaxParams params = new AjaxParams();
		if (param != null && !param.isEmpty()) {
			Iterator<String> keys = param.keySet().iterator();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String value = param.get(key);
				params.put(key, value);
			}
		}

		FinalHttp fh = new FinalHttp();
		Object value = fh.postSync(address, params);
		return value != null ? value.toString() : "";

	}
}
