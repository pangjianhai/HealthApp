package cn.com.health.pro;

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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.com.health.pro.abstracts.ParentMainActivity;
import cn.com.health.pro.adapter.ShareItemAdapter;
import cn.com.health.pro.model.ShareSentenceEntity;
import cn.com.health.pro.part.XListView;
import cn.com.health.pro.part.XListView.IXListViewListener;
import cn.com.health.pro.service.ShareCommentService;
import cn.com.health.pro.service.ViewForInfoService;
import cn.com.health.pro.util.CommonDateUtil;
import cn.com.health.pro.util.IShareCallbackOperator;
import cn.com.health.pro.util.ShareSentenceUtil;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 
 * @author pang
 * @todo 首页我的空间
 *
 */
public class MainPageLayoutSpaceActivity extends ParentMainActivity implements
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
	 * 当前页
	 */
	private int begin = 1;
	/**
	 * 一页多少行
	 */
	private int limit = 5;

	/**
	 * 能否继续加载的标志
	 */
	private boolean no_more = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.main_page_layout_space);

		findView();
		/**
		 * 先后顺序
		 */
		initListView();
		loadDataMore();

	}

	private void findView() {
		context = this;
		dataSourceList = new ArrayList<ShareSentenceEntity>();
		mListView = (XListView) findViewById(R.id.space_lv);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		share_bottom = (LinearLayout) findViewById(R.id.share_bottom);
		et_pop = (EditText) findViewById(R.id.tv_pop);
	}

	private void initListView() {
		itemAdapter = new ShareItemAdapter(MainPageLayoutSpaceActivity.this,
				MainPageLayoutSpaceActivity.this, dataSourceList);

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
		try {
			JSONObject d = new JSONObject();
			d.put("currentId", userId);
			d.put("date", search_day);
			d.put("begin", begin);
			d.put("limit", limit);
			begin = begin + 1;

			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					Map<String, Object> map = ShareSentenceUtil
							.parseJsonCondition(responseInfo.result);
					String searchDay = map.get("searchDay") + "";
					String begin = map.get("begin") + "";
					List<ShareSentenceEntity> list = (List<ShareSentenceEntity>) map
							.get("lst");
					/**
					 * 服务器端告诉移动端已经不需要翻页了，没有更多的数据了
					 */
					String nomore = map.get("nomore") + "";
					boolean if_has_nomore_field = true;
					/**
					 * 如果存在nomore提示則不需要继续进行加载
					 */
					if ("nomore".equals(nomore.trim())) {
						if_has_nomore_field = false;
					}
					asyncAddNewData(searchDay, begin, list, if_has_nomore_field);

				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			};
			Map map = new HashMap();
			System.out.println("d.toString():" + d.toString());
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.getFriendsShareByUserId, map, rcb);
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
	public void asyncAddNewData(String searchDay, String begin,
			List<ShareSentenceEntity> lst, boolean if_has_nomore_field) {
		/**
		 * 重置一些字段的值，为下一次分页做准备
		 */
		if (!if_has_nomore_field) {
			// 如果no_more==true则表明，服务器端已经没有更早的消息用来进行分页了
			no_more = if_has_nomore_field;
			Date sd = CommonDateUtil
					.preDate(CommonDateUtil.getDate(search_day));
			this.search_day = CommonDateUtil.formatDate(sd);
			this.begin = 1;
			Toast.makeText(getApplicationContext(), "已经没有数据了",
					Toast.LENGTH_SHORT).show();
		} else {
			this.search_day = searchDay;
			this.begin = Integer.parseInt(begin);
		}

		dataSourceList.addAll(lst);
		itemAdapter.notifyDataSetChanged();
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
	}

	@Override
	public void afterClickContent(String shareId) {
		Intent intent = new Intent(MainPageLayoutSpaceActivity.this,
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
		dataSourceList.clear();
		loadDataMore();
	}

	@Override
	public void onLoadMore() {
		loadDataMore();
	}

	public void add_friends(View v) {
		Intent intent = new Intent(MainPageLayoutSpaceActivity.this,
				FriendSeachOpsActivity.class);
		startActivity(intent);
	}

	@Override
	public void afterClickOk(String shareId) {
		itemAdapter.notifyDataSetChanged();
		Intent intent = new Intent(MainPageLayoutSpaceActivity.this,
				ViewForInfoService.class);
		intent.putExtra("type", ViewForInfoService.VIEW_ITEM_TYPE_SHARE);
		intent.putExtra("id", shareId);
		intent.putExtra("view", ViewForInfoService.VIEW_VIEW_TYPE_OK);
		startService(intent);

	}

	@Override
	public void afterClickNook(String shareId) {
		itemAdapter.notifyDataSetChanged();
		Intent intent = new Intent(MainPageLayoutSpaceActivity.this,
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
		Intent intent = new Intent(MainPageLayoutSpaceActivity.this,
				ShareCommentService.class);
		intent.putExtra("userId", userId);
		intent.putExtra("sentenceId", commentShareId);
		intent.putExtra("content", et_pop.getText().toString());
		startService(intent);
		et_pop.setText("");
		share_bottom.setVisibility(View.GONE);
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(MainPageLayoutSpaceActivity.this
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
					.hideSoftInputFromWindow(MainPageLayoutSpaceActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}
