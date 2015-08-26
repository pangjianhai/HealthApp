package cn.com.hzzc.health.pro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.adapter.UserItemAdapter;
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.model.UserItem;
import cn.com.hzzc.health.pro.task.FocusMeAsyncTask;
import cn.com.hzzc.health.pro.util.UserUtils;

/**
 * 
 * @author pang
 * @todo “关注我的”activity
 *
 */
public class MainPageLayoutMeFocusMeActivity extends BaseActivity {
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
	private int currentPage = 1;
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
		setContentView(R.layout.main_page_layout_me_focusme);
		Intent intent = getIntent();
		key = "1";// intent.getStringExtra("key");
		init();
	}

	private void init() {
		search_friends_listview = (ListView) findViewById(R.id.search_friends_listview);
		footer = getLayoutInflater().inflate(R.layout.info_search_more, null);
		search_friends_listview.addFooterView(footer);
		search_loadmore_btn = (Button) findViewById(R.id.search_loadmore_btn);
		load_progress_bar = (ProgressBar) findViewById(R.id.load_progress_bar);
		adapter = new UserItemAdapter(MainPageLayoutMeFocusMeActivity.this,
				userList);
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

	public void loadMoreData() {
		try {
			load_progress_bar.setVisibility(View.VISIBLE);
			search_loadmore_btn.setVisibility(View.GONE);
			JSONObject d = new JSONObject();
			d.put("currentId", HealthApplication.getUserId());
			d.put("begin", (currentPage - 1) * pageSize);
			d.put("limit", pageSize);
			currentPage = currentPage + 1;
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					List<UserItem> uis = UserUtils.parseJsonAddToList(data);
					searchOver(uis);
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					load_progress_bar.setVisibility(View.GONE);
					search_loadmore_btn.setVisibility(View.VISIBLE);
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.getFocusMeUser, map, rcb);
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
		Intent intent = new Intent(MainPageLayoutMeFocusMeActivity.this,
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
