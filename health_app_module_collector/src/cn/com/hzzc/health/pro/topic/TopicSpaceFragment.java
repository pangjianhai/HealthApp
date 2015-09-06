package cn.com.hzzc.health.pro.topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class TopicSpaceFragment extends BaseFragment implements
		IXListViewListener, ITopicCallbackOperator {
	private View mMainView;

	/**
	 * 适配器需要的数据结构
	 */
	private List<TopicEntity> dataSourceList = new ArrayList<TopicEntity>();

	/**
	 * 空间分享信息列表
	 */
	private XListView mListView;
	private TopicItemAdapter topicItemAdapter;
	private int currentPage = 1;
	private int pageRows = 20;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(
				R.layout.home_fragment_topic,
				(ViewGroup) getActivity().findViewById(
						R.id.home_fragment_parent_viewpager), false);
		findView();
		initListView();
		loadDataMore();
	}

	private void findView() {
		dataSourceList = new ArrayList<TopicEntity>();
		mListView = (XListView) mMainView.findViewById(R.id.space_lv);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
	}

	private void initListView() {
		topicItemAdapter = new TopicItemAdapter(getActivity(), this,
				dataSourceList);
		mListView.setAdapter(topicItemAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup viewGroup = (ViewGroup) mMainView.getParent();
		return mMainView;
	}

	private void realLoadData() {
		try {
			currentPage = currentPage + 1;
			JSONObject d = new JSONObject();
			d.put("page", currentPage + "");
			d.put("rows", 10);
			currentPage = currentPage + 1;
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					System.out.println("-------------------datga:" + data);
					List<TopicEntity> lst = TopicUtil.parseJsonAddToList(data);
					dataSourceList.addAll(lst);
					topicItemAdapter.notifyDataSetChanged();
					onLoadOver();
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					onLoadOver();
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.TopicUrl.get_page_topic, map, rcb);
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
	public void resetTitleStatus(float v) {
		if (v > 0) {
			((HomeFrameActivity) getActivity()).showTitle();
		} else {
			((HomeFrameActivity) getActivity()).hideTitle();
		}

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
		Intent intent = new Intent(getActivity(), ShowTopicActivity.class);
		intent.putExtra("topicId", topicId);
		getActivity().startActivity(intent);
	}
}
