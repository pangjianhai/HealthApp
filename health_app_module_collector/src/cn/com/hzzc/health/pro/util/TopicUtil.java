package cn.com.hzzc.health.pro.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.abstracts.ParentShareSentenceEntity;
import cn.com.hzzc.health.pro.model.ShareSentenceEntity;
import cn.com.hzzc.health.pro.model.TopicEntity;

/**
 * 
 * @author pang
 * @todo 健康主题工具类
 *
 */
public class TopicUtil {

	public static TopicEntity parseEntityByJSON(String data) {
		try {
			JSONObject obj = new JSONObject(data);
			String id = obj.getString("id");
			String name = obj.getString("name");
			String content = obj.getString("content");
			String createDate = obj.getString("createDate");
			String imgId = obj.getString("imgId");
			TopicEntity bean = new TopicEntity();
			bean.setId(id);
			bean.setDesc(content);
			bean.setName(name);
			bean.setImgId(imgId);
			bean.setCreateDate(createDate);
			return bean;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @param data
	 * @return
	 * @user:pang
	 * @data:2015年9月6日
	 * @todo:健康主题的工具类
	 * @return:List<TopicEntity>
	 */
	public static List<TopicEntity> parseJsonAddToList(String data) {
		List<TopicEntity> dataSourceList = new ArrayList<TopicEntity>();
		try {
			JSONObject or_obj = new JSONObject(data);
			JSONArray jarray = or_obj.getJSONArray("topiclst");
			if (jarray == null || jarray.length() == 0)
				return dataSourceList;
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject obj = jarray.getJSONObject(i);
				TopicEntity bean = new TopicEntity();
				// 通用字段的处理
				String id = obj.getString("id");
				String name = obj.getString("name");
				String content = obj.getString("content");
				if (content == null || "null".equals(content)) {
					content = "";
				}
				String createDate = obj.getString("createDate");
				String imgId = obj.getString("imgId");
				bean.setId(id);
				bean.setDesc(content);
				bean.setName(name);
				bean.setImgId(imgId);
				bean.setCreateDate(createDate);
				dataSourceList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSourceList;
	}

}
