package cn.com.health.pro.task;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import cn.com.health.pro.FriendSearchResultActivity;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.model.UserItem;
import cn.com.health.pro.util.CommonHttpUtil;
import cn.com.health.pro.util.UserUtils;

public class SearchUsersAsyncTask extends
		AsyncTask<String, Void, List<UserItem>> {

	private Context context;

	public SearchUsersAsyncTask(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected List<UserItem> doInBackground(String... params) {
		String para = params[0];
		String url = SystemConst.server_url
				+ SystemConst.FunctionUrl.searchUserByName + "?para=" + para;
		String data = CommonHttpUtil.sendHttpRequest(url);
		// System.out.println("=======data:" + data);
		return UserUtils.parseJsonAddToList(data);
	}

	@Override
	protected void onPostExecute(List result) {
		super.onPostExecute(result);
		((FriendSearchResultActivity) context).searchOver(result);

	}
}
