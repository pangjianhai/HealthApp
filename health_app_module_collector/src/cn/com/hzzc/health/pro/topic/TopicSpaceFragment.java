package cn.com.hzzc.health.pro.topic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.com.hzzc.health.pro.R;

public class TopicSpaceFragment extends BaseFragment {
	private View mMainView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("xlp", "fragment1-->oncreate()");
		// 动态加载布局文件
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(
				R.layout.home_fragment_topic,
				(ViewGroup) getActivity().findViewById(
						R.id.home_fragment_parent_viewpager), false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("xlp", "fragment1-->onCreateView()");
		ViewGroup viewGroup = (ViewGroup) mMainView.getParent();
		return mMainView;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v("xlp", "fragment1-->onDestroy()");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v("xlp", "fragment1-->onPause()");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("xlp", "fragment1-->onResume()");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.v("xlp", "fragment1-->onStart()");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.v("xlp", "fragment1-->onStop()");
	}

	@Override
	public void resetTitleStatus(float v) {
		// TODO Auto-generated method stub

	}
}
