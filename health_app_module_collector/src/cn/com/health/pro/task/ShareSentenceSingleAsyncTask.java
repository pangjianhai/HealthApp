package cn.com.health.pro.task;

import android.content.Context;
import android.os.AsyncTask;
import cn.com.health.pro.ShareSentenceAllDetailActivity;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.model.ShareSentenceEntity;
import cn.com.health.pro.util.CommonHttpUtil;
import cn.com.health.pro.util.ShareSentenceUtil;

/**
 * 获取健康详情接口
 */
@Deprecated
public class ShareSentenceSingleAsyncTask extends
		AsyncTask<String, Void, ShareSentenceEntity> {

	private Context context;

	public ShareSentenceSingleAsyncTask(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected ShareSentenceEntity doInBackground(String... params) {
		String share_sentence_id = params[0];
		String url = SystemConst.server_url
				+ SystemConst.FunctionUrl.getShareDetailById + "?para={key:'"
				+ share_sentence_id + "'}";
		String data = CommonHttpUtil.sendHttpRequest(url);
		return ShareSentenceUtil.parseJsonAddToEntity(data);
	}

	@Override
	protected void onPostExecute(ShareSentenceEntity result) {
		super.onPostExecute(result);
		((ShareSentenceAllDetailActivity) context).renderText(result);

	}

}
