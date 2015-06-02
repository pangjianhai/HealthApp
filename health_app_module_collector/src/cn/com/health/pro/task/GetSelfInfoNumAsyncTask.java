package cn.com.health.pro.task;

import android.content.Context;
import android.os.AsyncTask;
import cn.com.health.pro.MainPageLayoutMeActivity;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.model.SelfNum;
import cn.com.health.pro.util.CommonHttpUtil;
import cn.com.health.pro.util.UserUtils;

/**
 * 
 * @author pang
 * @todo 根据用户UUID取用户数据task
 *
 */
public class GetSelfInfoNumAsyncTask extends AsyncTask<String, Void, SelfNum> {

	private Context context;

	public GetSelfInfoNumAsyncTask(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected SelfNum doInBackground(String... params) {
		String para = params[0];
		String url = SystemConst.server_url
				+ SystemConst.FunctionUrl.getResultNumberUserById + "?para="
				+ para;
		String data = CommonHttpUtil.sendHttpRequest(url);
		return UserUtils.parsUserResult(data);
	}

	@Override
	protected void onPostExecute(SelfNum b) {
		super.onPostExecute(b);
		((MainPageLayoutMeActivity) context).getNum(b);
	}
}
