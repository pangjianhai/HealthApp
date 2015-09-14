package cn.com.hzzc.health.pro.topic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import cn.com.hzzc.health.pro.AppLoginStartActivity;
import cn.com.hzzc.health.pro.AppRegActivity;
import cn.com.hzzc.health.pro.FirstLoginTopUserListLayout;
import cn.com.hzzc.health.pro.FriendSeachOpsActivity;
import cn.com.hzzc.health.pro.MainPageLayoutInfoActivity;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.ShareSentenceAllDetailActivity;
import cn.com.hzzc.health.pro.ShowUserInfoDetail;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.abstracts.ParentShareSentenceEntity;
import cn.com.hzzc.health.pro.adapter.ShareItemAdapter;
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.controller.FragmentShareController;
import cn.com.hzzc.health.pro.model.PushBean;
import cn.com.hzzc.health.pro.model.ShareSentenceEntity;
import cn.com.hzzc.health.pro.part.XListView;
import cn.com.hzzc.health.pro.part.XListView.IXListViewListener;
import cn.com.hzzc.health.pro.service.ShareCommentService;
import cn.com.hzzc.health.pro.service.ViewForInfoService;
import cn.com.hzzc.health.pro.util.CommonDateUtil;
import cn.com.hzzc.health.pro.util.IShareCallbackOperator;
import cn.com.hzzc.health.pro.util.ShareSentenceUtil;
import cn.com.hzzc.health.pro.util.UserUtils;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @todo 健康空间
 * @author pang
 *
 */
