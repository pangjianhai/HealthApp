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
import android.widget.TextView;
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
 * @todo 主题空间
 * @author pang
 *
 */
public class HomeAllSpaceTopicFragment extends ParentFragment implements
		IXListViewListener, ITopicCallbackOperator {
	/*** 所属fragment ****/
	private HomeMainPageFragment parent;
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

	private TextView space_notice_msg;

	public HomeAllSpaceTopicFragment(HomeMainPageFragment parent) {
		this.parent = parent;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(R.layout.home_all_space_topic_fragment,
				null, false);
		single_push_bottom_ops_sc = (Button) mMainView
				.findViewById(R.id.single_push_bottom_ops_sc);
		space_notice_msg = (TextView) mMainView
				.findViewById(R.id.space_notice_msg);
		findView();
		initListView();
		// loadDataMore();
		single_push_bottom_ops_sc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toPage();
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
		mListView.setPullRefreshEnable(false);
		mListView.setPullLoadEnable(false);
		mListView.setXListViewListener(this);
	}

	private void initListView() {
		topicItemAdapter = new TopicItemAdapter(getActivity(), this,
				dataSourceList);
		mListView.setAdapter(topicItemAdapter);
		mListView.hideFooter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup viewGroup = (ViewGroup) mMainView.getParent();
		return mMainView;
	}

	private void realLoadData() {
		try {
			dataSourceList.clear();
			JSONObject d = new JSONObject();
			d.put("userId", userId + "");
			String url = SystemConst.server_url
					+ SystemConst.TopicUrl.getTopicForParticipation;
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					List<TopicEntity> lst = TopicUtil.parseJsonAddToList(data);
					if (lst == null || lst.isEmpty()) {
						space_notice_msg.setVisibility(View.VISIBLE);
						mListView.setVisibility(View.GONE);
					} else {
						dataSourceList.addAll(lst);
						topicItemAdapter.notifyDataSetChanged();
					}
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
	public void onRefresh() {
		onLoadOver();
	}

	@Override
	public void onLoadMore() {
		onLoadOver();
	}

	@Override
	public void afterClickTopic(String topicId, int index) {
		Intent intent = new Intent(getActivity(), ShowTopicActivity.class);
		intent.putExtra("topicId", topicId);
		getActivity().startActivity(intent);
	}

	public void toPage() {
		Intent intent = new Intent(getActivity(),
				TopicListUnenteredActivity.class);
		getActivity().startActivity(intent);
	}

	@Override
	public void onStart() {
		super.onStart();
		loadDataMore();
	}

	@Override
	public void screenScroll(float y) {
		parent.screenScroll(y);
	}

}
