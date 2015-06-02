package cn.com.health.pro.service;

import org.json.JSONObject;

import cn.com.health.pro.SystemConst;
import cn.com.health.pro.config.HealthApplication;
import cn.com.health.pro.util.CommonHttpUtil;
import android.app.IntentService;
import android.content.Intent;

/**
 * 
 * @author pang
 * @todo 对分享和信息库信息的点赞或差评
 *
 */
public class ViewForInfoService extends IntentService {

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

	public ViewForInfoService() {
		super("cn.com.health.fs.pro.groupname");
	}

	public ViewForInfoService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		String type = intent.getStringExtra("type");
		String id = intent.getStringExtra("id");
		String view = intent.getStringExtra("view");
		String userId = HealthApplication.getUserId();
		try {
			JSONObject j = new JSONObject();
			j.put("item_type", type);
			j.put("item_key", id);
			j.put("good_bad", view);
			j.put("userId", userId);
			String url = SystemConst.server_url
					+ SystemConst.FunctionUrl.view_for_share_or_healthinfo
					+ "?para=" + j.toString();
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