public class HomeAllSpaceShareFragment extends ParentFragment implements
		IXListViewListener, IShareCallbackOperator {
	/*** 所属fragment ****/
	private HomeMainPageFragment parent;
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
	private ImageButton share_comment_input_btn;
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

	private View mMainView;

	public HomeAllSpaceShareFragment(HomeMainPageFragment parent) {
		this.parent = parent;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(R.layout.home_all_space_share_fragment,
				(ViewGroup) getActivity().findViewById(R.id.vPager), false);
		findView();
		/**
		 * 先后顺序
		 */
		initListView();
		loadDataMore();
		initListener();
	}

	private void initListener() {
		FragmentShareController fsc = new FragmentShareController(this);
		share_comment_input_btn.setOnClickListener(fsc);
	}

	public void afterListerner(View v) {
		if (v.getId() == R.id.share_comment_input_btn) {
			comment_share(v);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		ViewGroup viewGroup = (ViewGroup) mMainView.getParent();
		return mMainView;
	}

	private void findView() {
		dataSourceList = new ArrayList<ShareSentenceEntity>();
		mListView = (XListView) mMainView.findViewById(R.id.space_lv);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		share_bottom = (LinearLayout) mMainView.findViewById(R.id.share_bottom);
		et_pop = (EditText) mMainView.findViewById(R.id.tv_pop);
		share_comment_input_btn = (ImageButton) mMainView
				.findViewById(R.id.share_comment_input_btn);
	}

	private void initListView() {
		itemAdapter = new ShareItemAdapter(getActivity(), this, dataSourceList);

		mListView.setAdapter(itemAdapter);
	}

	/**
	 * @user:pang
	 * @data:2015年8月13日
	 * @todo:加载数据的入口，无论是否登录都是从此开始加载
	 * @return:void
	 */
	private void loadDataMore() {
		if (HealthApplication.isLogin()) {// 如果已经登录
			loadDataMoreForLogin();
		} else {// 如果没有登录
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
					asyncAddNewDataForLogin(searchDay, b, list,
							if_has_nomore_field);

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
	 * @tags @param lst
	 * @date 2015年5月18日
	 * @todo 异步请求获取数据
	 * @author pang
	 */
	public void asyncAddNewDataForLogin(String searchDay, String begin,
			List<ShareSentenceEntity> lst, boolean if_has_nomore_field) {
		/**
		 * 重置一些字段的值，为下一次分页做准备 重置日期，获取前一日期的数据
		 */
		if (!if_has_nomore_field) {// 如果no_more==false则表明，服务器端已经没有更早的消息用来进行分页了
			no_more = if_has_nomore_field;
			Date sd = CommonDateUtil
					.preDate(CommonDateUtil.getDate(search_day));
			this.search_day = CommonDateUtil.formatDate(sd);
			this.begin = 1;
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
					onLoadOver();
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
		/******** 刷新加载在数据源的头部添加了数据，需要更新最新的ID，以便再次刷新 *********/
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
		} else {
			lastestShareId = null;
		}
	}

	/**
	 * 
	 * @tags
	 * @todo 刷新完毕或加载完毕，及时取数据网络出错也需要继续调用这个方法
	 * @author pang
	 */
	private void onLoadOver() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
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
			Intent intent = new Intent(getActivity(),
					FriendSeachOpsActivity.class);
			startActivity(intent);
		} else {

			no_login_alter(v);
		}
	}

	/**************************** 信息交互操作begin ***********************************/
	@Override
	public void afterClickAuthor(String shareId, int position) {
		if (!isLogin()) {
			return;
		}
		showUserByShareId(shareId);
	}

	@Override
	public void afterClickContent(String shareId, int position) {
		Intent intent = new Intent(getActivity(),
				ShareSentenceAllDetailActivity.class);
		intent.putExtra("share_sentence_id", shareId);
		startActivity(intent);
	}

	@Override
	public void afterClickNook(String shareId, int position) {
		if (!isLogin()) {
			return;
		}
		likeOrDislikeShare(false, position, shareId);
	}

	@Override
	public void afterClickOk(String shareId, int position) {
		if (!isLogin()) {
			return;
		}
		likeOrDislikeShare(true, position, shareId);

	}

	/**
	 * 
	 * @param like
	 * @param position
	 * @param shareId
	 * @user:pang
	 * @data:2015年8月16日
	 * @todo:顶还是踩
	 * @return:void
	 */
	private void likeOrDislikeShare(boolean like, int position, String shareId) {
		ShareSentenceEntity entity = dataSourceList.get(position);
		if (like) {
			entity.setOps(ParentShareSentenceEntity.OK);
			entity.setGoodNum((Integer.parseInt(entity.getGoodNum()) + 1) + "");
			itemAdapter.notifyDataSetChanged();
			Intent intent = new Intent(getActivity(), ViewForInfoService.class);
			intent.putExtra("type", ViewForInfoService.VIEW_ITEM_TYPE_SHARE);
			intent.putExtra("id", shareId);
			intent.putExtra("view", ViewForInfoService.VIEW_VIEW_TYPE_OK);
			getActivity().startService(intent);
		} else {
			entity.setOps(ParentShareSentenceEntity.NO_OK);
			entity.setBadNum((Integer.parseInt(entity.getBadNum()) + 1) + "");
			itemAdapter.notifyDataSetChanged();
			Intent intent = new Intent(getActivity(), ViewForInfoService.class);
			intent.putExtra("type", ViewForInfoService.VIEW_ITEM_TYPE_SHARE);
			intent.putExtra("id", shareId);
			intent.putExtra("view", ViewForInfoService.VIEW_VIEW_TYPE_NO);
			getActivity().startService(intent);
		}

	}

	/**
	 * 需要评论的分享信息在队列当中的位置
	 */
	int comment_share_num = -1;

	@Override
	public void afterClickReply(String shareId, int position) {
		if (!isLogin()) {
			return;
		}
		commentShareId = shareId;
		comment_share_num = position;
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
		((InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE)).showSoftInput(et_pop, 0);
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
			Toast.makeText(getActivity(), "评论内容不得为空", Toast.LENGTH_LONG).show();
			return;
		}
		/** 关于刷新页面begin **/
		ShareSentenceEntity sse = dataSourceList.get(comment_share_num);
		String oldCommentNum = sse.getCommentNum();
		int newCommentNum = Integer.parseInt(oldCommentNum) + 1;
		sse.setCommentNum(newCommentNum + "");
		itemAdapter.notifyDataSetChanged();
		comment_share_num = -1;
		/** 关于刷新页面end **/
		Intent intent = new Intent(getActivity(), ShareCommentService.class);
		intent.putExtra("userId", userId);
		intent.putExtra("sentenceId", commentShareId);
		intent.putExtra("content", comment_str);
		getActivity().startService(intent);
		et_pop.setText("");
		share_bottom.setVisibility(View.GONE);
		((InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				getActivity().getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * @param v
	 * @user:pang
	 * @data:2015年6月22日
	 * @todo:查看信息库
	 * @return:void
	 */
	public void to_info_rep(View v) {
		Intent intent = new Intent(getActivity(),
				MainPageLayoutInfoActivity.class);
		startActivity(intent);
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
					if (pb != null && pb.getLoginTimes() <= 1) {
						Intent intent = new Intent(getActivity(),
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

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * 
	 * @param shareId
	 * @user:pang
	 * @data:2015年7月21日
	 * @todo:根据分享信息查看人的信息
	 * @return:void
	 */
	private void showUserByShareId(String shareId) {
		try {
			JSONObject d = new JSONObject();
			d.put("shareId", shareId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					String userId = UserUtils.parseUserId(data);
					if (userId != null && !"".equals(userId)) {
						Intent intent = new Intent(getActivity(),
								ShowUserInfoDetail.class);
						intent.putExtra("uuid", userId);
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
					+ SystemConst.FunctionUrl.getUserIdByShareId, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// *******************************************************************************************
	public void no_login_alter(View v) {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View noLoginAlter = inflater.inflate(R.layout.app_nologin_alter,
				null, false);
		final PopupWindow popWindow = new PopupWindow(noLoginAlter, 400,
				WindowManager.LayoutParams.WRAP_CONTENT, true);
		ImageView close_nologin_alert_image = (ImageView) noLoginAlter
				.findViewById(R.id.close_nologin_alert_image);
		close_nologin_alert_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popWindow.dismiss();
			}
		});

		Button alert_login = (Button) noLoginAlter
				.findViewById(R.id.alert_login);
		alert_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popWindow.dismiss();
				Intent intent = new Intent();
				intent.setClass(getActivity(), AppLoginStartActivity.class);
				startActivity(intent);
			}

		});
		Button alert_reg = (Button) noLoginAlter.findViewById(R.id.alert_reg);
		alert_reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popWindow.dismiss();
				Intent intent = new Intent();
				intent.setClass(getActivity(), AppRegActivity.class);
				startActivity(intent);
			}

		});
		popWindow.setFocusable(true);
		backgroundAlpha(0.7f);
		popWindow.setOnDismissListener(new PoponDismissListener());
		popWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
	}

	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getActivity().getWindow()
				.getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getActivity().getWindow().setAttributes(lp);
	}

	class PoponDismissListener implements PopupWindow.OnDismissListener {

		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}

	}

	/**************************** 信息交互操作end ***********************************/

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
			((InputMethodManager) getActivity().getSystemService(
					Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					getActivity().getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public void screenScroll(float y) {
		parent.screenScroll(y);
	}

}