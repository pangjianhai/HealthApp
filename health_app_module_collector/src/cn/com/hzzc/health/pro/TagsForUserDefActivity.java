package cn.com.hzzc.health.pro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

	private ViewGroup container = null;
	public List<Tag> tags_selected = new ArrayList<Tag>();// 已经选中的标签
	/** 标签之间的间距 px */
	final int itemMargins = 4;
	/** 标签的行间距 px */
	final int lineMargins = 10;
	// 标签高度
	final int tag_btn_height = 45;
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
		container = (ViewGroup) findViewById(R.id.selected_tag_linearlayout);
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
					container.removeAllViews();// 清空所有的
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
			/**
			 * 模糊查询只需要取三十个就可以了，没有必要分页
			 */
			JSONObject d = new JSONObject();
			d.put("tagName", key);
			d.put("page", 1);
			d.put("rows", 30);

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
			System.out.println(tags_selected.size());
			container.removeAllViews();// 请控所有的

			int containerWidth = container.getMeasuredWidth()
					- container.getPaddingRight() - container.getPaddingLeft();

			final LayoutInflater inflater = getLayoutInflater();

			/** 用来测量字符的宽度 */
			final Paint paint = new Paint();
			final Button button = (Button) inflater.inflate(
					R.layout.tag_layout, null);
			final int itemPadding = button.getCompoundPaddingLeft()
					+ button.getCompoundPaddingRight();
			final LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, tag_btn_height);
			tvParams.setMargins(0, 0, 4, 0);

			paint.setTextSize(button.getTextSize());

			LinearLayout layout = new LinearLayout(this);
			layout.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			layout.setOrientation(LinearLayout.HORIZONTAL);
			container.addView(layout);

			final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(0, lineMargins, 0, 0);

			/** 一行剩下的空间 **/
			int remainWidth = containerWidth;

			for (Tag tag : tags_selected) {
				String tagName = tag.getDisplayName();
				final float itemWidth = paint.measureText(tagName)
						+ itemPadding;
				if (remainWidth > itemWidth) {
					addItemView(inflater, layout, tvParams, tagName);
				} else {
					resetTextViewMarginsRight(layout);
					layout = new LinearLayout(this);
					layout.setLayoutParams(params);
					layout.setOrientation(LinearLayout.HORIZONTAL);

					/** 将前面那一个textview加入新的一行 */
					addItemView(inflater, layout, tvParams, tagName);
					container.addView(layout);
					remainWidth = containerWidth;
				}
				remainWidth = (int) (remainWidth - itemWidth + 0.5f)
						- itemMargins;
			}
		}

	}

	/**
	 * @param viewGroup
	 * @user:pang
	 * @data:2015年8月12日
	 * @todo:对每一行的最后一个button元素做宽度上的处理
	 * @return:void
	 */
	private void resetTextViewMarginsRight(ViewGroup viewGroup) {
		final Button bt = (Button) viewGroup.getChildAt(viewGroup
				.getChildCount() - 1);
		bt.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, tag_btn_height));
	}

	/**
	 * 
	 * @param inflater
	 * @param viewGroup
	 * @param tvParams
	 * @param text
	 * @user:往每一行linearlayout中添加button元素
	 * @data:2015年8月12日
	 * @todo:TODO
	 * @return:void
	 */
	private void addItemView(LayoutInflater inflater, ViewGroup viewGroup,
			android.widget.LinearLayout.LayoutParams tvParams, String text) {
		final Button btn = (Button) inflater.inflate(R.layout.tag_layout, null);
		btn.setText(text);
		viewGroup.addView(btn, tvParams);
	}

	/**
	 * 
	 * @param v
	 * @user:pang
	 * @data:2015年8月12日
	 * @todo:确定进行标签贡献的http请求方法
	 * @return:void
	 */
	public void real_share_tag() {
		try {
			String tag = share_send_commont_tags_input.getText().toString();
			JSONObject d = new JSONObject();
			d.put("userId", userId);
			d.put("tagName", tag);

			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					Toast.makeText(getApplicationContext(), "分享成功，等待审核哦",
							Toast.LENGTH_SHORT).show();
					backoff(null);
				}

				@Override
				public void onFailure(HttpException error, String msg) {
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.shareTagToPlatform, map, rcb);
		} catch (Exception e) {
		}
	}

	/**
	 * @param v
	 * @user:pang
	 * @data:2015年8月12日
	 * @todo:用户点击“保存”按钮的时候进行最后的提示
	 * @return:void
	 */
	public void share_tag(View v) {
		String tag = share_send_commont_tags_input.getText().toString();
		if (tag == null || "".equals(tag.trim())) {
			can_not_empty_alert();
			return;
		}
		new AlertDialog.Builder(TagsForUserDefActivity.this).setTitle("保存提示")
				.setMessage("确定贡献标[" + tag + "]")
				.setPositiveButton("贡献", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						real_share_tag();
					}
				}).setNegativeButton("取消", null).show();
	}

	/**
	 * @user:pang
	 * @data:2015年8月12日
	 * @todo:贡献内容不得为空提示
	 * @return:void
	 */
	private void can_not_empty_alert() {
		new AlertDialog.Builder(TagsForUserDefActivity.this).setTitle("保存提示")
				.setMessage("内容不得为空").setPositiveButton("确定", null).show();
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
