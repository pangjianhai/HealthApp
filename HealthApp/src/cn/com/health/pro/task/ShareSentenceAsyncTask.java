package cn.com.health.pro.task;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import cn.com.health.pro.MineSpaceActivity;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.model.ShareSentenceEntity;
import cn.com.health.pro.util.CommonHttpUtil;
import cn.com.health.pro.util.ShareSentenceUtil;

public class ShareSentenceAsyncTask extends
		AsyncTask<String, Void, List<ShareSentenceEntity>> {

	private Context context;

	public ShareSentenceAsyncTask(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected List<ShareSentenceEntity> doInBackground(String... params) {
		String userId = params[0];
		String page = params[1];
		String url = SystemConst.server_url
				+ SystemConst.FunctionUrl.getShareByUserId + "?para={userId:'"
				+ userId + "',begin:" + page + ",limit:" + 10 + "}";
		String data = CommonHttpUtil.sendHttpRequest(url);
		return ShareSentenceUtil.parseJsonAddToList(data);
	}

	@Override
	protected void onPostExecute(List result) {
		super.onPostExecute(result);
		((MineSpaceActivity) context).asyncAddNewData(result);

	}

}
