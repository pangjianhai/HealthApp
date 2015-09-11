package cn.com.hzzc.health.pro.topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import cn.com.hzzc.health.pro.BaseActivity;
import cn.com.hzzc.health.pro.MainPageLayoutSpaceActivity;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.ShowUserInfoDetail;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.adapter.TopicPostItemAdapter;
import cn.com.hzzc.health.pro.model.TopicEntity;
import cn.com.hzzc.health.pro.model.TopicPostEntity;
import cn.com.hzzc.health.pro.part.XListView;
import cn.com.hzzc.health.pro.part.XListView.IXListViewListener;
import cn.com.hzzc.health.pro.util.ITopicCommentListener;
import cn.com.hzzc.health.pro.util.TopicUtil;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @todo 查看健康主题详情
 * @author pang
 *
 */
public class ShowTopicActivity extends BaseActivity implements
		IXListViewListener, ITopicCommentListener {

	private String topicId;

	private TextView topic_name, topic_uer_num, topic_comment_num;
	/********** 是否参与某一主题 ***********/
	private boolean isIn = false;// true:已经参与 false:未参与
	private Button is_in_topic;

	/******* 和主题相关的评论分页 ********/
	private XListView topic_post_lv;
	private int currentPage = 1;
	private int rows = 10;
	List<TopicPostEntity> ds = new ArrayList<TopicPostEntity>();
	private TopicPostItemAdapter adpater = null;

	private TextView topic_post_no_post_notice;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.topic_all_detail);
		initParam();
		initData();
		initInData();
		initNumData();
		initPostData();
	}

	private void initParam() {
		topicId = getIntent().getStringExtra("topicId");
		topic_name = (TextView) findViewById(R.id.topic_name);
		is_in_topic = (Button) findViewById(R.id.is_in_topic);
		topic_post_lv = (XListView) findViewById(R.id.topic_post_lv);
		topic_post_no_post_notice = (TextView) findViewById(R.id.topic_post_no_post_notice);

		topic_uer_num = (TextView) findViewById(R.id.topic_uer_num);
		topic_comment_num = (TextView) findViewById(R.id.topic_comment_num);
	}

	/**
	 * @user:pang
	 * @data:2015年9月6日
	 * @todo:初始化主题名称以及相关数据
	 * @return:void
	 */
	private void initData() {
		try {
			JSONObject d = new JSONObject();
			d.put("picId", topicId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					TopicEntity te = TopicUtil
							.parseEntityByJSON(responseInfo.result);
					topic_name.setText(te.getName());
				}

				@Override
				public void onFailure(HttpException error, String msg) {
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.TopicUrl.get_topic_info_by_id, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initNumData() {
		try {
			JSONObject d = new JSONObject();
			d.put("picId", topicId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					String participationNum = "0";
					String commentsNum = "0";
					try {
						JSONObject job = new JSONObject(data);
						participationNum = job.getString("participationNum");
						commentsNum = job.getString("commentsNum");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					topic_uer_num.setText(participationNum + "人参与");
					topic_comment_num.setText(commentsNum + "条发布");
					topic_uer_num.setVisibility(View.VISIBLE);
					topic_comment_num.setVisibility(View.VISIBLE);
				}

				@Override
				public void onFailure(HttpException error, String msg) {
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(
					SystemConst.server_url
							+ SystemConst.TopicUrl.querypicPostParticipationNumAndCommentNum,
					map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @user:pang
	 * @data:2015年9月6日
	 * @todo:初始化是否参与数据
	 * @return:void
	 */
	private void initInData() {
		try {
			JSONObject d = new JSONObject();
			d.put("userId", userId);
			d.put("picId", topicId);
			String url = SystemConst.server_url
					+ SystemConst.TopicUrl.isInTopic;
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					isIn = TopicUtil.parseFlag(data);
					if (isIn) {
						is_in_topic.setText("退出");
					} else {
						is_in_topic.setText("参与");
					}

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

	/**
	 * @param v
	 * @user:pang
	 * @data:2015年9月6日
	 * @todo:决定参与还是退出
	 * @return:void
	 */
	public void is_in_ops(View v) {
		if (!isIn) {
			goInTopic();
		} else {
			getOutTopic();
		}
	}

	/**
	 * @user:pang
	 * @data:2015年9月6日
	 * @todo:参与主题
	 * @return:void
	 */
	private void goInTopic() {
		try {
			JSONObject d = new JSONObject();
			d.put("userId", userId);
			d.put("picId", topicId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					isIn = true;
					is_in_topic.setText("退出");
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					error.printStackTrace();
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.TopicUrl.getInTopic, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @user:pang
	 * @data:2015年9月6日
	 * @todo:退出主题
	 * @return:void
	 */
	private void getOutTopic() {
		try {
			JSONObject d = new JSONObject();
			d.put("userId", userId);
			d.put("picId", topicId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					isIn = false;
					is_in_topic.setText("加入");
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					error.printStackTrace();
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.TopicUrl.getOutTopic, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @user:pang
	 * @data:2015年9月7日
	 * @todo:初始化评论信息
	 * @return:void
	 */
	public void initPostData() {
		topic_post_lv.setPullLoadEnable(true);
		topic_post_lv.setXListViewListener(this);
		adpater = new TopicPostItemAdapter(ShowTopicActivity.this,
				ShowTopicActivity.this, ds);
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
						topic_post_no_post_notice.setVisibility(View.VISIBLE);
						topic_post_lv.setVisibility(View.GONE);
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

	/**
	 * @param v
	 * @user:pang
	 * @data:2015年9月6日
	 * @todo:回退
	 * @return:void
	 */
	public void backoff(View v) {
		this.finish();
	}

	@Override
	public void onRefresh() {
		onLoadOver();
	}

	@Override
	public void onLoadMore() {
		realLoadData();
	}

	private void onLoadOver() {
		topic_post_lv.stopRefresh();
		topic_post_lv.stopLoadMore();
		topic_post_lv.setRefreshTime("刚刚");
	}

	/**
	 * @param v
	 * @user:pang
	 * @data:2015年9月9日
	 * @todo:添加主题评论
	 * @return:void
	 */
	public void addTopicComment(View v) {
		Intent intent = new Intent(ShowTopicActivity.this,
				TopicCommentAddActivity.class);
		intent.putExtra("topicId", topicId);
		intent.putExtra("topicName", topic_name.getText().toString());
		startActivity(intent);
		finish();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			boolean hasNew = getIntent().getBooleanExtra("hasNew", false);
			if (hasNew) {
				currentPage = 1;
				realLoadData();
			}
		}
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

	/**
	 * 查看评论详情
	 */
	@Override
	public void detailShow(int index, TopicPostEntity tpe) {
		Intent intent = new Intent(ShowTopicActivity.this,
				TopicCommentDetailActivity.class);
		intent.putExtra("postId", tpe.getId());
		startActivity(intent);
	}

	@Override
	public void userShow(int index, TopicPostEntity tpe) {
		Intent intent = new Intent(ShowTopicActivity.this,
				ShowUserInfoDetail.class);
		intent.putExtra("uuid", tpe.getUserId());
		startActivity(intent);
	}

	@Override
	public void to3Platform(int index, TopicPostEntity tpe) {
		// TODO Auto-generated method stub

	}
}
