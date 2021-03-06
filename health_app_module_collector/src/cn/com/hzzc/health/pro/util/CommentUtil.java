package cn.com.hzzc.health.pro.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.com.hzzc.health.pro.model.CommentEntity;

/**
 * 
 * @author pang
 * @todo 评论工具类
 *
 */
public class CommentUtil {

	/**
	 * 
	 * @param data
	 * @return
	 * @user:pang
	 * @data:2015年6月25日
	 * @todo:解析评论的信息
	 * @return:List<CollectionItem>
	 */
	public static List<CommentEntity> parseInfo(String data) {
		List<CommentEntity> ciList = new ArrayList<CommentEntity>();
		try {
			JSONObject or_obj = new JSONObject(data);
			JSONArray jarray = or_obj.getJSONArray("CommentEntitylst");
			if (jarray != null && jarray.length() > 0) {
				for (int i = 0; i < jarray.length(); i++) {
					JSONObject obj = jarray.getJSONObject(i);
					String userId = obj.getString("userId");
					String content = obj.getString("content");
					String userName = obj.getString("userName");
					// System.out.println("userName-----"+userName);
					String currentDate = obj.getString("currentDate");
					Date cDate = CommonDateUtil.getTime(currentDate);

					String atUserId = obj.has("repyUserId") ? obj
							.getString("repyUserId") : "";
					String atUserName = obj.has("repyUserName") ? obj
							.getString("repyUserName") : "";

					CommentEntity ci = new CommentEntity();
					ci.setUserId(userId);
					ci.setContent(content);
					ci.setUserName(userName);
					ci.setCommentDate(cDate);
					ci.setAtUserId(atUserId);
					ci.setAtUserName(atUserName);
					ciList.add(ci);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ciList;
	}
}
