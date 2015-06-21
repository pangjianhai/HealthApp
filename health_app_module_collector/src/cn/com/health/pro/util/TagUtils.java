package cn.com.health.pro.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.com.health.pro.model.SelfNum;
import cn.com.health.pro.model.Tag;
import cn.com.health.pro.model.UserItem;

/**
 * @todo 标签工具
 * @author pang
 *
 */
public class TagUtils {

	public static List<Tag> parseJsonAddToList(String data) {
		List<Tag> dataSourceList = new ArrayList<Tag>();
		try {
			if (data != null && !"".equals(data)) {
				JSONObject or_obj = new JSONObject(data);
				JSONArray jarray = or_obj.getJSONArray("taglst");
				if (jarray != null && jarray.length() > 0) {
					for (int i = 0; i < jarray.length(); i++) {
						JSONObject obj = jarray.getJSONObject(i);
						Tag bean = new Tag();
						// 通用字段的处理
						String id = obj.getString("id");
						String tagName = obj.getString("tagName");

						bean.setId(id);
						bean.setDisplayName(tagName);
						dataSourceList.add(bean);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSourceList;
	}

}
