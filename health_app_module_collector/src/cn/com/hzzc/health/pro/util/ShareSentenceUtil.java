package cn.com.hzzc.health.pro.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.model.ShareInOrderEntity;
import cn.com.hzzc.health.pro.model.ShareSentenceEntity;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

/**
 * 
 * @author pang
 * @todo 健康分享工具类
 *
 */
public class ShareSentenceUtil {

	public static ShareSentenceEntity parseJsonAddToEntity(String data) {
		ShareSentenceEntity bean = new ShareSentenceEntity();
		try {
			// 通用字段处理
			JSONObject obj = new JSONObject(data);
			String type = obj.getString("type");
			String content = obj.getString("content");
			String userId = obj.getString("userId");
			String userName = obj.getString("userName");
			String lookNum = obj.getString("lookNum");
			String likeNum = obj.getString("likeNum");
			String disLikeNum = obj.getString("disLikeNum");
			String commentNum = obj.has("commentNum") ? obj
					.getString("commentNum") : "0";
			String createDate = obj.getString("createDate");
			String tags = obj.has("tags") ? obj.getString("tags") : "";
			bean.setType(type);
			bean.setContent(content);
			bean.setUserId(userId);
			bean.setAuthor(userName);
			bean.setReadNum(lookNum);
			bean.setGoodNum(likeNum);
			bean.setBadNum(disLikeNum);
			bean.setCommentNum(commentNum);
			bean.setTags(tags);
			// 特殊字段处理
			if (SystemConst.ShareInfoType.SHARE_TYPE_FOOD.equals(type)) {
				String material = obj.getString("material");
				String function = obj.getString("function");
				bean.setMaterial(material);
				bean.setFunction(function);
			}
			// 图片字段处理
			// 图片的处理
			List<String> imgs = new ArrayList<String>();
			String img0 = obj.has("Img0") ? obj.getString("Img0") : "";
			String img1 = obj.has("Img1") ? obj.getString("Img1") : "";
			String img2 = obj.has("Img2") ? obj.getString("Img2") : "";
			String img3 = obj.has("Img3") ? obj.getString("Img3") : "";
			String img4 = obj.has("Img4") ? obj.getString("Img4") : "";
			String img5 = obj.has("Img5") ? obj.getString("Img5") : "";
			String img6 = obj.has("Img6") ? obj.getString("Img6") : "";
			String img7 = obj.has("Img7") ? obj.getString("Img7") : "";

			bean.setImg0(img0);
			bean.setImg1(img1);
			bean.setImg2(img2);
			bean.setImg3(img3);
			bean.setImg4(img4);
			bean.setImg5(img5);
			bean.setImg6(img6);
			bean.setImg7(img7);
			imgs.add(img0);
			imgs.add(img1);
			imgs.add(img2);
			imgs.add(img3);
			imgs.add(img4);
			imgs.add(img5);
			imgs.add(img6);
			imgs.add(img7);
			bean.setImgsIds(imgs);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * 
	 * @tags @param data
	 * @tags @return
	 * @date 2015年5月8日
	 * @todo 解析json字符串
	 * @author pang
	 */
	public static List<ShareSentenceEntity> parseJsonAddToList(String data) {
		List<ShareSentenceEntity> dataSourceList = new ArrayList<ShareSentenceEntity>();
		try {
			JSONObject or_obj = new JSONObject(data);
			JSONArray jarray = or_obj.getJSONArray("SentenceInfoHashLst");
			if (jarray == null || jarray.length() == 0)
				return dataSourceList;
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject obj = jarray.getJSONObject(i);
				ShareSentenceEntity bean = new ShareSentenceEntity();
				// 通用字段的处理
				String type = obj.getString("type");
				String id = obj.getString("id");
				String content = obj.getString("content");
				String userId = obj.getString("userId");
				String userName = obj.getString("userName");
				String lookNum = obj.getString("lookNum");
				String likeNum = obj.getString("likeNum");
				String disLikeNum = obj.getString("disLikeNum");
				String createDate = obj.getString("createDateStr");
				String commentNum = obj.getString("commentNum");
				String tags = obj.has("tags") ? obj.getString("tags") : "";
				bean.setId(id);
				bean.setType(type);
				bean.setContent(content);
				bean.setUserId(userId);
				bean.setAuthor(userName);
				bean.setReadNum(lookNum);
				bean.setGoodNum(likeNum);
				bean.setBadNum(disLikeNum);
				bean.setcDate(createDate);
				bean.setCommentNum(commentNum);
				bean.setTags(tags);
				// 图片的处理
				List<String> imgs = new ArrayList<String>();
				String img0 = obj.getString("img0");
				String img1 = obj.getString("img1");
				String img2 = obj.getString("img2");
				String img3 = obj.getString("img3");
				String img4 = obj.getString("img4");
				String img5 = obj.getString("img5");
				String img6 = obj.getString("img6");
				String img7 = obj.getString("img7");
				if (img0 != null && !"".equals(img0) && !"null".equals(img0)) {
					imgs.add(img0);
				}
				if (img1 != null && !"".equals(img1) && !"null".equals(img1)) {
					imgs.add(img1);
				}
				if (img2 != null && !"".equals(img2) && !"null".equals(img2)) {
					imgs.add(img2);
				}
				if (img3 != null && !"".equals(img3) && !"null".equals(img3)) {
					imgs.add(img3);
				}
				if (img4 != null && !"".equals(img4) && !"null".equals(img4)) {
					imgs.add(img4);
				}
				if (img5 != null && !"".equals(img5) && !"null".equals(img5)) {
					imgs.add(img5);
				}
				if (img6 != null && !"".equals(img6) && !"null".equals(img6)) {
					imgs.add(img6);
				}
				if (img7 != null && !"".equals(img7) && !"null".equals(img7)) {
					imgs.add(img7);
				}

				bean.setImg0(img0);
				bean.setImg1(img1);
				bean.setImg2(img2);
				bean.setImg3(img3);
				bean.setImg4(img4);
				bean.setImg5(img5);
				bean.setImg6(img6);
				bean.setImg7(img7);
				bean.setImgsIds(imgs);

				/**
				 * 特殊字段的处理
				 */
				if (SystemConst.ShareInfoType.SHARE_TYPE_FOOD.equals(type)) {
					String material = obj.getString("material");
					String function = obj.getString("function");
					bean.setMaterial(material);
					bean.setFunction(function);
				}

				dataSourceList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSourceList;
	}

	/**
	 * 
	 * @tags @param data
	 * @tags @return
	 * @date 2015年5月21日
	 * @todo 个人朋友圈健康信息
	 * @author pang
	 */
	public static Map<String, Object> parseJsonCondition(String data) {
		Map<String, Object> map = new HashMap<String, Object>();
		/**
		 * 先放空值
		 */
		map.put("searchDay", "");
		map.put("begin", "");
		map.put("lst", new ArrayList<ShareSentenceEntity>());
		map.put("nomore", "nomore");
		try {
			if (data != null && !"".equals(data)) {
				JSONObject or_obj = new JSONObject(data);
				if (or_obj.has("nomore")) {// 是否有更多的信息
					map.put("nomore", "nomore");
				} else {
					map.put("nomore", "");
				}
				if (or_obj.has("searchDay")) {// 下一次搜索的日期
					map.put("searchDay", or_obj.get("searchDay") + "");
				}
				if (or_obj.has("begin")) {// 下一次搜索的开始行
					map.put("begin", or_obj.get("begin") + "");
				}
				if (or_obj.has("SentenceInfoHashLst")) {
					List<ShareSentenceEntity> lst = parseJsonAddToList(data);// 解析上一次搜索出来的结果
					if (lst != null && lst.size() > 0) {
						map.put("lst", lst);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> parseJsonConditionForNoLogin(String data) {
		Map<String, Object> map = new HashMap<String, Object>();
		/**
		 * 先放空值
		 */
		map.put("searchDay", "");
		map.put("lst", new ArrayList<ShareSentenceEntity>());
		map.put("nomore", "nomore");
		try {
			if (data != null && !"".equals(data)) {
				JSONObject or_obj = new JSONObject(data);
				if (or_obj.has("nomore")) {// 是否有更多的信息
					map.put("nomore", "nomore");
				} else {
					map.put("nomore", "");
				}
				if (or_obj.has("searchDay")) {// 下一次搜索的日期
					map.put("searchDay", or_obj.get("searchDay") + "");
				}
				if (or_obj.has("begin")) {// 下一次搜索的开始行
					map.put("begin", or_obj.get("begin") + "");
				}
				if (or_obj.has("SentenceInfoHashLst")) {
					List<ShareSentenceEntity> lst = parseJsonAddToList(data);// 解析上一次搜索出来的结果
					if (lst != null && lst.size() > 0) {
						map.put("lst", lst);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static void testRequest(List<ShareSentenceEntity> dataSourceList) {

		for (int i = 0; i < 8; i++) {
			ShareSentenceEntity e = new ShareSentenceEntity();
			e.setId(java.util.UUID.randomUUID().toString());
			e.setAuthor("逄建海" + i);
			e.setContent("jjjjjjjjjjjjjjjjjjj\nkkkkkkkkkkkk\nkkkkkkkkkkkkkkkk"
					+ i);
			dataSourceList.add(e);
		}

	}

	/**
	 * 
	 * @param lst
	 * @param top_date
	 * @return
	 * @user:pang
	 * @data:2015年8月13日
	 * @todo:将服务器端的对象转换成本地数据库存储对象
	 * @return:List<ShareInOrderEntity>
	 */
	public static List<ShareInOrderEntity> convertServerShareToLocalTopShare(
			List<ShareSentenceEntity> lst, String top_date) {
		if (lst != null && !lst.isEmpty()) {
			List<ShareInOrderEntity> l = new ArrayList<ShareInOrderEntity>();
			for (ShareSentenceEntity remote : lst) {
				ShareInOrderEntity localEntity = new ShareInOrderEntity();
				/**
				 * 本地字段
				 */
				localEntity.setLocalCacheId(UUID.randomUUID().toString());
				localEntity.setLocalCacheBelongTopDate(top_date);
				/**
				 * 原生字段
				 */
				localEntity.setId(remote.getId());
				localEntity.setType(remote.getType());
				localEntity.setUserId(remote.getUserId());
				localEntity.setAuthor(remote.getAuthor());
				localEntity.setContent(remote.getContent());
				localEntity.setReadNum(remote.getReadNum());
				localEntity.setGoodNum(remote.getGoodNum());
				localEntity.setBadNum(remote.getBadNum());
				localEntity.setCommentNum(remote.getCommentNum());
				localEntity.setMaterial(remote.getMaterial());
				localEntity.setFunction(remote.getFunction());
				localEntity.setImg0(remote.getImg0());
				localEntity.setImg1(remote.getImg1());
				localEntity.setImg2(remote.getImg2());
				localEntity.setImg3(remote.getImg3());
				localEntity.setImg4(remote.getImg4());
				localEntity.setImg5(remote.getImg5());
				localEntity.setImg6(remote.getImg6());
				localEntity.setImg7(remote.getImg7());
				String cDate = remote.getcDate();
				localEntity.setcDate(cDate);
				if (cDate != null && !"".equals(cDate)) {
					localEntity.setCreateDate(CommonDateUtil.getDate(cDate));
				}
				l.add(localEntity);
			}
			return l;
		}
		return null;
	}

	/**
	 * 
	 * @param lst
	 * @return
	 * @user:pang
	 * @data:2015年8月13日
	 * @todo:将本地数据库存储的排行榜数据转换成http中间值
	 * @return:List<ShareSentenceEntity>
	 */
	public static List<ShareSentenceEntity> convertLocalTopShareToServerShare(
			List<ShareInOrderEntity> lst) {
		if (lst != null && !lst.isEmpty()) {
			List<ShareSentenceEntity> l = new ArrayList<ShareSentenceEntity>();
			for (ShareInOrderEntity localdb : lst) {
				ShareSentenceEntity serverEntity = new ShareSentenceEntity();
				serverEntity.setId(localdb.getId());
				serverEntity.setType(localdb.getType());
				serverEntity.setUserId(localdb.getUserId());
				serverEntity.setAuthor(localdb.getAuthor());
				serverEntity.setContent(localdb.getContent());
				serverEntity.setReadNum(localdb.getReadNum());
				serverEntity.setGoodNum(localdb.getGoodNum());
				serverEntity.setBadNum(localdb.getBadNum());
				serverEntity.setCommentNum(localdb.getCommentNum());
				serverEntity.setMaterial(localdb.getMaterial());
				serverEntity.setFunction(localdb.getFunction());
				String img0 = localdb.getImg0();
				String img1 = localdb.getImg1();
				String img2 = localdb.getImg2();
				String img3 = localdb.getImg3();
				String img4 = localdb.getImg4();
				String img5 = localdb.getImg5();
				String img6 = localdb.getImg6();
				String img7 = localdb.getImg7();
				serverEntity.setImg0(img0);
				serverEntity.setImg1(img1);
				serverEntity.setImg2(img2);
				serverEntity.setImg3(img3);
				serverEntity.setImg4(img4);
				serverEntity.setImg5(img5);
				serverEntity.setImg6(img6);
				serverEntity.setImg7(img7);
				serverEntity.setcDate(localdb.getcDate());

				List<String> imgs = new ArrayList<String>();
				if (img0 != null && !"".equals(img0) && !"null".equals(img0)) {
					imgs.add(img0);
				}
				if (img1 != null && !"".equals(img1) && !"null".equals(img1)) {
					imgs.add(img1);
				}
				if (img2 != null && !"".equals(img2) && !"null".equals(img2)) {
					imgs.add(img2);
				}
				if (img3 != null && !"".equals(img3) && !"null".equals(img3)) {
					imgs.add(img3);
				}
				if (img4 != null && !"".equals(img4) && !"null".equals(img4)) {
					imgs.add(img4);
				}
				if (img5 != null && !"".equals(img5) && !"null".equals(img5)) {
					imgs.add(img5);
				}
				if (img6 != null && !"".equals(img6) && !"null".equals(img6)) {
					imgs.add(img6);
				}
				if (img7 != null && !"".equals(img7) && !"null".equals(img7)) {
					imgs.add(img7);
				}
				serverEntity.setImgsIds(imgs);
				l.add(serverEntity);
			}
			return l;
		}
		return null;
	}

	/**
	 * 
	 * @param topDate
	 * @param db
	 * @param sioeList
	 * @user:pang
	 * @data:2015年8月13日
	 * @todo:将服务端排行榜数据缓存到本地
	 * @return:void
	 */
	public static void cacheShareTopToDB(String topDate, DbUtils db,
			List<ShareInOrderEntity> sioeList) {
		Date start = CommonDateUtil.getDate(topDate);
		Date end = new Date();
		int num = CommonDateUtil.between(start, end);
		/*** 如果距离当前时间相差五天以上就不予缓存 **/
		if (num <= 5) {
			try {
				System.out
						.println("*************cacheShareTopToDB************");
				long total = db.count(Selector.from(ShareInOrderEntity.class)
						.where("localCacheBelongTopDate", "=", topDate));
				/*** 如果某一日的排行榜没有被存储过则进行存储，否则不需要进行存储 ***/
				if (total <= 0) {
					db.saveAll(sioeList);
				}
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param topDate
	 * @param db
	 * @return
	 * @user:pang
	 * @data:2015年8月13日
	 * @todo:将缓存到本地的排行榜根据日期取出来
	 * @return:List<ShareInOrderEntity>
	 */
	public static List<ShareInOrderEntity> getDbTopShareByDate(String topDate,
			DbUtils db) {
		List<ShareInOrderEntity> lst = null;
		Date start = CommonDateUtil.getDate(topDate);
		Date end = new Date();
		int num = CommonDateUtil.between(start, end);
		/*** 如果距离当前时间相差五天以上就从服务器端获取 **/
		if (num <= 5) {
			try {
				System.out
						.println("*************getDbTopShareByDate************");
				lst = db.findAll(Selector.from(ShareInOrderEntity.class).where(
						"localCacheBelongTopDate", "=", topDate));
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
		return lst;
	}
}
