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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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
	private Button single_push_bottom_ops_sc;

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
		single_push_bottom_ops_sc = (Button) mMainView.findViewById(R.id.single_push_bottom_ops_sc);
		findView();
		initListView();
		loadDataMore();
		single_push_bottom_ops_sc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("======================>>>>>>>>>>>>>>>");
			}
		});
	}

	private Button findViewById(int singlePushBottomOpsSc) {
		// TODO Auto-generated method stub
		return null;
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
			JSONObject d = new JSONObject();
			d.put("page", currentPage + "");
			d.put("rows", 10);
			currentPage = currentPage + 1;
			String url = SystemConst.server_url
					+ SystemConst.TopicUrl.get_page_topic;
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
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
