package cn.com.hzzc.health.pro.topic;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.ShareByTagActivity;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.TagsForUserActivity;
import cn.com.hzzc.health.pro.TagsForUserDefActivity;
import cn.com.hzzc.health.pro.adapter.TagAdapter;
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.model.Tag;
import cn.com.hzzc.health.pro.part.MyScrollView;
import cn.com.hzzc.health.pro.part.MyScrollView.BtnOps;
import cn.com.hzzc.health.pro.util.TagUtils;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @todo 主页的标签碎片
 * @author pang
 *
 */
public class HomeMainTagFragment extends ParentFragment {

	private View view;
	/**
	 * 右侧标签页面的东西
	 */
	private EditText share_send_commont_tags_input;// 搜索框

	private MyScrollView my_scroll_view = null;
	private LinearLayout selected_tag_linearlayout = null;
	private TextView selected_tag_notice;

	private String key = "";
	private int page = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.home_main_tag_fragment, null);
		initTagInput();
		initListView();
		initSelfTag();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup viewGroup = (ViewGroup) view.getParent();
		return view;
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
				Intent it = new Intent(getActivity(), ShareByTagActivity.class);
				it.putExtra("tagId", tag.getId());
				startActivity(it);
			}
		};
		my_scroll_view = (MyScrollView) view.findViewById(R.id.my_scroll_view);
		my_scroll_view.setBtnOps(bo);
		selected_tag_notice = (TextView) view
				.findViewById(R.id.selected_tag_notice);
		/*** 如果没有自定义标签，则点击提示文字可以进入添加页面 ***/
		selected_tag_notice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 先判断是否处于显示状态
				if (selected_tag_notice.getVisibility() == View.VISIBLE) {
					add_my_tag(v);
				}
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
		selected_tag_linearlayout = (LinearLayout) view
				.findViewById(R.id.selected_tag_linearlayout);
		share_send_commont_tags_input = (EditText) view
				.findViewById(R.id.share_send_commont_tags_input);

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
						selected_tag_notice.setVisibility(View.GONE);
						repaintUI(list);
					} else {
						selected_tag_linearlayout.setVisibility(View.GONE);
						selected_tag_notice.setVisibility(View.VISIBLE);
						Toast.makeText(getActivity(), "点击右上角\"我的标签\"可以为自己设置标签",
								Toast.LENGTH_LONG).show();
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
		TextView btn2 = new TextView(getActivity());
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
				toTagSentence(tId, v);
			}
		});
		return btn2;
	}

	/**
	 * 
	 * @param tId
	 * @param v
	 * @user:pang
	 * @data:2015年9月15日
	 * @todo:查看标签搜索详情
	 * @return:void
	 */
	private void toTagSentence(String tId, View v) {
		HomeAllShowActivity ac = (HomeAllShowActivity) getActivity();
		if (!ac.isLogin()) {
			ac.no_login_alter(v);
			return;
		}
		Intent it = new Intent(getActivity(), ShareByTagActivity.class);
		it.putExtra("tagId", tId);
		startActivity(it);
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
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		Intent it = new Intent(getActivity(), TagsForUserActivity.class);
		startActivity(it);
	}

	/**
	 * 
	 * @param v
	 * @user:pang
	 * @data:2015年10月9日
	 * @todo:贡献标签
	 * @return:void
	 */
	public void to_self_def(View v) {
		Intent it = new Intent(getActivity(), TagsForUserDefActivity.class);
		startActivity(it);
	}

	@Override
	public void screenScroll(float y) {
		// TODO Auto-generated method stub

	}

	/**
	 * @todo 重新展示页面的时候移除之前的子元素，重新加载刷新
	 */
	@Override
	public void onResume() {
		super.onResume();
		/********** 如果用户已经对某个主题参与或者退出了，需要重新加载自己参与的主题列表 **********/
		selected_tag_linearlayout.removeAllViews();
		initSelfTag();
	}
}
