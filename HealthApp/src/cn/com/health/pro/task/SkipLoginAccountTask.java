package cn.com.health.pro.task;

import android.content.Context;
import android.os.AsyncTask;
import cn.com.health.pro.AppStartActivity;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.util.CommonHttpUtil;

/**
 * 
 * @author pang
 * @todo 自动登陆任务
 *
 */
public class SkipLoginAccountTask extends AsyncTask<String, Void, String> {

	private Context context;

	public SkipLoginAccountTask(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		String url = SystemConst.server_url + SystemConst.FunctionUrl.userLogin
				+ "?para=" + params[0];
		String data = CommonHttpUtil.sendHttpRequest(url);
		return data;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		((AppStartActivity) context).skipOver(result);
	}

}