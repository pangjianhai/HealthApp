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
import android.widget.TextView;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.ShowUserInfoDetail;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.adapter.TopicPostItemAdapter;
import cn.com.hzzc.health.pro.model.TopicPostEntity;
import cn.com.hzzc.health.pro.part.XListView;
import cn.com.hzzc.health.pro.part.XListView.IXListViewListener;
import cn.com.hzzc.health.pro.util.ITopicCommentListener;
import cn.com.hzzc.health.pro.util.TopicUtil;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @todo 主题下面的帖子列表
 * @author pang
 *
 */
public class TopicPostFragment extends BaseTopicFragment implements
		IXListViewListener, ITopicCommentListener {

	private TextView topic_post_no_post_notice;
	private View mMainView;
	private XListView topic_post_lv;
	private int currentPage = 1;
	private int rows = 10;
	List<TopicPostEntity> ds = new ArrayList<TopicPostEntity>();
	private TopicPostItemAdapter adpater = null;
	private String topicId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(
				R.layout.topic_post_list,
				(ViewGroup) getActivity().findViewById(
						R.id.topic_fragment_parent_viewpager), false);
		topicId = getActivity().getIntent().getStringExtra("topicId");
		findView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup viewGroup = (ViewGroup) mMainView.getParent();
		return mMainView;
	}

	private void findView() {
		topic_post_no_post_notice = (TextView) mMainView
				.findViewById(R.id.topic_post_no_post_notice);
		topic_post_lv = (XListView) mMainView.findViewById(R.id.space_lv);
		topic_post_lv.setPullRefreshEnable(false);
		topic_post_lv.setPullLoadEnable(true);
		topic_post_lv.setXListViewListener(this);
		adpater = new TopicPostItemAdapter(getActivity(),
				TopicPostFragment.this, ds);
		topic_post_lv.setAdapter(adpater);
		realLoadData();
	}

	/**
	 * @user:pang
	 * @data:2015年9月8日
	 * @todo:真正的获取数据
	 * @return:void
	 */
	private void realLoadData() {
		try {
			JSONObject d = new JSONObject();
			d.put("picId", topicId);
			d.put("page", currentPage + "");
			d.put("rows", rows);
			d.put("userId", userId);
			currentPage = currentPage + 1;
			String url = SystemConst.server_url
					+ SystemConst.TopicUrl.getCommentByTopic;
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					List<TopicPostEntity> lst = TopicUtil
							.parsePostsFromJson(data);
					if (lst == null || lst.isEmpty()) {// 没有发帖
						if (ds == null || ds.isEmpty()) {
							topic_post_no_post_notice
									.setVisibility(View.VISIBLE);
							topic_post_lv.setVisibility(View.GONE);
						}
					} else {
						ds.addAll(lst);
						adpater.notifyDataSetChanged();
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
		realLoadData();
	}

	@Override
	public void resetTitleStatus(float v) {

	}

	@Override
	public void addGood(int index, TopicPostEntity tpe) {
		ds.get(index).setIsGood(TopicPostEntity.GOOD_ALREADY);// 改变状态
		ds.get(index).setGoodNum(ds.get(index).getGoodNum() + 1);// 改变点赞书目
		adpater.notifyDataSetChanged();
		try {
			JSONObject d = new JSONObject();
			d.put("picPostId", tpe.getId());
			d.put("userId", userId);
			String url = SystemConst.server_url
					+ SystemConst.TopicUrl.clickPostCommentGoodNum;
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
				}

				@Override
				public void onFailure(HttpException error, String msg) {
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(url, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void detailShow(int index, TopicPostEntity tpe) {
		Intent intent = new Intent(getActivity(),
				TopicCommentDetailActivity.class);
		intent.putExtra("postId", tpe.getId());
		getActivity().startActivity(intent);

	}

	@Override
	public void userShow(int index, TopicPostEntity tpe) {
		Intent intent = new Intent(getActivity(), ShowUserInfoDetail.class);
		intent.putExtra("uuid", tpe.getUserId());
		getActivity().startActivity(intent);
	}

	@Override
	public void to3Platform(int index, TopicPostEntity tpe) {
		// TODO Auto-generated method stub

	}

}
