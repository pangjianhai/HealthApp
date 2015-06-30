package cn.com.health.pro;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import cn.com.health.pro.adapter.TopUserItemAdapter;
import cn.com.health.pro.model.UserItem;
import cn.com.health.pro.part.XListView;

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
		for (int i = 0; i < 8; i++) {
			UserItem ui = new UserItem();
			ui.setUserId("pangjianhai" + i);
			ui.setIfAddedInTopList(false);
			userList.add(ui);
		}
		adapter = new TopUserItemAdapter(FirstLoginTopUserListLayout.this,
				userList);
		search_friends_listview.setPullLoadEnable(false);
		search_friends_listview.setAdapter(adapter);

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
