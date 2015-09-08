package cn.com.hzzc.health.pro.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.hzzc.health.pro.model.TopicEntity;
import cn.com.hzzc.health.pro.model.TopicPostEntity;

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

	public static boolean parseFlag(String data) {
		try {
			JSONObject obj = new JSONObject(data);
			return obj.getBoolean("flag");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static List<TopicPostEntity> parsePostsFromJson(String data) {
		List<TopicPostEntity> dataSourceList = new ArrayList<TopicPostEntity>();
		try {
			JSONObject or_obj = new JSONObject(data);
			JSONArray jarray = or_obj.getJSONArray("topicPostlst");
			if (jarray == null || jarray.length() == 0)
				return dataSourceList;
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject obj = jarray.getJSONObject(i);
				TopicPostEntity bean = new TopicPostEntity();
				// 通用字段的处理
				String id = obj.getString("id");
				String comment = obj.getString("comment");
				String userId = obj.getString("userId");
				String userName = obj.getString("userName");
				String img0 = obj.getString("img0");
				String img1 = obj.getString("img1");
				String img2 = obj.getString("img2");
				String img3 = obj.getString("img3");
				String createDate = obj.getString("createDate");
				List imgs = new ArrayList<String>();
				if (img0 != null && !"".equals(img0)) {
					imgs.add(img0);
				} else if (img1 != null && !"".equals(img1)) {
					imgs.add(img1);
				} else if (img2 != null && !"".equals(img2)) {
					imgs.add(img2);
				} else if (img3 != null && !"".equals(img3)) {
					imgs.add(img3);
				}

				bean.setId(id);
				bean.setPostDate(createDate);
				bean.setUserId(userId);
				bean.setUserName(userName);
				bean.setImg0(img0);
				bean.setImg1(img1);
				bean.setImg2(img2);
				bean.setImg3(img3);
				bean.setShortMsg(comment);
				bean.setImgs(imgs);
				dataSourceList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSourceList;
	}

}
