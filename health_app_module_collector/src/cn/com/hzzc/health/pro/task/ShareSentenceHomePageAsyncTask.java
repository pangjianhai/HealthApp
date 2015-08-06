package cn.com.hzzc.health.pro.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import cn.com.hzzc.health.pro.MainPageLayoutSpaceActivity;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.model.ShareSentenceEntity;
import cn.com.hzzc.health.pro.util.CommonHttpUtil;
import cn.com.hzzc.health.pro.util.ShareSentenceUtil;

/**
 * 
 * @author pang
 * @todo 根据用户ID获取朋友圈的分享分页
 *
 */
@Deprecated
public class ShareSentenceHomePageAsyncTask extends
		AsyncTask<String, Void, Map<String, Object>> {

	private Context context;

	private boolean ifSend;

	public ShareSentenceHomePageAsyncTask(Context context) {
		super();
		this.context = context;
	}

	public ShareSentenceHomePageAsyncTask(Context context, boolean is) {
		super();
		this.context = context;
		this.ifSend = is;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Map<String, Object> doInBackground(String... params) {
		String data = null;
		if (ifSend) {
			String para = params[0];
			String url = SystemConst.server_url
					+ SystemConst.FunctionUrl.getFriendsShareByUserId
					+ "?para=" + para;
			data = CommonHttpUtil.sendHttpRequest(url);
		}
		Map<String, Object> map = ShareSentenceUtil.parseJsonCondition(data);
		return map;
	}

	@Override
	protected void onPostExecute(Map<String, Object> map) {
		super.onPostExecute(map);
		String searchDay = map.get("searchDay") + "";
		String begin = map.get("begin") + "";
		List<ShareSentenceEntity> list = (List<ShareSentenceEntity>) map
				.get("lst");
		/**
		 * 服务器端告诉移动端已经不需要翻页了，没有更多的数据了
		 */
		String nomore = map.get("nomore") + "";
		boolean if_has_nomore_field = true;
		/**
		 * 如果存在nomore提示則不需要继续进行加载
		 */
		if ("nomore".equals(nomore.trim())) {
			if_has_nomore_field = false;
		}
		((MainPageLayoutSpaceActivity) context).asyncAddNewData(searchDay,
				begin, list, if_has_nomore_field);

	}

}
