package cn.com.hzzc.health.pro.task;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import cn.com.hzzc.health.pro.MainPageLayoutMeFocusMeActivity;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.model.UserItem;
import cn.com.hzzc.health.pro.util.CommonHttpUtil;
import cn.com.hzzc.health.pro.util.UserUtils;

/**
 * 
 * @author pang
 * @todo 获取“关注我的”人员分页
 *
 */
public class FocusMeAsyncTask extends AsyncTask<String, Void, List<UserItem>> {

	private Context context;

	public FocusMeAsyncTask(Context context) {
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
				+ SystemConst.FunctionUrl.getFocusMeUser + "?para=" + para;
		String data = CommonHttpUtil.sendHttpRequest(url);
		return UserUtils.parseJsonAddToList(data);
	}

	@Override
	protected void onPostExecute(List result) {
		super.onPostExecute(result);
		((MainPageLayoutMeFocusMeActivity) context).searchOver(result);

	}
}
