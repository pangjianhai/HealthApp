package cn.com.health.pro;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;
import cn.com.health.pro.adapter.UserItemAdapter;
import cn.com.health.pro.entity.InfoEntity;
import cn.com.health.pro.model.UserItem;
import cn.com.health.pro.task.SearchUsersAsyncTask;

/**
 * 
 * @author pang
 * @todo 用户搜索结果
 *
 */
public class FriendSearchResultActivity extends BaseActivity {

	private ListView search_friends_listview;
	private List<UserItem> userList = new ArrayList<UserItem>();
	private UserItemAdapter adapter;
	/**
	 * 搜索关键词
	 */
	private String key;

	/**
	 * 当前页
	 */
	private int currentPage = 0;
	/**
	 * 一页多少行
	 */
	private int pageSize = 10;

	View footer;
	/**
	 * 加载更多按钮
	 */
	private Button search_loadmore_btn;
	ProgressBar load_progress_bar = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.friends_search_users_listview);
		Intent intent = getIntent();
		key = intent.getStringExtra("key");
		init();
	}

	private void init() {
		search_friends_listview = (ListView) findViewById(R.id.search_friends_listview);
		footer = getLayoutInflater().inflate(R.layout.info_search_more, null);
		search_friends_listview.addFooterView(footer);
		search_loadmore_btn = (Button) findViewById(R.id.search_loadmore_btn);
		load_progress_bar = (ProgressBar) findViewById(R.id.load_progress_bar);
		adapter = new UserItemAdapter(FriendSearchResultActivity.this, userList);
		search_friends_listview.setAdapter(adapter);

		search_loadmore_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadMoreData();
			}
		});

		search_friends_listview
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						UserItem ui = userList.get(position);
						checkSomeOne(ui.getUuid());
					}

				});
		loadMoreData();
	}

	private void loadMoreData() {
		load_progress_bar.setVisibility(View.VISIBLE);
		search_loadmore_btn.setVisibility(View.GONE);
		try {
			currentPage = currentPage + 1;
			SearchUsersAsyncTask task = new SearchUsersAsyncTask(
					FriendSearchResultActivity.this);
			JSONObject map = new JSONObject();
			map.put("UserId", key);
			map.put("begin", (currentPage - 1) * pageSize);
			map.put("limit", pageSize);
			task.execute(map.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void searchOver(List<UserItem> lst) {
		load_progress_bar.setVisibility(View.GONE);
		search_loadmore_btn.setVisibility(View.VISIBLE);
		if (lst.size() < pageSize) {
			search_loadmore_btn.setVisibility(View.GONE);
		}
		userList.addAll(lst);
		adapter.notifyDataSetChanged();
	}

	public void search_back(View view) {
		this.finish();
	}

	public void checkSomeOne(String id) {
		Intent intent = new Intent(FriendSearchResultActivity.this,
				ShowUserInfoDetail.class);
		intent.putExtra("uuid", id);
		startActivity(intent);
	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月19日
	 * @todo 关闭当前页
	 * @author pang
	 */
	public void go_back(View v) {
		finish();
	}
}
