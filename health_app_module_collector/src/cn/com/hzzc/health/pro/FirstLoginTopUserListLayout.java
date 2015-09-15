package cn.com.hzzc.health.pro;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.adapter.TopUserItemAdapter;
import cn.com.hzzc.health.pro.model.UserItem;
import cn.com.hzzc.health.pro.part.XListView;
import cn.com.hzzc.health.pro.util.CommonDateUtil;
import cn.com.hzzc.health.pro.util.UserUtils;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @todo 给注册首次对登陆的用户的推荐列表
 * @author pang
 *
 */
public class FirstLoginTopUserListLayout extends BaseActivity {

	private XListView search_friends_listview;
	private List<UserItem> userList = new ArrayList<UserItem>();
	private TopUserItemAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.first_login_top_user_list);
		search_friends_listview = (XListView) findViewById(R.id.notice_user_lv);
		adapter = new TopUserItemAdapter(FirstLoginTopUserListLayout.this,
				userList);
		search_friends_listview.setPullRefreshEnable(false);
		search_friends_listview.setPullLoadEnable(false);
		search_friends_listview.setAdapter(adapter);
		initData();
	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月30日
	 * @todo:初始化推荐好友的数据
	 * @return:void
	 */
	private void initData() {
		try {
			JSONObject d = new JSONObject();
			d.put("currentDate", CommonDateUtil.formatDate(new Date()));
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					List list = UserUtils.parseJsonAddToTopList(data);
					userList.addAll(list);
					adapter.notifyDataSetChanged();
				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.get_top_user_list, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param id
	 * @user:pang
	 * @data:2015年6月30日
	 * @todo:用户选择关注某个人
	 * @return:void
	 */
	public void checkSomeOne(String id) {
		try {
			JSONObject d = new JSONObject();
			d.put("currentId", userId);
			d.put("friendId", id);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.some_one_focus_another, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @param v
	 * @user:pang
	 * @data:2015年6月30日
	 * @todo:返回
	 * @return:void
	 */
	public void backoff(View v) {
		finish();
	}

	/**
	 * 
	 * @param v
	 * @user:pang
	 * @data:2015年6月30日
	 * @todo:一键关注
	 * @return:void
	 */
	public void focus_all(View v) {
		finish();
	}
}
