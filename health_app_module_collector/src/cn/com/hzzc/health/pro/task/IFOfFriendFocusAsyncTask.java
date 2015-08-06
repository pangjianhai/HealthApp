package cn.com.hzzc.health.pro.task;

import android.content.Context;
import android.os.AsyncTask;
import cn.com.hzzc.health.pro.ShowUserInfoDetail;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.util.CommonHttpUtil;
import cn.com.hzzc.health.pro.util.FocusUtil;

/**
 * 
 * @author pang
 * @todo 判断一个用户是否关注另外一个用户task
 *
 */
public class IFOfFriendFocusAsyncTask extends AsyncTask<String, Void, Boolean> {

	private Context context;

	public IFOfFriendFocusAsyncTask(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		String para = params[0];
		String url = SystemConst.server_url
				+ SystemConst.FunctionUrl.if_some_one_focus_another + "?para="
				+ para;
		String data = CommonHttpUtil.sendHttpRequest(url);
		return FocusUtil.commonFocusResult(data);
	}

	@Override
	protected void onPostExecute(Boolean b) {
		super.onPostExecute(b);
		/**
		 * 服务端返回true表示可以进行“关注”操作
		 * 
		 * 所以这里的！b是为了兼顾后台穿过来的意义以及activity中相应参数的意义
		 */
		((ShowUserInfoDetail) context).ifCanAddSomeone(!b);
	}
}
