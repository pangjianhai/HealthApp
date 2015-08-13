package cn.com.hzzc.health.pro;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hzzc.health.pro.abstracts.ParentMainActivity;
import cn.com.hzzc.health.pro.adapter.ShareItemAdapter;
import cn.com.hzzc.health.pro.model.ShareInOrderEntity;
import cn.com.hzzc.health.pro.model.ShareSentenceEntity;
import cn.com.hzzc.health.pro.part.XListView;
import cn.com.hzzc.health.pro.part.XListView.IXListViewListener;
import cn.com.hzzc.health.pro.service.ShareCommentService;
import cn.com.hzzc.health.pro.service.ViewForInfoService;
import cn.com.hzzc.health.pro.util.CommonDateUtil;
import cn.com.hzzc.health.pro.util.IShareCallbackOperator;
import cn.com.hzzc.health.pro.util.ShareSentenceUtil;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 
 * @author pang
 * @todo 首页我的排行
 *
 */
public class MainPageLayoutOrderActivity extends ParentMainActivity implements
		IXListViewListener, IShareCallbackOperator {

	/**
	 * 适配器需要的数据结构
	 */
	private List<ShareSentenceEntity> dataSourceList = new ArrayList<ShareSentenceEntity>();
	/**
	 * 上下文环境
	 */
	private Context context;

	/**
	 * 空间分享信息适配器
	 */
	private ShareItemAdapter itemAdapter;

	/**
	 * 弹出框
	 */
	private LinearLayout share_bottom;
	private EditText et_pop;
	private String commentShareId;
	/**
	 * 空间分享信息列表
	 */
	private XListView mListView;

	/**
	 * 查询的日期
	 */
	private String search_day = CommonDateUtil.formatDate(CommonDateUtil
			.getNowTimeDate());

	/**
	 * 时间
	 */
	private String today = CommonDateUtil.formatDate(new Date());
	private String yesterday = CommonDateUtil.formatDate(CommonDateUtil
			.preDate(new Date()));
	private String df_date = CommonDateUtil.formatDate(new Date());
	private ImageButton date_next, date_pre;
	private TextView date_content;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.main_page_layout_order);

		findView();
		/**
		 * 先后顺序
		 */
		initListView();
		loadDataMore();

	}

	/**
	 * @user:pang
	 * @data:2015年6月19日
	 * @todo:初始化组件
	 * @return:void
	 */
	private void findView() {
		context = this;
		dataSourceList = new ArrayList<ShareSentenceEntity>();
		mListView = (XListView) findViewById(R.id.space_lv);
		mListView.setPullLoadEnable(false);
		mListView.setXListViewListener(this);
		share_bottom = (LinearLayout) findViewById(R.id.share_bottom);
		et_pop = (EditText) findViewById(R.id.tv_pop);

		/**
		 * 选择按钮
		 */
		date_next = (ImageButton) findViewById(R.id.date_next);
		date_pre = (ImageButton) findViewById(R.id.date_pre);
		date_content = (TextView) findViewById(R.id.date_content);
		date_content.setText(df_date);
		date_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Date current_d = CommonDateUtil.getDate(df_date);
				Date next_day = CommonDateUtil.nextDay(current_d);
				df_date = CommonDateUtil.formatDate(next_day);
				loadDataMore();
			}
		});
		date_pre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Date current_d = CommonDateUtil.getDate(df_date);
				Date next_day = CommonDateUtil.preDate(current_d);
				df_date = CommonDateUtil.formatDate(next_day);
				loadDataMore();
			}
		});
	}

	private void initListView() {
		itemAdapter = new ShareItemAdapter(MainPageLayoutOrderActivity.this,
				MainPageLayoutOrderActivity.this, dataSourceList);

		mListView.setAdapter(itemAdapter);
	}

	/**
	 * 
	 * @tags
	 * @date 2015年5月6日
	 * @todo 同步初始化最初的几条数据
	 * @author pang
	 */
	private void loadDataMore() {
		/**
		 * 判断本地数据库是否已经存储，如果已经存储则取出来显示
		 */
		final DbUtils dbUtils = DbUtils.create(this);
		List<ShareInOrderEntity> lst = ShareSentenceUtil.getDbTopShareByDate(
				df_date, dbUtils);
		if (lst != null && !lst.isEmpty()) {
			System.out.println("-------------从本地数据库去除数据来了");
			List<ShareSentenceEntity> sseList = ShareSentenceUtil
					.convertLocalTopShareToServerShare(lst);
			afterGetOrder(sseList);
			return;
		}
		/**
		 * 如果本地不存在则远程获取，获取完后渲染，并且存到本地
		 */
		try {
			JSONObject d = new JSONObject();
			d.put("currentDate", df_date);

			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					List<ShareSentenceEntity> list = ShareSentenceUtil
							.parseJsonAddToList(responseInfo.result);
					afterGetOrder(list);

					List<ShareInOrderEntity> sioeList = ShareSentenceUtil
							.convertServerShareToLocalTopShare(list, df_date);
					if (sioeList != null && !sioeList.isEmpty()) {
						/**** 存储到本地 ***/
						ShareSentenceUtil.cacheShareTopToDB(df_date, dbUtils,
								sioeList);
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.get_share_order, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @tags @param lst
	 * @date 2015年5月18日
	 * @todo 异步请求获取数据
	 * @author pang
	 */
	public void afterGetOrder(List<ShareSentenceEntity> lst) {

		// 重绘UI
		if (df_date.equals(today)) {
			date_content.setText("今天");
		} else if (df_date.equals(yesterday)) {
			date_content.setText("昨天");
		} else {
			date_content.setText(df_date);
		}
		// 清空旧数据
		dataSourceList.clear();
		// 添加数据
		dataSourceList.addAll(lst);
		// 更新UI
		itemAdapter.notifyDataSetChanged();
		// 刷新
		onLoadOver();
	};

	/**
	 * 
	 * @tags
	 * @todo 刷新完毕或加载完毕
	 * @author pang
	 */
	private void onLoadOver() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚才");
		Toast.makeText(getApplicationContext(),
				"总共" + dataSourceList.size() + "条", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void afterClickContent(String shareId) {
		Intent intent = new Intent(MainPageLayoutOrderActivity.this,
				ShareSentenceAllDetailActivity.class);
		intent.putExtra("share_sentence_id", shareId);
		startActivity(intent);
	}

	@Override
	public void afterClickAuthor(String shareId) {
		Toast.makeText(context, "您要评论的分享作", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRefresh() {
		// loadDataMore();
	}

	@Override
	public void onLoadMore() {
		onLoadOver();
	}

	@Override
	public void afterClickOk(String shareId) {
		itemAdapter.notifyDataSetChanged();
		Intent intent = new Intent(MainPageLayoutOrderActivity.this,
				ViewForInfoService.class);
		intent.putExtra("type", ViewForInfoService.VIEW_ITEM_TYPE_SHARE);
		intent.putExtra("id", shareId);
		intent.putExtra("view", ViewForInfoService.VIEW_VIEW_TYPE_OK);
		startService(intent);

	}

	@Override
	public void afterClickNook(String shareId) {
		itemAdapter.notifyDataSetChanged();
		Intent intent = new Intent(MainPageLayoutOrderActivity.this,
				ViewForInfoService.class);
		intent.putExtra("type", ViewForInfoService.VIEW_ITEM_TYPE_SHARE);
		intent.putExtra("id", shareId);
		intent.putExtra("view", ViewForInfoService.VIEW_VIEW_TYPE_NO);
		startService(intent);
	}

	@Override
	public void afterClickReply(String shareId) {
		commentShareId = shareId;
		showInput();
	}

	/**
	 * 
	 * @tags
	 * @date 2015年5月24日
	 * @todo 弹出评论输入框
	 * @author pang
	 */
	public void showInput() {
		share_bottom.setVisibility(View.VISIBLE);
		et_pop.setFocusable(true);
		et_pop.requestFocus();
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.showSoftInput(et_pop, 0);
	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月24日
	 * @todo 发送评论
	 * @author pang
	 */
	public void comment_share(View v) {

		String comment_str = et_pop.getText().toString();
		if (comment_str == null || "".equals(comment_str.trim())) {
			Toast.makeText(getApplicationContext(), "评论内容不得为空",
					Toast.LENGTH_LONG).show();
			return;
		}

		Intent intent = new Intent(MainPageLayoutOrderActivity.this,
				ShareCommentService.class);
		intent.putExtra("userId", userId);
		intent.putExtra("sentenceId", commentShareId);
		intent.putExtra("content", comment_str);
		startService(intent);
		et_pop.setText("");
		share_bottom.setVisibility(View.GONE);
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(MainPageLayoutOrderActivity.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月26日
	 * @todo 点击输入框的其他部分就可以隐藏评论输入框
	 * @author pang
	 */
	public void closeInput(View v) {
		et_pop.setText("");
		int vi = share_bottom.getVisibility();
		if (vi == View.VISIBLE) {
			share_bottom.setVisibility(View.GONE);
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(MainPageLayoutOrderActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public void backoff(View v) {
		to_home_page();
		finish();
	}

}
