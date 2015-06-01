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
public class ShareCommentService extends IntentService {

	public ShareCommentService() {
		super("cn.com.health.fs.pro.groupname.for.comentshare");
	}

	public ShareCommentService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String userId = intent.getStringExtra("userId");
		String sentenceId = intent.getStringExtra("sentenceId");
		String content = intent.getStringExtra("content");
		try {
			JSONObject j = new JSONObject();
			j.put("userId", userId);
			j.put("sentenceId", sentenceId);
			j.put("content", content);
			String url = SystemConst.server_url
					+ SystemConst.FunctionUrl.comment_share + "?para="
					+ j.toString();
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
