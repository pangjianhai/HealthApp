package cn.com.health.pro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;
import cn.com.health.pro.adapter.TagAdapter;
import cn.com.health.pro.model.Tag;
import cn.com.health.pro.util.TagUtils;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 
 * @author pang
 * @todo 用户设置标签activity
 *
 */
public class TagsForUserActivity extends BaseActivity {

	/**
	 * 右侧标签页面的东西
	 */
	private EditText share_send_commont_tags_input;// 搜索框
	private ListView search_tags_listview;// listview
	private List<Tag> dataSource = new ArrayList<Tag>();// 标签源
	private TagAdapter adapter = null;// 适配器

	private LinearLayout selected_tag_linearlayout = null;
	public List<Tag> tags_selected = new ArrayList<Tag>();// 已经选中的标签
	private String will_del_tag_id = "";

	/*
	 * 标签分页
	 */
	private String key = "";
	private int page = 0;
	private int size = 5;
	private View footer;
	// 加载更多
	Button search_loadmore_btn = null;
	// 进度条
	ProgressBar load_progress_bar = null;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tags_for_users);

		initTagInput();
		initTagView();

		initSelfTag();
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
				key = edit.toString();// 关键词
				page = 0;// 重新搜索
				dataSource.clear();// 关键词换了，列表清空
				if (key != null && !"".equals(key.trim())) {
					freshDataForListView();
				}

			}
		});

	}

	public void initTagView() {

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
				afterTagSelected(tag);
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
	 * @param tag
	 * @user:pang
	 * @data:2015年6月21日
	 * @todo:选中一个标签之后更新UI
	 * @return:void
	 */
	private void afterTagSelected(Tag tag) {
		if (tags_selected != null && tags_selected.size() >= 4) {
			Toast.makeText(getApplicationContext(), "最多只能添加4个标签",
					Toast.LENGTH_SHORT).show();
			return;
		}
		// 如果没有被选中过则添加
		if (!ifChosen(tag)) {
			tags_selected.add(tag);// 将选择的tag放入list中
			selected_tag_linearlayout.setVisibility(View.VISIBLE);
			View btn2 = createButton(tag);
			selected_tag_linearlayout.addView(btn2);
		}
	}

	private View createButton(Tag tag) {
		final String tId = tag.getId();
		String tName = tag.getDisplayName();
		TextView btn2 = new TextView(this);
		btn2.setText(tName);
		btn2.setTextSize(14);
		btn2.setBackgroundResource(R.drawable.self_tag_shape);

		/**
		 * 添加监听事件，可以让用户删除标签
		 */
		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				will_del_tag_id = tId;// 点击标签允许其进行删除
				/**
				 * 注意构造builder的参数是rightView.getContext()
				 */
				AlertDialog.Builder builder = new AlertDialog.Builder(
						TagsForUserActivity.this).setTitle("提示").setMessage(
						"要删除吗？");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								repaintUI(will_del_tag_id);
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								will_del_tag_id = "";
							}
						});
				builder.show();

			}
		});
		return btn2;
	}

	public void repaintUI(String willDelId) {
		List<Tag> new_selected_list = new ArrayList<Tag>();
		if (tags_selected != null && !tags_selected.isEmpty()) {
			selected_tag_linearlayout.removeAllViews();// 请控所有的
			for (Tag tag : tags_selected) {
				final String tId = tag.getId();
				if (!tId.equals(willDelId)) {// 把选中的排除在外
					new_selected_list.add(tag);// 把留下来的tag放入新的list
					View btn2 = createButton(tag);
					selected_tag_linearlayout.addView(btn2);// 重绘linearlayout
				}
			}
			tags_selected.clear();
			tags_selected.addAll(new_selected_list);
		}

	}

	/**
	 * 
	 * @param tag
	 * @return
	 * @user:pang
	 * @data:2015年6月21日
	 * @todo:判断选中的标签是否已经被选中过了，如果是则忽略
	 * @return:boolean
	 */
	private boolean ifChosen(Tag tag) {
		String tagId = tag.getId();
		boolean b = false;
		if (tags_selected != null && !tags_selected.isEmpty()) {
			for (Tag t : tags_selected) {
				String id = t.getId();
				if (id.equals(tagId)) {
					return true;
				}
			}
		}
		return false;
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
	 * @param v
	 * @user:pang
	 * @data:2015年6月24日
	 * @todo:给用户添加标签
	 * @return:void
	 */
	public void add_tag_to_user(View v) {
		try {
			page = page + 1;
			JSONObject d = new JSONObject();
			d.put("userId", userId);
			d.put("tagsId", getTagStr());

			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					Toast.makeText(getApplicationContext(), "添加成功",
							Toast.LENGTH_SHORT).show();
					finish();
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
					+ SystemConst.FunctionUrl.add_tag_to_user, map, rcb);
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * @return
	 * @user:pang
	 * @data:2015年6月24日
	 * @todo:拼接用户选择的标签ID传到后台保存
	 * @return:String
	 */
	private String getTagStr() {
		if (tags_selected != null && !tags_selected.isEmpty()) {
			StringBuffer sb = new StringBuffer("");
			String rv = "";
			for (Tag tag : tags_selected) {
				sb.append(tag.getId()).append(",");
			}
			rv = sb.toString();
			rv = rv.substring(0, rv.length() - 1);
			return rv;
		}
		return "";
	}

	/************************************************** 初始化已经选过的标签 ************************************************************************/
	public void initSelfTag() {
		try {
			page = page + 1;
			JSONObject d = new JSONObject();
			d.put("userId", userId);

			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					List<Tag> list = TagUtils
							.parseJsonAddToList(responseInfo.result);
					if (list != null && !list.isEmpty()) {
						tags_selected.addAll(list);
						repaintUI();
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
	 * 
	 * @user:pang
	 * @data:2015年6月24日
	 * @todo:初始化activity的时候调用一次，将已经保存过的标签渲染出来
	 * @return:void
	 */
	public void repaintUI() {
		if (tags_selected != null && !tags_selected.isEmpty()) {
			selected_tag_linearlayout.removeAllViews();// 请控所有的
			for (Tag tag : tags_selected) {
				View btn2 = createButton(tag);
				selected_tag_linearlayout.addView(btn2);// 重绘linearlayout
			}
			selected_tag_linearlayout.setVisibility(View.VISIBLE);
		}

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
		finish();
	}

}
