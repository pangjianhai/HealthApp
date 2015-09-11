package cn.com.hzzc.health.pro.topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.adapter.UserItemAdapter;
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.model.UserItem;
import cn.com.hzzc.health.pro.part.XListView;
import cn.com.hzzc.health.pro.part.XListView.IXListViewListener;
import cn.com.hzzc.health.pro.util.UserUtils;

/**
 * @todo 主题下面的用户列表
 * @author pang
 *
 */
public class TopicUserFragment extends BaseTopicFragment implements
		IXListViewListener {

	private View mMainView;
	private XListView topic_post_lv;
	private int currentPage = 0;
	private int rows = 10;
	List<UserItem> ds = new ArrayList<UserItem>();
	private UserItemAdapter adpater;
	private String topicId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(
				R.layout.topic_user_list,
				(ViewGroup) getActivity().findViewById(
						R.id.topic_fragment_parent_viewpager), false);
		topicId = getActivity().getIntent().getStringExtra("topicId");
		findView();
		loadMoreData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup viewGroup = (ViewGroup) mMainView.getParent();
		return mMainView;
	}

	private void findView() {
		topic_post_lv = (XListView) mMainView.findViewById(R.id.space_lv);
		topic_post_lv.setPullRefreshEnable(false);
		topic_post_lv.setPullLoadEnable(false);
		topic_post_lv.setXListViewListener(this);
		adpater = new UserItemAdapter(getActivity(), ds);
		topic_post_lv.setAdapter(adpater);
	}

	public void loadMoreData() {
		try {
			JSONObject d = new JSONObject();
			d.put("currentId", HealthApplication.getUserId());
			d.put("begin", currentPage);
			d.put("limit", rows);
			currentPage = currentPage + 1;
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					List<UserItem> lst = UserUtils.parseJsonAddToList(data);
					ds.addAll(lst);
					adpater.notifyDataSetChanged();
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
					+ SystemConst.FunctionUrl.getFocusMeUser, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void onLoadOver() {
		topic_post_lv.stopRefresh();
		topic_post_lv.stopLoadMore();
		topic_post_lv.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		onLoadOver();
	}

	@Override
	public void onLoadMore() {
		loadMoreData();

	}

	@Override
	public void resetTitleStatus(float v) {
		// TODO Auto-generated method stub

	}

}
