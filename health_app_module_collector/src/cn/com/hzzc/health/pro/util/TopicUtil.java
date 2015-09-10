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
			String d = or_obj.getString("topiclst");
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
				String img4 = obj.getString("img4");
				String img5 = obj.getString("img5");
				String img6 = obj.getString("img6");
				String img7 = obj.getString("img7");
				String createDate = obj.getString("createDate");
				String goodNum = obj.getString("goodNum");
				boolean flag = obj.getBoolean("goodNumFlag");
				if (goodNum == null || "".equals(goodNum)
						|| "null".equals(goodNum)) {
					goodNum = "0";
				}
				List<String> imgs = new ArrayList<String>();
				if (isImgId(img0)) {
					imgs.add(img0);
				}
				if (isImgId(img1)) {
					imgs.add(img1);
				}
				if (isImgId(img2)) {
					imgs.add(img2);
				}
				if (isImgId(img3)) {
					imgs.add(img3);
				}
				if (isImgId(img4)) {
					imgs.add(img4);
				}
				if (isImgId(img5)) {
					imgs.add(img5);
				}
				if (isImgId(img6)) {
					imgs.add(img6);
				}
				if (isImgId(img7)) {
					imgs.add(img7);
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
				bean.setGoodNum(Integer.parseInt(goodNum));
				if (flag) {
					bean.setIsGood(TopicPostEntity.GOOD_ALREADY);
				} else {
					bean.setIsGood(TopicPostEntity.GOOD_NO);
				}
				dataSourceList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSourceList;
	}

	/**
	 * @param img
	 * @return
	 * @user:pang
	 * @data:2015年9月10日
	 * @todo:判断解析出来的图片ID是否是空还是真正的ID
	 * @return:boolean
	 */
	public static boolean isImgId(String img) {
		return (img != null && !"".equals(img) && !"null".equals(img));
	}

}
