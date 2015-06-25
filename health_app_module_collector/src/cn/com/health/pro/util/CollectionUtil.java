package cn.com.health.pro.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.com.health.pro.model.CollectionItem;

/**
 * 
 * @author pang
 * @todo 好友关注工具类
 *
 */
public class CollectionUtil {

	/**
	 * 
	 * @param data
	 * @return
	 * @user:pang
	 * @data:2015年6月25日
	 * @todo:解析收藏的信息
	 * @return:List<CollectionItem>
	 */
	public static List<CollectionItem> parseInfo(String data) {
		List<CollectionItem> ciList = new ArrayList<CollectionItem>();
		try {
			JSONObject or_obj = new JSONObject(data);
			JSONArray jarray = or_obj.getJSONArray("collects");
			if (jarray != null && jarray.length() > 0) {
				for (int i = 0; i < jarray.length(); i++) {
					JSONObject obj = jarray.getJSONObject(i);
					String type = obj.getString("type");
					String userId = obj.getString("userId");
					String title = obj.getString("title");
					String sentenceordocId = obj.getString("sentenceordocId");

					CollectionItem ci = new CollectionItem();
					ci.setId(sentenceordocId);
					ci.setTitle(title);
					ci.setType(type);
					ciList.add(ci);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ciList;
	}
}
