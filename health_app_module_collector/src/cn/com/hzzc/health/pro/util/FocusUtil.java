package cn.com.hzzc.health.pro.util;

import org.json.JSONObject;

/**
 * 
 * @author pang
 * @todo 好友关注工具类
 *
 */
public class FocusUtil {

	/**
	 * 
	 * @tags @param data
	 * @tags @return
	 * @date 2015年5月24日
	 * @todo 解析关注之后的返回结果，是否关注成功
	 * @author pang
	 */
	public static Boolean commonFocusResult(String data) {
		try {
			JSONObject or_obj = new JSONObject(data);
			String result = or_obj.getString("flag");
			if ("success".equals(result)) {
				return new Boolean(true);
			}
			return new Boolean(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Boolean(false);
	}
}
