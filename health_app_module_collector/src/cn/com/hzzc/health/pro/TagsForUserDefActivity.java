package cn.com.hzzc.health.pro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hzzc.health.pro.model.Tag;
import cn.com.hzzc.health.pro.util.TagUtils;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 
 * @author pang
 * @todo 用户分享标签activity
 *
 */
public class TagsForUserDefActivity extends BaseActivity {

	/**
	 * 右侧标签页面的东西
	 */
	private EditText share_send_commont_tags_input;// 搜索框

	private ViewGroup selected_tag_linearlayout = null;
	public List<Tag> tags_selected = new ArrayList<Tag>();// 已经选中的标签

	/*
	 * 标签分页
	 */
	private String key = "";

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tags_for_users_def);

		initTagInput();
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
		selected_tag_linearlayout = (ViewGroup) findViewById(R.id.selected_tag_linearlayout);
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
				if (key != null && !"".equals(key.trim())) {
					/**
					 * 根据关键词搜索标签绘制出来
					 */
					selected_tag_linearlayout.removeAllViews();// 清空所有的
					repaintLinearLayout();
				}

			}
		});

	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月23日
	 * @todo:根据关键词搜索标签
	 * @return:void
	 */
	public void repaintLinearLayout() {
		try {
			JSONObject d = new JSONObject();
			d.put("tagName", key);
			d.put("page", 1);
			d.put("rows", 50);

			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					List<Tag> list = TagUtils
							.parseJsonAddToList(responseInfo.result);
					tags_selected.clear();// 清空老数据
					tags_selected.addAll(list);// 添加新数据
					repaintUI();// 重新绘制UI
				}

				@Override
				public void onFailure(HttpException error, String msg) {
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.get_tag_by_key, map, rcb);
		} catch (Exception e) {
		}
	}

	/**
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

	private View createButton(Tag tag) {
		final String tId = tag.getId();
		String tName = tag.getDisplayName();
		TextView btn2 = new TextView(this);
		btn2.setText(tName);
		btn2.setTextSize(14);
		btn2.setPadding(0, 0, 2, 0);
		btn2.setBackgroundResource(R.drawable.self_tag_shape);
		return btn2;
	}

	/**
	 * @param v
	 * @user:pang
	 * @data:2015年8月11日
	 * @todo:分享标签
	 * @return:void
	 */
	public void share_tag(View v) {
		try {
			JSONObject d = new JSONObject();
			d.put("userId", userId);
			d.put("tagsId", "");

			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					Toast.makeText(getApplicationContext(), "添加成功",
							Toast.LENGTH_SHORT).show();
					Intent it = new Intent(getApplicationContext(),
							MainPageLayoutTagActivity.class);
					startActivity(it);
					finish();
				}

				@Override
				public void onFailure(HttpException error, String msg) {
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.add_tag_to_user, map, rcb);
		} catch (Exception e) {
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
