package cn.com.hzzc.health.pro.topic;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.adapter.TopicItemAdapter;
import cn.com.hzzc.health.pro.model.TopicEntity;
import cn.com.hzzc.health.pro.part.XListView;
import cn.com.hzzc.health.pro.part.XListView.IXListViewListener;
import cn.com.hzzc.health.pro.util.IShareCallbackOperator;

public class TopicSpaceFragment extends BaseFragment implements
		IXListViewListener, IShareCallbackOperator {
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

	@Override
	public void resetTitleStatus(float v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterClickReply(String shareId, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterClickContent(String shareId, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterClickAuthor(String shareId, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterClickOk(String shareId, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterClickNook(String shareId, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}
}
