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
import cn.com.health.pro.config.GlobalUserVariable;
import cn.com.health.pro.model.PushBean;
import cn.com.health.pro.model.ShareSentenceEntity;
import cn.com.health.pro.part.XListView;
import cn.com.health.pro.part.XListView.IXListViewListener;
import cn.com.health.pro.service.ShareCommentService;
import cn.com.health.pro.service.ViewForInfoService;
import cn.com.health.pro.util.CommonDateUtil;
import cn.com.health.pro.util.IShareCallbackOperator;
import cn.com.health.pro.util.ShareSentenceUtil;
import cn.com.health.pro.util.UserUtils;

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
	 * 最新ID
	 */
	private String lastestShareId;

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
	 * 登录用户需要的分页参数
	 */
	private String search_day = CommonDateUtil.formatDate(CommonDateUtil
			.getNowTimeDate());// 查询的日期
	private int begin = 0;// 当前页
	private int limit = 5;// 一页多少行
	private boolean no_more = true;// 能否继续加载的标志

	/**
	 * 非登录用户需要的分页参数
	 */
	private String n_search_day = CommonDateUtil.formatDate(CommonDateUtil
			.getNowTimeDate());// 查询的日期
	private boolean n_more = true;// 能否继续加载的标志

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

	private void loadDataMore() {
		if (isLogin()) {
			loadDataMoreForLogin();
		} else {
			loadDataMoreForNoLogin();
		}
	}

	/**
	 * 
	 * @tags
	 * @date 2015年5月6日
	 * @todo 同步初始化最初的几条数据
	 * @author pang
	 */
	private void loadDataMoreForLogin() {
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
					String searchDay = map.get("searchDay") + "";// 可以继续搜索的日期
					String b = map.get("begin") + "";// 可以继续搜索的页数
					List<ShareSentenceEntity> list = (List<ShareSentenceEntity>) map
							.get("lst");// 当前页的内容
					/**
					 * 服务器端告诉移动端已经不需要翻页了，没有更多的数据了
					 */
					String nomore = map.get("nomore") + "";// 是否当前日期能继续再取
					boolean if_has_nomore_field = true;
					/**
					 * 如果存在nomore提示則不需要继续进行加载
					 */
					if ("nomore".equals(nomore.trim())) {
						if_has_nomore_field = false;
					}
					/**
					 * 异步请求数据
					 */
					asyncAddNewData(searchDay, b, list, if_has_nomore_field);

				}

				@Override
				public void onFailure(HttpException error, String msg) {
					/**
					 * 出错之后直接停止加载
					 */
					onLoadOver();
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.getFriendsShareByUserId, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年7月21日
	 * @todo:TODO
	 * @return:void
	 */
	private void loadDataMoreForNoLogin() {
		if (!n_more) {
			onLoadOver();
			return;
		}
		try {
			JSONObject d = new JSONObject();
			d.put("searchDay", n_search_day);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					Map<String, Object> map = ShareSentenceUtil
							.parseJsonConditionForNoLogin(responseInfo.result);
					/**
					 * 服务器端告诉移动端已经不需要翻页了，没有更多的数据了
					 */
					String nomore = map.get("nomore") + "";// 是否当前日期能继续再取
					if ("nomore".equals(nomore.trim())) {
						n_more = false;// 不可以继续加载
						onLoadOver();
					} else {
						String searchDay = map.get("searchDay") + "";// 可以继续搜索的日期
						List<ShareSentenceEntity> list = (List<ShareSentenceEntity>) map
								.get("lst");// 当前页的内容
						asyncAddNewDataForNoLogin(searchDay, list);

					}

				}

				@Override
				public void onFailure(HttpException error, String msg) {
					/**
					 * 出错之后直接停止加载
					 */
					onLoadOver();
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.noLoginReadSpace, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void asyncAddNewDataForNoLogin(String searchDay,
			List<ShareSentenceEntity> lst) {
		// n_search_day
		Date sd = CommonDateUtil.getDate(searchDay);
		Date pre_sd = CommonDateUtil.preDate(sd);
		n_search_day = CommonDateUtil.formatDate(pre_sd);
		// 添加数据
		dataSourceList.addAll(lst);
		// 设置最新的ID
		setLastestShareId();
		// 更新UI
		itemAdapter.notifyDataSetChanged();
		onLoadOver();
	};

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月15日
	 * @todo:刷新最新数据
	 * @return:void
	 */
	private void freshData() {
		/**
		 * 如果未登录直接返回
		 */
		if (!isLogin()) {
			onLoadOver();
			return;
		}
		/**
		 * 如果登陆了则继续
		 */
		try {
			JSONObject d = new JSONObject();
			d.put("currentId", userId);
			d.put("currentSentenceId", lastestShareId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					List<ShareSentenceEntity> list = ShareSentenceUtil
							.parseJsonAddToList(responseInfo.result);
					if (list != null && !list.isEmpty()) {
						freshData(list);
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.get_refrish_share_sentencs, map,
					rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param list
	 * @user:pang
	 * @data:2015年7月21日
	 * @todo:登录用户刷新数据
	 * @return:void
	 */
	private void freshData(List<ShareSentenceEntity> list) {
		dataSourceList.addAll(0, list);
		setLastestShareId();
		itemAdapter.notifyDataSetChanged();
		onLoadOver();
	}

	/**
	 * 
	 * @param id
	 * @user:pang
	 * @data:2015年6月15日
	 * @todo:用户取得第一页数据的时候将第一页的第一个分享新的ID单独设置，以供刷新
	 * 
	 *                                           加载的时候和刷新的时候都需要设置最新的ID
	 * @return:void
	 */
	private void setLastestShareId() {
		/**
		 * 设置最新ID的时机
		 */
		if (dataSourceList != null && !dataSourceList.isEmpty()) {//
			lastestShareId = dataSourceList.get(0).getId();
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
		 * 重置一些字段的值，为下一次分页做准备 重置日期，获取前一日期的数据
		 */
		if (!if_has_nomore_field) {// 如果no_more==true则表明，服务器端已经没有更早的消息用来进行分页了
			no_more = if_has_nomore_field;
			Date sd = CommonDateUtil
					.preDate(CommonDateUtil.getDate(search_day));
			this.search_day = CommonDateUtil.formatDate(sd);
			this.begin = 1;
			Toast.makeText(getApplicationContext(), "已经没有数据了",
					Toast.LENGTH_SHORT).show();
		} else {
			/**
			 * 如果当天没有下一页了，后台会返回需要查询的下一天的日期和页数（肯定是第一页“1”）
			 */
			this.search_day = searchDay;
			this.begin = Integer.parseInt(begin);
		}
		// 添加数据
		dataSourceList.addAll(lst);
		// 设置最新的ID
		setLastestShareId();
		// 更新UI
		itemAdapter.notifyDataSetChanged();
		onLoadOver();
	};

	/**
	 * 
	 * @tags
	 * @todo 刷新完毕或加载完毕，及时取数据网络出错也需要继续调用这个方法
	 * @author pang
	 */
	private void onLoadOver() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚才");
	}

	@Override
	public void onRefresh() {
		freshData();
	}

	@Override
	public void onLoadMore() {
		loadDataMore();
	}

	/**
	 * @param v
	 * @user:pang
	 * @data:2015年7月20日
	 * @todo:搜索并添加好友
	 * @return:void
	 */
	public void add_friends(View v) {
		if (isLogin()) {// 只有登陆用户才能由此操作
			Intent intent = new Intent(MainPageLayoutSpaceActivity.this,
					FriendSeachOpsActivity.class);
			startActivity(intent);
		} else {
			no_login_alter(v);
		}
	}

	/**************************** 信息交互操作begin ***********************************/
	@Override
	public void afterClickAuthor(String shareId) {
		if (!isLogin()) {
			return;
		}
		Toast.makeText(context, "您要评论的分享作", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void afterClickOk(String shareId) {
		if (!isLogin()) {
			return;
		}
		itemAdapter.notifyDataSetChanged();
		Intent intent = new Intent(MainPageLayoutSpaceActivity.this,
				ViewForInfoService.class);
		intent.putExtra("type", ViewForInfoService.VIEW_ITEM_TYPE_SHARE);
		intent.putExtra("id", shareId);
		intent.putExtra("view", ViewForInfoService.VIEW_VIEW_TYPE_OK);
		startService(intent);

	}

	@Override
	public void afterClickContent(String shareId) {
		if (!isLogin()) {
			return;
		}
		Intent intent = new Intent(MainPageLayoutSpaceActivity.this,
				ShareSentenceAllDetailActivity.class);
		intent.putExtra("share_sentence_id", shareId);
		startActivity(intent);
	}

	@Override
	public void afterClickNook(String shareId) {
		if (!isLogin()) {
			return;
		}
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
		if (!isLogin()) {
			return;
		}
		commentShareId = shareId;
		showInput();
	}

	/**************************** 信息交互操作end ***********************************/
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

	/**
	 * @param v
	 * @user:pang
	 * @data:2015年6月22日
	 * @todo:查看信息库
	 * @return:void
	 */
	public void to_info_rep(View v) {
		Intent intent = new Intent(MainPageLayoutSpaceActivity.this,
				MainPageLayoutInfoActivity.class);
		startActivity(intent);
	}

	/**
	 * 主要用来应对如果当前用户是第一次登陆系统则推荐好友
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (GlobalUserVariable.if_need_to_push_top_user) {// 说明起码在一次登录周期内没有推荐过
			GlobalUserVariable.setIf_need_to_push_top_user(false);// 置为不需要推荐
			if (isLogin()) {
				if_need_to_push_top_user();
			}
		}

	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年7月13日
	 * @todo:判断有没有必要推荐用户
	 * @return:void
	 */
	private void if_need_to_push_top_user() {
		try {
			JSONObject d = new JSONObject();
			d.put("userUuid", userId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					PushBean pb = UserUtils.parseJsonAddToPushBean(data);
					if (pb.getLoginTimes() <= 1) {
						Intent intent = new Intent(
								MainPageLayoutSpaceActivity.this,
								FirstLoginTopUserListLayout.class);
						startActivity(intent);
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.if_need_to_push_top_user, map,
					rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
