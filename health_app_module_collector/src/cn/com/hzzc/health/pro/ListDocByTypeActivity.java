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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import cn.com.hzzc.health.pro.adapter.InfoAdapter;
import cn.com.hzzc.health.pro.entity.InfoEntity;
import cn.com.hzzc.health.pro.util.InfoUtil;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class ListDocByTypeActivity extends BaseActivity {
	/**
	 * 搜索关键词
	 */
	private String tId = "";
	/**
	 * 分页信息
	 */
	private int page = 0;
	private int rows = SystemConst.page_size;
	ListView listview = null;
	private List<InfoEntity> infoList = new ArrayList<InfoEntity>();
	/**
	 * 加载更多按钮
	 */
	private Button search_loadmore_btn;
	ProgressBar load_progress_bar = null;
	// 适配器
	InfoAdapter adapter = null;
	// footer视图
	View footer;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.list_doc_by_type);
		footer = getLayoutInflater().inflate(R.layout.info_search_more, null);
		listview = (ListView) findViewById(R.id.info_list);
		listview.addFooterView(footer);
		adapter = new InfoAdapter(ListDocByTypeActivity.this,
				R.layout.info_search_item, infoList);
		listview.setAdapter(adapter);

		tId = getIntent().getStringExtra("tId");
		search_loadmore_btn = (Button) findViewById(R.id.search_loadmore_btn);
		load_progress_bar = (ProgressBar) findViewById(R.id.load_progress_bar);
		search_loadmore_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadMoreData();
			}
		});
		loadMoreData();
		addListener();
	}

	public void addListener() {
		/**
		 * listview 选中条目事件
		 */
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				InfoEntity entity = (InfoEntity) infoList.get(position);
				String docId = entity.getId();
				Intent intent = new Intent(ListDocByTypeActivity.this,
						InfoDetailActivity.class);
				intent.putExtra("doc_id", docId);
				startActivity(intent);
			}
		});
	}

	public void loadMoreData() {
		try {
			page = page + 1;
			JSONObject j = new JSONObject();
			j.put("typeId", tId);
			j.put("page", page);
			j.put("rows", rows);
			Map map = new HashMap();
			map.put("para", j.toString());
			loadData(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param p
	 * @user:pang
	 * @data:2015年6月14日
	 * @todo:进行请求
	 * @return:void
	 */
	public void loadData(Map<String, String> p) {
		load_progress_bar.setVisibility(View.VISIBLE);
		search_loadmore_btn.setVisibility(View.GONE);
		RequestCallBack<String> r = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String rs = responseInfo.result;
				List<InfoEntity> lst = InfoUtil.parseInfoForType(rs);
				infoList.addAll(lst);
				adapter.notifyDataSetChanged();

				load_progress_bar.setVisibility(View.GONE);
				search_loadmore_btn.setVisibility(View.VISIBLE);
				if (lst.size() < rows) {
					search_loadmore_btn.setVisibility(View.GONE);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {

				load_progress_bar.setVisibility(View.GONE);
				search_loadmore_btn.setVisibility(View.VISIBLE);

			}
		};
		String URL = SystemConst.server_url
				+ SystemConst.FunctionUrl.get_docs_by_type;
		super.send_normal_request(URL, p, r);

	}

	/**
	 * 
	 * @param v
	 * @user:pang
	 * @data:2015年6月14日
	 * @todo:返回
	 * @return:void
	 */
	public void backoff(View v) {
		finish();
	}
}
