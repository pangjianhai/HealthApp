package cn.com.hzzc.health.pro;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.com.hzzc.health.pro.adapter.InfoAdapter;
import cn.com.hzzc.health.pro.entity.InfoEntity;
import cn.com.hzzc.health.pro.util.CommonHttpUtil;
import cn.com.hzzc.health.pro.util.HttpCallbackListener;

/**
 * 
 * @author pang
 * @todo 信息搜索activity
 *
 */
public class InfoSearchActivity extends BaseActivity {

	/**
	 * 搜索关键词
	 */
	private String key = "";
	private int rowNum = 0;
	private int pageCount = SystemConst.page_size;

	private List<InfoEntity> infoList = new ArrayList<InfoEntity>();

	// 关键词填写框子
	private EditText search_key;
	// 搜索按钮
	Button search_btn = null;
	// 加载更多
	Button search_loadmore_btn = null;
	// 进度条
	ProgressBar load_progress_bar = null;
	// 适配器
	InfoAdapter adapter = null;
	// footer视图
	View footer;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				search_loadmore_btn.setVisibility(View.VISIBLE);
				load_progress_bar.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.info_search_activity);
		ListView listview = (ListView) findViewById(R.id.info_list);
		footer = getLayoutInflater().inflate(R.layout.info_search_more, null);
		listview.addFooterView(footer);
		adapter = new InfoAdapter(InfoSearchActivity.this,
				R.layout.info_search_item, infoList);
		listview.setAdapter(adapter);

		// initData();
		// loadMoreData();
		//
		search_btn = (Button) findViewById(R.id.search_btn);
		search_loadmore_btn = (Button) findViewById(R.id.search_loadmore_btn);
		load_progress_bar = (ProgressBar) findViewById(R.id.load_progress_bar);
		search_key = (EditText) findViewById(R.id.search_key);
		search_btn = (Button) findViewById(R.id.search_btn);
		/**
		 * 点击搜索时间
		 */
		search_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 开始搜索
				rowNum = 0;
				key = search_key.getText().toString();
				infoList.clear();
				loadMoreData();
			}
		});
		/**
		 * 加载更多事件
		 */
		search_loadmore_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadMoreData();
			}
		});
		/**
		 * listview 选中条目事件
		 */
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				InfoEntity entity = (InfoEntity) infoList.get(position);
				String docId = entity.getId();
				Intent intent = new Intent(InfoSearchActivity.this,
						InfoDetailActivity.class);
				intent.putExtra("doc_id", docId);
				startActivity(intent);
			}
		});
		loadMoreData();

		// InputMethodManager m = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// m.toggleSoftInput(1, InputMethodManager.HIDE_NOT_ALWAYS);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	/**
	 * 添加更多
	 */
	private void loadMoreData() {
		load_progress_bar.setVisibility(View.VISIBLE);//
		search_loadmore_btn.setVisibility(View.GONE);//
		rowNum = rowNum + 1;
		CommonHttpUtil.sendHttpRequest(SystemConst.server_url
				+ "appSourceController/queryList.do?para={keywordsName:'" + key
				+ "',rows:" + pageCount + ",page:" + rowNum + "}",
				new HttpCallbackListener() {

					@Override
					public void onFinish(String response) {
						try {
							JSONObject or_obj = new JSONObject(response);
							JSONArray jarray = or_obj
									.getJSONArray("basedocument");
							for (int i = 0; i < jarray.length(); i++) {
								JSONObject obj = jarray.getJSONObject(i);
								String id = obj.getString("id");
								String type = obj.getString("typeName");
								String title = obj.getString("title");

								InfoEntity ie = new InfoEntity();
								ie.setTitle(title);
								ie.setType(type);
								ie.setId(id);
								infoList.add(ie);
							}

							Message msg = new Message();
							msg.what = 1;
							handler.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(Exception e) {
						Toast.makeText(InfoSearchActivity.this, "查询出现问题，请重新搜索",
								Toast.LENGTH_SHORT);
					}
				});

	}

}
