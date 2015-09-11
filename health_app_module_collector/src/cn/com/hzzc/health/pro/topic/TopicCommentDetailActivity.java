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
import cn.com.hzzc.health.pro.part.CircularImage;
import cn.com.hzzc.health.pro.part.SentenceGridView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @todo 主题评论详情
 * @author pang
 *
 */
public class TopicCommentDetailActivity extends BaseActivity {

	private String postId;

	private CircularImage topic_post_detail_author_photo;
	private TextView topic_post_detail_author_username,
			topic_post_detail_content;
	private Button topic_post_detail_author_focus;
	private SentenceGridView topic_post_imgs_gridview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.topic_comment_detail);
		postId = getIntent().getStringExtra("postId");
		initPart();
		initData();
	}

	private void initPart() {
		topic_post_detail_author_photo = (CircularImage) findViewById(R.id.topic_post_detail_author_photo);
		topic_post_detail_author_username = (TextView) findViewById(R.id.topic_post_detail_author_username);
		topic_post_detail_content = (TextView) findViewById(R.id.topic_post_detail_content);
		topic_post_detail_author_focus = (Button) findViewById(R.id.topic_post_detail_author_focus);
		topic_post_imgs_gridview = (SentenceGridView) findViewById(R.id.topic_post_imgs_gridview);
	}

	/**
	 * @user:pang
	 * @data:2015年8月16日
	 * @todo:初始化操作状态
	 * @return:void
	 */
	private void initData() {
		try {
			JSONObject d = new JSONObject();
			// d.put("currentId", userId);
			d.put("picPostId", postId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					System.out.println("data:" + data);
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					error.printStackTrace();
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.TopicUrl.getTopicPostBytopicPostId, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void backoff(View v) {
		finish();
	}
}
