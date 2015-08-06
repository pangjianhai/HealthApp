package cn.com.hzzc.health.pro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.adapter.CollectionItemAdapter;
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.model.CollectionItem;
import cn.com.hzzc.health.pro.util.CollectionUtil;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

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
	 * 当前页
	 */
	private int currentPage = 0;
	/**
	 * 一页多少行
	 */
	private int pageSize = SystemConst.page_size;

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

		loadMoreData();
	}

	private void loadMoreData() {
		load_progress_bar.setVisibility(View.VISIBLE);
		search_loadmore_btn.setVisibility(View.GONE);
		try {
			currentPage = currentPage + 1;
			JSONObject d = new JSONObject();
			d.put("userId", HealthApplication.getUserId());
			d.put("pageNum", currentPage);
			d.put("pageSize", pageSize);

			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					List<CollectionItem> lst = CollectionUtil
							.parseInfo(responseInfo.result);
					searchOver(lst);
				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			System.out.println("d.toString():" + d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.get_collection_by_id, map, rcb);
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

	/**
	 * 
	 * @param view
	 * @user:pang
	 * @data:2015年6月25日
	 * @todo:退出当前页面
	 * @return:void
	 */
	public void search_back(View view) {
		this.finish();
	}

	/**
	 * 
	 * @param id
	 * @user:pang
	 * @data:2015年6月2日
	 * @todo:查看收藏
	 * @return:void
	 */
	public void checkCollection(String id, String type) {
		if (SystemConst.CollectionType.COLLECTION_TYPE_SHARE.equals(type)) {
			Intent intent = new Intent(MainPageLayoutMeCollectionActivity.this,
					ShareSentenceAllDetailActivity.class);
			intent.putExtra("share_sentence_id", id);
			startActivity(intent);
		} else if (SystemConst.CollectionType.COLLECTION_TYPE_DOC.equals(type)) {
			Intent intent = new Intent(MainPageLayoutMeCollectionActivity.this,
					InfoDetailActivity.class);
			intent.putExtra("doc_id", id);
			startActivity(intent);
		}
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
