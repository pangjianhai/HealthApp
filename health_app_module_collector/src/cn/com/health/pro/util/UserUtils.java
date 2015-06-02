package cn.com.health.pro.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.com.health.pro.model.SelfNum;
import cn.com.health.pro.model.UserItem;

public class UserUtils {

	public static List<UserItem> parseJsonAddToList(String data) {
		List<UserItem> dataSourceList = new ArrayList<UserItem>();
		try {
			if (data != null && !"".equals(data)) {
				JSONObject or_obj = new JSONObject(data);
				JSONArray jarray = or_obj.getJSONArray("users");
				if (jarray != null && jarray.length() > 0) {
					for (int i = 0; i < jarray.length(); i++) {
						JSONObject obj = jarray.getJSONObject(i);
						UserItem bean = new UserItem();
						// 通用字段的处理
						String id = obj.getString("id");
						String userName = obj.getString("userName");
						String userId = obj.getString("userId");
						String img = obj.getString("headImg");
						String tags = "需要添加";

						bean.setUuid(id);
						bean.setUserId(userId);
						bean.setUserName(userName);
						bean.setImg(img);
						bean.setTags(tags);
						dataSourceList.add(bean);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSourceList;
	}

	public static UserItem parseUserItemFromJSON(String data) {
		UserItem bean = new UserItem();
		try {
			if (data != null && !"".equals(data)) {
				JSONObject or_obj = new JSONObject(data);
				JSONObject obj = or_obj.getJSONObject("User");

				String id = obj.getString("id");
				String userName = obj.getString("userName");
				String userId = obj.getString("userId");
				String img = obj.getString("headImg");
				String email = obj.getString("email");
				String sex = obj.getString("sex");
				String birthday = obj.getString("birthday");
				String sentence = obj.getString("sentence");

				bean.setUuid(id);
				bean.setUserId(userId);
				bean.setUserName(userName);
				bean.setImg(img);
				bean.setEmail(email);
				bean.setSex(sex);
				bean.setBirthday(birthday);
				bean.setSentence(sentence);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

	public static SelfNum parsUserResult(String data) {
		SelfNum bean = new SelfNum();
		try {
			if (data != null && !"".equals(data)) {
				JSONObject obj = new JSONObject(data);

				String id = obj.getString("sentenceNum");
				String attentionUserNum = obj.getString("attentionUserNum");// 我关注的
				String userAttentionNum = obj.getString("userAttentionNum");// 关注我的

				bean.setShareNum(id);
				bean.setFocusMyNum(userAttentionNum);
				bean.setMyFocusNum(attentionUserNum);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

}
