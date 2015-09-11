package cn.com.hzzc.health.pro.topic;

import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import cn.com.hzzc.health.pro.BaseActivity;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.part.CircularImage;
import cn.com.hzzc.health.pro.part.SentenceGridView;

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

	private void initData() {

	}
}
