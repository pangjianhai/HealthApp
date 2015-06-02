package cn.com.health.pro.service;

import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.util.CommonHttpUtil;

/**
 * 
 * @author pang
 * @todo 对分享和信息库信息的收藏
 *
 */
public class CollectionForInfoService extends IntentService {

	/**
	 * 操作消息的类型
	 */
	public static final String VIEW_ITEM_TYPE_SHARE = "0";
	public static final String VIEW_ITEM_TYPE_INFO = "1";

	/**
	 * 意见倾向
	 */
	public static final String VIEW_VIEW_TYPE_OK = "0";
	public static final String VIEW_VIEW_TYPE_NO = "1";

	public CollectionForInfoService() {
		super("cn.com.health.fs.pro.groupname.collect");
	}

	public CollectionForInfoService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String userId = intent.getStringExtra("userId");
		String sentenceordocId = intent.getStringExtra("sentenceordocId");
		String type = intent.getStringExtra("type");
		try {
			JSONObject j = new JSONObject();
			j.put("userId", userId);
			j.put("sentenceordocId", sentenceordocId);
			j.put("type", type);
			String url = SystemConst.server_url
					+ SystemConst.FunctionUrl.collect_info_or_sentence
					+ "?para=" + j.toString();
			System.out.println("url:" + url);
			String data = CommonHttpUtil.sendHttpRequest(url);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
