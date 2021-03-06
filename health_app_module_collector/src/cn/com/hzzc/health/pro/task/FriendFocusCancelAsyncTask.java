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
 * @todo 取消一个用户关注另一个用户task
 *
 */
@Deprecated
public class FriendFocusCancelAsyncTask extends
		AsyncTask<String, Void, Boolean> {

	private Context context;

	public FriendFocusCancelAsyncTask(Context context) {
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
				+ SystemConst.FunctionUrl.cancel_some_one_focus_another
				+ "?para=" + para;
		String data = CommonHttpUtil.sendHttpRequest(url);
		return FocusUtil.commonFocusResult(data);
	}

	@Override
	protected void onPostExecute(Boolean b) {
		super.onPostExecute(b);
		((ShowUserInfoDetail) context).cancelFocusOver(b);
	}
}
