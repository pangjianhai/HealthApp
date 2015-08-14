package cn.com.hzzc.health.pro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.com.hzzc.health.pro.adapter.ShareItemAdapter;
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.model.ShareSentenceEntity;
import cn.com.hzzc.health.pro.part.XListView;
import cn.com.hzzc.health.pro.part.XListView.IXListViewListener;
import cn.com.hzzc.health.pro.service.ShareCommentService;
import cn.com.hzzc.health.pro.util.IShareCallbackOperator;
import cn.com.hzzc.health.pro.util.ShareSentenceUtil;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 
 * @author pang
 * @todo 根据标签获取分享信息activity
 *
 */
public class ShareByTagActivity extends BaseActivity implements
		IXListViewListener, IShareCallbackOperator {

	private String tagId = "";
	/**
	 * 适配器需要的数据结构
	 */
	private List<ShareSentenceEntity> dataSourceList = new ArrayList<ShareSentenceEntity>();
	/**
	 * 上下文环境
	 */
	private Context context;
	private LinearLayout share_bottom;
	private String commentShareId;

	private PopupWindow pw;
	private RelativeLayout rl;
	private EditText et_pop;
	/**
	 * 空间分享信息适配器
	 */
	private ShareItemAdapter itemAdapter;
	/**
	 * 空间分享信息列表
	 */
	private XListView mListView;
	/**
	 * 当前页
	 */
	private int currentPage = 0;
	/**
	 * 一页多少行
	 */
	private int pageSize = SystemConst.page_size;

	private String userId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.share_by_tag);
		userId = HealthApplication.getUserId();
		tagId = getIntent().getStringExtra("tagId");
		findView();
		/**
		 * 先后顺序
		 */
		initListView();
		loadDataMore();
	}

	public void showInput() {
		share_bottom.setVisibility(View.VISIBLE);
		et_pop.setFocusable(true);
		et_pop.requestFocus();
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.showSoftInput(et_pop, 0);
	}

	private void findView() {
		context = this;
		dataSourceList = new ArrayList<ShareSentenceEntity>();
		mListView = (XListView) findViewById(R.id.lv);
		share_bottom = (LinearLayout) findViewById(R.id.share_bottom);
		rl = (RelativeLayout) findViewById(R.id.rl);
		et_pop = (EditText) findViewById(R.id.tv_pop);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);

	}

	private void addPop() {
		// TODO Auto-generated method stub
		pw = new PopupWindow(context);
		pw.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		pw.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		pw.setContentView(View.inflate(context, R.layout.share_space_bottom,
				null));
		pw.setFocusable(true);
		pw.setOutsideTouchable(true);
		pw.showAtLocation(rl, Gravity.BOTTOM, 0, 0);
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
			currentPage = currentPage + 1;
			JSONObject d = new JSONObject();
			d.put("tagId", tagId);
			d.put("begin", currentPage + "");
			d.put("limit", 4);

			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					List<ShareSentenceEntity> lst = ShareSentenceUtil
							.parseJsonAddToList(responseInfo.result);
					asyncAddNewData(lst);
				}

				@Override
				public void onFailure(HttpException error, String msg) {
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.get_share_by_tag, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void asyncAddNewData(List<ShareSentenceEntity> lst) {
		dataSourceList.addAll(lst);
		itemAdapter.notifyDataSetChanged();
		onLoadOver();
	};

	private void initListView() {
		itemAdapter = new ShareItemAdapter(ShareByTagActivity.this,
				ShareByTagActivity.this, dataSourceList);

		mListView.setAdapter(itemAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * 刷新数据
	 */
	@Override
	public void onRefresh() {
		dataSourceList.clear();
		loadDataMore();

	}

	/**
	 * 加载数据
	 */
	@Override
	public void onLoadMore() {
		loadDataMore();

	}

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
	public void afterClickReply(String shareId, int position) {
		commentShareId = shareId;
		showInput();
	}

	@Override
	public void afterClickContent(String shareId, int position) {
		Intent intent = new Intent(ShareByTagActivity.this,
				ShareSentenceAllDetailActivity.class);
		intent.putExtra("share_sentence_id", shareId);
		startActivity(intent);
	}

	@Override
	public void afterClickAuthor(String shareId, int position) {
		Toast.makeText(context, "您要评论的分享作", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void afterClickOk(String shareId, int position) {
		itemAdapter.notifyDataSetChanged();
	}

	@Override
	public void afterClickNook(String shareId, int position) {
		itemAdapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月24日
	 * @todo 发送评论
	 * @author pang
	 */
	public void comment_share(View v) {
		Intent intent = new Intent(ShareByTagActivity.this,
				ShareCommentService.class);
		intent.putExtra("userId", userId);
		intent.putExtra("sentenceId", commentShareId);
		intent.putExtra("content", et_pop.getText().toString());
		startService(intent);
		et_pop.setText("");
		share_bottom.setVisibility(View.GONE);
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(ShareByTagActivity.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public void closeInput(View v) {
		et_pop.setText("");
		int vi = share_bottom.getVisibility();
		if (vi == View.VISIBLE) {
			share_bottom.setVisibility(View.GONE);
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(ShareByTagActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public void backoff(View v) {
		finish();
	}
}
