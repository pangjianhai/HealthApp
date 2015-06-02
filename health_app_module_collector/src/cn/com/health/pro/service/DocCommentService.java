package cn.com.health.pro.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.util.CommonHttpUtil;

/**
 * 
 * @author pang
 * @todo 对文档的评论
 *
 */
public class DocCommentService extends IntentService {

	public DocCommentService() {
		super("cn.com.health.fs.pro.groupname.for.comentdoc");
	}

	public DocCommentService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String userId = intent.getStringExtra("userId");
		String docId = intent.getStringExtra("docId");
		String content = intent.getStringExtra("content");
		try {
			JSONObject j = new JSONObject();
			j.put("userId", userId);
			j.put("docId", docId);
			j.put("content", content);
			String url = SystemConst.server_url
					+ SystemConst.FunctionUrl.comment_doc;
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
