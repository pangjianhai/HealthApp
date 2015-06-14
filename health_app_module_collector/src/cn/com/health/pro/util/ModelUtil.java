package cn.com.health.pro.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.com.health.pro.entity.InfoEntity;
import cn.com.health.pro.model.InfoTypeEntity;

/**
 * @TODO 模块工具
 * @author pang
 *
 */
public class ModelUtil {
	/**
	 * @tags @param data
	 * @tags @return
	 * @date 2015年5月18日
	 * @todo 解析信息
	 * @author pang
	 */
	public static List<InfoTypeEntity> parseInfo(String data) {
		List<InfoTypeEntity> infoList = new ArrayList<InfoTypeEntity>();
		try {
			JSONObject or_obj = new JSONObject(data);
			JSONArray jarray = or_obj.getJSONArray("moldlst");
			if (jarray != null && jarray.length() > 0) {
				for (int i = 0; i < jarray.length(); i++) {
					JSONObject obj = jarray.getJSONObject(i);
					String moldlId = obj.getString("moldlId");
					String moldName = obj.getString("moldName");
					String exsubscribe = obj.getString("exsubscribe");

					InfoTypeEntity ie = new InfoTypeEntity();
					ie.setId(moldlId);
					ie.setName(moldName);
					if ("true".equals(exsubscribe)) {
						ie.setIfFocus("Y");
					} else {
						ie.setIfFocus("N");
					}
					infoList.add(ie);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infoList;
	}
}
