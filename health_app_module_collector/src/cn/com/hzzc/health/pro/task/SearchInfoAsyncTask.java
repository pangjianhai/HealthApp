package cn.com.hzzc.health.pro.task;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import cn.com.hzzc.health.pro.MainPageLayoutInfoActivity;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.entity.InfoEntity;
import cn.com.hzzc.health.pro.util.CommonHttpUtil;
import cn.com.hzzc.health.pro.util.InfoUtil;

public class SearchInfoAsyncTask extends
		AsyncTask<String, Void, List<InfoEntity>> {

	private Context context;

	public SearchInfoAsyncTask(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((MainPageLayoutInfoActivity) context).beforeSearch();
	}

	@Override
	protected List<InfoEntity> doInBackground(String... params) {
		String para = params[0];
		String url = SystemConst.server_url
				+ SystemConst.FunctionUrl.getInfoByKey + "?para=" + para;
		String data = CommonHttpUtil.sendHttpRequest(url);
		return InfoUtil.parseInfo(data);
	}

	@Override
	protected void onPostExecute(List result) {
		super.onPostExecute(result);
		((MainPageLayoutInfoActivity) context).searchOver(result);

	}
}
