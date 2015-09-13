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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.ShareSentenceAllDetailActivity;
import cn.com.hzzc.health.pro.ShowUserInfoDetail;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.abstracts.ParentShareSentenceEntity;
import cn.com.hzzc.health.pro.adapter.OrderShareItemAdapter;
import cn.com.hzzc.health.pro.config.ShareConst;
import cn.com.hzzc.health.pro.model.ShareInOrderEntity;
import cn.com.hzzc.health.pro.model.ShareSentenceEntity;
import cn.com.hzzc.health.pro.part.XListView;
import cn.com.hzzc.health.pro.part.XListView.IXListViewListener;
import cn.com.hzzc.health.pro.service.ShareCommentService;
import cn.com.hzzc.health.pro.service.ViewForInfoService;
import cn.com.hzzc.health.pro.util.CommonDateUtil;
import cn.com.hzzc.health.pro.util.IShareCallbackOperator;
import cn.com.hzzc.health.pro.util.ShareSentenceUtil;
import cn.com.hzzc.health.pro.util.UserUtils;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class HomeMainOrderFragment extends ParentFragment implements
		IXListViewListener, IShareCallbackOperator {

	/**
	 * 整体布局view
	 */
	View view = null;

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
	private OrderShareItemAdapter itemAdapter;

	/**
	 * 需要评论的分享信息在队列当中的位置
	 */
	int comment_share_num = -1;

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
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.home_main_order_fragment, null);
		findView();
		/**
		 * 先后顺序
		 */
		initListView();
		loadDataMore();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup viewGroup = (ViewGroup) view.getParent();
		return view;
	}

	/**
	 * @user:pang
	 * @data:2015年6月19日
	 * @todo:初始化组件
	 * @return:void
	 */
	private void findView() {
		context = getActivity();
		dataSourceList = new ArrayList<ShareSentenceEntity>();
		mListView = (XListView) view.findViewById(R.id.space_lv);
		mListView.setPullRefreshEnable(false);
		mListView.setPullLoadEnable(false);
		mListView.setXListViewListener(this);
		share_bottom = (LinearLayout) view.findViewById(R.id.share_bottom);
		et_pop = (EditText) view.findViewById(R.id.tv_pop);

		/**
		 * 选择按钮
		 */
		date_next = (ImageButton) view.findViewById(R.id.date_next);
		date_pre = (ImageButton) view.findViewById(R.id.date_pre);
		date_content = (TextView) view.findViewById(R.id.date_content);
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
		itemAdapter = new OrderShareItemAdapter(getActivity(), this,
				dataSourceList);

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
		final DbUtils dbUtils = DbUtils.create(getActivity());
		List<ShareInOrderEntity> lst = ShareSentenceUtil.getDbTopShareByDate(
				df_date, dbUtils);
		if (lst != null && !lst.isEmpty()) {
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
					onLoadOver();
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
		mListView.setRefreshTime("刚刚");
		Toast.makeText(getActivity(), "总共" + dataSourceList.size() + "条",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void afterClickContent(String shareId, int position) {
		Intent intent = new Intent(getActivity(),
				ShareSentenceAllDetailActivity.class);
		intent.putExtra("share_sentence_id", shareId);
		startActivity(intent);
	}

	@Override
	public void afterClickAuthor(String shareId, int position) {
		ShareSentenceEntity sse = dataSourceList.get(position);
		// entity
		ShareSDK.initSDK(getActivity());
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(ShareConst.share_title);
		// text是分享文本，所有平台都需要这个字段
		String content = sse.getContent();
		// System.out.println("------------------"+content);
		if (content.length() > 100) {
			content = content.substring(0, 90) + "...";
		}
		oks.setText(content);
		// url仅在微信（包括好友和朋友圈）中使用，查看分享信息的详情
		String info_url = SystemConst.server_url
				+ SystemConst.FunctionUrl.weixin_getShareById + "?id="
				+ sse.getId();
		// System.out.println("------------------"+info_url);
		// 朋友圈、微信好友打开的链接
		oks.setUrl(info_url);
		// 人人网和QQ空间点击打开的链接
		oks.setTitleUrl(info_url);
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(info_url);
		String image = sse.getImg0();
		if (image == null || "".equals(image.trim())) {
			image = "";
		}
		String pic_url = SystemConst.server_url
				+ SystemConst.FunctionUrl.weixin_getShareImgById + "?id="
				+ image;
		// System.out.println("------------------"+pic_url);
		// 微信、朋友圈、QQ看到的提示图片
		oks.setImageUrl(pic_url);

		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));

		// 启动分享GUI
		oks.show(getActivity());
	}

	@Override
	public void onRefresh() {
		onLoadOver();
	}

	@Override
	public void onLoadMore() {
		onLoadOver();
	}

	@Override
	public void afterClickOk(String shareId, int position) {

	}

	@Override
	public void afterClickNook(String shareId, int position) {

	}

	@Override
	public void afterClickReply(String shareId, int position) {
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

	@Override
	public void screenScroll(float y) {
		// TODO Auto-generated method stub
		
	}

}
