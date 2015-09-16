package cn.com.hzzc.health.pro.topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import cn.com.hzzc.health.pro.BaseActivity;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.adapter.TopicItemAdapter;
import cn.com.hzzc.health.pro.model.TopicEntity;
import cn.com.hzzc.health.pro.part.XListView;
import cn.com.hzzc.health.pro.part.XListView.IXListViewListener;
import cn.com.hzzc.health.pro.util.ITopicCallbackOperator;
import cn.com.hzzc.health.pro.util.TopicUtil;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @todo 未参与话题一览
 * @author pang
 *
 */
public class TopicListUnenteredActivity extends BaseActivity implements
		IXListViewListener, ITopicCallbackOperator {

	private List<TopicEntity> dataSourceList = new ArrayList<TopicEntity>();

	/**
	 * 空间分享信息列表
	 */
	private XListView mListView;
	private TopicItemAdapter topicItemAdapter;
	private int currentPage = 1;
	private int pageRows = 10;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.topic_list_unentered);
		findView();
		initListView();
		loadDataMore();
	}

	private void findView() {
		dataSourceList = new ArrayList<TopicEntity>();
		mListView = (XListView) findViewById(R.id.topic_lv);
		mListView.setPullRefreshEnable(false);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
	}

	private void initListView() {
		topicItemAdapter = new TopicItemAdapter(
				TopicListUnenteredActivity.this,
				TopicListUnenteredActivity.this, dataSourceList);
		mListView.setAdapter(topicItemAdapter);
	}

	private void realLoadData() {
		try {
			JSONObject d = new JSONObject();
			d.put("name", "测试");
			d.put("page", currentPage);
			d.put("rows", pageRows);
			currentPage = currentPage + 1;
			String url = SystemConst.server_url
					+ SystemConst.TopicUrl.get_page_topic;
			System.out.println("url:" + url);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					List<TopicEntity> lst = TopicUtil.parseJsonAddToList(data);
					dataSourceList.addAll(lst);
					topicItemAdapter.notifyDataSetChanged();
					/****** 如果返回的集合为空或者数目小鱼需要取得行数则不需要再加载******/
					if (lst == null || lst.size() < pageRows) {
						mListView.setPullLoadEnable(false);
					}
					onLoadOver();
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					error.printStackTrace();
					onLoadOver();
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(url, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void loadDataMore() {
		realLoadData();
	}

	private void onLoadOver() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		onLoadOver();
	}

	@Override
	public void onLoadMore() {
		loadDataMore();
	}

	@Override
	public void afterClickTopic(String topicId, int index) {
		Intent intent = new Intent(TopicListUnenteredActivity.this,
				ShowTopicDetailActivity.class);
		intent.putExtra("topicId", topicId);
		startActivity(intent);
	}

	public void backoff(View v) {
		this.finish();
	}
}
