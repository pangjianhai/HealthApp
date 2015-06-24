package cn.com.health.pro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.com.health.pro.abstracts.ParentMainActivity;
import cn.com.health.pro.adapter.TagAdapter;
import cn.com.health.pro.model.Tag;
import cn.com.health.pro.util.TagUtils;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 
 * @author pang
 * @todo 查看标签库的所有标签
 *
 */
public class MainPageLayoutTagActivity extends ParentMainActivity {

	/**
	 * 右侧标签页面的东西
	 */
	private EditText share_send_commont_tags_input;// 搜索框
	private ListView search_tags_listview;// listview
	private List<Tag> dataSource = new ArrayList<Tag>();// 标签源
	private TagAdapter adapter = null;// 适配器

	private LinearLayout selected_tag_linearlayout = null;

	private View footer;
	// 加载更多
	Button search_loadmore_btn = null;
	// 进度条
	ProgressBar load_progress_bar = null;

	private String key = "";
	private int page = 0;
	private int size = 5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.main_page_layout_tag);
		initListView();
		initTagInput();
		initSelfTag();

	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月23日
	 * @todo:初始化标签选择listview
	 * @return:void
	 */
	private void initListView() {
		search_tags_listview = (ListView) findViewById(R.id.search_tags_listview);
		/**
		 * 加载按钮的操作view
		 */
		footer = getLayoutInflater().inflate(R.layout.info_search_more, null);
		search_tags_listview.addFooterView(footer);
		adapter = new TagAdapter(this, dataSource);
		search_tags_listview.setAdapter(adapter);

		search_tags_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Tag tag = dataSource.get(position);
				String tagId = tag.getId();
				Intent it = new Intent(MainPageLayoutTagActivity.this,
						ShareByTagActivity.class);
				it.putExtra("tagId", tagId);
				startActivity(it);
			}
		});

		/**
		 * 获取“加载更多数据”和“进度条”的view，通过footer获取
		 */
		search_loadmore_btn = (Button) footer
				.findViewById(R.id.search_loadmore_btn);
		load_progress_bar = (ProgressBar) footer
				.findViewById(R.id.load_progress_bar);
		/**
		 * 加载更多事件
		 */
		search_loadmore_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				freshDataForListView();
			}
		});
	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月23日
	 * @todo:初始化输入框
	 * @return:void
	 */
	public void initTagInput() {
		selected_tag_linearlayout = (LinearLayout) findViewById(R.id.selected_tag_linearlayout);
		share_send_commont_tags_input = (EditText) findViewById(R.id.share_send_commont_tags_input);
		share_send_commont_tags_input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence text, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence text, int start,
					int count, int after) {

			}

			/**
			 * 每次重置搜索条件
			 */
			@Override
			public void afterTextChanged(Editable edit) {
				clearListView();// 清空listview重新生成
				key = edit.toString();// 关键词
				page = 0;// 重新搜索
				dataSource.clear();// 关键词换了，列表清空
				if (key != null && !"".equals(key.trim())) {
					freshDataForListView();
				}

			}
		});

	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月23日
	 * @todo:进行新的关键词搜索之前清空之前的搜索
	 * @return:void
	 */
	public void clearListView() {
		search_tags_listview.setVisibility(View.GONE);
		dataSource.clear();
		adapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月23日
	 * @todo:根据关键词搜索标签
	 * @return:void
	 */
	public void freshDataForListView() {
		try {
			page = page + 1;
			JSONObject d = new JSONObject();
			d.put("tagName", key);
			d.put("page", page);
			d.put("rows", size);

			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					search_tags_listview.setVisibility(View.VISIBLE);
					List<Tag> list = TagUtils
							.parseJsonAddToList(responseInfo.result);
					if (list != null && !list.isEmpty()) {
						dataSource.addAll(list);
						adapter.notifyDataSetChanged();
					}
					if (list == null || list.size() < size) {
						load_progress_bar.setVisibility(View.GONE);
						search_loadmore_btn.setVisibility(View.GONE);
					} else {
						load_progress_bar.setVisibility(View.GONE);
						search_loadmore_btn.setVisibility(View.VISIBLE);
					}

				}

				@Override
				public void onFailure(HttpException error, String msg) {
					load_progress_bar.setVisibility(View.GONE);
					search_loadmore_btn.setVisibility(View.GONE);
				}
			};
			load_progress_bar.setVisibility(View.VISIBLE);
			search_loadmore_btn.setVisibility(View.GONE);
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.get_tag_by_key, map, rcb);
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月24日
	 * @todo:初始化用户已经选中过的标签
	 * @return:void
	 */
	public void initSelfTag() {
		try {
			JSONObject d = new JSONObject();
			d.put("userId", userId);

			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					List<Tag> list = TagUtils
							.parseJsonAddToList(responseInfo.result);
					if (list != null && !list.isEmpty()) {
						repaintUI(list);
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.get_tags_by_user, map, rcb);
		} catch (Exception e) {
		}

	}

	/**
	 * 
	 * @param list
	 * @user:pang
	 * @data:2015年6月24日
	 * @todo:根据用户选中过的标签渲染页面
	 * @return:void
	 */
	private void repaintUI(List<Tag> list) {
		for (Tag tag : list) {
			View tagBtn = createMyButtonTag(tag);
			selected_tag_linearlayout.addView(tagBtn);
		}
		selected_tag_linearlayout.setVisibility(View.VISIBLE);
	}

	/**
	 * 
	 * @param tag
	 * @return
	 * @user:pang
	 * @data:2015年6月24日
	 * @todo:创造渲染的单个元素
	 * @return:View
	 */
	private View createMyButtonTag(Tag tag) {
		final String tId = tag.getId();
		String tName = tag.getDisplayName();
		TextView btn2 = new TextView(this);
		btn2.setText(tName);
		btn2.setTextSize(14);
		btn2.setPadding(0, 0, 2, 0);
		btn2.setBackgroundResource(R.drawable.self_tag_shape);
		/**
		 * 添加监听事件，可以让用户删除标签
		 */
		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(MainPageLayoutTagActivity.this,
						ShareByTagActivity.class);
				it.putExtra("tagId", tId);
				startActivity(it);
			}
		});
		return btn2;
	}

	/**
	 * 
	 * @param v
	 * @user:pang
	 * @data:2015年6月23日
	 * @todo:添加自定义标签
	 * @return:void
	 */
	public void add_my_tag(View v) {
		Intent it = new Intent(getApplicationContext(),
				TagsForUserActivity.class);
		startActivity(it);
		finish();
	}

	/**
	 * 
	 * @param v
	 * @user:pang
	 * @data:2015年6月24日
	 * @todo:返回
	 * @return:void
	 */
	public void backoff(View v) {
		to_home_page();
		finish();
	}
}
