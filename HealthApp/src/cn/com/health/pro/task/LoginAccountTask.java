package cn.com.health.pro.task;

import android.content.Context;
import android.os.AsyncTask;
import cn.com.health.pro.AppLoginStartActivity;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.util.CommonHttpUtil;

/**
 * 
 * @author pang
 * @todo 登陆任务
 *
 */
public class LoginAccountTask extends AsyncTask<String, Void, String> {

	private Context context;

	public LoginAccountTask(Context context) {
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
		System.out.println("url:" + url);
		String data = CommonHttpUtil.sendHttpRequest(url);
		System.out.println("data:" + data);
		return data;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		((AppLoginStartActivity) context).loginOver(result);
	}

}
