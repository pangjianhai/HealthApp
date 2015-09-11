package cn.com.hzzc.health.pro.topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.adapter.HomeFrameAdapter;
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.model.TopicEntity;
import cn.com.hzzc.health.pro.util.TopicUtil;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * @todo 查看健康主题详情
 * @author pang
 *
 */
public class ShowTopicActivity extends FragmentActivity {

	private TextView topic_name, topic_uer_num, topic_comment_num;
	/********** 是否参与某一主题 ***********/
	private boolean isIn = false;// true:已经参与 false:未参与
	private Button is_in_topic;

	/****** 分页有关 *******/
	private ViewPager viewPager;
	private List<Fragment> lists = new ArrayList<Fragment>();
	private HomeFrameAdapter myAdapter;
	private String topicId;
	String userId;
	/**** 指示 ****/
	private View left_arrow, right_arrow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.topic_all_detail);
		userId = HealthApplication.getUserId();
		topicId = getIntent().getStringExtra("topicId");
		Fragment sspace = new TopicPostFragment();
		Fragment tspace = new TopicUserFragment();
		lists.add(sspace);
		lists.add(tspace);

		myAdapter = new HomeFrameAdapter(getSupportFragmentManager(), lists);
		/**
		 * 初始化viewpaper
		 */
		viewPager = (ViewPager) findViewById(R.id.topic_fragment_parent_viewpager);
		viewPager.setAdapter(myAdapter);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		initParam();
		initData();
		initInData();
		initNumData();
	}

	private void initParam() {
		topic_name = (TextView) findViewById(R.id.topic_name);
		is_in_topic = (Button) findViewById(R.id.is_in_topic);

		topic_uer_num = (TextView) findViewById(R.id.topic_uer_num);
		topic_comment_num = (TextView) findViewById(R.id.topic_comment_num);

		left_arrow = findViewById(R.id.left_arrow);
		right_arrow = findViewById(R.id.right_arrow);
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

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int index) { // arg0:点击的第几页
			System.out.println("************index:" + index);
			int orange_color = Color.parseColor("#FFA500");
			int white_color = Color.parseColor("#dedede");
			if (index == 0) {

				left_arrow.setBackgroundColor(orange_color);
				right_arrow.setBackgroundColor(white_color);
			} else if (index == 1) {
				left_arrow.setBackgroundColor(white_color);
				right_arrow.setBackgroundColor(orange_color);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

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

	/********************* 页面 **************************/
	/**
	 * 回调接口
	 */
	public interface MyTouchListener {
		public void onTouchEvent(MotionEvent event);
	}

	/*
	 * 保存MyTouchListener接口的列表
	 */
	private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<ShowTopicActivity.MyTouchListener>();

	/**
	 * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
	 * 
	 * @param listener
	 */
	public void registerMyTouchListener(MyTouchListener listener) {
		myTouchListeners.add(listener);
	}

	/**
	 * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
	 * 
	 * @param listener
	 */
	public void unRegisterMyTouchListener(MyTouchListener listener) {
		myTouchListeners.remove(listener);
	}

	/**
	 * 分发触摸事件给所有注册了MyTouchListener的接口
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		for (MyTouchListener listener : myTouchListeners) {
			listener.onTouchEvent(ev);
		}
		return super.dispatchTouchEvent(ev);
	}

	public void send_normal_request(String url, Map<String, String> p,
			RequestCallBack<?> rcb) {
		RequestParams params = new RequestParams();
		if (p != null) {
			Iterator<Map.Entry<String, String>> it = p.entrySet().iterator();
			/**
			 * 添加参数
			 */
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				params.addBodyParameter(entry.getKey(), entry.getValue());
			}
		}
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, params, rcb);
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
}
