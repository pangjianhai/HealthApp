package cn.com.health.pro;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

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
import android.widget.Toast;
import cn.com.health.pro.adapter.CollectionItemAdapter;
import cn.com.health.pro.config.HealthApplication;
import cn.com.health.pro.model.CollectionItem;
import cn.com.health.pro.task.CollectionAsyncTask;

/**
 * 
 * @author pang
 * @todo “我的收藏”activity
 *
 */
public class MainPageLayoutMeCollectionActivity extends BaseActivity {
	private ListView my_collection_listview;
	private List<CollectionItem> collList = new ArrayList<CollectionItem>();
	private CollectionItemAdapter adapter;
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
		setContentView(R.layout.main_page_layout_me_collection);
		Intent intent = getIntent();
		key = "1";// intent.getStringExtra("key");
		init();
	}

	private void init() {
		my_collection_listview = (ListView) findViewById(R.id.my_collection_listview);
		footer = getLayoutInflater().inflate(R.layout.info_search_more, null);
		my_collection_listview.addFooterView(footer);
		search_loadmore_btn = (Button) findViewById(R.id.search_loadmore_btn);
		load_progress_bar = (ProgressBar) findViewById(R.id.load_progress_bar);
		adapter = new CollectionItemAdapter(
				MainPageLayoutMeCollectionActivity.this, collList);
		my_collection_listview.setAdapter(adapter);

		search_loadmore_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadMoreData();
			}
		});

		my_collection_listview
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						System.out.println("------------>>>>");
						CollectionItem ui = collList.get(position);
						checkSomeOne("");
					}

				});
		loadMoreData();
	}

	private void loadMoreData() {
		load_progress_bar.setVisibility(View.VISIBLE);
		search_loadmore_btn.setVisibility(View.GONE);
		try {
			currentPage = currentPage + 1;
			CollectionAsyncTask task = new CollectionAsyncTask(
					MainPageLayoutMeCollectionActivity.this);
			JSONObject map = new JSONObject();
			map.put("currentId", HealthApplication.getUserId());
			map.put("begin", (currentPage - 1) * pageSize);
			map.put("limit", pageSize);
			map.put("limit", pageSize);
			task.execute(map.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void searchOver(List<CollectionItem> lst) {
		load_progress_bar.setVisibility(View.GONE);
		search_loadmore_btn.setVisibility(View.VISIBLE);
		if (lst.size() < pageSize) {
			search_loadmore_btn.setVisibility(View.GONE);
		}
		collList.addAll(lst);
		adapter.notifyDataSetChanged();
	}

	public void search_back(View view) {
		this.finish();
	}

	public void checkSomeOne(String id) {
		Toast.makeText(MainPageLayoutMeCollectionActivity.this,
				"xxxxxxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(MainPageLayoutMeCollectionActivity.this,
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
