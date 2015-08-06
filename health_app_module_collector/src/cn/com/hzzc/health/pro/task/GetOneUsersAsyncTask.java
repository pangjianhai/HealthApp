package cn.com.hzzc.health.pro.task;

import android.content.Context;
import android.os.AsyncTask;
import cn.com.hzzc.health.pro.ShowUserInfoDetail;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.model.UserItem;
import cn.com.hzzc.health.pro.util.CommonHttpUtil;
import cn.com.hzzc.health.pro.util.UserUtils;

/**
 * 
 * @author pang
 * @todo 根据用户UUID取用户数据task
 *
 */
public class GetOneUsersAsyncTask extends AsyncTask<String, Void, UserItem> {

	private Context context;

	public GetOneUsersAsyncTask(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected UserItem doInBackground(String... params) {
		String para = params[0];
		String url = SystemConst.server_url
				+ SystemConst.FunctionUrl.getUserById + "?para=" + para;
		String data = CommonHttpUtil.sendHttpRequest(url);
		return UserUtils.parseUserItemFromJSON(data);
	}

	@Override
	protected void onPostExecute(UserItem b) {
		super.onPostExecute(b);
		((ShowUserInfoDetail) context).getOver(b);
	}
}
