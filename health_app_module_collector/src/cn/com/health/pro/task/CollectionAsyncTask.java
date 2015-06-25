package cn.com.health.pro.task;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import cn.com.health.pro.MainPageLayoutMeCollectionActivity;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.model.CollectionItem;
import cn.com.health.pro.util.CommonHttpUtil;

/**
 * 
 * @author pang
 * @todo 获取“我的收藏”人员分页
 *
 */
@Deprecated
public class CollectionAsyncTask extends
		AsyncTask<String, Void, List<CollectionItem>> {

	private Context context;

	public CollectionAsyncTask(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected List<CollectionItem> doInBackground(String... params) {
		String para = params[0];
		String url = SystemConst.server_url
				+ SystemConst.FunctionUrl.getFocusMeUser + "?para=" + para;
		// String data = CommonHttpUtil.sendHttpRequest(url);
		// return UserUtils.parseJsonAddToList(data);
		List<CollectionItem> lst = new ArrayList<CollectionItem>();
		for (int i = 0; i < 11; i++) {
			CollectionItem c1 = new CollectionItem();
			c1.setId("1");
			c1.setTitle("xxxxxxxxxxxxxxxx" + i);
			lst.add(c1);
		}
		return lst;
	}

	@Override
	protected void onPostExecute(List result) {
		super.onPostExecute(result);
		((MainPageLayoutMeCollectionActivity) context).searchOver(result);

	}
}
