package cn.com.hzzc.health.pro.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import cn.com.hzzc.health.pro.FriendSearchResultActivity;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.model.UserItem;
import cn.com.hzzc.health.pro.util.CommonHttpUtil;
import cn.com.hzzc.health.pro.util.UserUtils;

/**
 * @todo 根据关键词搜索用户task
 * @author pang
 *
 */
@Deprecated
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
				+ SystemConst.FunctionUrl.searchUserByName;
		Map<String, String> map = new HashMap<String, String>();
		map.put("para", para);
		String data = CommonHttpUtil.sendHttpRequest(url, map);
		return UserUtils.parseJsonAddToList(data);
	}

	@Override
	protected void onPostExecute(List result) {
		super.onPostExecute(result);
		((FriendSearchResultActivity) context).searchOver(result);

	}
}
