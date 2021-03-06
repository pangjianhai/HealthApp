package cn.com.hzzc.health.pro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;
import cn.com.hzzc.health.pro.abstracts.ParentMainActivity;
import cn.com.hzzc.health.pro.adapter.TagAdapter;
import cn.com.hzzc.health.pro.model.Tag;
import cn.com.hzzc.health.pro.part.MyScrollView;
import cn.com.hzzc.health.pro.part.MyScrollView.BtnOps;
import cn.com.hzzc.health.pro.util.TagUtils;

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
	private List<Tag> dataSource = new ArrayList<Tag>();// 标签源
	private TagAdapter adapter = null;// 适配器

	private MyScrollView my_scroll_view = null;
	private LinearLayout selected_tag_linearlayout = null;

	private View footer;

	private String key = "";
	private int page = 0;
	private int size = SystemConst.page_size;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.main_page_layout_tag);
		initTagInput();
		initListView();
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
		BtnOps bo = new BtnOps() {

			@Override
			public void afterClick(Tag tag) {
				Intent it = new Intent(MainPageLayoutTagActivity.this,
						ShareByTagActivity.class);
				it.putExtra("tagId", tag.getId());
				startActivity(it);
			}
		};
		my_scroll_view = (MyScrollView) findViewById(R.id.my_scroll_view);
		my_scroll_view.setBtnOps(bo);
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
				if (key != null && !"".equals(key.trim())) {
					my_scroll_view.restartNewKeySearch(key);
				}

			}
		});

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
					} else {
						Toast.makeText(getApplicationContext(),
								"点击右上角可以为自己设置标签", Toast.LENGTH_LONG).show();
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
	 * @data:2015年8月11日
	 * @todo:点击弹出框和标签的操作有关
	 * @return:void
	 */
	public void tag_ops_pop(View v) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.tag_space_ops_window, null);

		PopupWindow window = new PopupWindow(view,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);
		// 设置popWindow的显示和消失动画
		// window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window.showAsDropDown(v);
		/**
		 * popwindow按钮地方法
		 */
		Button tag_space_ops_share_tag = (Button) view
				.findViewById(R.id.tag_space_ops_share_tag);
		tag_space_ops_share_tag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				to_self_def(v);
			}

		});
		Button tag_space_ops_self_def = (Button) view
				.findViewById(R.id.tag_space_ops_self_def);
		tag_space_ops_self_def.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_my_tag(v);
			}

		});
		/**
		 * 让popwindow消失
		 */
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				System.out.println("popWindow消失");

			}
		});
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

	public void to_self_def(View v) {
		Intent it = new Intent(getApplicationContext(),
				TagsForUserDefActivity.class);
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
