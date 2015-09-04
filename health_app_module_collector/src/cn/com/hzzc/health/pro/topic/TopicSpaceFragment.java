package cn.com.hzzc.health.pro.topic;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.com.hzzc.health.pro.MainPageLayoutSpaceActivity;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.adapter.TopicItemAdapter;
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.model.TopicEntity;
import cn.com.hzzc.health.pro.part.XListView;
import cn.com.hzzc.health.pro.part.XListView.IXListViewListener;
import cn.com.hzzc.health.pro.util.ITopicCallbackOperator;

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
		for (int i = 0; i < 10; i++) {
			TopicEntity te = new TopicEntity();
			te.setName("topic" + i);
			te.setPostNum(4);
			te.setUserNum(3);
			te.setDesc("XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			dataSourceList.add(te);

		}
		topicItemAdapter.notifyDataSetChanged();
		onLoadOver();
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
		System.out.println("------------------afterClickTopic");
		Intent intent = new Intent(getActivity(), ShowTopicActivity.class);
		getActivity().startActivity(intent);
	}
}
