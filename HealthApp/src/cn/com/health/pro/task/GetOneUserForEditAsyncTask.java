package cn.com.health.pro.task;

import android.content.Context;
import android.os.AsyncTask;
import cn.com.health.pro.EditUserInfoDetail;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.model.UserItem;
import cn.com.health.pro.util.CommonHttpUtil;
import cn.com.health.pro.util.UserUtils;

/**
 * 
 * @author pang
 * @todo 根据用户UUID取用户数据task
 *
 */
public class GetOneUserForEditAsyncTask extends
		AsyncTask<String, Void, UserItem> {

	private Context context;

	public GetOneUserForEditAsyncTask(Context context) {
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
		((EditUserInfoDetail) context).getOver(b);
	}
}
