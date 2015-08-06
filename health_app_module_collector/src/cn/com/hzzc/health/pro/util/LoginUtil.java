package cn.com.hzzc.health.pro.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @todo 登陆工具类
 * @author pang
 *
 */
public class LoginUtil {

	/**
	 * 
	 * @param data
	 * @return
	 * @user:pang
	 * @data:2015年7月9日
	 * @todo:解析找回密码http请求返回的结果
	 * @return:String
	 */
	public static String parseEmailResult(String data) {
		try {
			JSONObject or_obj = new JSONObject(data);
			return or_obj.getString("flag");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "error";
	}
}
