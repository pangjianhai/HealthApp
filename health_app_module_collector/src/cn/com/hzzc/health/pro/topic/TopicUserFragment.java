package cn.com.hzzc.health.pro.topic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.part.XListView.IXListViewListener;

public class TopicUserFragment extends BaseTopicFragment implements
		IXListViewListener {

	private View mMainView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(
				R.layout.topic_user_list,
				(ViewGroup) getActivity().findViewById(
						R.id.topic_fragment_parent_viewpager), false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup viewGroup = (ViewGroup) mMainView.getParent();
		return mMainView;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetTitleStatus(float v) {
		// TODO Auto-generated method stub

	}

}
