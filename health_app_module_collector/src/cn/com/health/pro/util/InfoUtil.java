package cn.com.health.pro.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.com.health.pro.entity.InfoEntity;

/**
 * 
 * @author pang
 * @todo 信息解析工具类
 *
 */
public class InfoUtil {

	/**
	 * @tags @param data
	 * @tags @return
	 * @date 2015年5月18日
	 * @todo 解析信息
	 * @author pang
	 */
	public static List<InfoEntity> parseInfo(String data) {
		List<InfoEntity> infoList = new ArrayList<InfoEntity>();
		try {
			JSONObject or_obj = new JSONObject(data);
			JSONArray jarray = or_obj.getJSONArray("basedocument");
			if (jarray != null && jarray.length() > 0) {
				for (int i = 0; i < jarray.length(); i++) {
					JSONObject obj = jarray.getJSONObject(i);
					String id = obj.getString("id");
					String type = obj.getString("typeName");
					String title = obj.getString("title");

					InfoEntity ie = new InfoEntity();
					ie.setTitle(title);
					ie.setType(type);
					ie.setId(id);
					infoList.add(ie);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infoList;
	}

	/**
	 * 
	 * @param data
	 * @return
	 * @user:pang
	 * @data:2015年6月14日
	 * @todo:解析
	 * @return:List<InfoEntity>
	 */
	public static List<InfoEntity> parseInfoForType(String data) {
		List<InfoEntity> infoList = new ArrayList<InfoEntity>();
		try {
			JSONObject or_obj = new JSONObject(data);
			JSONArray jarray = or_obj.getJSONArray("baseDocumentlst");
			if (jarray != null && jarray.length() > 0) {
				for (int i = 0; i < jarray.length(); i++) {
					JSONObject obj = jarray.getJSONObject(i);
					String id = obj.getString("id");
					String type = obj.getString("typeName");
					String title = obj.getString("title");

					InfoEntity ie = new InfoEntity();
					ie.setTitle(title);
					ie.setType(type);
					ie.setId(id);
					infoList.add(ie);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infoList;
	}
}
