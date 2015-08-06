package cn.com.hzzc.health.pro.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.util.CommonHttpUtil;

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
					+ SystemConst.FunctionUrl.comment_share;
			Map<String, String> map = new HashMap<String, String>();
			map.put("para", j.toString());
			String data = CommonHttpUtil.sendHttpRequest(url, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
