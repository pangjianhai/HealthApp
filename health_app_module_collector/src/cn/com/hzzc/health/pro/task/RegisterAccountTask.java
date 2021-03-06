package cn.com.hzzc.health.pro.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import cn.com.hzzc.health.pro.AppEmailRegActivity;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.util.CommonHttpUtil;

/**
 * 
 * @author pang
 * @todo 注册task
 *
 */
@Deprecated
public class RegisterAccountTask extends AsyncTask<String, Void, String> {

	private Context context;
	private ProgressDialog pdialog;

	public RegisterAccountTask(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pdialog = ProgressDialog.show(context, "正在分享...", "系统正在处理您的请求");
	}

	@Override
	protected String doInBackground(String... params) {
		String url = SystemConst.server_url + SystemConst.FunctionUrl.addUser
				+ "?para=" + params[0];
		String data = CommonHttpUtil.sendHttpRequest(url);
		return data;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		pdialog.dismiss();
		((AppEmailRegActivity) context).regOver(result);
	}

}
