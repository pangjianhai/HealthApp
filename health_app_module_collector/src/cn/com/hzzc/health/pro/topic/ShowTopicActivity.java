package cn.com.hzzc.health.pro.topic;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import cn.com.hzzc.health.pro.BaseActivity;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.model.TopicEntity;
import cn.com.hzzc.health.pro.util.TopicUtil;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @todo 查看健康主题详情
 * @author pang
 *
 */
public class ShowTopicActivity extends BaseActivity {

	private String topicId;

	private TextView topic_name;
	/********** 是否参与某一主题 ***********/
	private boolean isIn = false;
	private Button is_in_topic;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.topic_all_detail);
		initParam();
		initData();
		initInData();
	}

	private void initParam() {
		topicId = getIntent().getStringExtra("topicId");
		topic_name = (TextView) findViewById(R.id.topic_name);
		is_in_topic = (Button) findViewById(R.id.is_in_topic);
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
			send_normal_request(SystemConst.server_url
					+ SystemConst.TopicUrl.isInTopic, map, rcb);
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
